package com.vuvk.swinger.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.vuvk.swinger.Config;
import com.vuvk.swinger.Game;

public class DesktopLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.allowSoftwareMode = false;
        config.useHDPI = false;
        config.useGL30 = false;
        config.foregroundFPS = config.backgroundFPS = 120;
        config.vSyncEnabled = true;
        config.width  = Config.WIDTH;
        config.height = Config.HEIGHT;
        config.title  = Config.TITLE;

        new LwjglApplication(new Game(), config);
    }
}
