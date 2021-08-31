/**
    Copyright (C) 2021 Anton "Vuvk" Shcherbatykh <vuvk69@gmail.com>
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package com.vuvk.audiosystem;

import com.jogamp.openal.AL;
import com.jogamp.openal.ALFactory;
import com.jogamp.openal.util.ALut;
import com.vuvk.swinger.utils.Utils;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public final class AudioSystem {

    private final static Logger LOGGER = Logger.getLogger(AudioSystem.class.getName());

    final static AL al = ALFactory.getAL();
    final static List<SoundBuffer> SOUND_BUFFERS = new CopyOnWriteArrayList<>();
    final static List<Sound> SOUNDS = new CopyOnWriteArrayList<>();
    final static List<Music> MUSICS = new CopyOnWriteArrayList<>();

    private static boolean inited = false;

    private static AudioListener LISTENER_INSTANCE = null;
    private static float musicsVolume = 1.0f;
    private static float soundsVolume = 1.0f;

    private static Timer updateSoundsTimer;
    private static Timer updateMusicsTimer;

    public static void init() {
        if (!inited) {
            // Initialize OpenAL and clear the error bit.
            ALut.alutInit();

            LISTENER_INSTANCE = new AudioListener();

            // запускаем таймер слежения за звуками
            updateSoundsTimer = new Timer("Update Sounds Timer");
            updateSoundsTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    for (Iterator<Sound> it = SOUNDS.iterator();
                         it.hasNext();
                    ) {
                        Sound sound = it.next();
                        if (sound == null) {
                            continue;
                        }

                        sound.setVolume(soundsVolume);

                        if (sound.isStopped() &&
                           !sound.isLooping() &&
                            sound.isPlayOnce()
                         ) {
                            sound.dispose();
                            SOUNDS.remove(sound);
                        }
                    }
                }
            }, 0, 1000);

            // запускаем таймер слежения за музыками
            updateMusicsTimer = new Timer("Update Musics Timer");
            updateMusicsTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    for (Iterator<Music> it = MUSICS.iterator();
                             it.hasNext();
                        ) {
                        Music music = it.next();
                        // не валидный экземпляр или даже не открыт? уходи
                        if (music == null || !music.isOpened()) {
                            continue;
                        }

                        if (music.isPaused() || (music.isStopped() && !music.isLooping())) {
                            continue;
                        }

                        music.setVolume(musicsVolume);

                        if (music.update()) {
                            if (!music.playback()) {
                                music.stop();

                                if (music.isLooping()) {
                                    music.play();
                                }
                            }
                        }
                    }
                }
            }, 0, 200);

            inited = true;
        }
    }

    public static boolean isInited() {
        return inited;
    }

    public static void disposeAll() {
        if (inited) {
            disposeAllSoundSources();
            disposeAllSoundBuffers();
            disposeAllMusics();

            System.gc();
        }
    }

    public static void deinit() {
        if (inited) {
            LISTENER_INSTANCE = null;

            updateSoundsTimer.cancel();
            updateSoundsTimer = null;

            updateMusicsTimer.cancel();
            updateMusicsTimer = null;

            disposeAll();

            //checkError();
            ALut.alutExit();

            inited = false;
        }
    }

    /**
     * Check for OpenAL errors...
     */
    public static int checkError() {
        int errorCode = al.alGetError();
        if (errorCode != AL.AL_NO_ERROR) {
            System.err.println("AL Error - " + Integer.toHexString(errorCode));
//            throw new ALException("OpenAL error raised...");
        }
        return errorCode;
    }

    public static void disposeAllSoundBuffers() {
        if (inited) {
            for (SoundBuffer buffer : SOUND_BUFFERS) {
                if (buffer != null) {
                    buffer.dispose();
                }
            }
            SOUND_BUFFERS.clear();
        }
    }

    public static void disposeAllSoundSources() {
        if (inited) {
            for (Sound sound : SOUNDS) {
                if (sound != null) {
                    sound.dispose();
                }
            }
            SOUNDS.clear();
        }
    }

    public static void disposeAllMusics() {
        if (inited) {
            for (Music music : MUSICS) {
                if (music != null) {
                    music.dispose();
                }
            }
            MUSICS.clear();
        }
    }

    public static AudioListener getAudioListener() {
        return LISTENER_INSTANCE;
    }

    public static float getSoundsVolume() {
        return soundsVolume;
    }

    public static float getMusicsVolume() {
        return musicsVolume;
    }

    public static void setSoundsVolume(float soundsVolume) {
        AudioSystem.soundsVolume = Utils.limit(soundsVolume, 0, 1f);
    }

    public static void setMusicsVolume(float musicsVolume) {
        AudioSystem.musicsVolume = Utils.limit(musicsVolume, 0, 1f);
    }

    public static SoundBuffer newSoundBuffer(String path) {
        if (inited) {
            SoundBuffer buffer = new SoundBuffer();
            if (buffer.load(path)) {
                SOUND_BUFFERS.add(0, buffer);
                return buffer;
            } else {
                buffer.dispose();
            }
        }

        return null;
    }

    public static Sound newSound(SoundBuffer buffer) {
        if (inited) {
            if (buffer != null && buffer.getBuffer()[0] != 0) {
                Sound sound = new Sound();
                if (sound.setBuffer(buffer)) {
                    SOUNDS.add(0, sound);
                    return sound;
                } else {
                    sound.dispose();
                }
            }
        }

        return null;
    }

    public static Music newMusic(String path) {
        if (inited) {
            try {
                Music music = new Music(new File(path).toURI().toURL());
                //if (music.open()) {
                    MUSICS.add(0, music);
                    return music;
                /*} else {
                    music.dispose();
                }*/
            } catch (MalformedURLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }

    /** play random sound */
    public static void playRandom(Sound ... sounds) {
        int chance = new Random().nextInt(sounds.length);
        sounds[chance].play();
    }

    /** play once random sound and destroy it sound */
    public static void playRandomOnce(Sound ... sounds) {
        int chance = new Random().nextInt(sounds.length);
        sounds[chance].playOnce();
    }
}
