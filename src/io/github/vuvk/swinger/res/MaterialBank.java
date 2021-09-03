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
package io.github.vuvk.swinger.res;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public final class MaterialBank {
    // guard
    public final static Material GUARD_ATK   = new Material(TextureBank.GUARD_ATK,  10.0f, true);
    public final static Material GUARD_STAND = new Material(TextureBank.GUARD_STAND, 0.0f, true);
    public final static Material GUARD_WALK  = new Material(TextureBank.GUARD_WALK,  7.5f, false);
    public final static Material GUARD_PAIN  = new Material(TextureBank.GUARD_PAIN,  0.0f, true);
    public final static Material GUARD_DIE   = new Material(TextureBank.GUARD_DIE,   7.5f, true);
    public final static Material GUARD_DEAD  = new Material(TextureBank.GUARD_DEAD,  0.0f, true);

    public static List<Material> BANK = new CopyOnWriteArrayList<>();

    public static Material[] getBank() {
        return BANK.toArray(new Material[BANK.size()]);
    }

    public static void deleteBank() {
        synchronized (BANK) {
            BANK.clear();
        }
    }

    private MaterialBank() {};
}
