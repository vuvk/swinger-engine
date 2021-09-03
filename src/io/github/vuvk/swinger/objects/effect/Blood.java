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
package io.github.vuvk.swinger.objects.effect;

import io.github.vuvk.swinger.Engine;
import io.github.vuvk.swinger.math.Vector2;
import io.github.vuvk.swinger.math.Vector3;
import io.github.vuvk.swinger.res.TextureBank;

import java.io.Serializable;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class Blood extends Effect implements Serializable {
    public final static double ANIM_SPEED = 7.5;
    private Vector2 direction;
    
    public Blood(Vector3 pos) {
        super(TextureBank.BLOOD, ANIM_SPEED, true, pos);
        
        double dir = Math.toRadians(Math.random() * 360.0);
        direction = new Vector2(Math.cos(dir), Math.sin(dir));
    }    
    
    @Override
    public void update() {
        super.update();
        double deltaTime = Engine.getDeltaTime();
        
        Vector3 pos = getPos();
        setPos(pos.add(direction.mul(deltaTime * 0.5)));
        getPos().z -= deltaTime;
    }
}
