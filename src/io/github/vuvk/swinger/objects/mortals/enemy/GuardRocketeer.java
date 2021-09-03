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
package io.github.vuvk.swinger.objects.mortals.enemy;

import io.github.vuvk.audiosystem.AudioSystem;
import io.github.vuvk.audiosystem.Sound;
import io.github.vuvk.swinger.audio.SoundBank;
import io.github.vuvk.swinger.math.Vector3;
import io.github.vuvk.swinger.objects.projectiles.Projectile;
import io.github.vuvk.swinger.objects.projectiles.Rocket;
import io.github.vuvk.swinger.res.MaterialBank;
import java.io.Serializable;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class GuardRocketeer extends ProjectileEnemy implements Serializable {
    private final static double VIEW_ANGLE = 90;
    private final static double VIEW_DISTANCE = 10;
    private final static double MIN_ATK_DISTANCE = 2;
    private final static double MAX_ATK_DISTANCE = VIEW_DISTANCE;
    private final static double SHOOT_SPEED = 0.5;
    private final static double ACCURACY = 5.0;
    private final static int BULLETS_PER_SHOOT = 3;
    private final static double DAMAGE = 25.0;

    private final static double HEALTH = 100.0;
    private final static double RADIUS = 0.3;

    public GuardRocketeer(final Vector3 pos) {
        this(pos, 0.0);
    }

    public GuardRocketeer(final Vector3 pos, double direction) {
        super(MaterialBank.GUARD_STAND,
              MaterialBank.GUARD_ATK,
              MaterialBank.GUARD_WALK,
              MaterialBank.GUARD_PAIN,
              MaterialBank.GUARD_DIE,
              MaterialBank.GUARD_DEAD,
              pos, direction,
              HEALTH, RADIUS);

        attackSounds = new Sound[] { AudioSystem.newSound(SoundBank.SOUND_BUFFER_BAZOOKA) };
        alarmSounds = new Sound[] {
            AudioSystem.newSound(SoundBank.SOUND_BUFFER_ALARM1),
            AudioSystem.newSound(SoundBank.SOUND_BUFFER_ALARM2),
            AudioSystem.newSound(SoundBank.SOUND_BUFFER_ALARM3),
            AudioSystem.newSound(SoundBank.SOUND_BUFFER_ALARM4)
        };
        painSounds = new Sound[] {
            AudioSystem.newSound(SoundBank.SOUND_BUFFER_PAIN1),
            AudioSystem.newSound(SoundBank.SOUND_BUFFER_PAIN2)
        };
        dieSounds = new Sound[] {
            AudioSystem.newSound(SoundBank.SOUND_BUFFER_DIE1),
            AudioSystem.newSound(SoundBank.SOUND_BUFFER_DIE2)
        };
        
        attackSounds[0].setMaxDistance(10);

        setViewAngle(VIEW_ANGLE);
        setViewDistance(VIEW_DISTANCE);
        setMinAttackDistance(MIN_ATK_DISTANCE);
        setMaxAttackDistance(MAX_ATK_DISTANCE);
        setShootSpeed(SHOOT_SPEED);
        setAccuracy(ACCURACY);
        setBulletsPerShoot(BULLETS_PER_SHOOT);
        setDamage(DAMAGE);

        setLive(true);
    }

    @Override
    protected Projectile createProjectile(Vector3 pos, double direction) {
        return new Rocket(pos, direction);
    }
}
