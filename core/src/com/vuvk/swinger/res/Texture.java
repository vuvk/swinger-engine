/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.res;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 *
 * @author tai-prg3
 */
public final class Texture extends Image {
    public final static int WIDTH  = 128;
    public final static int HEIGHT = 128;
    public final static int WIDTH_POT  = 7;
    public final static int HEIGHT_POT = 7;
        
    /*
    public Texture(final BufferedImage image) {
        super(image, WIDTH, HEIGHT);
    }
    
    public Texture(final File file) {
        super(file, WIDTH, HEIGHT);
    }
    */
    public Texture(final String path) {
        super(path, WIDTH, HEIGHT);
    }
}
