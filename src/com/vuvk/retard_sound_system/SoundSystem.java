/**
    Copyright 2019-2021 Anton "Vuvk" Shcherbatykh

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
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
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

    final static int SAMPLE_RATE = 44100;
    final static int SAMPLE_SIZE_IN_BITS = 16;
    final static int MONO_CHANNELS = 1;
    final static int STEREO_CHANNELS = 2;
    final static boolean SIGNED = true;
    final static boolean BIG_ENDIAN = false;
    final static int MAX_WRITE_LINE_TRIES = 100;
    /** use this if you want read sounds to cache before write it to line */
    final static boolean CACHED = false;
    /** use this to increase speed but reduce quality  */
    final static boolean FAST_MODE = true;

    private static boolean started = false;

    private static SourceDataLine monoLine   = null;
    private static SourceDataLine stereoLine = null;

    final static List<SoundBuffer> SOUND_BUFFERS = new CopyOnWriteArrayList<>();
    final static List<Music> MUSICS = new CopyOnWriteArrayList<>();

    private static final SoundList MONO_SOUNDS   = new SoundList();
    private static final SoundList STEREO_SOUNDS = new SoundList();
    //private static final List<Sound> MONO_SOUNDS   = new CopyOnWriteArrayList<>();//SoundList();
    //private static final List<Sound> STEREO_SOUNDS = new CopyOnWriteArrayList<>();//SoundList();

    private final static int CACHE_SIZE = 65536;
    private static SoundCache monoCache;
    private static SoundCache stereoCache;

    // volumes
    private static float musicVolume  = 1.0f;
    private static float soundsVolume = 1.0f;


    private SoundSystem() {}

    private static void init() {
        try {
            monoLine = AudioSystem.getSourceDataLine(getAudioMonoFormat());
            monoLine.open();
            monoLine.start();

            // for init line
            monoLine.write(new byte[2], 0, 2);
            monoLine.drain();

            monoCache = new SoundCache(CACHE_SIZE, monoLine);
        } catch (LineUnavailableException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        try {
            stereoLine = AudioSystem.getSourceDataLine(getAudioStereoFormat());
            stereoLine.open();
            stereoLine.start();

            // for init line
            stereoLine.write(new byte[4], 0, 4);
            stereoLine.drain();

            stereoCache = new SoundCache(CACHE_SIZE << 1, stereoLine);
        } catch (LineUnavailableException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    static AudioFormat getAudioMonoFormat() {
        return new AudioFormat(SAMPLE_RATE,
                               SAMPLE_SIZE_IN_BITS,
                               MONO_CHANNELS,
                               SIGNED,
                               BIG_ENDIAN);
    }

    static AudioFormat getAudioStereoFormat() {
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
     * add sound to queue
     * @param sound
     */
    static void addSound(Sound sound) {
        switch (sound.getChannels()) {
            case 1 :
                MONO_SOUNDS.add(sound);
                break;
            case 2 :
                STEREO_SOUNDS.add(sound);
                break;
        }
    }

    /**
     * remove sound from queue
     * */
    static void removeSound(Sound sound) {
        if (sound != null/* && isPlaying(sound)*/) {
            switch (sound.getChannels()) {
                case 1 :
                    MONO_SOUNDS.remove(sound);
                    break;
                case 2 :
                    STEREO_SOUNDS.remove(sound);
                    break;
            }
        }
    }

    /**
     * update line without caching
     * @param sounds list of sounds in system (mono or stereo)
     * @param line line for write (mono or stereo)
     */
    private static void updateLine(SoundList sounds, SourceDataLine line) {
        if (sounds.size == 0) {
            return;
        }

        int channels = line.getFormat().getChannels();
        int bufferSize = channels * (SAMPLE_SIZE_IN_BITS >> 3);
        /*int bufferSize = 0;
        for (Sound sound : sounds) {
            bufferSize += sound.getInputAudioStream().getFrameLength();
        }
        bufferSize /= sounds.size();
*/
        double[] mixer   = new double[bufferSize >> 1];
        int   [] counter = new int   [bufferSize];
        byte  [] result  = new byte  [bufferSize];
        byte  [] buffer  = new byte  [bufferSize];
        
        double volume;
        int cntReaded = 0;
        int soundsCount = 0;

        Sound[] data = sounds.getSounds();
        for (int s = 0; s < data.length; ++s) {
            Sound sound = data[s];
            if (sound == null) {
                break;
            }
            
            volume = sound.getVolume() * soundsVolume;
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
            if (FAST_MODE) {
                int offset = 0;
                int cnt;
                int maxSizeForWrite = channels * (SAMPLE_SIZE_IN_BITS >> 3);
                int skips = 0;
                while (offset < result.length && skips < MAX_WRITE_LINE_TRIES) {
                    cnt = line.available();
                    if (cnt > 0) {
                        int sizeForWrite = Math.min(cnt, maxSizeForWrite);
                        line.write(result, offset, sizeForWrite);
                        offset += maxSizeForWrite;
                    } else {
                        ++skips;
                    }
                }
            } else {
                line.write(result, 0, result.length);
            }
        }
    }

    /**
     * update line with caching
     * @param sounds list of sounds in system (mono or stereo)
     * @param line line for write (mono or stereo)
     */
    private static void updateLine(SoundList sounds, SourceDataLine line, SoundCache cache) {
        if (sounds.size == 0) {
            return;
        }

        int channels = line.getFormat().getChannels();
        int bufferSize = channels * (SAMPLE_SIZE_IN_BITS >> 3);

        double[] mixer   = new double[bufferSize >> 1];
        int   [] counter = new int   [bufferSize];
        byte  [] buffer  = new byte  [bufferSize];
        double volume;
        int cntReaded = 0;
        int soundsCount = 0;

        
        Sound[] data = sounds.getSounds();
        for (int s = 0; s < data.length; ++s) {
            Sound sound = data[s];
            if (sound == null) {
                break;
            }

            volume = sound.getVolume() * soundsVolume;
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

    private static void updateMonoLine() {
        if (monoLine != null) {
            if (CACHED) {
                updateLine(MONO_SOUNDS, monoLine, monoCache);
            } else {
                updateLine(MONO_SOUNDS, monoLine);
            }
        }
    }

    private static void updateStereoLine() {
        if (stereoLine != null) {
            if (CACHED) {
                updateLine(STEREO_SOUNDS, stereoLine, stereoCache);
            } else {
                updateLine(STEREO_SOUNDS, stereoLine);
            }
        }
    }

	public static void setMusicVolume(float musicVolume) {
        SoundSystem.musicVolume = musicVolume;
	}

	public static void setSoundsVolume(float soundsVolume) {
        SoundSystem.soundsVolume = soundsVolume;
	}

	public static float getMusicVolume() {
        return musicVolume;
	}

	public static float getSoundsVolume() {
        return soundsVolume;
	}

    /**
     * Start Sound System
     */
    public static void start() {
        if (!isStarted()) {
            init();

            started = true;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isStarted()) {
                        // чтобы поток не забивал проц
                        long prev = System.nanoTime();
                        updateMonoLine();
                        long delta = System.nanoTime() - prev;
                        if (delta < 45) {
                            try {
                                Thread.sleep(0, (int) (45 - delta));
                            } catch (InterruptedException ignored) {}
                        }
                    }
                    stop();
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
                            } catch (InterruptedException ignored) {}
                        }
                    }
                    stop();
                }
            }, "RSS Update Stereo Line Thread").start();
        }
    }

    /**
     * Stop all sounds and stop sound system.
     */
    public static void stop() {
        started = false;

        stopAll();

        for (Music mus : MUSICS) {
            mus.close();
        }
        MUSICS.clear();

        if (monoCache != null) {
            monoCache.close();
        }
        if (stereoCache != null) {
            stereoCache.close();
        }
        monoCache = null;
        stereoCache = null;

        if (monoLine != null) {
            monoLine.close();
        }
        if (stereoLine != null) {
            stereoLine.close();
        }
        monoLine = null;
        stereoLine = null;
    }

    /**
     * sound system was started?
     * @return true if sound system was started
     */
    public static boolean isStarted() {
        return started;
    }

    /**
     * check playing of sound
     * @param sound Sound for check
     * @return true if it playing now
     */
    public static boolean isPlaying(Sound sound) {
        return (MONO_SOUNDS.contains(sound) || STEREO_SOUNDS.contains(sound));
    }

    /**
     * play sound with or without checking if it play already (if it play, sound not start play)
     * @param sound
     * @param checkPlayng
     */
    public static void playSound(Sound sound, boolean checkPlaying) {
        if (isStarted() && sound != null) {
            if (checkPlaying) {
                if (sound.isPlaying()) {
                    return;
                }
            }

            sound.play();
        }
    }

    public static void stopMusic() {
        for(Music mus : MUSICS) {
            mus.stop();
        }
    }

    public static void stopAll() {
        stopMusic();
        
        for (int i = 0; i < MONO_SOUNDS.size; ++i) {
            Sound snd = MONO_SOUNDS.get(i);
            if (snd != null) {
                snd.stop();
            }
        }
        //MONO_SOUNDS.forEach(Sound::stop);
        MONO_SOUNDS.clear();
        
        for (int i = 0; i < STEREO_SOUNDS.size; ++i) {
            Sound snd = STEREO_SOUNDS.get(i);
            if (snd != null) {
                snd.stop();
            }
        }
        //STEREO_SOUNDS.forEach(Sound::stop);
        STEREO_SOUNDS.clear();
    }

    /**
     * Удалить все звуковые буфферы безвозвратно.
     * Delete all Sound Buffers forever.
     * */
    public static void deleteSoundBuffers() {
        synchronized (SoundSystem.SOUND_BUFFERS) {
            SOUND_BUFFERS.forEach(SoundBuffer::close);
            SOUND_BUFFERS.clear();
        }
    }

    /**
     * Проиграть случайный звук из массива (даже если какой-то из них уже проигрывается)
     * Play random sound
     * @param sounds Набор звуков
     */
    public static void playRandom(Sound[] sounds) {
        playRandom(sounds, false);
    }

    /**
     * Проиграть случайный звук из массива
     * Play random sound
     * @param sounds Набор звуков
     * @param checkPlaying Проверка играется ли какой-то звук из массива, если да, то не играть
     */
    public static void playRandom(Sound[] sounds, boolean checkPlaying) {
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
