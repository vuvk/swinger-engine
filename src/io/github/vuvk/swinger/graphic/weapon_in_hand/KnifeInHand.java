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

import io.github.vuvk.swinger.audio.SoundBank;
import io.github.vuvk.swinger.graphic.Renderer;
import io.github.vuvk.swinger.objects.weapon.AmmoType;
import io.github.vuvk.swinger.res.Image;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class KnifeInHand extends RayWeaponInHand {
    private static final int FRAMES_COUNT = 7;
    private static final Image[] FRAMES = new Image[FRAMES_COUNT];
    private static final String[] FILES = {
        "resources/pics/weapons/knife0.png",
        "resources/pics/weapons/knife1.png",
        "resources/pics/weapons/knife2.png",
        "resources/pics/weapons/knife3.png"
    };
    private static volatile KnifeInHand instance = null;
    private static final double ACCURACY = 0.0;
    private static final int BULLETS_PER_SHOOT = 1;
    private static final double DISTANCE = 1.25;
    private static final double ANIM_SPEED = 25.0;
    private static final double DAMAGE = 40.0;
    private static final int FRAME_FOR_SHOOT = 3;
    private static final AmmoType AMMO_TYPE = AmmoType.NOTHING;

    @Override
    public void init() {
        setFrames(FRAMES);
        setDistance(DISTANCE);
        setDamage(DAMAGE);
        setAnimSpeed(ANIM_SPEED);
        setFrameForShoot(FRAME_FOR_SHOOT);
        setAccuracy(ACCURACY);
        setBulletPerShoot(BULLETS_PER_SHOOT);
        setSoundShoot(SoundBank.SOUND_BUFFER_KNIFE);
        setAmmoType(AMMO_TYPE);
    }

    private KnifeInHand() {
        super();
        init();
    }

    public static void loadFrames() {
        int rW = Renderer.WIDTH;
        int rH = Renderer.HEIGHT;

        int min = Math.min(rW, rH);
        int size = min >> 1;

        for (int i = 0; i < FILES.length; ++i) {
            FRAMES[i] = new Image(FILES[i], size, size);
        }
        for (int i = 4; i < 7; ++i) {
            FRAMES[i] = FRAMES[7 - i];
        }
    }

    public static synchronized KnifeInHand getInstance() {
        if (instance == null) {
            synchronized (KnifeInHand.class) {
                if (instance == null) {
                    instance = new KnifeInHand();
                }
            }
        }
        return instance;
    }
}
