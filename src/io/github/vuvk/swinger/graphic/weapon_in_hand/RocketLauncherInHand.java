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

import io.github.vuvk.swinger.objects.weapon.AmmoType;
import io.github.vuvk.swinger.graphic.Renderer;
import io.github.vuvk.swinger.math.Vector3;
import io.github.vuvk.swinger.objects.projectiles.Projectile;
import io.github.vuvk.swinger.objects.projectiles.Rocket;
import io.github.vuvk.swinger.res.Image;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class RocketLauncherInHand extends ProjectileWeaponInHand {    
    private final static int FRAMES_COUNT = 5;
    private final static Image[] FRAMES = new Image[FRAMES_COUNT];
    private final static String[] FILES = { 
        "resources/pics/weapons/rocketlauncher1.png", 
        "resources/pics/weapons/rocketlauncher2.png",
        "resources/pics/weapons/rocketlauncher3.png",
        "resources/pics/weapons/rocketlauncher4.png"
    };    
    private static RocketLauncherInHand instance = null;
    private final static double ACCURACY = 2.5;
    private final static int BULLETS_PER_SHOOT = 1;
    private final static double DISTANCE = 0;
    private final static double ANIM_SPEED = 15.0;
    private final static double SHOOT_DELAY = 0.0;
    private final static double DAMAGE = 25.0;
    private final static int FRAME_FOR_SHOOT = 2;
    private final static AmmoType AMMO_TYPE = AmmoType.ROCKET;

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
        // setSoundShoot(SoundBank.SOUND_BUFFER_BAZOOKA);
        setAmmoType(AMMO_TYPE);
    }
    
    private RocketLauncherInHand() {
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
        FRAMES[4] = FRAMES[1];
    }
    
    public static RocketLauncherInHand getInstance() {
        if (instance == null) {
            instance = new RocketLauncherInHand();
        }        
        return instance;
    }    

    @Override
    protected Projectile createProjectile(Vector3 pos, double direction) {
        return new Rocket(pos, direction);
    }
}
