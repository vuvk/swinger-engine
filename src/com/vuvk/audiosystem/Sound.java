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

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class Sound extends AudioSource {
    private SoundBuffer buffer = null;

    public Sound() {
        super();
        setRelative(true);
    }

    @Override
    public void dispose() {
        setBuffer(null);
        
        super.dispose();

        AudioSystem.SOUNDS.remove(this);
    }

    public SoundBuffer getBuffer() {
        return buffer;
    }

    boolean setBuffer(SoundBuffer buffer) {
        this.buffer = buffer;
        if (AudioSystem.isInited() && source[0] != 0) {
            if (buffer != null && buffer.getBuffer()[0] != 0) {
                AudioSystem.al.alSourcei(source[0], AL.AL_BUFFER, buffer.getBuffer()[0]);
            } else {
                AudioSystem.al.alSourcei(source[0], AL.AL_BUFFER, 0);
            }
            return AudioSystem.al.alGetError() == AL.AL_NO_ERROR;
        } else {
            return false;
        }
    }

    @Override
    public Sound setLooping(boolean looping) {
        if (AudioSystem.isInited() && source[0] != 0) {
            AudioSystem.al.alSourcei(source[0], AL.AL_LOOPING, (looping) ? 1 : 0);
        }
        return (Sound) super.setLooping(looping);
    }

    @Override
    public Sound play() {
        if (AudioSystem.isInited() && source[0] != 0) {
            setVolume(AudioSystem.getSoundsVolume());
            AudioSystem.al.alSourcePlay(source[0]);
        }
        return this;
    }

    @Override
    public Sound pause() {
        if (AudioSystem.isInited() && source[0] != 0) {
            AudioSystem.al.alSourcePause(source[0]);
        }
        return this;
    }

    @Override
    public Sound stop() {
        if (AudioSystem.isInited() && source[0] != 0) {
            if (!isStopped()) {
                AudioSystem.al.alSourceStop(source[0]);
            }
        }
        return this;
    }
}
