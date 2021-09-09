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
public class MinigunInHand  extends RayWeaponInHand {
    private static final int FRAMES_COUNT = 4;
    private static final Image[] FRAMES = new Image[FRAMES_COUNT];
    private static final String[] FILES = {
        "resources/pics/weapons/minigun_hand0.png",
        "resources/pics/weapons/minigun_hand1.png",
        "resources/pics/weapons/minigun_hand2.png",
        "resources/pics/weapons/minigun_hand3.png"
    };
    private static volatile MinigunInHand instance = null;
    private static final double ACCURACY = 6.0;
    private static final int BULLETS_PER_SHOOT = 1;
    private static final double DISTANCE = 100;
    private static final double ANIM_SPEED = 40.0;
    private static final double SHOOT_DELAY = 0.005;
    private static final double DAMAGE = 15.0;
    private static final int FRAME_FOR_SHOOT = 2;
    private static final AmmoType AMMO_TYPE = AmmoType.PISTOL;

    @Override
    public void init() {
        setFrames(FRAMES);
        setDistance(DISTANCE);
        setDamage(DAMAGE);
        setAnimSpeed(ANIM_SPEED);
        setFrameForShoot(FRAME_FOR_SHOOT);
        setShootDelay(SHOOT_DELAY);
        setAccuracy(ACCURACY);
        setBulletPerShoot(BULLETS_PER_SHOOT);
        setSoundShoot(SoundBank.SOUND_BUFFER_MINIGUN);
        setAmmoType(AMMO_TYPE);
    }

    private MinigunInHand() {
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
        //FRAMES[4] = FRAMES[1];
    }

    public static synchronized MinigunInHand getInstance() {
        if (instance == null) {
            synchronized (MinigunInHand.class) {
                if (instance == null) {
                    instance = new MinigunInHand();
                }
            }
        }
        return instance;
    }
}
