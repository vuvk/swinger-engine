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
package io.github.vuvk.swinger.utils;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public final class Utils {

    public static int limit(int value, int min, int max) {
        return (value < min) ? min :
               (value > max) ? max : value;
    }

    public static float limit(float value, float min, float max) {
        return (value < min) ? min :
               (value > max) ? max : value;
    }

    public static double limit(double value, double min, double max) {
        return (value < min) ? min :
               (value > max) ? max : value;
    }

    private Utils() {}
}
