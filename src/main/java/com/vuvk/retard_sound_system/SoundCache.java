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

import javax.sound.sampled.SourceDataLine;

/**
 * class for caching sound data
 * @author vuvk
 */
class SoundCache {
    private SourceDataLine line;
    private final byte[] buffer;
    private int bufferSize = -1;

    SoundCache(int cacheSize, SourceDataLine line) {
        this.buffer = new byte[cacheSize];
        this.line = line;
    }

    void writeToLine() {
        int offset = 0;
        int length = (bufferSize % 2 == 0) ? bufferSize : bufferSize + 1;

        if (SoundSystem.FAST_MODE) {
            int cnt;

            // пытаемся записать
            int maxSizeForWrite = line.getFormat().getChannels() * (SoundSystem.SAMPLE_SIZE_IN_BITS >> 3);
            int skips = 0;
            int maxSkips = SoundSystem.MAX_WRITE_LINE_TRIES * (length / maxSizeForWrite);
            while (offset < length && skips < maxSkips) {
                cnt = line.available();
                if (cnt > 0) {
                    int sizeForWrite = Math.min(cnt, maxSizeForWrite);
                    line.write(buffer, offset, sizeForWrite);
                    //offset += sizeForWrite;
                    offset += maxSizeForWrite;
                } else {
                    ++skips;
                }
            }
        } else {
            line.write(buffer, 0, length);                
        }        
    }

    void write(byte value) {
        if (isFull()) {
            writeToLine();                
            reset();
        }
        buffer[++bufferSize] = value;
    }

    void reset() {
        bufferSize = -1;
    }

    void drain() {
        if (!isEmpty()) {
            writeToLine();
            reset();
        }
    }

    boolean isFull() {
        return (bufferSize == buffer.length - 1);
    }

    boolean isEmpty() {
        return (bufferSize == -1);
    }
}