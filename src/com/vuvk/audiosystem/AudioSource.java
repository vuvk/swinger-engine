package com.vuvk.audiosystem;

import com.jogamp.openal.AL;

import java.util.Arrays;

/**
 *
 * @author vuvk
 */
public abstract class AudioSource extends Disposable {
    protected int[] source = new int[1];
    private boolean looping = false;

    // Position, Velocity, Direction of the source sound.
    protected final float[] position = { 0.0f, 0.0f, 0.0f };
    protected final float[] velocity = { 0.0f, 0.0f, 0.0f };
    protected final float[] direction = { 0.0f, 0.0f, 0.0f };

    protected AudioSource() {
        AudioSystem.al.alGenSources(1, source, 0);
        AudioSystem.checkError();

        setPosition(position);
        setVelocity(velocity);
        setDirection(direction);
    }

    public boolean isLooping() {
        return looping;
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

    public float getPitch() {
        return AudioSystem.al.alGetFloat(AL.AL_PITCH);
    }

    public float getGain() {
        return AudioSystem.al.alGetFloat(AL.AL_GAIN);
    }

    private int getParami(int param) {
        int[] ret = new int[1];
        AudioSystem.al.alGetSourcei(source[0], AL.AL_SOURCE_STATE, ret, 0);
        return ret[0];
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

    public AudioSource setPosition(float x, float y, float z) {
        this.position[0] = x;
        this.position[1] = y;
        this.position[2] = z;
        AudioSystem.al.alSourcefv(source[0], AL.AL_POSITION, position, 0);
        return this;
    }

    public AudioSource setVelocity(final float[] velocity) {
        if (velocity.length < 3) {
            return this;
        }
        return setVelocity(velocity[0], velocity[1], velocity[2]);
    }

    public AudioSource setVelocity(float x, float y, float z) {
        this.velocity[0] = x;
        this.velocity[1] = y;
        this.velocity[2] = z;
        AudioSystem.al.alSourcefv(source[0], AL.AL_VELOCITY, velocity, 0);
        return this;
    }

    public AudioSource setDirection(final float[] direction) {
        if (direction.length < 3) {
            return this;
        }
        return setDirection(direction[0], direction[1], direction[2]);
    }

    public AudioSource setDirection(float x, float y, float z) {
        this.direction[0] = x;
        this.direction[1] = y;
        this.direction[2] = z;
        AudioSystem.al.alSourcefv(source[0], AL.AL_DIRECTION, direction, 0);
        return this;
    }

    public AudioSource setPlayOnce(boolean playOnce) {
        if (playOnce) {
            setLooping(false);
        }
        return this;
    }

    public AudioSource setPitch(float pitch) {
        AudioSystem.al.alSourcef (source[0], AL.AL_PITCH, pitch);
        return this;
    }

    public AudioSource setVolume(float gain) {
        AudioSystem.al.alSourcef (source[0], AL.AL_GAIN, gain);
        return this;
    }

    public AudioSource setLooping(boolean looping) {
        this.looping = looping;
        return this;
    }

    public AudioSource setRelative(boolean relative) {
        AudioSystem.al.alSourcei(source[0], AL.AL_SOURCE_RELATIVE, (relative) ? AL.AL_TRUE : AL.AL_FALSE);
        return this;
    }

    @Override
    public void dispose() {
        stop();
        AudioSystem.al.alDeleteSources(1, source, 0);
        AudioSystem.checkError();

        source[0] = 0;

        Arrays.fill(position, 0);
        Arrays.fill(velocity, 0);
        Arrays.fill(direction, 0);
    }

    public abstract AudioSource play();

    public AudioSource playOnce() {
        setLooping(false).play();
        return this;
    }

    public abstract AudioSource pause();

    public abstract AudioSource stop();
}