package com.vuvk.audiosystem;

import com.jogamp.openal.AL;

/**
 *
 * @author vuvk
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
        AudioSystem.SOUNDS.remove(this);

        super.dispose();
    }

    public SoundBuffer getBuffer() {
        return buffer;
    }

    boolean setBuffer(SoundBuffer buffer) {
        this.buffer = buffer;
        if (buffer != null && buffer.getBuffer()[0] != 0) {
            AudioSystem.al.alSourcei(source[0], AL.AL_BUFFER, buffer.getBuffer()[0]);
        } else {
            AudioSystem.al.alSourcei(source[0], AL.AL_BUFFER, 0);
        }
        return AudioSystem.al.alGetError() == AL.AL_NO_ERROR;
    }

    @Override
    public Sound setLooping(boolean looping) {
        AudioSystem.al.alSourcei(source[0], AL.AL_LOOPING, (looping) ? 1 : 0);
        return (Sound) super.setLooping(looping);
    }

    @Override
    public Sound play() {
        AudioSystem.al.alSourcePlay(source[0]);
        return this;
    }

    @Override
    public Sound pause() {
        AudioSystem.al.alSourcePause(source[0]);
        return this;
    }

    @Override
    public Sound stop() {
        AudioSystem.al.alSourceStop(source[0]);
        return this;
    }
}
