/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.graphic.weapon_in_hand;

import swingraycaster.audio.SoundBank;
import swingraycaster.graphic.Renderer;
import swingraycaster.res.Image;

/**
 *
 * @author tai-prg3
 */
public class ShotgunInHand extends RayWeaponInHand {
    private final static int FRAMES_COUNT = 8;
    private final static Image[] FRAMES = new Image[FRAMES_COUNT];
    private final static String[] FILES = { 
        "resources/pics/weapons/Обрез кадр 1.png",
        "resources/pics/weapons/Обрез кадр 2.png",
        "resources/pics/weapons/Обрез кадр 3.png",
        "resources/pics/weapons/Обрез кадр 4.png",
        "resources/pics/weapons/Обрез кадр 5.png",
        "resources/pics/weapons/Обрез кадр 6.png",
        "resources/pics/weapons/Обрез кадр 7.png",
        "resources/pics/weapons/Обрез кадр 8.png",
    };   
    private static ShotgunInHand instance = null;
    private final static double ACCURACY = 15.0;
    private final static int BULLETS_PER_SHOOT = 10;
    private final static double DISTANCE = 100;
    private final static double ANIM_SPEED = 15.0;
    private final static double SHOOT_DELAY = 0.5;
    private final static double DAMAGE = 5.0;
    private final static int FRAME_FOR_SHOOT = 1;
    
    private ShotgunInHand() {
        super();
        setFrames(FRAMES);
        setDistance(DISTANCE);
        setDamage(DAMAGE);
        setAnimSpeed(ANIM_SPEED);
        setFrameForShoot(FRAME_FOR_SHOOT);
        setShootDelay(SHOOT_DELAY);
        setAccuracy(ACCURACY);
        setBulletPerShoot(BULLETS_PER_SHOOT);
        setSoundShoot(SoundBank.SOUND_BUFFER_SHOTGUN);
    }
    
    public static void loadFrames() {
        if (instance == null) {
            int rW = Renderer.WIDTH;
            int rH = Renderer.HEIGHT;

            int min = Math.min(rW, rH);        
            int size = min >> 1;

            for (int i = 0; i < FILES.length; ++i) {
                FRAMES[i] = new Image(FILES[i], size, size);
            }
            //FRAMES[4] = FRAMES[1];
        }
    }
    
    public static ShotgunInHand getInstance() {
        if (instance == null) {
            instance = new ShotgunInHand();
        }        
        return instance;
    }    
    
}
