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
package com.vuvk.utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Copy of java.io.ByteArrayInputStream without synchronizing
 */
public final class FastByteArrayInputStream extends InputStream {

    private byte[] buffer;
    private int pos;
    private int mark = 0;
    private int count;

    public FastByteArrayInputStream(byte[] buffer) {
        this(buffer, 0, buffer.length);
    }

    public FastByteArrayInputStream(byte[] buffer, int offset, int length) {
        this.buffer = buffer;
        this.pos = offset;
        this.count = Math.min(offset + length, buffer.length);
        this.mark = offset;
    }

    public int read() {
        return (pos < count)        ? 
                (buffer[pos++] & 0xff) : 
                -1;
    }

    public int read(byte[] buffer, int offset, int length) {
        if (buffer == null) {
            throw new NullPointerException();
        } else if (offset < 0 || length < 0 || length > buffer.length - offset) {
            throw new IndexOutOfBoundsException();
        }

        if (pos >= count) {
            return -1;
        }

        int available = count - pos;
        if (length > available) {
            length = available;
        }
        if (length <= 0) {
            return 0;
        }
        
        Utils.arrayFastCopy(this.buffer, pos, buffer, offset, length);
        pos += length;
        return length;
    }

    @Override
    public long skip(long num) {
        long available = count - pos;
        if (num <= available) {
            if (num >= 0) {
                pos += num;
                return num;
            }
        }

        return 0;
    }
    
    public int available() {
        return count - pos;
    }
    
    public boolean markSupported() {
        return true;
    }

    public void mark(int limit) {
        mark = pos;
    }

    public void reset() {
        pos = mark;
    }
}
