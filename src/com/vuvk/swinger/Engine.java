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
package com.vuvk.swinger;



/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class Engine {
    /** time of current frame */
    private long time = System.currentTimeMillis();
    /** time of previous frame */
    private long oldTime = time;
    /** */
    private static double deltaTime = 0;
    /** */
    private static int fps = 0;
    private int fpsCounter = 0;
    private double fpsTimer = 0;

    public void update() {
        // timing for input and FPS counter
        oldTime = time;
        time = System.currentTimeMillis();
        deltaTime = (time - oldTime) / 1000.0; //frameTime is the time this frame has taken, in seconds

        // calc FPS
        ++fpsCounter;
        fpsTimer += deltaTime;
        if (fpsTimer >= 1.0) {
            fpsTimer -= 1.0;
            fps = fpsCounter;
            fpsCounter = 0;
        }
    }

    /**
     * @return the deltaTime
     */
    public static double getDeltaTime() {
        return deltaTime;
    }

    /**
     * @return the fps
     */
    public static int getFps() {
        return fps;
    }
}