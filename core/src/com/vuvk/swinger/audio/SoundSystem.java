/*
 * Copyright 2019 .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vuvk.swinger.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tai-prg3
 */
public final class SoundSystem {
    private final static Array<Music> LIB = new Array<>(false, 50);
    private static boolean started = false;
    
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
                                it.remove();
                            }
                        }
                    }
                    
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {}
                }
                
                for (Music sound : LIB) {
                    sound.dispose();
                }
                LIB.clear();
            }
        }, "SoundSystem Thread");
        
        soundThread.setPriority(Thread.MIN_PRIORITY);
        soundThread.start();
    }
    
    public static void unload() {
        started = false;
    }
        
    public static Music loadSound(FileHandle file) {
        return Gdx.audio.newMusic(file);
    }
    
    /**
     * Проиграть звук один раз, после чего он безопасно удаляется
     * @param path Путь
     */
    public static void playOnce(FileHandle file) {
        Music sound = loadSound(file);
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
            sound.stop();
        }

        int variant = Math.abs(new Random().nextInt()) % sounds.length;
        sounds[variant].play();
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
                if (sound.isPlaying()) {
                    return;
                }
            }
        } else {
            for (Music sound : sounds) {
                sound.stop();
            }
        }

        int variant = Math.abs(new Random().nextInt()) % sounds.length;
        sounds[variant].play();
    }
}
