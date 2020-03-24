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
package com.vuvk.swinger.res;

import com.badlogic.gdx.Gdx;
import com.vuvk.swinger.res.Image;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class Material implements Serializable {
    transient public  final static List<Material> LIB = new ArrayList<>();
    transient private final static List<Material> FOR_ADD_TO_LIB = new ArrayList<>();
    transient private final static List<Material> FOR_DELETE_FROM_LIB = new ArrayList<>();
    
    // для анимации массив двумерный, где x - кадры, а y - текстуры для углов поворота (1 или 8 штук)
    private Image[][] frames;
    private double animSpeed;
    private double delay = 0;
    private int frameNum = 0;
    private boolean animate = true;
    private boolean playOnce;
    protected double brightness = 0;
    
    
    public Material(Image frame) {
        this(new Image[]{frame});
    }
    
    public Material(Image[] frames) {
        this(frames, 0);
    }
    
    public Material(Image[][] frames) {
        this(frames, 0);
    }
    
    public Material(Image frame, double animSpeed) {
        this(new Image[]{frame}, animSpeed);
    }
    
    public Material(Image[] frames, double animSpeed) {
        this(frames, animSpeed, false);
    }
    
    public Material(Image[][] frames, double animSpeed) {
        this(frames, animSpeed, false);
    }
    
    public Material(Image frame, double animSpeed, boolean playOnce) {
        this(new Image[]{frame}, animSpeed, playOnce);
    }
    
    public Material(Image[] frames, double animSpeed, boolean playOnce) {
        setFrames(frames);
        setAnimSpeed(animSpeed);
        this.playOnce = playOnce;
        //LIB.add(this);
        markForAdd();
    }
    
    public Material(Image[][] frames, double animSpeed, boolean playOnce) {
        setFrames(frames);
        setAnimSpeed(animSpeed);
        this.playOnce = playOnce;
        //LIB.add(this);
        markForAdd();
    }
    
    @Override
    public void finalize() {
        destroy();
    }
    
    public void destroy() {        
        //synchronized(LIB) {
            LIB.remove(this);
        //}
    }
    
    public static Material[] getLib() {
        Material[] materials = new Material[LIB.size()];
        int i = 0;
        for (Iterator<Material> it = LIB.iterator(); it.hasNext(); ) {
            materials[i] = it.next();
            ++i;
        }
        return materials;
    }
    
    public static void deleteAll() {
        LIB.clear();
        FOR_ADD_TO_LIB.clear();
        FOR_DELETE_FROM_LIB.clear();
    }
    
    /**
     * Копировать параметры из подобного объекта
     * @param another 
     */
    public void duplicate(Material another) {
        this.frames    = another.frames;
        this.animSpeed = another.animSpeed;
        this.frameNum  = another.frameNum;
        this.animate   = another.animate;
        this.playOnce  = another.playOnce;
        this.brightness = another.brightness;
    }

    public double getBrightness() {
        return brightness;
    }  

    public Image[][] getFrames() {
        return frames;
    }
    
    public Image[] getSideFrames() {
        return frames[frameNum];
    }

    public double getAnimSpeed() {
        return animSpeed;
    }
    
    public boolean isAnimate() {
        return animate;
    }   

    public boolean isPlayOnce() {
        return playOnce;
    }
    
    public boolean hasSides() {
        return (getSideFrames().length > 1);
    }    
    
    public void setBrightness(double brightness) {
        this.brightness = brightness;
    }
    
    public void setFrames(Image[][] frames) {
        this.frames = frames;
        this.frameNum = 0;
    }
    
    public void setFrames(Image[] frames) {
        Image[][] temp = new Image[frames.length][1];
        for (int i = 0; i < frames.length; ++i) {
            temp[i][0] = frames[i];
        }
        setFrames(temp);
    }
    
    public void setFrames(Image frame) {
        setFrames(new Image[][] {new Image[] {frame}});
    }
    
    public void setAnimSpeed(double animSpeed) {
        this.animSpeed = animSpeed;
    }
    
    public void play() {
        playOnce = false;
        animate = true;
        frameNum = 0;        
    }
    
    public void playOnce() {
        playOnce = true;
        animate = true;
        frameNum = 0;
    }
    
    /**
     * Пометить объект на добавление
     */
    public void markForAdd() {
        FOR_ADD_TO_LIB.add(this);
    }
    
    /**
     * Пометить объект на удаление
     */
    public void markForDelete() {
        FOR_DELETE_FROM_LIB.add(this);
    }
    
    public void update() {
        if (animate) {
            if (animSpeed > 0.0 && frames.length > 1) {
                if (delay < 1.0) {
                    delay += animSpeed * Gdx.graphics.getDeltaTime();
                } else {
                    delay -= 1.0;
                    ++frameNum;
                    if (frameNum >= frames.length) {
                        if (!playOnce) {
                            frameNum = 0;
                        // одноразовая анимация
                        } else {
                            frameNum = frames.length - 1;
                            animate = false;
                        }
                    }
                }
            } else {
                animate = false;
            }
        }
    }
    
    public static void updateAll() {                
        if (FOR_ADD_TO_LIB.size() > 0) {
            for (Material mat : FOR_ADD_TO_LIB) {
                LIB.add(mat);
            }
            FOR_ADD_TO_LIB.clear();
        }
        
        if (FOR_DELETE_FROM_LIB.size() > 0) {
            for (Iterator<Material> it = FOR_DELETE_FROM_LIB.iterator(); it.hasNext(); ) {
                /*it.next().finalize();*/
                it.next().destroy();
            }
            FOR_DELETE_FROM_LIB.clear();
        }
        
        for (Material anim : LIB) {
            anim.update();
        }
    }
}
