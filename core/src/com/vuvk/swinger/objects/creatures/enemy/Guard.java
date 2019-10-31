/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.objects.creatures.enemy;

//import com.vuvk.retard_sound_system.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.vuvk.swinger.audio.SoundBank;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.res.MaterialBank;

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
    private final static FileHandle[] ATK_SOUNDS = { SoundBank.FILE_PISTOL };
    private final static FileHandle[] ALARM_SOUNDS = { 
        SoundBank.FILE_ALARM1,
        SoundBank.FILE_ALARM2,
        SoundBank.FILE_ALARM3,
        SoundBank.FILE_ALARM4
    };
    private final static FileHandle[] PAIN_SOUNDS = {
        SoundBank.FILE_PAIN1,
        SoundBank.FILE_PAIN2
    };
    private final static FileHandle[] DIE_SOUNDS  = {
        SoundBank.FILE_DIE1,
        SoundBank.FILE_DIE2
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
    protected FileHandle[] getAlarmSounds() {
        return ALARM_SOUNDS;
    }

    @Override
    protected FileHandle[] getAttackSounds() {
        return ATK_SOUNDS;
    }

    @Override
    protected FileHandle[] getPainSounds() {
        return PAIN_SOUNDS;
    }

    @Override
    protected FileHandle[] getDieSounds() {
        return DIE_SOUNDS;
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
