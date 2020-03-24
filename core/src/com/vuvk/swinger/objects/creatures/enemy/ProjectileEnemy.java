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
package com.vuvk.swinger.objects.creatures.enemy;

import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.projectiles.Projectile;
import com.vuvk.swinger.res.Material;
import java.io.Serializable;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public abstract class ProjectileEnemy extends Enemy implements Serializable {
    
    protected ProjectileEnemy(Material idle, 
                              Material atk, 
                              Material walk, 
                              Material pain, 
                              Material die, 
                              Material dead, 
                              Vector3 pos,
                              double direction, 
                              double health, 
                              double radius
    ) {
        super(idle, atk, walk, pain, die, dead, pos, direction, health, radius);
    }    
     
    protected abstract Projectile createProjectile(Vector3 pos, double direction);
    
    @Override
    protected void shoot() {  
        double direction = getDirection();
        // делаем смещение направления с учетом кучности
        if (getAccuracy() != 0.0) {
            direction += -getAccuracy() * 0.5 + Math.random() * getAccuracy();
        }
        
        createProjectile(getPos().add(getViewVector().mul(getRadius() * 2)), 
                         direction);
    }
}
