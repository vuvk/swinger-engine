/**
    Copyright (C) 2019-2020 Anton "Vuvk" Shcherbatykh <vuvk69@gmail.com>
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
package io.github.vuvk.utils;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public final class ArrayUtils {
    private ArrayUtils() {}
    
    public static void fill(byte[] array, byte value) {
        final int len = array.length;
        if (len > 0) {
            array[0] = value;
        }
        for (int i = 1; i < len; i += i) {
            System.arraycopy(array, 0, array, i,
                ((len - i) < i) ? (len - i) : i);
        }
    }  
    
    public static void fill(short[] array, short value) {
        final int len = array.length;
        if (len > 0) {
            array[0] = value;
        }
        for (int i = 1; i < len; i += i) {
            System.arraycopy(array, 0, array, i,
                ((len - i) < i) ? (len - i) : i);
        }
    }  
    
    public static void fill(char[] array, char value) {
        final int len = array.length;
        if (len > 0) {
            array[0] = value;
        }
        for (int i = 1; i < len; i += i) {
            System.arraycopy(array, 0, array, i,
                ((len - i) < i) ? (len - i) : i);
        }
    }  
    
    public static void fill(int[] array, int value) {
        final int len = array.length;
        if (len > 0) {
            array[0] = value;
        }
        for (int i = 1; i < len; i += i) {
            System.arraycopy(array, 0, array, i,
                ((len - i) < i) ? (len - i) : i);
        }
    }    
    
    public static void fill(long[] array, long value) {
        final int len = array.length;
        if (len > 0) {
            array[0] = value;
        }
        for (int i = 1; i < len; i += i) {
            System.arraycopy(array, 0, array, i,
                ((len - i) < i) ? (len - i) : i);
        }
    }    
    
    public static void fill(float[] array, float value) {
        final int len = array.length;
        if (len > 0) {
            array[0] = value;
        }
        for (int i = 1; i < len; i += i) {
            System.arraycopy(array, 0, array, i,
                ((len - i) < i) ? (len - i) : i);
        }
    }    
    
    public static void fill(double[] array, double value) {
        final int len = array.length;
        if (len > 0) {
            array[0] = value;
        }
        for (int i = 1; i < len; i += i) {
            System.arraycopy(array, 0, array, i,
                ((len - i) < i) ? (len - i) : i);
        }
    }    
    
    public static void fill(boolean[] array, boolean value) {
        final int len = array.length;
        if (len > 0) {
            array[0] = value;
        }
        for (int i = 1; i < len; i += i) {
            System.arraycopy(array, 0, array, i,
                ((len - i) < i) ? (len - i) : i);
        }
    }    
    
    public static void fill(final Object[] array, final Object value) {
        final int len = array.length;
        if (len > 0) {
            array[0] = value;
        }
        for (int i = 1; i < len; i += i) {
            System.arraycopy(array, 0, array, i,
                ((len - i) < i) ? (len - i) : i);
        }
    }
    
    public static void copy(final Object[] src, final Object[] dest) {
        System.arraycopy(src, 0, dest, 0, src.length);
    }
    
    public static void copy(final Object src, int srcPos, final Object dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }
}
