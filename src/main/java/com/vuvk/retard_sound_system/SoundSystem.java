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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author vuvk
 */
public final class SoundSystem {
    private static final Logger LOG = Logger.getLogger(SoundSystem.class.getName());    
    
    public  final static int SAMPLE_RATE = 44100;
    public  final static int SAMPLE_SIZE_IN_BITS = 16;
    private final static int MONO_CHANNELS = 1;
    private final static int STEREO_CHANNELS = 2;
    private final static boolean SIGNED = true;
    private final static boolean BIG_ENDIAN = false;
    private final static int MAX_WRITE_LINE_TRIES = 100;
    private final static boolean CACHED = false;
    
    private static boolean started = false;
    
    private static SourceDataLine MONO_LINE   = null;
    private static SourceDataLine STEREO_LINE = null;
    
    /**
     * class for queue of sounds
     */
    private static class SoundList {
        final static int MAX_SOUNDS = 16;        
        final Sound[] sounds = new Sound[MAX_SOUNDS];
        int size = 0;
              
        void add(Sound sound) {
            if (sound == null) {
                return;
            }
            
            if (!isFull()) {
                for (int i = 0; i < sounds.length; ++i) {
                    if (sounds[i] == null) {
                        sounds[i] = sound;
                        ++size;
                        break;
                    }
                }
            }
        }
        
        void addAll(Collection<Sound> forAdd) {
            for (Sound snd : forAdd) {
                if (!isFull()) {
                    add(snd);
                } else {
                    break;
                }
            }
        }
        
        void addAll(SoundList forAdd) {
            if (forAdd.isEmpty()) {
                return;
            }
            
            for (Sound snd : forAdd.getSounds()) {
                if (!isFull() && snd != null) {
                    add(snd);
                } else {
                    break;
                }
            }
        }
        
        void remove(Sound sound) {
            if (sound == null) {
                return;
            }
            
            if (!isEmpty()) {
                for (int i = 0; i < sounds.length; ++i) {
                    Sound snd = sounds[i];
                    if (snd != null && snd.equals(sound)) {
                        sounds[i] = null;
                        --size;
                        break;
                    }
                }
            }
        }
        
        void removeAll(Collection<Sound> forDel) {
            for (Sound snd : forDel) {
                if (!isEmpty() && snd != null) {
                    remove(snd);
                } else {
                    break;
                }
            }
        }
        
        void removeAll(SoundList forDel) {
            if (forDel.isEmpty()) {
                return;
            }
            
            for (Sound snd : forDel.getSounds()) {
                if (!isEmpty()) {
                    remove(snd);
                } else {
                    break;
                }
            }
        }
        
        void clear() {
            Arrays.fill(sounds, null);
            size = 0;
        }
        
