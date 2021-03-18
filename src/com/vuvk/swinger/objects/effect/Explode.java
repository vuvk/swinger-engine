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
package com.vuvk.swinger.objects.effect;

import com.vuvk.retard_sound_system.Sound;
import com.vuvk.swinger.audio.SoundBank;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.res.TextureBank;
import java.io.Serializable;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class Explode extends Effect implements Serializable {
    private final static double ANIM_SPEED = 10.0;

    public Explode(Vector3 pos) {
        super(TextureBank.EXPLODE, ANIM_SPEED, true, pos);
//        setBrightness(Fog.END);
        new Sound(SoundBank.SOUND_BUFFER_EXPLODE).play();
    }
}
