/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.audio;

//import com.vuvk.retard_sound_system.SoundBuffer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import java.io.File;

/**
 *
 * @author tai-prg3
 */
public final class SoundBank {    
    /* paths */
    /* effects */
    private final static String PATH_EXPLODE = "resources/snd/weapons/Rocket Explode.wav";
    
    /* weapons */
    private final static String PATH_KNIFE   = "resources/snd/weapons/Knife.wav";
    private final static String PATH_PISTOL  = "resources/snd/weapons/Pistol.wav";
//  private final static String PATH_SHOTGUN = "resources/snd/weapons/shotgun.wav";
    private final static String PATH_RIFLE   = "resources/snd/weapons/Machine Gun.wav";
    private final static String PATH_MINIGUN = "resources/snd/weapons/gatling.wav";
    private final static String PATH_BAZOOKA = "resources/snd/weapons/Rocket Launcher.wav";
    
    /* enemies */
    private final static String PATH_ALARM1 = "resources/snd/enemies/Achtung!.wav";
    private final static String PATH_ALARM2 = "resources/snd/enemies/Halt.wav";
    private final static String PATH_ALARM3 = "resources/snd/enemies/Halt 2.wav";
    private final static String PATH_ALARM4 = "resources/snd/enemies/Halten Sie!.wav";
    private final static String PATH_PAIN1  = "resources/snd/enemies/Enemy Pain.wav";
    private final static String PATH_PAIN2  = "resources/snd/enemies/Oof!.wav";
    private final static String PATH_DIE1   = "resources/snd/enemies/Death 1.wav";
    private final static String PATH_DIE2   = "resources/snd/enemies/Death 2.wav";
    
    /* events */
    private final static String PATH_DOOR_OPEN  = "resources/snd/world/door_start.wav";
    private final static String PATH_DOOR_CLOSE = "resources/snd/world/door_stop.wav";
    
    /* items */
    private final static String PATH_GET_WEAPON = "resources/snd/items/pkup.wav";
    private final static String PATH_GET_KEY    = "resources/snd/items/Key.wav";
    private final static String PATH_GET_AMMO   = "resources/snd/items/Ammo.wav";
    
    /* phrases */
    private final static String PATH_NEED_KEY1 = "resources/snd/player/need_key1.wav";
    private final static String PATH_NEED_KEY2 = "resources/snd/player/need_key2.wav";
    
    /* music */
    private final static String PATH_MUSIC1 = "resources/snd/music.mp3";
    
    
    /* handles */
    /* effects */
    public final static FileHandle FILE_EXPLODE = Gdx.files.internal(PATH_EXPLODE);
    
    /* weapons */
    public final static FileHandle FILE_KNIFE   = Gdx.files.internal(PATH_KNIFE  );
    public final static FileHandle FILE_PISTOL  = Gdx.files.internal(PATH_PISTOL );
    public final static FileHandle FILE_RIFLE   = Gdx.files.internal(PATH_RIFLE  );
    public final static FileHandle FILE_MINIGUN = Gdx.files.internal(PATH_MINIGUN);    
    public final static FileHandle FILE_BAZOOKA = Gdx.files.internal(PATH_BAZOOKA);
    
    /* enemies */
    public final static FileHandle FILE_ALARM1 = Gdx.files.internal(PATH_ALARM1);
    public final static FileHandle FILE_ALARM2 = Gdx.files.internal(PATH_ALARM2);
    public final static FileHandle FILE_ALARM3 = Gdx.files.internal(PATH_ALARM3);
    public final static FileHandle FILE_ALARM4 = Gdx.files.internal(PATH_ALARM4);
    public final static FileHandle FILE_PAIN1  = Gdx.files.internal(PATH_PAIN1);
    public final static FileHandle FILE_PAIN2  = Gdx.files.internal(PATH_PAIN2);
    public final static FileHandle FILE_DIE1   = Gdx.files.internal(PATH_DIE1);
    public final static FileHandle FILE_DIE2   = Gdx.files.internal(PATH_DIE2);
    
    /* events */
    public final static FileHandle FILE_DOOR_OPEN  = Gdx.files.internal(PATH_DOOR_OPEN );
    public final static FileHandle FILE_DOOR_CLOSE = Gdx.files.internal(PATH_DOOR_CLOSE);
    
    /* items */
    public final static FileHandle FILE_GET_WEAPON = Gdx.files.internal(PATH_GET_WEAPON);
    public final static FileHandle FILE_GET_KEY    = Gdx.files.internal(PATH_GET_KEY);
    public final static FileHandle FILE_GET_AMMO   = Gdx.files.internal(PATH_GET_AMMO);
    
    /* phrases */
    public final static FileHandle FILE_NEED_KEY1 = Gdx.files.internal(PATH_NEED_KEY1);
    public final static FileHandle FILE_NEED_KEY2 = Gdx.files.internal(PATH_NEED_KEY2);
    
    /* все звуки здесь? */
    public final static Array<FileHandle> HANDLES = new Array<>(false, 10);
    
    private SoundBank() {}
}
