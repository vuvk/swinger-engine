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

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class AudioListener {
    // Position of the listener.
    private final float[] position = new float[3];

    // Velocity of the listener.
    private final float[] velocity = new float[3];

    // Orientation of the listener. (first 3 elements are "at", second 3 are "up")
    private final float[] orientation = new float[6];

    AudioListener() {
        setPosition(0.0f, 0.0f, 0.0f);
        setVelocity(0.0f, 0.0f, 0.0f);
        setOrientation(0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f);
    }

    public float[] getPosition() {
        return position;
    }

    public float[] getVelocity() {
        return velocity;
    }

    public float[] getOrientation() {
        return orientation;
    }

    public AudioListener setPosition(final float[] position) {
        if (position.length < 3) {
            return this;
        }
        return setPosition(position[0], position[1], position[2]);
    }

    public AudioListener setPosition(final double[] position) {
        if (position.length < 3) {
            return this;
        }
        return setPosition((float) position[0], (float) position[1], (float) position[2]);
    }

    public AudioListener setPosition(float x, float y, float z) {
        if (AudioSystem.isInited()) {
            this.position[0] = x;
            this.position[1] = y;
            this.position[2] = z;
            AudioSystem.al.alListenerfv(AL.AL_POSITION, position, 0);
        }
        return this;
    }

    public AudioListener setVelocity(final float[] velocity) {
        if (velocity.length < 3) {
            return this;
        }
        return setVelocity(velocity[0], velocity[1], velocity[2]);
    }

    public AudioListener setVelocity(float x, float y, float z) {
        if (AudioSystem.isInited()) {
            this.velocity[0] = x;
            this.velocity[1] = y;
            this.velocity[2] = z;
            AudioSystem.al.alListenerfv(AL.AL_VELOCITY, velocity, 0);
        }
        return this;
    }

    public AudioListener setOrientation(final float[] orientation) {
        if (orientation.length < 6) {
            return this;
        }
        return setOrientation(
            orientation[0],
            orientation[1],
            orientation[2],
            orientation[3],
            orientation[4],
            orientation[5]
        );
    }

    public AudioListener setOrientation(final float[] at, final float[] up) {
        if (at.length < 3 || up.length < 3) {
            return this;
        }
        return setOrientation(
            at[0], at[1], at[2],
            up[0], up[1], up[2]
        );
    }

    public AudioListener setOrientation(float atX, float atY, float atZ, float upX, float upY, float upZ) {
        if (AudioSystem.isInited()) {
            orientation[0] = atX;
            orientation[1] = atY;
            orientation[2] = atZ;
            orientation[3] = upX;
            orientation[4] = upY;
            orientation[5] = upZ;
            AudioSystem.al.alListenerfv(AL.AL_ORIENTATION, orientation, 0);
        }
        return this;
    }
}
