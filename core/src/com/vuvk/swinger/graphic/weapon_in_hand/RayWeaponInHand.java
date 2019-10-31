/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.graphic.weapon_in_hand;

import com.vuvk.swinger.math.Ray;
import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.creatures.enemy.Enemy;
import com.vuvk.swinger.objects.creatures.Player;
import com.vuvk.swinger.objects.effect.Blood;
import com.vuvk.swinger.objects.effect.Puff;
import java.util.List;

/**
 *
 * @author tai-prg3
 */
public abstract class RayWeaponInHand extends WeaponInHand {
    protected RayWeaponInHand() {
        super();
    }
    
    @Override
    protected void shoot(double direction) {        
        double rad = Math.toRadians(direction);
        Vector2 dir = new Vector2(Math.cos(rad), Math.sin(rad));
        
        Player player = Player.getInstance();        
        Vector3 pos = player.getPos();
        Vector2 plane = player.getCamera().getPlane();
        
        Vector2 collisionPoint = new Vector2();        
        Ray ray = new Ray(pos.add(dir.mul(player.getRadius())), dir, getDistance());
        
        double wallDist = ray.getSolid(new Vector2(), collisionPoint); 
        Enemy target = (Enemy) ray.getCreature(player);
        boolean targetShooted = false;  // была ли поражена живая цель
                            
        if (target != null) {
            Vector3 diff = target.getPos().sub(pos);
            // дистанция считается, как transform.y при расчете проекции на экране
            double targetDist = (-plane.y * diff.x + plane.x * diff.y) / (plane.x * dir.y - dir.x * plane.y);

            // если дистанция до стены меньше, чем до врага, значит луч встретил стену на пути, во врага не попал
            if (wallDist > targetDist) {
                if (target.getHealth() > 0.0) { 
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
                    new Blood(new Vector3(collisionPoint))/*.markForAdd()*/;
                    targetShooted = true;
                }
            }
        }
        
        if (!targetShooted) {
            if (wallDist <= getDistance()) {
                // создать пыль в месте столкновения со стеной
                // немного смещаем точку назад, чтобы было видно эффект
                new Puff(new Vector3(collisionPoint.sub(dir.mul(0.05))))/*.markForAdd()*/;     
            }             
        }
    }
}
