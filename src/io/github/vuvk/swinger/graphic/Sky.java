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
package io.github.vuvk.swinger.graphic;

import io.github.vuvk.swinger.objects.Camera;
import io.github.vuvk.swinger.res.Image;
import io.github.vuvk.swinger.res.TextureBank;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public final class Sky extends Image {
    private static volatile Sky instance;
    //private final static Player PLAYER = Player.getInstance();

    private final double xStep;
    private int xStart;

    public Sky(final String path) {
        super(path, Renderer.HEIGHT << 2, Renderer.HEIGHT);
        xStep = width / 360.0;
    }

    public int getXStart() {
        return xStart;
    }

    public static synchronized Sky getInstance() {
        if (instance == null) {
            synchronized (Sky.class) {
                if (instance == null) {
                    instance = new Sky(TextureBank.PICS_FOLDER + "sky.png");
                }
            }
        }
        return instance;
    }

    public void update() {
        Camera camera = Renderer.getInstance().getActiveCamera();
        if (camera == null) {
            xStart = 0;
        } else {
            xStart = (int)(camera.getDirection() * xStep);
            if (xStart >= width) {
                xStart = 0;
            }
        }
    }
}
