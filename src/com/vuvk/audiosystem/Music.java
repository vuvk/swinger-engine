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
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class Music extends AudioSource {
    private static enum State {
        PLAYING,
        PAUSED,
        STOPPED
    };

    // The size of a chunk from the stream that we want to read for each update.
    private final static int BUFFER_SIZE = 4096*16;
    // The number of buffers used in the audio pipeline
    private final static int NUM_BUFFERS = 2;

    private OggDecoder oggDecoder;

    // Buffers hold sound data. There are two of them by default (front/back)
    private int[] buffers = new int[NUM_BUFFERS];
    private byte[] pcm = new byte[BUFFER_SIZE];

    private int format;	// OpenAL data format
    private int rate;	// sample rate

    private State state = State.STOPPED;
    private boolean opened = false;

    private URL url;

    public Music(URL url) {
        super();

        this.url = url;

        if (AudioSystem.isInited()) {
            AudioSystem.al.alGetError();
            AudioSystem.al.alGenBuffers(NUM_BUFFERS, buffers, 0);
            AudioSystem.checkError();
            AudioSystem.al.alSourcef(source[0], AL.AL_ROLLOFF_FACTOR,  0.0f);
        }

        setRelative(false);

        stop();
    }

    /**
     * Open the Ogg/Vorbis stream and initialize OpenAL based
     * on the stream properties
     */
    boolean open() {
        oggDecoder = new OggDecoder(url);

        if (!oggDecoder.initialize()) {
            System.err.println("Error initializing ogg stream...");
            stop();
            return false;
        }

        // TODO: I am not if this is the right way to fix the endian
        // problems I am having... but this seems to fix it on Linux
        oggDecoder.setSwap(true);

        int numChannels = oggDecoder.numChannels();
        int numBytesPerSample = 2;

        if (numChannels == 1) {
            format = AL.AL_FORMAT_MONO16;
        } else {
            format = AL.AL_FORMAT_STEREO16;
        }

        rate = oggDecoder.sampleRate();

        opened = true;

        return true;
    }

    /**
     * OpenAL cleanup
     */
    @Override
    public void dispose() {
        super.dispose();

        url = null;
        if (AudioSystem.isInited()) {
            for (int i = 0; i < buffers.length; ++i) {
                if (buffers[i] != 0) {
                    AudioSystem.al.alGetError();
                    AudioSystem.al.alDeleteBuffers(1, buffers, i);
                    AudioSystem.checkError();
                }
            }
            Arrays.fill(buffers, 0);
        }

        opened = false;
    }

    /**
     * Play the Ogg stream
     */
    boolean playback() {
        if (isPlaying()) {
            return true;
        }

        for (int i = 0; i < NUM_BUFFERS; i++) {
            if (!stream(buffers[i])) {
                return false;
            }
        }

        if (AudioSystem.isInited() && source[0] != 0) {
            AudioSystem.al.alSourceQueueBuffers(source[0], NUM_BUFFERS, buffers, 0);
            AudioSystem.al.alSourcePlay(source[0]);
        }

        return true;
    }

    /**
     * Update the stream if necessary
     */
    boolean update() {
        if (!AudioSystem.isInited() || source[0] <= 0) {
            return false;
        }

        int[] processed = new int[1];
        AudioSystem.al.alGetSourcei(source[0], AL.AL_BUFFERS_PROCESSED, processed, 0);

        while (processed[0] > 0) {
            int[] buffer = new int[1];

            AudioSystem.al.alSourceUnqueueBuffers(source[0], 1, buffer, 0);
            AudioSystem.checkError();

            if (!stream(buffer[0])) {
                return false;
            }

            AudioSystem.al.alSourceQueueBuffers(source[0], 1, buffer, 0);
            AudioSystem.checkError();

            processed[0]--;
        }

        return true;
    }

    /**
     * Reloads a buffer (reads in the next chunk)
     */
    private boolean stream(int buffer) {
        if (oggDecoder == null || !AudioSystem.isInited()) {
            opened = false;
            return false;
        }

        int size;

        try {
            if ((size = oggDecoder.read(pcm)) <= 0) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        ByteBuffer data = ByteBuffer.wrap(pcm, 0, size);
        AudioSystem.al.alBufferData(buffer, format, data, size, rate);
        AudioSystem.checkError();

        return true;
    }

    /**
     * Empties the queue
     */
    void clearQueue() {
        int[] queued = new int[1];

        AudioSystem.al.alGetSourcei(source[0], AL.AL_BUFFERS_QUEUED, queued, 0);

        while (queued[0] > 0) {
            int[] buffer = new int[1];

            AudioSystem.al.alSourceUnqueueBuffers(source[0], 1, buffer, 0);
            AudioSystem.checkError();

            queued[0]--;
        }

        oggDecoder = null;
    }

    @Override
    public boolean isPlaying() {
        return super.isPlaying() && state == State.PLAYING;
    }

    @Override
    public boolean isPaused() {
        return super.isPaused() && state == State.PAUSED;
    }

    @Override
    public boolean isStopped() {
        return super.isStopped() && state == State.STOPPED;
    }

    boolean isOpened() {
        return opened;
    }

    @Override
    public Music play() {
        /*
        if (!this.isPaused()) {
            // добавить в обслуживание, если ещё нет и может проигрываться
            if (!AudioSystem.MUSICS.contains(this) && open() && playback()) {
                AudioSystem.MUSICS.add(0, this);
            }
        } else {
            AudioSystem.al.alSourcePlay(source[0]);
        }*/

        if (this.isPlaying()) {
            return this;
        }

        if (!this.isPaused()) {
            if (!isOpened() && !open() && !playback()) {
                stop();
                return this;
            }
        }

        //playback();
        if (AudioSystem.isInited() && source[0] != 0) {
            setVolume(AudioSystem.getMusicsVolume());
            AudioSystem.al.alSourcePlay(source[0]);
        }
        state = State.PLAYING;

        return this;
    }

    @Override
    public Music pause() {
        if (AudioSystem.isInited() && source[0] != 0) {
            AudioSystem.al.alSourcePause(source[0]);
        }
        state = State.PAUSED;
        return this;
    }

    @Override
    public Music stop() {
        if (AudioSystem.isInited() && source[0] != 0) {
            AudioSystem.al.alSourceStop(source[0]);
        }
        clearQueue();
        state = State.STOPPED;
        opened = false;
        return this;
    }
}
