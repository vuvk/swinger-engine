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
import io.github.vuvk.swinger.math.Vector2;
import io.github.vuvk.swinger.objects.weapon.AmmoType;
import io.github.vuvk.swinger.res.Image;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class RifleInHand extends RayWeaponInHand {
    private static final int FRAMES_COUNT = 3;
    private static final Image[] FRAMES = new Image[FRAMES_COUNT];
    private static final String[] FILES = {
        "resources/pics/weapons/mp40_0.png",
        "resources/pics/weapons/mp40_1.png",
        "resources/pics/weapons/mp40_2.png"
    };
    private static volatile RifleInHand instance = null;
    private static final double ACCURACY = 5.0;
    private static final int BULLETS_PER_SHOOT = 1;
    private static final double DISTANCE = 100;
    private static final double ANIM_SPEED = 15.0;
    private static final double SHOOT_DELAY = 0.015;
    private static final double DAMAGE = 20.0;
    private static final int FRAME_FOR_SHOOT = 1;
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
        setSoundShoot(SoundBank.SOUND_BUFFER_RIFLE);
        setAmmoType(AMMO_TYPE);
    }

    private RifleInHand() {
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
            FRAMES[i].setCenter(new Vector2(0.6, 0.5));
        }
        //FRAMES[4] = FRAMES[1];
    }

    public static synchronized RifleInHand getInstance() {
        if (instance == null) {
            synchronized (RifleInHand.class) {
                if (instance == null) {
                    instance = new RifleInHand();
                }
            }
        }
        return instance;
    }
}
