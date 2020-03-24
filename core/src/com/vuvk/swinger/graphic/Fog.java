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
package com.vuvk.swinger.graphic;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
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
