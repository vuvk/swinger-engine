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
package com.vuvk.swinger.graphic.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ParticleControllerInfluencer;
import com.vuvk.swinger.graphic.Renderer;
import com.vuvk.swinger.math.Vector2;
import java.util.Random;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class ScreenBlood {
    public static ScreenBlood[] DROPS;
    
    private Vector2 pos;
    private double speed;
    
    public static final Pixmap DROP = new Pixmap(4, 4, Pixmap.Format.RGBA8888);
    static {        
        for (int x = 0; x < DROP.getWidth(); ++x) {
            for (int y = 0; y < DROP.getHeight(); ++y) {
                DROP.drawPixel(x, y, 0xff0000ff);                
            }
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
            pos.y += speed * Gdx.graphics.getDeltaTime();
        }
    }
}