        boolean contains(Sound sound) {
            if (sound != null) {            
                if (!isEmpty()) {
                    for (int i = 0; i < sounds.length; ++i) {
                        Sound snd = sounds[i];
                        if (snd != null && snd.equals(sound)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        
        boolean isEmpty() {
            return size == 0;
        }
        
        boolean isFull() {
            return (size == MAX_SOUNDS);
        }
        
        final Sound[] getSounds() {
            Sound[] sounds = new Sound[this.sounds.length];
            System.arraycopy(this.sounds, 0, sounds, 0, sounds.length);
            return sounds;
        }
    }
    
    private static final SoundList MONO_SOUNDS = new SoundList();
    private static final SoundList STEREO_SOUNDS = new SoundList();
    final static List<Music> MUSICS = new ArrayList<>();
    
    private final static List<Sound> MONO_SOUNDS_FOR_ADD   = new ArrayList<>();
    private final static List<Sound> MONO_SOUNDS_FOR_DEL   = new ArrayList<>();
    private final static List<Sound> STEREO_SOUNDS_FOR_ADD = new ArrayList<>();
    private final static List<Sound> STEREO_SOUNDS_FOR_DEL = new ArrayList<>();  
    
    /**
     * class for caching sound data
     */
    private static class SoundCache {
        private SourceDataLine line;
        private final byte[] buffer;
        private int bufferSize = -1;

        SoundCache(int cacheSize, SourceDataLine line) {
            this.buffer = new byte[cacheSize];
            this.line = line;
        }
        
        void writeToLine() {
            int offset = 0;
            int cnt;
            bufferSize = (bufferSize % 2 == 0) ? bufferSize : bufferSize + 1;            
            
            // пытаемся записать
            int maxSizeForWrite = line.getFormat().getChannels() * (SAMPLE_SIZE_IN_BITS >> 3);
            int skips = 0;
            while (offset < bufferSize && skips < MAX_WRITE_LINE_TRIES) {
                cnt = line.available();
                if (cnt > 0) {
                    int sizeForWrite = Math.min(cnt, maxSizeForWrite);
                    line.write(buffer, offset, sizeForWrite);
                    //offset += sizeForWrite;
                    offset += maxSizeForWrite;
                } else {
                    ++skips;
                }
            }
        }
        
        void write(byte value) {
            if (isFull()) {
                writeToLine();                
                reset();
            }
            buffer[++bufferSize] = value;
        }
        
        void reset() {
            bufferSize = -1;
        }
        
        void drain() {
            if (!isEmpty()) {
                writeToLine();
                reset();
            }
        }
        
        boolean isFull() {
            return (bufferSize == buffer.length - 1);
        }
        
        boolean isEmpty() {
            return (bufferSize == -1);
        }
    }
    private final static int CACHE_SIZE = 2048;
    private static SoundCache MONO_CACHE;
    private static SoundCache STEREO_CACHE;
    
    private SoundSystem() {}
    
    private static void init() {
        try {
            MONO_LINE = AudioSystem.getSourceDataLine(getAudioMonoFormat());
            MONO_LINE.open(getAudioMonoFormat());
            MONO_LINE.start();
                        
            // for init line
            MONO_LINE.write(new byte[2], 0, 2);
            MONO_LINE.drain();     
            
            MONO_CACHE = new SoundCache(CACHE_SIZE, MONO_LINE);
        } catch (LineUnavailableException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        
        try {
            STEREO_LINE = AudioSystem.getSourceDataLine(getAudioStereoFormat());
            STEREO_LINE.open(getAudioStereoFormat());
            STEREO_LINE.start();
            
            // for init line
            STEREO_LINE.write(new byte[4], 0, 4);
            STEREO_LINE.drain();
            
            STEREO_CACHE = new SoundCache(CACHE_SIZE << 1, STEREO_LINE);
        } catch (LineUnavailableException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    
    public static void start() {    
        if (!isStarted()) {
            init();
        
            started = true;

            new Thread(new Runnable() {
                Thread thisThread = Thread.currentThread();
                
                @Override
                public void run() {
                    while (isStarted()) {        
                        // чтобы поток не забивал проц
                        long prev = System.nanoTime(); 
                        updateMonoLine();
                        long delta = System.nanoTime() - prev;
                        if (delta < 45) {
                            try {
                                Thread.currentThread().sleep(0, (int) (45 - delta));
                            } catch (InterruptedException ex) {}
                        }
                    }
                }
            }, "RSS Update Mono Line Thread").start();
            
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isStarted()) {
                        // чтобы поток не забивал проц
                        long prev = System.nanoTime(); 
                        updateStereoLine();
                        long delta = System.nanoTime() - prev;
                        if (delta < 90) {
                            try {
                                Thread.sleep(0, (int) (90 - delta));
                            } catch (InterruptedException ex) {}
                        }
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
        MONO_SOUNDS_FOR_DEL.clear();
        STEREO_SOUNDS_FOR_DEL.clear();
        MUSICS.clear();
        
        MONO_CACHE = null;
        STEREO_CACHE = null;
        
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
        if (sound != null) {
            /*if (isPlaying(sound)) {
                return;
            }*/
            
            switch (sound.getChannels()) {
                case 1 : 
                    synchronized (MONO_SOUNDS_FOR_ADD) {
                        MONO_SOUNDS_FOR_ADD.add(sound);
                    }
                    break;
                case 2 :
                    synchronized (STEREO_SOUNDS_FOR_ADD) {
                        STEREO_SOUNDS_FOR_ADD.add(sound);
                    }
                    break;
            }
        }
    }
    
    static void stopSound(Sound sound) {
        if (sound != null/* && isPlaying(sound)*/) {            
            switch (sound.getChannels()) {
                case 1 : 
                    synchronized (MONO_SOUNDS_FOR_DEL) {
                        MONO_SOUNDS_FOR_DEL.add(sound);
                    }
                    break;
                case 2 :
                    synchronized (STEREO_SOUNDS_FOR_DEL) {
                        STEREO_SOUNDS_FOR_DEL.add(sound);
                    }
                    break;
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
    private static void updateLine(final SoundList sounds, final SourceDataLine line) {            
        final int channels = line.getFormat().getChannels();
        final int bufferSize = channels * (SAMPLE_SIZE_IN_BITS >> 3);

        final double[] mixer   = new double[bufferSize >> 1];
        final int[]    counter = new int   [bufferSize];
        final byte[]   result  = new byte  [bufferSize];
        final byte[]   buffer  = new byte  [bufferSize];
        double volume;
        int cntReaded = 0;
        int soundsCount = 0;

        if (!sounds.isEmpty()) {
            Sound[] data = sounds.getSounds();
            for (final Sound sound : data) {
                if (sound == null) {
                    continue;
                }
                
                volume = sound.getVolume();
                cntReaded = sound.read(buffer);

                if (cntReaded > 0) {
                    if (volume > 0.0) {
                        for (int i = 0, n = 0; i < cntReaded; i += 2, ++n) {
                            int low  = buffer[i    ];
                            int high = buffer[i + 1];

                            int value = (short)(((high & 0xFF) << 8) | (low & 0xFF));

                            if (volume < 1.0) {
                                value *= volume;
                            } 
                            mixer[n] += value;      
                            counter[n]++;
                        }
                        ++soundsCount;
                    }
                } else if (cntReaded == -1) {
                    if (sound.isLooping()) {
                        sound.rewind();
                    } else {
                        sound.stop();
                    }
                }
            }

            if (soundsCount > 0) {
                // собираем средний звук
                for (int i = 0, n = 0; i < mixer.length; n += 2, ++i) {
                    int value = (int) (mixer[i] / counter[i]);
                    result[n    ] = (byte) value;
                    result[n + 1] = (byte) (value >> 8);
                }
                
                // пытаемся записать
                int offset = 0;
                int cnt;
                int maxSizeForWrite = channels * (SAMPLE_SIZE_IN_BITS >> 3);
                int skips = 0;
                while (offset < result.length && skips < MAX_WRITE_LINE_TRIES) {
                    cnt = line.available();
                    if (cnt > 0) {
                        int sizeForWrite = Math.min(cnt, maxSizeForWrite);
                        line.write(result, offset, sizeForWrite);
                        //offset += sizeForWrite;
                        offset += maxSizeForWrite;
                    } else {
                        ++skips;
                    }
                }
            }
        }
    }
    
    /**
     * update line with caching
     * @param sounds list of sounds in system (mono or stereo)
     * @param line line for write (mono or stereo)
     */
    private static void updateLine(final SoundList sounds, final SourceDataLine line, final SoundCache cache) {   
        final int channels = line.getFormat().getChannels();
        final int bufferSize = channels * (SAMPLE_SIZE_IN_BITS >> 3);

        final double[] mixer   = new double[bufferSize >> 1];
        final int[]    counter = new int   [bufferSize];
        final byte[]   buffer  = new byte  [bufferSize];
        double volume;
        int cntReaded = 0;
        int soundsCount = 0;

        if (!sounds.isEmpty()) {
            Sound[] data = sounds.getSounds();
            for (final Sound sound : data) {
                if (sound == null) {
                    continue;
                }
                
                volume = sound.getVolume();
                cntReaded = sound.read(buffer);

                if (cntReaded > 0) {
                    if (volume > 0.0) {
                        for (int i = 0, n = 0; i < cntReaded; i += 2, ++n) {
                            int low  = buffer[i    ];
                            int high = buffer[i + 1];

                            int value = (short)(((high & 0xFF) << 8) | (low & 0xFF));

                            if (volume < 1.0) {
                                value *= volume;
                            } 
                            mixer[n] += value;      
                            counter[n]++;
                        }
                        ++soundsCount;
                    }
                } else if (cntReaded == -1) {
                    if (sound.isLooping()) {
                        sound.rewind();
                    } else {
                        sound.stop();
                    }
                }
            }

            if (soundsCount > 0) {
                // собираем средний звук
                for (int i = 0; i < mixer.length; ++i) {
                    int value = (int) (mixer[i] / counter[i]);
                    
                    // и сразу в кэш
                    cache.write((byte) value);
                    cache.write((byte) (value >> 8));
                }                
            } else {
                if (!cache.isEmpty()) {
                    cache.drain();
                }
            }
        }
    }
    
    private static void updateMonoLine() {        
        if (MONO_LINE != null) {            
            if (!MONO_SOUNDS_FOR_DEL.isEmpty()) {
                synchronized (MONO_SOUNDS_FOR_DEL) {
                    MONO_SOUNDS.removeAll(MONO_SOUNDS_FOR_DEL);
                    MONO_SOUNDS_FOR_DEL.clear();
                }
            }
            
            if (!MONO_SOUNDS_FOR_ADD.isEmpty()) {
                synchronized (MONO_SOUNDS_FOR_ADD) {
                    MONO_SOUNDS.addAll(MONO_SOUNDS_FOR_ADD);
                    MONO_SOUNDS_FOR_ADD.clear();  
                }
            }
            
            if (!MONO_SOUNDS.isEmpty() || !MONO_CACHE.isEmpty()) {
                if (CACHED) {
                    updateLine(MONO_SOUNDS, MONO_LINE, MONO_CACHE);                    
                } else {
                    updateLine(MONO_SOUNDS, MONO_LINE);
                }
            }
        }
    }
    
    private static void updateStereoLine() {
        if (STEREO_LINE != null) {            
            if (!STEREO_SOUNDS_FOR_DEL.isEmpty()) {
                synchronized (STEREO_SOUNDS_FOR_DEL) {
                    STEREO_SOUNDS.removeAll(STEREO_SOUNDS_FOR_DEL);
                    STEREO_SOUNDS_FOR_DEL.clear();
                }
            }
            
            if (!STEREO_SOUNDS_FOR_ADD.isEmpty()) {
                synchronized (STEREO_SOUNDS_FOR_ADD) {
                    STEREO_SOUNDS.addAll(STEREO_SOUNDS_FOR_ADD);
                    STEREO_SOUNDS_FOR_ADD.clear();  
                }
            }
            
            if (!STEREO_SOUNDS.isEmpty() || !STEREO_CACHE.isEmpty()) {
                if (CACHED) {
                    updateLine(STEREO_SOUNDS, STEREO_LINE, STEREO_CACHE);
                } else {
                    updateLine(STEREO_SOUNDS, STEREO_LINE);
                }
            }
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
        
        if (isStarted()) {
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
}