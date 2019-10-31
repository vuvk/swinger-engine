/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.graphic.weapon_in_hand;

import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.creatures.Player;
import com.vuvk.swinger.objects.projectiles.Projectile;

/**
 *
 * @author tai-prg3
 */
public abstract class ProjectileWeaponInHand extends WeaponInHand {
    protected ProjectileWeaponInHand() {
        super();
    }
     
    protected abstract Projectile createProjectile(Vector3 pos, double direction);
    
    @Override
    protected void shoot(double direction) {
//        playSoundShoot();
        
        Player player = Player.getInstance();
        Vector2 view = player.getCamera().getView();
        createProjectile(player.getPos().add(view.mul(player.getRadius() * 2.0)), 
                         direction);
    }
}
