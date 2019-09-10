/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.audio;

import com.vuvk.retard_sound_system.SoundBuffer;
import java.io.File;

/**
 *
 * @author tai-prg3
 */
public final class SoundBank {    
    /* paths */
    /* effects */
    private final static String SOUND_FILE_EXPLODE = "resources/snd/weapons/Rocket Explode.wav";
    
    /* weapons */
    private final static String SOUND_FILE_KNIFE   = "resources/snd/weapons/Knife.wav";
    private final static String SOUND_FILE_PISTOL  = "resources/snd/weapons/Pistol.wav";
//  private final static String SOUND_FILE_SHOTGUN = "resources/snd/weapons/shotgun.wav";
    private final static String SOUND_FILE_RIFLE   = "resources/snd/weapons/Machine Gun.wav";
    private final static String SOUND_FILE_BAZOOKA = "resources/snd/weapons/Rocket Launcher.wav";
    
    /* enemies */
    private final static String SOUND_FILE_ALARM1 = "resources/snd/enemies/Achtung!.wav";
    private final static String SOUND_FILE_ALARM2 = "resources/snd/enemies/Halt.wav";
    private final static String SOUND_FILE_ALARM3 = "resources/snd/enemies/Halt 2.wav";
    private final static String SOUND_FILE_ALARM4 = "resources/snd/enemies/Halten Sie!.wav";
    private final static String SOUND_FILE_PAIN1  = "resources/snd/enemies/Enemy Pain.wav";
    private final static String SOUND_FILE_PAIN2  = "resources/snd/enemies/Oof!.wav";
    private final static String SOUND_FILE_DIE1   = "resources/snd/enemies/Death 1.wav";
    private final static String SOUND_FILE_DIE2   = "resources/snd/enemies/Death 2.wav";
    
    /* events */
    private final static String SOUND_FILE_DOOR_OPEN  = "resources/snd/world/door_start.wav";
    private final static String SOUND_FILE_DOOR_CLOSE = "resources/snd/world/door_stop.wav";
    
    /* items */
    private final static String SOUND_FILE_GET_WEAPON = "resources/snd/items/pkup.wav";
    private final static String SOUND_FILE_GET_KEY    = "resources/snd/items/Key.wav";
    
    /* phrases */
    private final static String SOUND_FILE_NEAD_KEY1 = "resources/snd/player/need_key1.wav";
    private final static String SOUND_FILE_NEAD_KEY2 = "resources/snd/player/need_key2.wav";
    
    /* music */
    public final static String SOUND_FILE_MUSIC1 = "resources/snd/music.mp3";
    
    
    
    /* buffers */
    /* effects */
    public final static SoundBuffer SOUND_BUFFER_EXPLODE = new SoundBuffer(new File(SOUND_FILE_EXPLODE));
    
    /* weapons */
    public final static SoundBuffer SOUND_BUFFER_KNIFE   = new SoundBuffer(new File(SOUND_FILE_KNIFE  ));
    public final static SoundBuffer SOUND_BUFFER_PISTOL  = new SoundBuffer(new File(SOUND_FILE_PISTOL ));
//  public final static SoundBuffer SOUND_BUFFER_SHOTGUN = new SoundBuffer(new File(SOUND_FILE_SHOTGUN));
    public final static SoundBuffer SOUND_BUFFER_RIFLE   = new SoundBuffer(new File(SOUND_FILE_RIFLE  ));
    public final static SoundBuffer SOUND_BUFFER_BAZOOKA = new SoundBuffer(new File(SOUND_FILE_BAZOOKA));
    
    /* enemies */
    public final static SoundBuffer SOUND_BUFFER_ALARM1 = new SoundBuffer(new File(SOUND_FILE_ALARM1));
    public final static SoundBuffer SOUND_BUFFER_ALARM2 = new SoundBuffer(new File(SOUND_FILE_ALARM2));
    public final static SoundBuffer SOUND_BUFFER_ALARM3 = new SoundBuffer(new File(SOUND_FILE_ALARM3));
    public final static SoundBuffer SOUND_BUFFER_ALARM4 = new SoundBuffer(new File(SOUND_FILE_ALARM4));
    public final static SoundBuffer SOUND_BUFFER_PAIN1  = new SoundBuffer(new File(SOUND_FILE_PAIN1));
    public final static SoundBuffer SOUND_BUFFER_PAIN2  = new SoundBuffer(new File(SOUND_FILE_PAIN2));
    public final static SoundBuffer SOUND_BUFFER_DIE1   = new SoundBuffer(new File(SOUND_FILE_DIE1));
    public final static SoundBuffer SOUND_BUFFER_DIE2   = new SoundBuffer(new File(SOUND_FILE_DIE2));
    
    /* events */
    public final static SoundBuffer SOUND_BUFFER_DOOR_OPEN  = new SoundBuffer(new File(SOUND_FILE_DOOR_OPEN ));
    public final static SoundBuffer SOUND_BUFFER_DOOR_CLOSE = new SoundBuffer(new File(SOUND_FILE_DOOR_CLOSE));
    
    /* items */
    public final static SoundBuffer SOUND_BUFFER_GET_WEAPON = new SoundBuffer(new File(SOUND_FILE_GET_WEAPON));
    public final static SoundBuffer SOUND_BUFFER_GET_KEY = new SoundBuffer(new File(SOUND_FILE_GET_KEY));
    
    /* phrases */
    public final static SoundBuffer SOUND_BUFFER_NEAD_KEY1 = new SoundBuffer(new File(SOUND_FILE_NEAD_KEY1));
    public final static SoundBuffer SOUND_BUFFER_NEAD_KEY2 = new SoundBuffer(new File(SOUND_FILE_NEAD_KEY2));
    
    
    private SoundBank() {}
}
