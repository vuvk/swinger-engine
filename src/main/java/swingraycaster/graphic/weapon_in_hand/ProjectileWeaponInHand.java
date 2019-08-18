/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.graphic.weapon_in_hand;

import swingraycaster.math.Vector2;
import swingraycaster.math.Vector3;
import swingraycaster.objects.creatures.Player;
import swingraycaster.objects.projectiles.Projectile;

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
        playSoundShoot();
        
        Player player = Player.getInstance();
        createProjectile(player.getPos().add(player.getView().mul(player.getRadius() * 2.0)), 
                         direction);
    }
}
