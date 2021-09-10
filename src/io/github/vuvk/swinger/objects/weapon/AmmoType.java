/**
    Copyright (C) 2019-2021 Anton "Vuvk" Shcherbatykh <vuvk69@gmail.com>
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

import java.io.Serializable;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public enum AmmoType implements Serializable {
    NOTHING (0  ),
    PISTOL  (250),
    SHOTGUN (50 ),
    ROCKET  (25 );

    private final int max;

    private AmmoType(int max) {
        this.max = max;
    }

    public int getMax() {
        return max;
    }
}
