/**
    Copyright (C) 2019-2021 Anton "Vuvk" Shcherbatykh <vuvk69@gmail.com>
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
    OLDSCHOOL,
    LINEAR;

    public static int COLOR = 0xFF;
    public static int RED;
    public static int GREEN;
    public static int BLUE;

    public static double   START = 2.0;
    public static double   END   = 8.0;
    public static int      OLDSCHOOL_QUALITY = 8;  // чем больше, тем лучше качество

    // предрасчитанная таблица яркостей тумана в точке по координате Y
    // для олдскульного тумана
    public static double[] OLDSCHOOL_TABLE;
    // для линейного тумана
    public static double[] LINEAR_TABLE;

    public static void init() {
        RED   = (COLOR >> 24) & 0xFF;
        GREEN = (COLOR >> 16) & 0xFF;
        BLUE  = (COLOR >>  8) & 0xFF;

        double factor = 1.0 / (END - START);

        // предварительный расчет таблицы с шагами яркости тумана
        // для олдскульного тумана
        double oldschoolDistanceStep = (END - START) / OLDSCHOOL_QUALITY;
        double invOldschoolDistanceStep = 1.0 / oldschoolDistanceStep;
        // таблица с яркостями для олдскул-тумана
        double[] oldschoolBrightnesses = new double[OLDSCHOOL_QUALITY];
        
        final double brightnessStep = 1.0 / OLDSCHOOL_QUALITY;
        double brightness = brightnessStep;
        for (int i = 0; i < OLDSCHOOL_QUALITY; ++i) {
            oldschoolBrightnesses[i] = brightness;
            brightness += brightnessStep;
        }
        
        // считаем таблицу для олдскульного тумана
        OLDSCHOOL_TABLE = new double[Renderer.HEIGHT];
        for (int y = 0; y < OLDSCHOOL_TABLE.length; ++y) {
            double fogBrightness;
            if (Renderer.DISTANCES[y] < START) {
                fogBrightness = 0.0;
            } else if (Renderer.DISTANCES[y] >= END) {
                fogBrightness = 1.0;
            } else {
                int pos = (int)((Renderer.DISTANCES[y] - START) * invOldschoolDistanceStep);
                fogBrightness = oldschoolBrightnesses[pos];
            }
            
            OLDSCHOOL_TABLE[y] = fogBrightness;
        }
                
        // считаем таблицу для линейного тумана
        LINEAR_TABLE = new double[Renderer.HEIGHT];
        for (int y = 0; y < LINEAR_TABLE.length; ++y) {
            LINEAR_TABLE[y] = (Renderer.DISTANCES[y] - START) * factor;
        }
    }
}
