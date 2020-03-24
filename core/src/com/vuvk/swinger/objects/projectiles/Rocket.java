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
package com.vuvk.swinger.objects.projectiles;

import com.badlogic.gdx.Gdx;
import com.vuvk.swinger.graphic.Renderer;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.effect.Explode;
import com.vuvk.swinger.objects.effect.Smoke;
import com.vuvk.swinger.res.TextureBank;
import java.io.Serializable;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class Rocket extends Projectile  implements Serializable{
    private final static double DAMAGE = 100.0;
    private final static double RADIUS = 0.1;
    private final static double MOVE_SPEED = 5.0;
    private final static double ANIM_SPEED = 0.0;
    private double timeToSmoke = 0.0;
    
    public Rocket(Vector3 pos, double direction) {
        super(TextureBank.ROCKET, pos);
        setDirection(direction);
        setRadius(RADIUS);
        setMoveSpeed(MOVE_SPEED);
        setAnimSpeed(ANIM_SPEED);
        setDamage(DAMAGE);
    }

    @Override
    protected void createDestroyEffect() {
        new Explode(getPos())/*.markForAdd()*/;
    }
    
    @Override
    public void update() {
        super.update();
        
        if (timeToSmoke < 0.25) {
            timeToSmoke += Gdx.graphics.getDeltaTime();
        } else {
            timeToSmoke -= 0.25;
            new Smoke(getPos());
        }
    }
}
