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

import java.util.Arrays;
import java.util.Collection;

/**
 * class for queue of sounds
 * @author vuvk
 */
class SoundList {
    final static int MAX_SOUNDS = 8;        
    final Sound[] sounds = new Sound[MAX_SOUNDS];
    int size = 0;

    void add(Sound sound) {
        if (sound == null) {
            return;
        }

        if (!isFull()) {
            sounds[size++] = sound;
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

        Sound[] data = forAdd.getSounds();
        for (int i = 0; i < forAdd.getSize(); ++i) {
            if (!isFull()) {
                add(data[i]);
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
            for (int i = 0; i < size; ++i) {
                if (sounds[i].equals(sound)) {
                    sounds[i] = sounds[--size];
                    sounds[size] = null;
                    break;
                }
            }
        }
    }

    void removeAll(Collection<Sound> forDel) {
        for (Sound snd : forDel) {
            if (!isEmpty()) {
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

        Sound[] data = forDel.getSounds();
        for (int i = 0; i < forDel.getSize(); ++i) {
            if (!isEmpty()) {
                remove(data[i]);
            } else {
                break;
            }
        }
    }

    int getSize() {
        return size;
    }

    void clear() {
        Arrays.fill(sounds, null);
        size = 0;
    }

    boolean contains(Sound sound) {
        if (sound != null) { 
            for (int i = 0; i < size; ++i) {
                if (sounds[i].equals(sound)) {
                    return true;
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
        return sounds;
    }
}
