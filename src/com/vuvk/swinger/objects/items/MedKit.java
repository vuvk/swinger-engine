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

import java.io.Serializable;

import com.vuvk.retard_sound_system.Sound;
import com.vuvk.retard_sound_system.SoundSystem;
import com.vuvk.swinger.audio.SoundBank;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.Sprite;
import com.vuvk.swinger.objects.mortals.Player;
import com.vuvk.swinger.res.Material;

/**
 * Валяющиеся аптечки
 * @author Anton "Vuvk" Shcherbatykh
 */
public class MedKit extends Sprite implements Serializable {
    private final double volume;

    public MedKit(Material material, Vector3 pos, double volume) {
        super(material, pos);
        this.volume = volume;
    }

    public void update() {
        super.update();

        Player player = Player.getInstance();

        if (player != null && player.getPos().distance(getPos()) < 0.5) {
            double hp = player.getHealth();
            double maxHp = player.getMaxHealth();
            if (hp < maxHp) {
                hp += volume;
                if (hp > maxHp) {
                    hp = maxHp;
                }

                player.setHealth(hp);
                SoundSystem.playRandom(new Sound[] { 
                    new Sound(SoundBank.SOUND_BUFFER_GET_MEDKIT1),
                    new Sound(SoundBank.SOUND_BUFFER_GET_MEDKIT2) 
                });

                destroy();
            }
        }
    }
}
