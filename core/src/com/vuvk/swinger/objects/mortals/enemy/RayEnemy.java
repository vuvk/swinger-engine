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
package com.vuvk.swinger.objects.mortals.enemy;

import com.vuvk.swinger.math.Ray;
import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.mortals.Mortal;
import com.vuvk.swinger.objects.effect.Blood;
import com.vuvk.swinger.objects.effect.Puff;
import com.vuvk.swinger.res.Material;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public abstract class RayEnemy extends Enemy implements Serializable {
    
    protected RayEnemy(Material idle, 
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
    
    @Override
    protected void shoot() {
        double direction = getDirection();
        Vector2 dir;
        // делаем смещение направления с учетом кучности
        if (getAccuracy() != 0.0) {
            direction += -getAccuracy() * 0.5 + Math.random() * getAccuracy();
            double rad = Math.toRadians(direction);
            dir = new Vector2(Math.cos(rad), Math.sin(rad));
        } else {
            dir = getViewVector();
        }
                
        Vector2 collisionPoint = new Vector2();        
        Ray ray = new Ray(getPos(), dir, getMaxAttackDistance());
        
        double wallDist = ray.getSolid(new Vector2(), collisionPoint); 
        Mortal target = ray.getMortal(this);
        boolean targetShooted = false;  // была ли поражена живая цель
                            
        if (target != null) {
            double targetDist = getPos().distance(target.getPos());

            // если дистанция до стены меньше, чем до врага, значит луч встретил стену на пути, во врага не попал
            if (wallDist > targetDist) {
                /*if (target.getHealth() > 0.0)*/ { 
                    target.applyDamage(getDamage());
                    
                    List<Vector2> intersections = ray.getSegment().intersect(target.getBB());
                    // ищем ближайшую точку столкновения
                    collisionPoint = intersections.get(0);
                    double nearDist = collisionPoint.distance(pos);     
                    for (int i = 1; i < intersections.size(); ++i) {
                        Vector2 point = intersections.get(i);
                        double dist = point.distance(pos);
                        
                        if (dist < nearDist) {
                            nearDist = dist;
                            collisionPoint = point;
                        }
                    }
                    
                    // создать кровь в месте столкновения
                    if (target.isLive()) {
                        new Blood(new Vector3(collisionPoint))/*.markForAdd()*/;
                    } else {
                        new Puff(new Vector3(collisionPoint.sub(dir.mul(0.05))))/*.markForAdd()*/;   
                    }
                    targetShooted = true;
                }
            }
        }
        
        if (!targetShooted) {
            /*if (wallDist <= getMaxAttackDistance())*/ {
                // создать пыль в месте столкновения со стеной
                // немного смещаем точку назад, чтобы было видно эффект
                new Puff(new Vector3(collisionPoint.sub(dir.mul(0.05))))/*.markForAdd()*/;     
            }             
        }        
    }    
}
