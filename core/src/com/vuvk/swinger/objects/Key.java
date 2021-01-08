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
package com.vuvk.swinger.objects;

//import com.vuvk.retard_sound_system.Sound;
import com.vuvk.swinger.audio.SoundBank;
import com.vuvk.swinger.audio.SoundSystem;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.mortals.Player;
import com.vuvk.swinger.res.Material;
import java.io.Serializable;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class Key extends Sprite implements Serializable {

    //private Player PLAYER = Player.getInstance();
    //private final static List<Key> LIB = new ArrayList<>();

    private int keyNum;

    public Key(Material material, Vector3 pos, int keyNum) {
        super(material, pos);
        this.keyNum = keyNum;
    //    LIB.add(this);
    }

    public int getKeyNum() {
        return keyNum;
    }

    public void update() {
        super.update();

        Player player = Player.getInstance();

        if (player.getPos().distance(getPos()) < 0.5) {
            SoundSystem.playOnce(SoundBank.FILE_GET_KEY);
            player.addKey(this);

            destroy();
        }
    }
}