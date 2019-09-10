/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.graphic;

/**
 *
 * @author tai-prg3
 */
public enum Fog {
    NOTHING,
    OLD,
    SMOOTH;    
    
    public final static double   START = 2.0;
    public final static double   END   = 8.0;
    public final static double   FACTOR = 1.0 / (END - START);
    public final static int      SIMPLE_QUALITY = 8;  // чем больше, тем лучше качество
    public final static double   SIMPLE_DISTANCE_STEP = (END - START) / SIMPLE_QUALITY;
    public final static double   INV_SIMPLE_DISTANCE_STEP = 1.0 / SIMPLE_DISTANCE_STEP;
    public final static double[] SIMPLE_BRIGHTNESS = new double[SIMPLE_QUALITY];
    
    public static void init() {
        final double brightnessStep = 1.0 / SIMPLE_QUALITY;
        double brightness = 1.0 - brightnessStep;
        for (int i = 0; i < SIMPLE_QUALITY; ++i) {
            SIMPLE_BRIGHTNESS[i] = brightness;
            brightness -= brightnessStep;
        }
    }
}
