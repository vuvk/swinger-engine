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
package com.vuvk.swinger.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.vuvk.swinger.Config;
import com.vuvk.swinger.Game;

public class DesktopLauncher {
    public static void main (String ... args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.allowSoftwareMode = true;
        config.useHDPI = false;
        config.useGL30 = false;
        config.foregroundFPS = config.backgroundFPS = 120;
        config.resizable = false;
        /*config.vSyncEnabled = Config.vSync;
        config.width  = Config.WIDTH;
        config.height = Config.HEIGHT;*/
        config.title  = Config.TITLE;

        LwjglApplication app = new LwjglApplication(new Game(), config);

        Config.load();
        Config.init();

        Gdx.graphics.setTitle(Config.TITLE);
        
        Game.setVSync(Config.vSync);
        Game.setFullscreenMode(Config.fullscreen);
    }
}
