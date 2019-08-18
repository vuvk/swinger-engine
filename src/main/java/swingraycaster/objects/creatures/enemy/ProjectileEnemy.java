/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.objects.creatures.enemy;

import swingraycaster.math.Vector2;
import swingraycaster.math.Vector3;
import swingraycaster.objects.projectiles.Projectile;
import swingraycaster.res.Material;

/**
 *
 * @author tai-prg3
 */
public abstract class ProjectileEnemy extends Enemy {
    
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
        
        createProjectile(getPos().add(getViewVector().mul(getRadius() * 2.0)), 
                         direction);
    }
}
