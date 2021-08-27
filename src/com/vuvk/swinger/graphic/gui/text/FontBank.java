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

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public final class FontBank {
    private static final Logger LOG = Logger.getLogger(FontBank.class.getName());    
    
    private final static String PATH_FONT_OUTLINE = "resources/pics/gui/fonts/outline_24x32.png";
    private final static String PATH_FONT_BUBBLA  = "resources/pics/gui/fonts/anuvverbubbla_8x8.png";
    private final static String PATH_FONT_MIDDLE  = "resources/pics/gui/fonts/kromagrad_16x16.png";
    private final static String PATH_FONT_MENU    = "resources/pics/gui/fonts/kromasky_16x16.png";

    public final static Font FONT_OUTLINE = new Font();
    public final static Font FONT_BUBBLA  = new Font();
    public final static Font FONT_MIDDLE  = new Font();
    public final static Font FONT_MENU    = new Font();

    private FontBank() {}

    public static void load() {
        try {
            BufferedImage fontOutlineImg = ImageIO.read(new File(PATH_FONT_OUTLINE));
            for (int x = 0; x * 24 < fontOutlineImg.getWidth(); ++x) {
                BufferedImage symbolImg = fontOutlineImg.getSubimage(x * 24, 0, 24, 32);
                FONT_OUTLINE.setSymbol(x + 32, new Symbol(symbolImg));
            }

            BufferedImage fontBubblaImg = ImageIO.read(new File(PATH_FONT_BUBBLA));
            for (int x = 0; x * 8 < fontBubblaImg.getWidth(); ++x) {
                BufferedImage symbolImg = fontBubblaImg.getSubimage(x * 8, 0, 8, 8);
                FONT_BUBBLA.setSymbol(x + 32, new Symbol(symbolImg));
            }

            BufferedImage fontMiddleImg = ImageIO.read(new File(PATH_FONT_MIDDLE));
            for (int x = 0; x * 16 < fontMiddleImg.getWidth(); ++x) {
                BufferedImage symbolImg = fontMiddleImg.getSubimage(x * 16, 0, 16, 16);
                FONT_MIDDLE.setSymbol(x + 32, new Symbol(symbolImg));
            }

            BufferedImage fontMenuImg = ImageIO.read(new File(PATH_FONT_MENU));
            for (int x = 0; x * 16 < fontMenuImg.getWidth(); ++x) {
                BufferedImage symbolImg = fontMenuImg.getSubimage(x * 16, 0, 16, 16);
                FONT_MENU.setSymbol(x + 32, new Symbol(symbolImg));
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
}
