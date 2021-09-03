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
package io.github.vuvk.swinger.objects.items;

import io.github.vuvk.audiosystem.AudioSystem;
import io.github.vuvk.audiosystem.Sound;
import io.github.vuvk.swinger.audio.SoundBank;
import io.github.vuvk.swinger.math.Vector3;
import io.github.vuvk.swinger.objects.Sprite;
import io.github.vuvk.swinger.objects.mortals.Player;
import io.github.vuvk.swinger.res.Material;
import java.io.Serializable;

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
                AudioSystem.playRandomOnce(new Sound[] {
                    AudioSystem.newSound(SoundBank.SOUND_BUFFER_GET_MEDKIT1),
                    AudioSystem.newSound(SoundBank.SOUND_BUFFER_GET_MEDKIT2)
                });

                destroy();
            }
        }
    }
}
