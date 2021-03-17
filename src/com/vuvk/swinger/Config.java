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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import com.vuvk.retard_sound_system.SoundSystem;
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
        File config = new File("./config.json");
        if (config.exists()) {
            try {
                JsonObject json = JsonParser.parseReader(new FileReader(config)).getAsJsonObject();
                
                float musicVolume = (json.has("music_volume")) ? json.get("music_volume").getAsFloat() : 1.0f;
                float soundVolume = (json.has("sound_volume")) ? json.get("sound_volume").getAsFloat() : 1.0f;
                //SoundSystem.setMusicVolume(musicVolume);
                //SoundSystem.setVolume(soundVolume);

                interlacing  = (json.has("interlacing"))  ? json.get("interlacing").getAsBoolean()  : false;
                antialiasing = (json.has("antialiasing")) ? json.get("antialiasing").getAsBoolean() : false;
                quality      = (json.has("quality"))      ? json.get("quality").getAsInt()          : 0;
                multithreading = (json.has("multithreading")) ? json.get("multithreading").getAsBoolean() : true;
                mouseLook    = (json.has("mouselook"))    ? json.get("mouselook").getAsBoolean()    : false;
                drawSky      = (json.has("draw_sky"))     ? json.get("draw_sky").getAsBoolean()     : true;
                if (json.has("fog")) {
                    fog = Fog.getByNum(json.get("fog").getAsInt());
                }
                WIDTH  = (json.has("window_width"))   ? json.get("window_width").getAsInt()    : 640;
                HEIGHT = (json.has("window_height"))  ? json.get("window_height").getAsInt()   : 480;
                TITLE  = (json.has("window_title"))   ? json.get("window_title").getAsString() : "swinger engine";
                fullscreen = (json.has("fullscreen")) ? json.get("fullscreen").getAsBoolean()  : false;
                vSync  = (json.has("vsync"))          ? json.get("vsync").getAsBoolean()       : true;

            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void save() {
        File config = new File("./config.json");
        if (config.exists()) {
            config.delete();
        }

        try (JsonWriter writer = new JsonWriter(new FileWriter(config))) {
            writer.beginObject();
            //writer.name("music_volume").value(SoundSystem.getMusicVolume());
            //writer.name("sound_volume").value(SoundSystem.getVolume());
            writer.name("interlacing").value(interlacing);
            writer.name("antialiasing").value(antialiasing);
            writer.name("quality").value(quality);
            writer.name("multithreading").value(multithreading);
            writer.name("mouselook").value(mouseLook);
            writer.name("draw_sky").value(drawSky);
            writer.name("fog").value(fog.getNum());
            writer.name("window_width").value(WIDTH);
            writer.name("window_height").value(HEIGHT);
            writer.name("window_title").value(TITLE);
            writer.name("fullscreen").value(fullscreen);
            writer.name("vsync").value(vSync);
            writer.endObject();
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
