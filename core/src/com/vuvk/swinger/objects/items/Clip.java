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
package com.vuvk.swinger.objects.items;

import com.vuvk.swinger.audio.SoundBank;
import com.vuvk.swinger.audio.SoundSystem;
import com.vuvk.swinger.graphic.weapon_in_hand.AmmoPack;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.Sprite;
import com.vuvk.swinger.objects.mortals.Player;
import com.vuvk.swinger.objects.weapon.AmmoType;
import com.vuvk.swinger.res.Material;
import java.io.Serializable;

/**
 * Валяющиеся обоймы
 * @author Anton "Vuvk" Shcherbatykh
 */
public class Clip extends Sprite implements Serializable {
    private final AmmoType ammoType;
    private final int volume;

    public Clip(Material material, Vector3 pos, AmmoType ammoType, int volume) {
        super(material, pos);
        this.ammoType = ammoType;
        this.volume = volume;
    }

    public void update() {
        super.update();

        Player player = Player.getInstance();

        if (player != null && player.getPos().distance(getPos()) < 0.5) {
            int curAmmo = AmmoPack.PACK.get(ammoType);
            int maxAmmo = ammoType.getMax();

            if (curAmmo < maxAmmo) {
                curAmmo += volume;
                if (curAmmo > maxAmmo) {
                    curAmmo = maxAmmo;
                }
                AmmoPack.PACK.put(ammoType, curAmmo);

                SoundSystem.playOnce(SoundBank.FILE_GET_AMMO);

                destroy();
            }
        }
    }
}
