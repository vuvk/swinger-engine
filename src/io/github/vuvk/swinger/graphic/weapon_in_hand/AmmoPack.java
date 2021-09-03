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
import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public final class AmmoPack implements Serializable {
    public final static Map<AmmoType, Integer> PACK = new HashMap<>();
        
    public static void reset() {
        PACK.clear();
        PACK.put(AmmoType.NOTHING, 0 );
        PACK.put(AmmoType.PISTOL,  20);
        PACK.put(AmmoType.SHOTGUN, 0 );
        PACK.put(AmmoType.ROCKET,  0 );
    }
    
    private AmmoPack() {}
}
