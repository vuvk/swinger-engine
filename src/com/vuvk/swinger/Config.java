/**
    Copyright (C) 2019-2021 Anton "Vuvk" Shcherbatykh <vuvk69@gmail.com>
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
*/

import com.vuvk.swinger.audio.SoundSystem;
import com.vuvk.swinger.graphic.Fog;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public final class Config {

    private static final Logger LOG = Logger.getLogger(Config.class.getName());

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
        try {
            File config = new File("./config.json");
       
            //Json json = new Json();
            //JsonValue json = new JsonReader().parse(config);
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject)parser.parse(new FileReader(config));
            //if (json == null) {
            //    return;
            //}

            /*
            float musicVolume = (json.has("music_volume")) ? json.getFloat("music_volume") : 1.0f;
            float soundVolume = (json.has("sound_volume")) ? json.getFloat("sound_volume") : 1.0f;
            SoundSystem.setMusicVolume(musicVolume);
            SoundSystem.setVolume(soundVolume);

            interlacing  = (json.has("interlacing"))  ? json.getBoolean("interlacing")  : false;
            antialiasing = (json.has("antialiasing")) ? json.getBoolean("antialiasing") : false;
            quality      = (json.has("quality"))      ? json.getInt("quality")          : 0;
            multithreading = (json.has("multithreading")) ? json.getBoolean("multithreading") : true;
            mouseLook    = (json.has("mouselook"))    ? json.getBoolean("mouselook")    : false;
            drawSky      = (json.has("draw_sky"))     ? json.getBoolean("draw_sky")     : true;
            if (json.has("fog")) {
                fog = Fog.getByNum(json.getInt("fog"));
            }
            WIDTH  = (json.has("window_width"))   ? json.getInt("window_width")    : 640;
            HEIGHT = (json.has("window_height"))  ? json.getInt("window_height")   : 480;
            TITLE  = (json.has("window_title"))   ? json.getString("window_title") : "swinger engine";
            fullscreen = (json.has("fullscreen")) ? json.getBoolean("fullscreen")  : false;
            vSync  = (json.has("vsync"))          ? json.getBoolean("vsync")       : true;
            */
            
            
            float musicVolume = (json.containsKey("music_volume")) ? (Float)json.get("music_volume") : 1.0f;
            float soundVolume = (json.containsKey("sound_volume")) ? (Float)json.get("sound_volume") : 1.0f;
            SoundSystem.setMusicVolume(musicVolume);
            SoundSystem.setVolume(soundVolume);

            interlacing  = (json.containsKey("interlacing"))  ? (Boolean)json.get("interlacing")  : false;
            antialiasing = (json.containsKey("antialiasing")) ? (Boolean)json.get("antialiasing") : false;
            quality      = (json.containsKey("quality"))      ? (Integer)json.get("quality")          : 0;
            multithreading = (json.containsKey("multithreading")) ? (Boolean)json.get("multithreading") : true;
            mouseLook    = (json.containsKey("mouselook"))    ? (Boolean)json.get("mouselook")    : false;
            drawSky      = (json.containsKey("draw_sky"))     ? (Boolean)json.get("draw_sky")     : true;
            if (json.containsKey("fog")) {
                fog = Fog.getByNum((Integer)json.get("fog"));
            }
            WIDTH  = (json.containsKey("window_width"))   ? (Integer)json.get("window_width")  : 640;
            HEIGHT = (json.containsKey("window_height"))  ? (Integer)json.get("window_height") : 480;
            TITLE  = (json.containsKey("window_title"))   ? (String) json.get("window_title")  : "swinger engine";
            fullscreen = (json.containsKey("fullscreen")) ? (Boolean)json.get("fullscreen")    : false;
            vSync  = (json.containsKey("vsync"))          ? (Boolean)json.get("vsync")         : true;
            
        } catch (ParseException | IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

    }

    public static void save() {
        File config = new File("./config.json");
        if (config.exists()) {
            config.delete();
        }

        /*
        Json json = new Json();
        JsonWriter writer = new JsonWriter(config.writer(false));
        json.setWriter(writer);

        json.writeObjectStart();
        json.writeValue("music_volume",   SoundSystem.getMusicVolume());
        json.writeValue("sound_volume",   SoundSystem.getVolume());
        json.writeValue("interlacing",    interlacing);
        json.writeValue("antialiasing",   antialiasing);
        json.writeValue("quality",        quality);
        json.writeValue("multithreading", multithreading);
        json.writeValue("mouselook",      mouseLook);
        json.writeValue("draw_sky",       drawSky);
        json.writeValue("fog",            fog.getNum());
        json.writeValue("window_width",   WIDTH);
        json.writeValue("window_height",  HEIGHT);
        json.writeValue("window_title",   TITLE);
        json.writeValue("fullscreen",     fullscreen);
        json.writeValue("vsync",          vSync);
        json.writeObjectEnd();

        try {
            writer.close();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        */

        JSONObject json = new JSONObject();
        json.put("music_volume",   SoundSystem.getMusicVolume());
        json.put("sound_volume",   SoundSystem.getVolume());
        json.put("interlacing",    interlacing);
        json.put("antialiasing",   antialiasing);
        json.put("quality",        quality);
        json.put("multithreading", multithreading);
        json.put("mouselook",      mouseLook);
        json.put("draw_sky",       drawSky);
        json.put("fog",            fog.getNum());
        json.put("window_width",   WIDTH);
        json.put("window_height",  HEIGHT);
        json.put("window_title",   TITLE);
        json.put("fullscreen",     fullscreen);
        json.put("vsync",          vSync);

        try (FileWriter writer = new FileWriter(config)) {
            writer.write(json.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
