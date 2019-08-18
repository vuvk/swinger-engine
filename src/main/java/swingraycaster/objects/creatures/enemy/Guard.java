/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.objects.creatures.enemy;

import swingraycaster.audio.Sound;
import swingraycaster.audio.SoundBank;
import swingraycaster.math.Vector3;
import swingraycaster.res.MaterialBank;

/**
 *
 * @author tai-prg3
 */
public class Guard extends RayEnemy {
    private final static double VIEW_ANGLE = 90;
    private final static double VIEW_DISTANCE = 10;
    private final static double MIN_ATK_DISTANCE = 2;
    private final static double MAX_ATK_DISTANCE = VIEW_DISTANCE;
    private final static double SHOOT_DELAY = 0.5;
    private final static double ACCURACY = 5.0;
    private final static int BULLETS_PER_SHOOT = 1;
    private final static double DAMAGE = 25.0;
    
    
    private final static double HEALTH = 100.0;
    private final static double RADIUS = 0.3;
    private final Sound[] atkSounds = { new Sound(SoundBank.SOUND_BUFFER_PISTOL) };
    private final Sound[] alarmSounds = {
        new Sound(SoundBank.SOUND_BUFFER_ALARM1),
        new Sound(SoundBank.SOUND_BUFFER_ALARM2),
        new Sound(SoundBank.SOUND_BUFFER_ALARM3),
        new Sound(SoundBank.SOUND_BUFFER_ALARM4)
    };
    private final Sound[] painSounds = {
        new Sound(SoundBank.SOUND_BUFFER_PAIN1),
        new Sound(SoundBank.SOUND_BUFFER_PAIN2) 
    };
    private final Sound[] dieSounds  = {
        new Sound(SoundBank.SOUND_BUFFER_DIE1),
        new Sound(SoundBank.SOUND_BUFFER_DIE2) 
    };
    
    public Guard(final Vector3 pos, double direction) {
        super(MaterialBank.GUARD_STAND, 
              MaterialBank.GUARD_ATK,
              MaterialBank.GUARD_WALK,
              MaterialBank.GUARD_PAIN,
              MaterialBank.GUARD_DIE,
              MaterialBank.GUARD_DEAD,
              pos, direction,
              HEALTH, RADIUS);
    }

    @Override
    protected Sound[] getAlarmSounds() {
        return alarmSounds;
    }

    @Override
    protected Sound[] getAttackSounds() {
        return atkSounds;
    }

    @Override
    protected Sound[] getPainSounds() {
        return painSounds;
    }

    @Override
    protected Sound[] getDieSounds() {
        return dieSounds;
    }

    @Override
    protected double getViewDistance() {
        return VIEW_DISTANCE;
    }

    @Override
    protected double getViewAngle() {
        return VIEW_ANGLE;
    }

    @Override
    protected double getMinAttackDistance() {
        return MIN_ATK_DISTANCE;
    }

    @Override
    protected double getMaxAttackDistance() {
        return MAX_ATK_DISTANCE;
    }

    @Override
    protected double getShootDelay() {
        return SHOOT_DELAY;
    }

    @Override
    protected double getDamage() {
        return DAMAGE;
    }

    @Override
    protected double getAccuracy() {
        return ACCURACY;
    }

    @Override
    protected int getBulletsPerShoot() {
        return BULLETS_PER_SHOOT;
    }
}
