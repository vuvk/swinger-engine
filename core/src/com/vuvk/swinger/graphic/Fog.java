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

import com.vuvk.swinger.Config;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public enum Fog {
    NOTHING,
    OLD,
    SMOOTH;

    public static int COLOR = 0xFF;
    public static int RED;
    public static int GREEN;
    public static int BLUE;

    public static double   START = 2.0;
    public static double   END   = 8.0;
    public static double   FACTOR;
    public static int      SIMPLE_QUALITY;  // чем больше, тем лучше качество
    public static double   SIMPLE_DISTANCE_STEP;
    public static double   INV_SIMPLE_DISTANCE_STEP;
    public static double[] SIMPLE_BRIGHTNESS;
    public static int[]    GRADIENT_TABLE;

    public static void init() {
        RED   = (COLOR >> 24) & 0xFF;
        GREEN = (COLOR >> 16) & 0xFF;
        BLUE  = (COLOR >>  8) & 0xFF;

        FACTOR = 1.0f / (END - START);
        SIMPLE_QUALITY = 8;
        SIMPLE_DISTANCE_STEP = (END - START) / SIMPLE_QUALITY;
        INV_SIMPLE_DISTANCE_STEP = 1.0f / SIMPLE_DISTANCE_STEP;
        SIMPLE_BRIGHTNESS = new double[SIMPLE_QUALITY];

        final float brightnessStep = 1.0f / SIMPLE_QUALITY;
        float brightness = brightnessStep;
        for (int i = 0; i < SIMPLE_QUALITY; ++i) {
            SIMPLE_BRIGHTNESS[i] = brightness;
            brightness += brightnessStep;
        }

        GRADIENT_TABLE = new int[Config.HEIGHT];
    }
}
