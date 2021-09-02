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
package com.vuvk.swinger.desktop;

import com.vuvk.swinger.Config;
import com.vuvk.swinger.Game;

public class DesktopLauncher {
    public static void main (String ... args) {
        Config.load();

        System.setProperty("sun.java2d.opengl", (Config.useOpenGL) ? "True" : "False");

        Game game = Game.getInstance();
        game.gameLoop();
    }
}
