/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.graphic.gui.text;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author vuvk
 */
public final class FontBank {
    private final static String FONT_FILE_OUTLINE = "resources/pics/gui/outline_24x32.png";
    private final static String FONT_FILE_BUBBLA  = "resources/pics/gui/anuvverbubbla_8x8.png";
    private final static String FONT_FILE_MIDDLE  = "resources/pics/gui/kromagrad_16x16.png";    
    
    public final static Font FONT_OUTLINE = new Font();
    public final static Font FONT_BUBBLA  = new Font();
    public final static Font FONT_MIDDLE = new Font();
    public final static Font FONT_WHAT = new Font();
    
    
    static {
        try {
            BufferedImage fontOutlineImg = ImageIO.read(new File(FONT_FILE_OUTLINE));
            for (int x = 0; x * 24 < fontOutlineImg.getWidth(); ++x) {      
                BufferedImage symbolImg = fontOutlineImg.getSubimage(x * 24, 0, 24, 32);
                FONT_OUTLINE.setSymbol(x + 32, new Symbol(symbolImg));
            }
            
            BufferedImage fontBubblaImg = ImageIO.read(new File(FONT_FILE_BUBBLA));
            for (int x = 0; x * 8 < fontBubblaImg.getWidth(); ++x) {      
                BufferedImage symbolImg = fontBubblaImg.getSubimage(x * 8, 0, 8, 8);
                FONT_BUBBLA.setSymbol(x + 32, new Symbol(symbolImg));
            }
            
            BufferedImage fontMiddleImg = ImageIO.read(new File(FONT_FILE_MIDDLE));
            for (int x = 0; x * 16 < fontMiddleImg.getWidth(); ++x) {      
                BufferedImage symbolImg = fontMiddleImg.getSubimage(x * 16, 0, 16, 16);
                FONT_MIDDLE.setSymbol(x + 32, new Symbol(symbolImg));
            }
            
            for (int i = 0; i < FONT_OUTLINE.getSymbols().length; ++i) {
                Symbol symbol;
                if (i % 2 == 0) {
                    symbol = FONT_OUTLINE.getSymbol(i);
                } else {
                    symbol = FONT_MIDDLE.getSymbol(i);                    
                }
                FONT_WHAT.setSymbol(i, symbol);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    } 
    
    private FontBank() {}
}
