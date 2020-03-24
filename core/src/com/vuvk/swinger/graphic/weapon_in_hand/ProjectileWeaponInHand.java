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
package com.vuvk.swinger.graphic.weapon_in_hand;

import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.creatures.Player;
import com.vuvk.swinger.objects.projectiles.Projectile;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
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
