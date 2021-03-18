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
package com.vuvk.swinger.audio;

import com.vuvk.retard_sound_system.Music;
import com.vuvk.retard_sound_system.SoundBuffer;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
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
    private final static String PATH_GET_WEAPON = "resources/snd/items/Weapon.wav";
    private final static String PATH_GET_KEY    = "resources/snd/items/Key.wav";
    private final static String PATH_GET_AMMO   = "resources/snd/items/Ammo.wav";
    private final static String PATH_GET_MEDKIT1 = "resources/snd/items/Health.wav";
    private final static String PATH_GET_MEDKIT2 = "resources/snd/items/Medkit.wav";


    /* phrases */
    private final static String PATH_NEED_KEY1 = "resources/snd/player/need_key1.wav";
    private final static String PATH_NEED_KEY2 = "resources/snd/player/need_key2.wav";

    /* dies */
    private final static String PATH_PLAYER_DIE = "resources/snd/player/Player Dies.wav";

    /* music */
    private final static String PATH_MUSIC_TITLE = "resources/snd/music/title.ogg";
    private final static String PATH_MUSIC1 = "resources/snd/music/music.ogg";

    /* menu */
    private final static String PATH_MENU_SELECT = "resources/snd/menu/Menu Select.wav";
    private final static String PATH_MENU_TOGGLE = "resources/snd/menu/Menu Toggle.wav";



    /* soundbuffers */
    /* effects */
    public final static SoundBuffer SOUND_BUFFER_EXPLODE = new SoundBuffer(PATH_EXPLODE);

    /* weapons */
    public final static SoundBuffer SOUND_BUFFER_KNIFE   = new SoundBuffer(PATH_KNIFE  );
    public final static SoundBuffer SOUND_BUFFER_PISTOL  = new SoundBuffer(PATH_PISTOL );
    public final static SoundBuffer SOUND_BUFFER_RIFLE   = new SoundBuffer(PATH_RIFLE  );
    public final static SoundBuffer SOUND_BUFFER_MINIGUN = new SoundBuffer(PATH_MINIGUN);
    public final static SoundBuffer SOUND_BUFFER_BAZOOKA = new SoundBuffer(PATH_BAZOOKA);

    /* enemies */
    public final static SoundBuffer SOUND_BUFFER_ALARM1 = new SoundBuffer(PATH_ALARM1);
    public final static SoundBuffer SOUND_BUFFER_ALARM2 = new SoundBuffer(PATH_ALARM2);
    public final static SoundBuffer SOUND_BUFFER_ALARM3 = new SoundBuffer(PATH_ALARM3);
    public final static SoundBuffer SOUND_BUFFER_ALARM4 = new SoundBuffer(PATH_ALARM4);
    public final static SoundBuffer SOUND_BUFFER_PAIN1  = new SoundBuffer(PATH_PAIN1);
    public final static SoundBuffer SOUND_BUFFER_PAIN2  = new SoundBuffer(PATH_PAIN2);
    public final static SoundBuffer SOUND_BUFFER_DIE1   = new SoundBuffer(PATH_DIE1);
    public final static SoundBuffer SOUND_BUFFER_DIE2   = new SoundBuffer(PATH_DIE2);

    /* events */
    public final static SoundBuffer SOUND_BUFFER_DOOR_OPEN  = new SoundBuffer(PATH_DOOR_OPEN );
    public final static SoundBuffer SOUND_BUFFER_DOOR_CLOSE = new SoundBuffer(PATH_DOOR_CLOSE);

    /* items */
    public final static SoundBuffer SOUND_BUFFER_GET_WEAPON = new SoundBuffer(PATH_GET_WEAPON);
    public final static SoundBuffer SOUND_BUFFER_GET_KEY    = new SoundBuffer(PATH_GET_KEY);
    public final static SoundBuffer SOUND_BUFFER_GET_AMMO   = new SoundBuffer(PATH_GET_AMMO);
    public final static SoundBuffer SOUND_BUFFER_GET_MEDKIT1 = new SoundBuffer(PATH_GET_MEDKIT1);
    public final static SoundBuffer SOUND_BUFFER_GET_MEDKIT2 = new SoundBuffer(PATH_GET_MEDKIT2);

    /* phrases */
    public final static SoundBuffer SOUND_BUFFER_NEED_KEY1 = new SoundBuffer(PATH_NEED_KEY1);
    public final static SoundBuffer SOUND_BUFFER_NEED_KEY2 = new SoundBuffer(PATH_NEED_KEY2);

    /* dies */
    public final static SoundBuffer SOUND_BUFFER_PLAYER_DIE = new SoundBuffer(PATH_PLAYER_DIE);

    /* music */
    public static final Music MUSIC_TITLE = new Music(PATH_MUSIC_TITLE);
    public static final Music MUSIC1      = new Music(PATH_MUSIC1);
    
    /* menu */
    public static final SoundBuffer SOUND_BUFFER_MENU_SELECT = new SoundBuffer(PATH_MENU_SELECT);
    public static final SoundBuffer SOUND_BUFFER_MENU_TOGGLE = new SoundBuffer(PATH_MENU_TOGGLE);


    /* все звуки здесь? */
    //public final static ArrayList<File> HANDLES = new ArrayList<>();

    private SoundBank() {}
}
