/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.res;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import swingraycaster.graphic.Renderer;
import swingraycaster.graphic.Renderer;
import swingraycaster.res.Image;
import swingraycaster.res.Texture;

/**
 *
 * @author tai-prg3
 */
public class Material {
    private final static List<Material> LIB = new ArrayList<>();
    private final static List<Material> FOR_ADD_TO_LIB = new ArrayList<>();
    private final static List<Material> FOR_DELETE_FROM_LIB = new ArrayList<>();
    
    // для анимации массив двумерный, где x - кадры, а y - текстуры для углов поворота (1 или 8 штук)
    private Image[][] frames;
    private double animSpeed;
    private double delay = 0.0;
    private int frameNum = 0;
    private boolean animate = true;
    private boolean playOnce;
    protected double brightness = 0.0;
    
    
    public Material(Image frame) {
        this(new Image[]{frame});
    }
    
    public Material(Image[] frames) {
        this(frames, 0.0);
    }
    
    public Material(Image[][] frames) {
        this(frames, 0.0);
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
        setFrames(frame);
        setAnimSpeed(animSpeed);
        this.playOnce = playOnce;
        //LIB.add(this);
        markForAdd();
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
        LIB.remove(this);
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
    private void markForAdd() {
        FOR_ADD_TO_LIB.add(this);
    }
    
    /**
     * Пометить объект на удаление
     */
    protected void markForDelete() {
        FOR_DELETE_FROM_LIB.add(this);
    }
    
    public void update() {
        if (animate) {
            if (animSpeed > 0.0 && frames.length > 1) {
                if (delay < 1.0) {
                    delay += animSpeed * Renderer.getDeltaTime();
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
                it.next().finalize();
            }
            FOR_DELETE_FROM_LIB.clear();
        }
        
        for (Material anim : LIB) {
            anim.update();
        }
    }
}
