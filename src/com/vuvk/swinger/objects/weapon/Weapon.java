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
package com.vuvk.swinger.objects.weapon;

import com.vuvk.audiosystem.AudioSystem;
import com.vuvk.swinger.audio.SoundBank;
import com.vuvk.swinger.graphic.weapon_in_hand.AmmoPack;
import com.vuvk.swinger.graphic.weapon_in_hand.WeaponInHand;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.Sprite;
import com.vuvk.swinger.objects.mortals.Player;
import com.vuvk.swinger.res.Material;
import java.io.Serializable;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public abstract class Weapon extends Sprite implements Serializable {
    //private Player PLAYER = Player.getInstance();
    //private final static List<Key> LIB = new ArrayList<>();

    private int weaponNum;
    private AmmoType ammoType;
    private int ammoInClip;

    public Weapon(Material material, Vector3 pos, int weaponNum, AmmoType ammoType) {
        super(material, pos);
        this.weaponNum = weaponNum;
        this.ammoType = ammoType;
    }

    public void setWeaponNum(int weaponNum) {
        this.weaponNum = weaponNum;
    }

    public void setAmmoType(AmmoType ammoType) {
        this.ammoType = ammoType;
    }

    public int getWeaponNum() {
        return weaponNum;
    }

    public AmmoType getAmmoType() {
        return ammoType;
    }

    abstract public WeaponInHand getInstance();

    public Weapon setAmmoInClip(int ammoInClip) {
        this.ammoInClip = ammoInClip;
        return this;
    }

    /**
     * Получить количество патронов в обойме
     * @return число патронов
     */
    public int getAmmoInClip() {
        return ammoInClip;
    };

    @Override
    public void update() {
        super.update();

        Player player = Player.getInstance();

        if (player.getPos().distance(getPos()) < 0.5) {
            AudioSystem.newSound(SoundBank.SOUND_BUFFER_GET_WEAPON).playOnce();
            player.addWeaponInHand(this);

            // добавить игроку патронов того же типа, что и оружие
            int curAmmo = AmmoPack.PACK.get(ammoType);
            curAmmo += ammoInClip;
            int maxAmmo = ammoType.getMax();
            if (curAmmo > maxAmmo) {
                curAmmo = maxAmmo;
            }
            AmmoPack.PACK.put(ammoType, curAmmo);

            destroy();
        }
    }
}
