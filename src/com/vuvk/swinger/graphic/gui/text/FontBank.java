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
package com.vuvk.swinger.graphic.gui.text;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public final class FontBank {
    private final static String FONT_FILE_OUTLINE = "resources/pics/gui/fonts/outline_24x32.png";
    private final static String FONT_FILE_BUBBLA  = "resources/pics/gui/fonts/anuvverbubbla_8x8.png";
    private final static String FONT_FILE_MIDDLE  = "resources/pics/gui/fonts/kromagrad_16x16.png";
    private final static String FONT_FILE_MENU    = "resources/pics/gui/fonts/kromasky_16x16.png";

    public final static Font FONT_OUTLINE = new Font();
    public final static Font FONT_BUBBLA  = new Font();
    public final static Font FONT_MIDDLE  = new Font();
    public final static Font FONT_MENU    = new Font();

    private FontBank() {}

    public static void load() {
        Pixmap fontOutlineImg = new Pixmap(Gdx.files.internal(FONT_FILE_OUTLINE));
        for (int x = 0; x * 24 < fontOutlineImg.getWidth(); ++x) {
            Pixmap symbolImg = new Pixmap(24, 32, Pixmap.Format.RGBA8888);
            symbolImg.drawPixmap(fontOutlineImg, x * 24, 0, 24, 32, 0, 0, 24, 32);
            FONT_OUTLINE.setSymbol(x + 32, new Symbol(symbolImg));
            symbolImg.dispose();
        }
        fontOutlineImg.dispose();

        Pixmap fontBubblaImg = new Pixmap(Gdx.files.internal(FONT_FILE_BUBBLA));
        for (int x = 0; x * 8 < fontBubblaImg.getWidth(); ++x) {
            Pixmap symbolImg = new Pixmap(8, 8, Pixmap.Format.RGBA8888);
            symbolImg.drawPixmap(fontBubblaImg, x * 8, 0, 8, 8, 0, 0, 8, 8);
            FONT_BUBBLA.setSymbol(x + 32, new Symbol(symbolImg));
            symbolImg.dispose();
        }
        fontBubblaImg.dispose();

        Pixmap fontMiddleImg = new Pixmap(Gdx.files.internal(FONT_FILE_MIDDLE));
        for (int x = 0; x * 16 < fontMiddleImg.getWidth(); ++x) {
            Pixmap symbolImg = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
            symbolImg.drawPixmap(fontMiddleImg, x * 16, 0, 16, 16, 0, 0, 16, 16);
            FONT_MIDDLE.setSymbol(x + 32, new Symbol(symbolImg));
            symbolImg.dispose();
        }
        fontMiddleImg.dispose();

        Pixmap fontMenuImg = new Pixmap(Gdx.files.internal(FONT_FILE_MENU));
        for (int x = 0; x * 16 < fontMenuImg.getWidth(); ++x) {
            Pixmap symbolImg = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
            symbolImg.drawPixmap(fontMiddleImg, x * 16, 0, 16, 16, 0, 0, 16, 16);
            FONT_MENU.setSymbol(x + 32, new Symbol(symbolImg));
            symbolImg.dispose();
        }
        fontMenuImg.dispose();
    }
}
