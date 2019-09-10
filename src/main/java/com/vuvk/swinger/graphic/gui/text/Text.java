/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.graphic.gui.text;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vuvk
 */
public class Text {
    
    private final static ArrayList<Text> TEXTS = new ArrayList<>();
    private static final Logger LOG = Logger.getLogger(Text.class.getName());    
    
    private Point location;
    private Font font;
    private String message;
    
    public Text(final Font font) {
        this(font, "");
    }
    
    public Text(final Font font, String message) {
        this(font, message, new Point());
    }
    
    public Text(final Font font, String message, Point location) {
        setFont(font);
        setMessage(message);
        setLocation(location);
        
        TEXTS.add(this);
    }
    
    @Override
    public void finalize() {
        TEXTS.remove(this);
        try {
            super.finalize();
        } catch (Throwable ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    public Point getLocation() {
        return location;
    }

    public Font getFont() {
        return font;
    }

    public String getMessage() {
        return message;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setMessage(String message) {
        this.message = message;
    }    
    
    public void draw(Graphics g) {
        int x = location.x,
            y = location.y;
        
        if (message.length() == 0 || font == null) {
            return;
        }
        
        for (int i = 0; i < message.length(); ++i) {
            int ascii = message.charAt(i);
            Symbol symbol = font.getSymbol(ascii);
            if (symbol != null) {
                BufferedImage img = symbol.getImage();
                if (img != null) {
                    g.drawImage(img, x, y, null);
                    x += img.getWidth();
                }
            }
        }
    }
    
    public static void drawAll(Graphics g) {
        for (Text text : TEXTS) {
            text.draw(g);
        }
    }
    
    public static void remove(Text text) {
        TEXTS.remove(text);
    }
    
    public static void clearAll() {
        TEXTS.clear();
    }
}
