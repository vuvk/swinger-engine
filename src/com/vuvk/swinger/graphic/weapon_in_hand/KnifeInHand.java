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
package com.vuvk.swinger.graphic.weapon_in_hand;

import com.vuvk.retard_sound_system.Sound;
import com.vuvk.swinger.audio.SoundBank;
import com.vuvk.swinger.graphic.Renderer;
import com.vuvk.swinger.objects.weapon.AmmoType;
import com.vuvk.swinger.res.Image;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class KnifeInHand extends RayWeaponInHand {
    private final static int FRAMES_COUNT = 7;
    private final static Image[] FRAMES = new Image[FRAMES_COUNT];
    private final static String[] FILES = { 
        "resources/pics/weapons/knife0.png", 
        "resources/pics/weapons/knife1.png", 
        "resources/pics/weapons/knife2.png",
        "resources/pics/weapons/knife3.png"
    };    
    private static KnifeInHand instance = null;
    private final static double ACCURACY = 0.0;
    private final static int BULLETS_PER_SHOOT = 1;
    private final static double DISTANCE = 1.25;
    private final static double ANIM_SPEED = 25.0;
    private final static double DAMAGE = 40.0;
    private final static int FRAME_FOR_SHOOT = 3;
    private final static AmmoType AMMO_TYPE = AmmoType.NOTHING;

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
    
    public static KnifeInHand getInstance() {
        if (instance == null) {
            instance = new KnifeInHand();
        }
        
        return instance;
    }
}
