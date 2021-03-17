/**
    Copyright (C) 2019-2020 Anton "Vuvk" Shcherbatykh <vuvk69@gmail.com>
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
package com.vuvk.swinger.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import java.util.Iterator;
import java.util.Random;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public final class SoundSystem {
    private final static Array<Music> LIB = new Array<>(false, 50);
    private static boolean started = false;

    private static Music MUSIC = null;
    private static float MUSIC_VOLUME = 1.0f;
    private static float VOLUME = 1.0f;

    private SoundSystem() {}

    public static void load() {
        started = true;

        Thread soundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (started) {
                    for (Iterator<Music> it = LIB.iterator(); it.hasNext(); ) {
                        Music sound = it.next();
                        if (sound != null) {
                            if (!sound.isPlaying()) {
                                sound.dispose();
                                sound = null;
                                it.remove();
                            }
                        }
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {}
                }

                for (Music sound : LIB) {
                    if (sound != null) {
                        sound.stop();
                        sound.dispose();
                        sound = null;
                    }
                }
                LIB.clear();
            }
        }, "SoundSystem Thread");

        soundThread.setPriority(Thread.MIN_PRIORITY);
        soundThread.start();
    }

    public static float getVolume() {
        return VOLUME;
    }

    public static float getMusicVolume() {
        return MUSIC_VOLUME;
    }

    public static void setVolume(float volume) {
        if (volume < 0.0f) {
            volume = 0.0f;
        } else {
            if (volume > 1.0f) {
                volume = 1.0f;
            }
        }
        VOLUME = volume;

        updateVolume();
    }

    public static void setMusicVolume(float volume) {
        if (volume < 0.0f) {
            volume = 0.0f;
        } else {
            if (volume > 1.0f) {
                volume = 1.0f;
            }
        }
        MUSIC_VOLUME = volume;

        updateVolume();
    }

    public static void updateVolume() {
        for (Music sound : LIB) {
            if (sound != null) {
                sound.setVolume(VOLUME);
            }
        }

        if (MUSIC != null) {
            MUSIC.setVolume(MUSIC_VOLUME);
        }
    }

    public static void unload() {
        started = false;
    }

    public static Music loadSound(FileHandle file) {
        Music sound = null;
        if (file != null && !file.isDirectory() && file.exists()) {
            try {
                sound = Gdx.audio.newMusic(file);
            } catch (Exception ignored) {}
        }

        return sound;
    }

    public static void play(Music sound, boolean looping) {
        if (sound == null) {
            return;
        }

        sound.stop();
        sound.setLooping(looping);
        sound.setVolume(VOLUME);
        sound.play();
        LIB.add(sound);
    }

    /**
     * Проиграть звук один раз, после чего он безопасно удаляется
     * @param path Путь
     */
    public static void playOnce(FileHandle file) {
        Music sound = loadSound(file);
        if (sound == null) {
            return;
        }

        sound.setVolume(VOLUME);
        sound.play();
        LIB.add(sound);
    }

    public static void playOnceRandom(FileHandle ... files) {
        if (files == null || files.length == 0) {
            return;
        }

        int variant = Math.abs(new Random().nextInt()) % files.length;
        playOnce(files[variant]);
    }

    /**
     * Проиграть случайный звук из массива (даже если какой-то из них уже проигрывается)
     * Play random sound
     * @param sounds Набор звуков
     */
    public static void playRandom(Sound ... sounds) {
        if (sounds == null || sounds.length == 0) {
            return;
        }

        for (Sound sound : sounds) {
            if (sound != null) {
                sound.stop();
            }
        }

        int variant = Math.abs(new Random().nextInt()) % sounds.length;
        Sound soundForPlay = sounds[variant];
        if (soundForPlay != null) {
            sounds[variant].play(VOLUME);
        }
    }

    /**
     * Проиграть случайный звук из массива (даже если какой-то из них уже проигрывается)
     * Play random sound
     * @param sounds Набор звуков
     */
    public static void playRandom(Music ... sounds) {
        playRandom(false, sounds);
    }

    /**
     * Проиграть случайный звук из массива
     * Play random sound
     * @param checkPlaying Проверка играется ли какой-то звук из массива, если да, то не играть
     * @param sounds Набор звуков
     */
    public static void playRandom(boolean checkPlaying, Music ... sounds) {
        if (sounds == null || sounds.length == 0) {
            return;
        }

        if (checkPlaying) {
            for (Music sound : sounds) {
                if (sound != null && sound.isPlaying()) {
                    return;
                }
            }
        } else {
            for (Music sound : sounds) {
                if (sound != null) {
                    sound.stop();
                }
            }
        }

        int variant = Math.abs(new Random().nextInt()) % sounds.length;
        Music soundForPlay = sounds[variant];
        if (soundForPlay != null) {
            sounds[variant].setVolume(VOLUME);
            sounds[variant].play();
        }
    }

    public static void playMusic(FileHandle music) {
        stopMusic();
        MUSIC = loadSound(music);
        if (MUSIC != null) {
            MUSIC.setLooping(true);
            MUSIC.setVolume(MUSIC_VOLUME);
            MUSIC.play();
        }
    }

    public static void stopMusic() {
        if (MUSIC != null) {
            MUSIC.stop();
            MUSIC.dispose();
            MUSIC = null;
        }
    }
}
