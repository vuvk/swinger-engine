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
package io.github.vuvk.swinger.graphic.weapon_in_hand;

import io.github.vuvk.swinger.objects.weapon.AmmoType;
import io.github.vuvk.swinger.utils.Utils;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public final class AmmoPack implements Serializable {
    private final static Map<AmmoType, Integer> PACK = new HashMap<>();

    public static void reset() {
        PACK.clear();
        PACK.put(AmmoType.NOTHING, 0 );
        PACK.put(AmmoType.PISTOL,  20);
        PACK.put(AmmoType.SHOTGUN, 0 );
        PACK.put(AmmoType.ROCKET,  0 );
    }

    public static int getNum(AmmoType ammoType) {
        return PACK.get(ammoType);
    }

    public static final Map<AmmoType, Integer> getPack() {
        return PACK;
    }

    public static void setNum(AmmoType ammoType, int volume) {
        PACK.put(
            ammoType,
            Utils.limit(
                volume,
                0,
                ammoType.getMax()
            )
        );
    }

    public static void setPack(Map<AmmoType, Integer> pack) {
        reset();

        Integer pistolAmmo  = pack.get(AmmoType.PISTOL);
        Integer shotgunAmmo = pack.get(AmmoType.SHOTGUN);
        Integer rocketAmmo  = pack.get(AmmoType.ROCKET);

        if (pistolAmmo  != null) { PACK.put(AmmoType.PISTOL,  pistolAmmo);  }
        if (shotgunAmmo != null) { PACK.put(AmmoType.SHOTGUN, shotgunAmmo); }
        if (rocketAmmo  != null) { PACK.put(AmmoType.ROCKET,  rocketAmmo);  }
    }

    private AmmoPack() {}
}
