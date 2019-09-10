/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.objects.effect;

import com.vuvk.retard_sound_system.Sound;
import java.io.File;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.Sprite;
import com.vuvk.swinger.res.Texture;

/**
 *
 * @author tai-prg3
 */
public abstract class Effect extends Sprite {
    private Sound soundEffect;
    /*
    private final static List<Effect> LIB = new ArrayList<>();
    private final static List<Effect> FOR_DELETE_FROM_LIB = new ArrayList<>();
    */
    /*private Texture[] frames;
    private Sprite sprite;
    private int curFrameNum = 0;
    private double animDelay = 0;
    private double animSpeed;
    private boolean playOnce;*/
    
    protected Effect(Texture[] frames, double animSpeed, boolean playOnce, Vector3 pos) {
        super(frames, animSpeed, playOnce, pos);        
    //    LIB.add(this);
    }

    protected Effect(Texture[][] frames, double animSpeed, boolean playOnce, Vector3 pos) {
        super(frames, animSpeed, playOnce, pos);        
    //    LIB.add(this);
    }
    
    protected void setSoundEffect(File audioFile) {
        soundEffect = new Sound(audioFile, true);
    }
    
    protected void setSoundEffect(String audioFilePath) {
        setSoundEffect(new File(audioFilePath));
    }

    public void setSoundEffect(Sound soundEffect) {
        this.soundEffect = soundEffect;
    }    
    
/*
    @Override
    public void finalize() {
        LIB.remove(this);
    }
    */
    /*
    public Vector3 getPos() {
        return sprite.getPos();
    }

    public void setPos(Vector3 pos) {
        sprite.setPos(pos);
    }
    */
    @Override
    public void update() {
        super.update();
        
        if (!isAnimate() && isPlayOnce()) {
            markForDelete();
        }
        
        /*if (animDelay < 1.0) {
            animDelay += animSpeed * Renderer.getDeltaTime();
        } else {
            animDelay = 0.0;
            ++curFrameNum;
            
            if (curFrameNum >= frames.length) {
                if (playOnce) {
                    FOR_DELETE_FROM_LIB.add(this);
                    return;
                } else {
                    curFrameNum = 0;
                }
            }
            
            sprite.setTexture(frames[curFrameNum]);
        }*/
    }
    /*
    public static void updateAll() {
        for (Effect effect : LIB) {
            effect.update();
        }
        
        if (FOR_DELETE_FROM_LIB.size() > 0) {
            for (Iterator<Effect> it = FOR_DELETE_FROM_LIB.iterator(); it.hasNext(); ) {
                it.next().finalize();
            }
            FOR_DELETE_FROM_LIB.clear();
        }
    }*/
}
