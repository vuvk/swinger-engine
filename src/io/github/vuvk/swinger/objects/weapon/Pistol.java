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
package io.github.vuvk.swinger.objects.weapon;

import io.github.vuvk.swinger.graphic.weapon_in_hand.PistolInHand;
import io.github.vuvk.swinger.graphic.weapon_in_hand.WeaponInHand;
import io.github.vuvk.swinger.math.Vector3;
import io.github.vuvk.swinger.res.Material;
import java.io.Serializable;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class Pistol extends Weapon implements Serializable {
    
    private final static int WEAPON_NUM = 1;
    private final static AmmoType AMMO_TYPE = AmmoType.PISTOL;
    
    public Pistol(Material material, Vector3 pos) {
        super(material, pos, WEAPON_NUM, AMMO_TYPE);
    }

    @Override
    public WeaponInHand getInstance() {
        return PistolInHand.getInstance();
    }
}
