/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.graphic.gui.text;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vuvk
 */
public class Symbol {
    transient private static final Logger LOG = Logger.getLogger(Symbol.class.getName());
    
    
    /*
    private BufferedImage image;

    public Symbol(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }*/
    
    private Texture texture;
    
    public Symbol(Pixmap pixmap) {
        setTexture(new Texture(pixmap));
    }
        
    public Symbol(Texture texture) {
        setTexture(texture);
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
    
    @Override
    public void finalize() {
        texture.dispose();
        try {
            super.finalize();
        } catch (Throwable ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }    
}
