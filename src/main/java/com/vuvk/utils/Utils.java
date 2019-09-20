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

/**
 *
 * @author vuvk
 */
public final class Utils {
    private Utils() {}
    
    public static void arrayFastFill(byte[] array, byte value) {
        final int len = array.length;
        if (len > 0) {
            array[0] = value;
        }
        for (int i = 1; i < len; i += i) {
            System.arraycopy(array, 0, array, i,
                ((len - i) < i) ? (len - i) : i);
        }
    }  
    
    public static void arrayFastFill(short[] array, short value) {
        final int len = array.length;
        if (len > 0) {
            array[0] = value;
        }
        for (int i = 1; i < len; i += i) {
            System.arraycopy(array, 0, array, i,
                ((len - i) < i) ? (len - i) : i);
        }
    }  
    
    public static void arrayFastFill(char[] array, char value) {
        final int len = array.length;
        if (len > 0) {
            array[0] = value;
        }
        for (int i = 1; i < len; i += i) {
            System.arraycopy(array, 0, array, i,
                ((len - i) < i) ? (len - i) : i);
        }
    }  
    
    public static void arrayFastFill(int[] array, int value) {
        final int len = array.length;
        if (len > 0) {
            array[0] = value;
        }
        for (int i = 1; i < len; i += i) {
            System.arraycopy(array, 0, array, i,
                ((len - i) < i) ? (len - i) : i);
        }
    }    
    
    public static void arrayFastFill(long[] array, long value) {
        final int len = array.length;
        if (len > 0) {
            array[0] = value;
        }
        for (int i = 1; i < len; i += i) {
            System.arraycopy(array, 0, array, i,
                ((len - i) < i) ? (len - i) : i);
        }
    }    
    
    public static void arrayFastFill(float[] array, float value) {
        final int len = array.length;
        if (len > 0) {
            array[0] = value;
        }
        for (int i = 1; i < len; i += i) {
            System.arraycopy(array, 0, array, i,
                ((len - i) < i) ? (len - i) : i);
        }
    }    
    
    public static void arrayFastFill(double[] array, double value) {
        final int len = array.length;
        if (len > 0) {
            array[0] = value;
        }
        for (int i = 1; i < len; i += i) {
            System.arraycopy(array, 0, array, i,
                ((len - i) < i) ? (len - i) : i);
        }
    }    
    
    public static void arrayFastFill(boolean[] array, boolean value) {
        final int len = array.length;
        if (len > 0) {
            array[0] = value;
        }
        for (int i = 1; i < len; i += i) {
            System.arraycopy(array, 0, array, i,
                ((len - i) < i) ? (len - i) : i);
        }
    }    
    
    public static void arrayFastFill(final Object[] array, final Object value) {
        final int len = array.length;
        if (len > 0) {
            array[0] = value;
        }
        for (int i = 1; i < len; i += i) {
            System.arraycopy(array, 0, array, i,
                ((len - i) < i) ? (len - i) : i);
        }
    }
    
    public static void arrayFastCopy(final Object[] src, final Object[] dest) {
        System.arraycopy(src, 0, dest, 0, src.length);
    }
    
    public static void arrayFastCopy(final Object src, int srcPos, final Object dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }
}
