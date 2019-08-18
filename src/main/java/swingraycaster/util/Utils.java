/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.util;

/**
 *
 * @author tai-prg3
 */
public final class Utils {
    private Utils() {}
    
    public static void arrayFastFill(final int[] array, final int value) {
        final int len = array.length;
        if (len > 0) {
            array[0] = value;
        }
        for (int i = 1; i < len; i += i) {
            System.arraycopy(array, 0, array, i,
                ((len - i) < i) ? (len - i) : i);
        }
    }    
    
    public static void arrayFastFill(final double[] array, final double value) {
        final int len = array.length;
        if (len > 0) {
            array[0] = value;
        }
        for (int i = 1; i < len; i += i) {
            System.arraycopy(array, 0, array, i,
                ((len - i) < i) ? (len - i) : i);
        }
    }    
    
    public static void arrayFastFill(final boolean[] array, final boolean value) {
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
    
    public static void arrayFastCopy(final Object[] src, final int srcPos, final Object[] dest, final int destPos, final int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }
}
