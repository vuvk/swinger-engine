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
package com.vuvk.swinger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.vuvk.swinger.audio.SoundSystem;
import com.vuvk.swinger.graphic.Fog;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public final class Config {
    public static boolean interlacing = false;
    public static boolean antialiasing = false;
    public static int quality = 0;  // чем больше, тем хуже качество
    public static boolean multithreading = true;
    public static boolean mouseLook = false;
    public static boolean console = false;
    public static String consoleCommand = "";
    public static boolean drawSky = true;
    public static Fog fog = Fog.NOTHING;
    public static boolean vSync = true;
    public static boolean fullscreen = false;
    public final static int THREADS_COUNT = Runtime.getRuntime().availableProcessors() + 1;
    public final static boolean STEP_BY_STEP_RENDERING = false;
    public final static int STEP_BY_STEP_DELAY = 10;

    public /*final*/ static int WIDTH  = 640;
    public /*final*/ static int HEIGHT = 480;
    public static float ASPECT_RATIO;
    public static int HALF_WIDTH;
    public static int HALF_HEIGHT;
    public /*final*/ static String TITLE = "swinger engine";
    public static boolean QUIT = false;

    public static boolean buildForMobiles = false;

    public static boolean draw = false;


    private Config() {}

    public static void load() {
        FileHandle config = Gdx.files.internal("config.json");
        if (config.exists()) {
            //Json json = new Json();
            JsonValue jsonlevel = new JsonReader().parse(config);

            float musicVolume = (jsonlevel.has("music_volume")) ? jsonlevel.getFloat("music_volume") : 1.0f;
            float soundVolume = (jsonlevel.has("sound_volume")) ? jsonlevel.getFloat("sound_volume") : 1.0f;
            SoundSystem.setMusicVolume(musicVolume);
            SoundSystem.setVolume(soundVolume);

            interlacing  = (jsonlevel.has("interlacing"))  ? jsonlevel.getBoolean("interlacing")  : false;
            antialiasing = (jsonlevel.has("antialiasing")) ? jsonlevel.getBoolean("antialiasing") : false;
            quality      = (jsonlevel.has("quality"))      ? jsonlevel.getInt("quality")          : 0;
            multithreading = (jsonlevel.has("multithreading")) ? jsonlevel.getBoolean("multithreading") : true;
            mouseLook    = (jsonlevel.has("mouselook"))    ? jsonlevel.getBoolean("mouselook")    : false;
            drawSky      = (jsonlevel.has("draw_sky"))     ? jsonlevel.getBoolean("draw_sky")     : true;
            if (jsonlevel.has("fog")) {
                switch (jsonlevel.getInt("fog")) {
                    case 0 : fog = Fog.NOTHING; break;
                    case 1 : fog = Fog.OLD;     break;
                    default:
                    case 2 : fog = Fog.SMOOTH;  break;
                }
            }
            WIDTH  = (jsonlevel.has("window_width"))   ? jsonlevel.getInt("window_width")    : 640;
            HEIGHT = (jsonlevel.has("window_height"))  ? jsonlevel.getInt("window_height")   : 480;
            TITLE  = (jsonlevel.has("window_title"))   ? jsonlevel.getString("window_title") : "swinger engine";
            fullscreen = (jsonlevel.has("fullscreen")) ? jsonlevel.getBoolean("fullscreen")  : false;
            vSync  = (jsonlevel.has("vsync"))          ? jsonlevel.getBoolean("vsync")       : true;
        }
    }

    public static void save() {

    }

    public static void init() {
        HALF_WIDTH   = WIDTH  >> 1;
        HALF_HEIGHT  = HEIGHT >> 1;
        ASPECT_RATIO = (float)WIDTH / HEIGHT;

        if (interlacing && antialiasing) {
            antialiasing = false;
        }

        if (buildForMobiles) {
            multithreading = true;
            mouseLook = false;
        }
    }
}
