/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.graphic.gui.text;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.vuvk.swinger.Config;
import com.vuvk.swinger.math.Vector2;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vuvk
 */
public class Text {
    
    private final static Array<Text> TEXTS = new Array<>(false, 50);
    transient private static final Logger LOG = Logger.getLogger(Text.class.getName());    
    
    private Vector2 location;
    private Font font;
    protected String message;
    protected boolean visible = true;
    
    public Text(final Font font) {
        this(font, "");
    }
    
    public Text(final Font font, String message) {
        this(font, message, new Vector2());
    }
    
    public Text(final Font font, String message, Vector2 location) {
        setFont(font);
        setMessage(message);
        setLocation(location);
        
        TEXTS.add(this);
    }
    
    @Override
    public void finalize() {
        TEXTS.removeValue(this, true);
        try {
            super.finalize();
        } catch (Throwable ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    public Vector2 getLocation() {
        return location;
    }

    public Font getFont() {
        return font;
    }

    public String getMessage() {
        return message;
    }

    public boolean isVisible() {
        return visible;
    }   

    public void setLocation(Vector2 location) {
        this.location = location;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setMessage(String message) {
        this.message = message;
    }    

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public void draw(Batch batch) {            
        if (message == null       || 
            message.length() == 0 || 
            font == null          || 
            !isVisible()
           ) {
            return;
        }
         
        float x = (float) location.x,
              y = Config.HEIGHT - (float) location.y;
        
        for (int i = 0; i < message.length(); ++i) {
            int ascii = message.charAt(i);
            Symbol symbol = font.getSymbol(ascii);
            if (symbol != null) {
                Texture img = symbol.getTexture();
                if (img != null) {
                    batch.draw(img, x, y);
                    x += img.getWidth();
                }
            }
        }
    }
    
    public static void drawAll(Batch batch) {
        for (Text text : TEXTS) {
            text.draw(batch);
        }
    }
    
    public static void remove(Text text) {
        TEXTS.removeValue(text, true);
    }
    
    public static void clearAll() {
        TEXTS.clear();
    }
}
