/**
    Copyright (C) 2021 Anton "Vuvk" Shcherbatykh <vuvk69@gmail.com>
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
package io.github.vuvk.swinger;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public final class Const {
    private Const() {}

    public static final int THREADS_COUNT = Runtime.getRuntime().availableProcessors() + 1;
    public static final boolean STEP_BY_STEP_RENDERING = false;
    public static final int STEP_BY_STEP_DELAY = 10; 
}
