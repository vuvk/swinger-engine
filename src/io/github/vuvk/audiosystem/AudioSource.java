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
package io.github.vuvk.audiosystem;

import com.jogamp.openal.AL;
import java.util.Arrays;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public abstract class AudioSource extends Disposable {
    protected int[] source = { 0 };
    /** play in loop? */
    private boolean looping = false;
    /** play once and destroy? */
    private boolean playOnce = false;

    // Position, Velocity, Direction of the source sound.
    protected final float[] position = { 0.0f, 0.0f, 0.0f };
    protected final float[] velocity = { 0.0f, 0.0f, 0.0f };
    protected final float[] direction = { 0.0f, 0.0f, 0.0f };

    protected AudioSource() {
        if (AudioSystem.isInited()) {
            AudioSystem.al.alGetError();
            AudioSystem.al.alGenSources(1, source, 0);
            if (source[0] != 0) {
                AudioSystem.checkError();
            }
        }

        setPosition(position);
        setVelocity(velocity);
        setDirection(direction);
    }

    public boolean isLooping() {
        return looping;
    }

    public boolean isPlayOnce() {
        return playOnce;
    }

    public final float[] getPosition() {
        return position;
    }

    public final float[] getVelocity() {
        return velocity;
    }

	public float[] getDirection() {
        return direction;
	}

    protected float getParamf(int param) {
        if (AudioSystem.isInited() && source[0] != 0) {
            float[] val = new float[1];
            AudioSystem.al.alGetSourcef(source[0], param, val, 0);
            return val[0];
        } else {
            return Float.NaN;
        }
    }

    protected int getParami(int param) {
        if (AudioSystem.isInited() && source[0] != 0) {
            int[] ret = new int[1];
            AudioSystem.al.alGetSourcei(source[0], AL.AL_SOURCE_STATE, ret, 0);
            return ret[0];
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public float getPitch() {
        return getParamf(AL.AL_PITCH);
    }

    public float getGain() {
        return getParamf(AL.AL_GAIN);
    }

    public float getMaxDistance() {
        return getParamf(AL.AL_MAX_DISTANCE);
    }

    public float getRollofFactor() {
        return getParamf(AL.AL_ROLLOFF_FACTOR);
    }

    private int getState() {
        return getParami(AL.AL_SOURCE_STATE);
    }

    public boolean isPlaying() {
        return (getState() == AL.AL_PLAYING);
    }

    public boolean isPaused() {
        return (getState() == AL.AL_PAUSED);
    }

    public boolean isStopped() {
        return (getState() == AL.AL_STOPPED);
    }

    public boolean isRelative() {
        return getParami(AL.AL_SOURCE_RELATIVE) == AL.AL_TRUE;
    }

    public AudioSource setPosition(final float[] position) {
        if (position.length < 3) {
            return this;
        }
        return setPosition(position[0], position[1], position[2]);
    }

    public AudioSource setPosition(final double[] position) {
        if (position.length < 3) {
            return this;
        }
        return setPosition((float) position[0], (float) position[1], (float) position[2]);
    }

    public AudioSource setPosition(float x, float y, float z) {
        if (AudioSystem.isInited() && source[0] != 0) {
            this.position[0] = x;
            this.position[1] = y;
            this.position[2] = z;
            AudioSystem.al.alSourcefv(source[0], AL.AL_POSITION, position, 0);
        } else {
            Arrays.fill(position, 0);
        }
        return this;
    }

    public AudioSource setVelocity(final float[] velocity) {
        if (velocity.length < 3) {
            return this;
        }
        return setVelocity(velocity[0], velocity[1], velocity[2]);
    }

    public AudioSource setVelocity(float x, float y, float z) {
        if (AudioSystem.isInited() && source[0] != 0) {
            this.velocity[0] = x;
            this.velocity[1] = y;
            this.velocity[2] = z;
            AudioSystem.al.alSourcefv(source[0], AL.AL_VELOCITY, velocity, 0);
        } else {
            Arrays.fill(velocity, 0);
        }
        return this;
    }

    public AudioSource setDirection(final float[] direction) {
        if (direction.length < 3) {
            return this;
        }
        return setDirection(direction[0], direction[1], direction[2]);
    }

    public AudioSource setDirection(float x, float y, float z) {
        if (AudioSystem.isInited() && source[0] != 0) {
            this.direction[0] = x;
            this.direction[1] = y;
            this.direction[2] = z;
            AudioSystem.al.alSourcefv(source[0], AL.AL_DIRECTION, direction, 0);
        } else {
            Arrays.fill(direction, 0);
        }
        return this;
    }

    public AudioSource setPlayOnce(boolean playOnce) {
        if (playOnce) {
            setLooping(false);
        }
        this.playOnce = playOnce;

        return this;
    }

    public AudioSource setPitch(float pitch) {
        if (AudioSystem.isInited() && source[0] != 0) {
            AudioSystem.al.alSourcef (source[0], AL.AL_PITCH, pitch);
        }
        return this;
    }

    public AudioSource setVolume(float gain) {
        if (AudioSystem.isInited() && source[0] != 0) {
            AudioSystem.al.alSourcef (source[0], AL.AL_GAIN, gain);
        }
        return this;
    }

    public AudioSource setLooping(boolean looping) {
        this.looping = looping;
        if (looping) {
            playOnce = false;
        }

        return this;
    }

    public AudioSource setRelative(boolean relative) {
        if (AudioSystem.isInited() && source[0] != 0) {
            AudioSystem.al.alSourcei(source[0], AL.AL_SOURCE_RELATIVE, (relative) ? AL.AL_TRUE : AL.AL_FALSE);
        }
        return this;
    }

    public AudioSource setMaxDistance(float distance) {
        if (AudioSystem.isInited() && source[0] != 0) {
            AudioSystem.al.alSourcef (source[0], AL.AL_MAX_DISTANCE, distance);
        }
        return this;
    }

    public AudioSource setRolloffFactor(float rollof) {
        if (AudioSystem.isInited() && source[0] != 0) {
            AudioSystem.al.alSourcef (source[0], AL.AL_ROLLOFF_FACTOR, rollof);
        }
        return this;
    }

    @Override
    public void dispose() {
        stop();

        if (AudioSystem.isInited() && source[0] != 0) {
            AudioSystem.al.alGetError();
            AudioSystem.al.alDeleteSources(1, source, 0);
            AudioSystem.checkError();
            source[0] = 0;
        }

        Arrays.fill(position, 0);
        Arrays.fill(velocity, 0);
        Arrays.fill(direction, 0);
    }

    public abstract AudioSource play();

    /** play once and destroy */
    public AudioSource playOnce() {
        setPlayOnce(true).play();
        return this;
    }

    public abstract AudioSource pause();

    public abstract AudioSource stop();
}
