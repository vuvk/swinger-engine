/**
    Copyright 2019 Anton "Vuvk" Shcherbatykh

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.vuvk.retard_sound_system;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author vuvk
 */
public final class SoundSystem {
    private static final Logger LOG = Logger.getLogger(SoundSystem.class.getName());    
    
    public  final static float SAMPLE_RATE = 44100f;
    public  final static int   SAMPLE_SIZE_IN_BITS = 16;
    private final static int   MONO_CHANNELS = 1;
    private final static int   STEREO_CHANNELS = 2;
    private final static boolean SIGNED = true;
    private final static boolean BIG_ENDIAN = false;
    
    private static boolean started = false;
    
    private static SourceDataLine MONO_LINE   = null;
    private static SourceDataLine STEREO_LINE = null;
    
    private final static Set<Sound> MONO_SOUNDS = new HashSet<>();
    private final static Set<Sound> STEREO_SOUNDS = new HashSet<>();
    final static Set<Music> MUSICS = new HashSet<>();
    
    private final static List<Sound> MONO_SOUNDS_FOR_ADD   = new Vector<>();
    private final static List<Sound> STEREO_SOUNDS_FOR_ADD = new Vector<>();
    private final static List<Sound> MONO_SOUNDS_FOR_DELETE   = new Vector<>();
    private final static List<Sound> STEREO_SOUNDS_FOR_DELETE = new Vector<>();    
    
    private static class SoundCache {
        private final byte[] buffer;
        private int bufferSize = -1;

        public SoundCache(int cacheSize) {
            buffer = new byte[cacheSize];
        }
        
        public void add(byte value) {
            if (!isFull()) {
                //++bufferSize;
                buffer[++bufferSize] = value;
            }
        }
        
        public void reset() {
            bufferSize = -1;
        }
        
        public boolean isFull() {
            return (bufferSize == buffer.length - 1);
        }
        
        public boolean isEmpty() {
            return (bufferSize == -1);
        }

        public final byte[] getBuffer() {
            return buffer;
        }

        public int getBufferSize() {
            return bufferSize + 1;
        }
    }
    private final static int CACHE_SIZE = 2048;
    private final static SoundCache MONO_CACHE   = new SoundCache(CACHE_SIZE);
    private final static SoundCache STEREO_CACHE = new SoundCache(CACHE_SIZE << 1);
        
    private SoundSystem() {}
    
