package com.vuvk.audiosystem;

import com.jogamp.openal.AL;
import com.jogamp.openal.util.ALut;
import java.nio.ByteBuffer;

/**
 *
 * @author vuvk
 */
public class SoundBuffer extends Disposable {

    private final int[] buffer = new int[1];
    private final int[] format = new int[1];
    private final int[] size = new int[1];
    private final int[] freq = new int[1];
    private final int[] loop = new int[1];

    SoundBuffer() {}

    boolean load(String path) {
        if (AudioSystem.isInited()) {
            if (buffer[0] == 0) {
                dispose();
            }
            
            ByteBuffer[] data = new ByteBuffer[1];

            // Load wav data into a buffer.
            AudioSystem.al.alGetError();
            AudioSystem.al.alGenBuffers(1, buffer, 0);
            if (AudioSystem.checkError() != AL.AL_NO_ERROR) {
                return false;
            }

            ALut.alutLoadWAVFile(path, format, data, size, freq, loop);
            AudioSystem.al.alBufferData(buffer[0], format[0], data[0], size[0], freq[0]);
            data[0].clear();

            return (AudioSystem.checkError() == AL.AL_NO_ERROR);
        } else {
            return false;
        }
    }

    @Override
    public void dispose() {
        for (Sound sound : AudioSystem.SOUNDS) {
            if (sound != null && this.equals(sound.getBuffer())) {
                sound.stop();
                sound.setBuffer(null);
            }
        }

        if (buffer[0] != 0) {
            AudioSystem.al.alGetError();
            AudioSystem.al.alDeleteBuffers(1, buffer, 0);
            AudioSystem.checkError();
            buffer[0] = 0;
        }
        AudioSystem.SOUND_BUFFERS.remove(this);

        format[0] = size[0] = freq[0] = loop[0] = AL.AL_INVALID;
    }

    final int[] getBuffer() {
        return buffer;
    }

    final int[] getFormat() {
        return format;
    }

    public int getSize() {
        return size[0];
    }

    public int getFreq() {
        return freq[0];
    }

    public boolean isLoop() {
        return (loop[0] == 1);
    }
}
