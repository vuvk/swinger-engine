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
    
    public final static float   START = 2.0f;
    public final static float   END   = 8.0f;
    public final static float   FACTOR = 1.0f / (END - START);
    public final static int      SIMPLE_QUALITY = 8;  // чем больше, тем лучше качество
    public final static float   SIMPLE_DISTANCE_STEP = (END - START) / SIMPLE_QUALITY;
    public final static float   INV_SIMPLE_DISTANCE_STEP = 1.0f / SIMPLE_DISTANCE_STEP;
    public final static float[] SIMPLE_BRIGHTNESS = new float[SIMPLE_QUALITY];
    
    public static void init() {
        final float brightnessStep = 1.0f / SIMPLE_QUALITY;
        float brightness = 1.0f - brightnessStep;
        for (int i = 0; i < SIMPLE_QUALITY; ++i) {
            SIMPLE_BRIGHTNESS[i] = brightness;
            brightness -= brightnessStep;
        }
    }
}