    private static void init() {
        try {
            while (MONO_LINE == null) {
                DataLine.Info lineInfo = new DataLine.Info(SourceDataLine.class, getAudioMonoFormat());
                MONO_LINE = (SourceDataLine) AudioSystem.getLine(lineInfo);                
            }
            MONO_LINE.open(getAudioMonoFormat());
            MONO_LINE.start();
            
            // for init line
            MONO_LINE.write(new byte[2], 0, 2);
            MONO_LINE.drain();
        } catch (LineUnavailableException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        
        try {
            while (STEREO_LINE == null) {
                DataLine.Info lineInfo = new DataLine.Info(SourceDataLine.class, getAudioStereoFormat());
                STEREO_LINE = (SourceDataLine) AudioSystem.getLine(lineInfo);
            }
            STEREO_LINE.open(getAudioStereoFormat());
            STEREO_LINE.start();
            
            // for init line
            STEREO_LINE.write(new byte[4], 0, 4);
            STEREO_LINE.drain();
        } catch (LineUnavailableException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    
    public static void start() {    
        if (!isStarted()) {
            init();
        
            started = true;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (MONO_LINE != null &&
                           isStarted()) {
                        updateMonoLine();
                    }
                }
            }, "RSS Update Mono Line Thread").start();
            
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (STEREO_LINE != null &&
                           isStarted()) {
                        updateStereoLine();
                    }
                }
            }, "RSS Update Stereo Line Thread").start();
        }        
    }
    
    public static void stop() {
        started = false;
        
        for (Music music : MUSICS) {
            music.stop();
        }
        
        MONO_SOUNDS.clear();
        STEREO_SOUNDS.clear();
        MONO_SOUNDS_FOR_ADD.clear();
        STEREO_SOUNDS_FOR_ADD.clear();
        MONO_SOUNDS_FOR_DELETE.clear();
        STEREO_SOUNDS_FOR_DELETE.clear();
        MUSICS.clear();
        
        MONO_CACHE.reset();
        STEREO_CACHE.reset();
        
        MONO_LINE.close();
        STEREO_LINE.close();
        
        MONO_LINE = null;
        STEREO_LINE = null;
    }

    public static final boolean isStarted() {
        return started;
    }   
    
    static boolean isPlaying(Sound sound) {
        return (MONO_SOUNDS.contains(sound) || STEREO_SOUNDS.contains(sound));
    }
    
    static void playSound(Sound sound) {
        if (sound != null/* && !isPlaying(sound)*/) {  
            AudioFormat format = sound.getFormat();
            if (format != null) {
                int channels = format.getChannels();
                if (channels == 1) {
                    MONO_SOUNDS_FOR_ADD.add(sound);
                } else if (channels == 2) {
                    STEREO_SOUNDS_FOR_ADD.add(sound);
                }
            }
        }
    }
    
    static void stopSound(Sound sound) {
        if (sound != null/* && isPlaying(sound)*/) {        
            AudioFormat format = sound.getFormat();
            if (format != null) {
                int channels = format.getChannels();
                if (channels == 1) {
                    MONO_SOUNDS_FOR_DELETE.add(sound);
                } else if (channels == 2) {
                    STEREO_SOUNDS_FOR_DELETE.add(sound);
                }        
            }
        }
    }
    
    public static AudioFormat getAudioMonoFormat() {
        return new AudioFormat(SAMPLE_RATE,
                               SAMPLE_SIZE_IN_BITS,
                               MONO_CHANNELS,
                               SIGNED,
                               BIG_ENDIAN);
    }
    
    public static AudioFormat getAudioStereoFormat() {
        return new AudioFormat(SAMPLE_RATE,
                               SAMPLE_SIZE_IN_BITS,
                               STEREO_CHANNELS,
                               SIGNED,
                               BIG_ENDIAN);
    }   
    
    static AudioInputStream getEncodedAudioInputStream(InputStream in) {   
        try {
            return getEncodedAudioInputStream(AudioSystem.getAudioInputStream(in));
        } catch (UnsupportedAudioFileException | IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    private static AudioInputStream getEncodedAudioInputStream(AudioInputStream in) {   
        if (in != null) {
            try {
                // получаем формат аудио
                AudioFormat inFormat = in.getFormat();
                // получаем несжатый формат
                if (inFormat != null) {
                    AudioFormat outFormat = (inFormat.getChannels() == 1) ? getAudioMonoFormat() : getAudioStereoFormat();
                    return AudioSystem.getAudioInputStream(outFormat, in);                    
                }
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        
        return null;        
    }
    
    static AudioInputStream getEncodedAudioInputStream(URL url) {   
        try {
            return getEncodedAudioInputStream(AudioSystem.getAudioInputStream(url));
        } catch (UnsupportedAudioFileException | IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    static AudioInputStream getEncodedAudioInputStream(String path) { 
        return getEncodedAudioInputStream(new File(path));
    }
    
    static AudioInputStream getEncodedAudioInputStream(File file) { 
        try {
            return getEncodedAudioInputStream(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * update line without caching
     * @param sounds list of sounds in system (mono or stereo)
     * @param line line for write (mono or stereo)
     */
    private static void updateLine(final Set<Sound> sounds, final SourceDataLine line) {
        if (!sounds.isEmpty()) {            
            int channels = line.getFormat().getChannels();
            int bufferSize = (SAMPLE_SIZE_IN_BITS >> 3) * channels;
            
            double[] mixer = new double [bufferSize >> 1];
            int[]  counter = new int [bufferSize];
            byte[] result  = new byte[bufferSize];
            int cntReaded = 0;
            int soundsCount = 0;
            
            for (Sound sound : sounds) {    
                final double volume = sound.getVolume();
                final byte[] buffer = new byte[bufferSize];
                cntReaded = sound.read(buffer);                                
                
                if (cntReaded > 0) {                    
                    for (int i = 0, n = 0; i < cntReaded; i += 2, ++n) {                        
                        byte low  = buffer[i];
                        byte high = buffer[i + 1];

                        int value = (short)(((high & 0xFF) << 8) | (low & 0xFF));
                        
                        mixer[n] += value * volume;                            
                        counter[n]++;
                    }
                    ++soundsCount;
                } else {
                    if (sound.isLooping()) {
                        sound.rewind();
                    } else {
                        sound.stop();
                    }
                }
            }
            
            if (soundsCount > 0) {
                // собираем средний звук
                for (int i = 0; i < mixer.length; i++) {
                    int value = (int)(mixer[i] / counter[i]);
                    result[i * 2]     = (byte) value;
                    result[i * 2 + 1] = (byte) (value >> 8);
                }
                
                // отправляем в звуковую карту
                line.write(result, 0, result.length);    
            }
            
            if (line.available() >= SAMPLE_RATE * channels) {
                line.drain();
            }
        }
    }
    
    /**
     * update line with caching
     * @param sounds list of sounds in system (mono or stereo)
     * @param line line for write (mono or stereo)
     */
    private static void updateLine(final Set<Sound> sounds, final SourceDataLine line, SoundCache cache) {
        if (!sounds.isEmpty()) {            
            int channels = line.getFormat().getChannels();
            int bufferSize = (SAMPLE_SIZE_IN_BITS >> 3) * channels;
            
            double[] mixer = new double [bufferSize >> 1];
            int[]  counter = new int [bufferSize];
            byte[] result  = new byte[bufferSize];
            int cntReaded = 0;
            int soundsCount = 0;
            
            for (Sound sound : sounds) {    
                final double volume = sound.getVolume();
                final byte[] buffer = new byte[bufferSize];
                cntReaded = sound.read(buffer);                                
                
                if (cntReaded > 0) {                    
                    for (int i = 0, n = 0; i < cntReaded; i += 2, ++n) {                         
                        byte low  = buffer[i];
                        byte high = buffer[i + 1];

                        int value = (short)(((high & 0xFF) << 8) | (low & 0xFF));
                        
                        mixer[n] += value * volume;                        
                        counter[n]++;
                    }
                    ++soundsCount;
                } else {
                    if (sound.isLooping()) {
                        sound.rewind();
                    } else {
                        sound.stop();
                    }
                }
            }
            
            if (soundsCount > 0) {
                // собираем средний звук
                for (int i = 0; i < mixer.length; i++) {
                    int value = (int)(mixer[i] / counter[i]);
                    result[i * 2]     = (byte) value;
                    result[i * 2 + 1] = (byte) (value >> 8);
                }
                // наполняем кэш
                for (int i = 0; i < result.length; i++) {
                    if (!cache.isFull()) {
                        cache.add(result[i]);
                    } else {
                        line.write(cache.getBuffer(), 0, cache.getBufferSize());
                        cache.reset();
                        cache.add(result[i]);
                    }
                }
            } else if (!cache.isEmpty()) {
                line.write(cache.getBuffer(), 0, cache.getBufferSize());
                cache.reset();
            }
            
            if (line.available() >= SAMPLE_RATE * channels) {
                line.drain();
            }
        }
    }
    
    private static void updateMonoLine() {        
        if (MONO_LINE != null) {    
            if (!MONO_SOUNDS_FOR_DELETE.isEmpty()) {
                MONO_SOUNDS.removeAll(MONO_SOUNDS_FOR_DELETE);
                MONO_SOUNDS_FOR_DELETE.clear();            
            }        
            if (!MONO_SOUNDS_FOR_ADD.isEmpty()) {
                MONO_SOUNDS.addAll(MONO_SOUNDS_FOR_ADD);
                MONO_SOUNDS_FOR_ADD.clear();            
            }
            updateLine(MONO_SOUNDS, MONO_LINE, MONO_CACHE);
        }
    }
    
    private static void updateStereoLine() {        
        if (STEREO_LINE != null) {
            if (!STEREO_SOUNDS_FOR_DELETE.isEmpty()) {
                STEREO_SOUNDS.removeAll(STEREO_SOUNDS_FOR_DELETE);
                STEREO_SOUNDS_FOR_DELETE.clear();            
            }
            if (!STEREO_SOUNDS_FOR_ADD.isEmpty()) {
                STEREO_SOUNDS.addAll(STEREO_SOUNDS_FOR_ADD);
                STEREO_SOUNDS_FOR_ADD.clear();
            }     
            updateLine(STEREO_SOUNDS, STEREO_LINE, STEREO_CACHE);            
        }
    }
    
    
    /**
     * Проиграть случайный звук из массива (даже если какой-то из них уже проигрывается)
     * Play random sound
     * @param sounds Набор звуков
     */
    public static void playRandom(Sound ... sounds) {
        playRandom(false, sounds);
    }
    
    /**
     * Проиграть случайный звук из массива
     * Play random sound
     * @param checkPlaying Проверка играется ли какой-то звук из массива, если да, то не играть
     * @param sounds Набор звуков
     */
    public static void playRandom(boolean checkPlaying, Sound ... sounds) {
        if (sounds == null || sounds.length == 0) {
            return;
        }
        
        if (checkPlaying) {
            for (Sound sound : sounds) {
                if (sound.isPlaying()) {
                    return;
                }
            }
        }
        
        int variant = Math.abs(new Random().nextInt()) % sounds.length;
        sounds[variant].play();
    }
}