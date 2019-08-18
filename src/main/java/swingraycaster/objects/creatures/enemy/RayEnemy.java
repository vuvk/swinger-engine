/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.objects.creatures.enemy;

import java.awt.Point;
import java.util.List;
import swingraycaster.math.Ray;
import swingraycaster.math.Vector2;
import swingraycaster.math.Vector3;
import swingraycaster.objects.creatures.Creature;
import swingraycaster.objects.creatures.Player;
import swingraycaster.objects.effect.Blood;
import swingraycaster.objects.effect.Puff;
import swingraycaster.res.Material;

/**
 *
 * @author tai-prg3
 */
public abstract class RayEnemy extends Enemy {
    
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
        
        double wallDist = ray.getSolid(new Point(), collisionPoint); 
        Creature target = ray.getCreature(this);
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
                    new Blood(new Vector3(collisionPoint))/*.markForAdd()*/;
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
