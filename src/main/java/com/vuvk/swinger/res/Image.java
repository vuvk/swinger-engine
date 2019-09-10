/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.res;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import com.vuvk.swinger.graphic.Sky;
import com.vuvk.swinger.math.BoundingBox;
import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.util.ImageUtils;

/**
 *
 * @author tai-prg3
 */
public/* abstract*/ class Image {  
    private static final Logger LOG = Logger.getLogger(Image.class.getName());    
    
    protected int[][] pixels;
    protected int width;
    protected int height;
    protected Vector2 center = new Vector2(0.5, 0.5);
    private boolean alphaChannel;
    protected BoundingBox volume = new BoundingBox(0, 1, 0, 1);
    
    public Image(final BufferedImage image) {
        this(image, -1, -1);
    }
    
    public Image(final File file) {
        this(file, -1, -1);
    }
    
    public Image(final String path) {
        this(path, -1, -1);
    }
    
    public Image(final BufferedImage image, int newWidth, int newHeight) {
        load(image, newWidth, newHeight);
    }
    
    public Image(final File file, int newWidth, int newHeight) {
        load(file, newWidth, newHeight);
    }
    
    public Image(final String path, int newWidth, int newHeight) {
        this(new File(path), newWidth, newHeight);
    }
    
    private void load(final BufferedImage image, int newWidth, int newHeight) {
        BufferedImage imageForLoad = image;        
        /*if (newWidth > 0 && newHeight > 0)*/ {            
            imageForLoad = ImageUtils.resizeImage(image, newWidth, newHeight);            
        }
        
        width  = imageForLoad.getWidth();
        height = imageForLoad.getHeight();
        pixels = new int[width][height];
        int[] buffer = new int[width * height];

        imageForLoad.getRGB(0, 0, width, height, buffer, 0, width);

        // заполняем массив пикселей
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                pixels[x][y] = buffer[y * width + x];
                // заодно узнаем есть ли прозрачные пиксели
                if (pixels[x][y] >> 24 == 0) {
                    alphaChannel = true;
                }
            }
        }        
        
        if (alphaChannel) {
            // ищем полезный объем
            int x, y;
            int left, right, top, bottom;
            boolean founded;

            // слева
            founded = false;
            left = 0;
            for (x = 0; x < width && !founded; ++x) {
                for (y = 0; y < height; ++y) {
                    if (pixels[x][y] >> 24 != 0) {
                        left = x;
                        founded = true;
                        break;
                    }
                }
            }

            // справа
            founded = false;
            right = width - 1;
            for (x = width - 1; x >= 0 && !founded; --x) {
                for (y = 0; y < height; ++y) {
                    if (pixels[x][y] >> 24 != 0) {
                        right = x;
                        founded = true;
                        break;
                    }
                }
            }

            // сверху
            founded = false;
            top = 0;
            for (y = 0; y < height && !founded; ++y) {
                for (x = 0; x < width; ++x) {
                    if (pixels[x][y] >> 24 != 0) {
                        top = y;
                        founded = true;
                        break;
                    }
                }
            }

            // снизу
            founded = false;
            bottom = height - 1;
            for (y = height - 1; y >= 0  && !founded; --y) {
                for (x = 0; x < width; ++x) {
                    if (pixels[x][y] >> 24 != 0) {
                        bottom = y;
                        founded = true;
                        break;
                    }
                }
            }

            // считаем объем в относительных величинах [0.0 - 1.0]
            volume.setLeft  ((double)left   / (width  - 1));
            volume.setRight ((double)right  / (width  - 1));
            volume.setTop   ((double)top    / (height - 1));
            volume.setBottom((double)bottom / (height - 1));
        }
    }

    public void setCenter(Vector2 center) {
        this.center = center;
    }       
    
    private void load(final File file, int newWidth, int newHeight) {
        try {
            load(ImageIO.read(file), newWidth, newHeight);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex + " (" + file.getName() + ")");
        }
    }
    
    private void load(final String path, int newWidth, int newHeight) {
        load(new File(path), newWidth, newHeight);
    }
    
    private void load(final BufferedImage image) {
        load(image, -1, -1);
    }
    
    private void load(final File file) {
        load(file, -1, -1);
    }
    
    private void load(final String path) {
        load(new File(path));
    }
    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    public int[][] getPixels() {
        return pixels;
    }
    
    public int getPixel(int x, int y) {
        return pixels[x][y];
    }
    
    public int[] getColumn(int x) {        
        return pixels[x];
    }
    
    public BoundingBox getVolume() {
        return volume;
    }    

    public Vector2 getCenter() {
        return center;
    }
    
    public boolean hasAlphaChannel() {
        return alphaChannel;
    }  
}
