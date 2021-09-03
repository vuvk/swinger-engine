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
package io.github.vuvk.swinger.graphic.gui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import io.github.vuvk.swinger.Engine;
import io.github.vuvk.swinger.graphic.Renderer;
import io.github.vuvk.swinger.math.Vector2;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class ScreenBlood {
    public static ScreenBlood[] drops;

    private final Vector2 pos;
    private double speed;

    public static final BufferedImage DROP = new BufferedImage(4, 4, BufferedImage.TYPE_INT_ARGB);
    static {
        for (int i = 0; i < 16; ++i) {
            DROP.setRGB(i % 4, i / 4, 0xFFFF0000);
        }
    }

    public ScreenBlood(Vector2 pos) {
        this.pos = pos;

        speed = new Random().nextDouble() * 100;
        if (speed == 0.0) {
            speed = 1.0;
        }
    }

    public Vector2 getPos() {
        return pos;
    }

    public double getSpeed() {
        return speed;
    }

    public void update() {
        if (pos.y < Renderer.HEIGHT) {
            pos.y += speed * Engine.getDeltaTime();
        }
    }
}
