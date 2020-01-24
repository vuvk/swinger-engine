/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.res;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Filter;
import com.badlogic.gdx.graphics.Pixmap.Format;
import java.util.logging.Logger;
import com.vuvk.swinger.math.BoundingBox;
import com.vuvk.swinger.math.Vector2;
import java.io.Serializable;

/**
 *
 * @author tai-prg3
 */
public/* abstract*/ class Image implements Serializable {  
    transient private static final Logger LOG = Logger.getLogger(Image.class.getName());    
    
    protected int[][] pixels;
    protected int width;
    protected int height;
    protected final Vector2 center = new Vector2(0.5f, 0.5f);
    private boolean alphaChannel;
    protected final BoundingBox volume = new BoundingBox(0, 1, 0, 1);
    protected final String path;
    
    /*
    public Image(final BufferedImage image) {
        this(image, -1, -1);
    }
    
    public Image(final File file) {
        this(file, -1, -1);
    }*/
    
    public Image(final String path) {
        this(path, -1, -1);
    }
    /*
    public Image(final BufferedImage image, int newWidth, int newHeight) {
        load(image, newWidth, newHeight);
    }
    
    public Image(final File file, int newWidth, int newHeight) {
        load(file, newWidth, newHeight);
    }
    */
    
    public Image(final String path, int newWidth, int newHeight) {
        this.path = path;
        
        FileHandle file = Gdx.files.internal(path);
        if (file != null && file.exists()) {
            load(Gdx.files.internal(path), newWidth, newHeight);
        }
    }
    
    
    private void load(final FileHandle file, int newWidth, int newHeight) {        
        Pixmap image = new Pixmap(file);
        image.setFilter(Filter.NearestNeighbour);
        image.setBlending(Pixmap.Blending.None);
        //Pixmap imageForLoad = new Pixmap(image.getWidth(), image.getHeight(), Format.RGBA8888);
        //imageForLoad.setFilter(Filter.NearestNeighbour);
        //imageForLoad.drawPixmap(image, 0, 0, image.getWidth(), image.getHeight(), 0, 0, image.getWidth(), image.getHeight());
        //imageForLoad.setFilter(Filter.NearestNeighbour);
            
        Pixmap imageForLoad;
        if (newWidth > 0 && newHeight > 0) {
            imageForLoad = new Pixmap(newWidth, newHeight, Format.RGBA8888);
            imageForLoad.setFilter(Filter.NearestNeighbour);
            imageForLoad.setBlending(Pixmap.Blending.None);
            imageForLoad.drawPixmap(image, 0, 0, image.getWidth(), image.getHeight(), 0, 0, newWidth, newHeight);
            image.dispose();
        } else {
            imageForLoad = image;
        }
        
        //imageForLoad.setFilter(Pixmap.Filter.NearestNeighbour);
        
        width  = imageForLoad.getWidth();
        height = imageForLoad.getHeight();
        pixels = new int[width][height];
                
        //IntBuffer buffer = imageForLoad.getPixels().asIntBuffer();

        // заполняем массив пикселей
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                pixels[x][y] = imageForLoad.getPixel(x, y);
                // заодно узнаем есть ли прозрачные пиксели
                if (((pixels[x][y]) & 0xFF) == 0) {
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
                    if (((pixels[x][y]) & 0xFF) != 0) {
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
                    if (((pixels[x][y]) & 0xFF) != 0) {
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
                    if (((pixels[x][y]) & 0xFF) != 0) {
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
                    if (((pixels[x][y]) & 0xFF) != 0) {
                        bottom = y;
                        founded = true;
                        break;
                    }
                }
            }

            // считаем объем в относительных величинах [0.0 - 1.0]
            volume.setLeft  ((float)left   / (width  - 1));
            volume.setRight ((float)right  / (width  - 1));
            volume.setTop   ((float)top    / (height - 1));
            volume.setBottom((float)bottom / (height - 1));
        }
        
        imageForLoad.dispose();
    }

    public void setCenter(Vector2 center) {
        this.center.set(center);
    }       
    /*
    private void load(final FileHandle file, int newWidth, int newHeight) {
        load(new Pixmap(file), newWidth, newHeight);
    }
    */
    /*
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
    */
    
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
        //if (pixels != null) {
            return pixels[x][y];
        /*} else {
            return 0;
        }*/
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
    
    public final String getPath() {
        return path;
    }
}
