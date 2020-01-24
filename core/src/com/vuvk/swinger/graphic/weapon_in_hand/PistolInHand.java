/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.graphic.weapon_in_hand;

import com.vuvk.swinger.objects.weapon.AmmoType;
import com.vuvk.swinger.audio.SoundBank;
import com.vuvk.swinger.graphic.Renderer;
import com.vuvk.swinger.res.Image;

/**
 *
 * @author tai-prg3
 */
public class PistolInHand extends RayWeaponInHand {
    private final static int FRAMES_COUNT = 4;
    private final static Image[] FRAMES = new Image[FRAMES_COUNT];
    private final static String[] FILES = { 
        "resources/pics/weapons/pistol0.png", 
        "resources/pics/weapons/pistol1.png",
        "resources/pics/weapons/pistol2.png"
    };
    private static PistolInHand instance = null;
    private final static double ACCURACY = 5.0;
    private final static int BULLETS_PER_SHOOT = 1;
    private final static double DISTANCE = 100;
    private final static double ANIM_SPEED = 15.0;
    private final static double SHOOT_DELAY = 0.5;
    private final static double DAMAGE = 25.0;
    private final static int FRAME_FOR_SHOOT = 1;
    private final static AmmoType AMMO_TYPE = AmmoType.PISTOL;

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
        setSoundShoot(SoundBank.FILE_PISTOL);
        setAmmoType(AMMO_TYPE);
    }
    
    private PistolInHand() {
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
        FRAMES[3] = FRAMES[2];
    }
    
    public static PistolInHand getInstance() {
        if (instance == null) {
            instance = new PistolInHand();
        }        
        return instance;
    }    
}