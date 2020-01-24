/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.objects.effect;

import com.badlogic.gdx.Gdx;
import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.res.TextureBank;
import java.io.Serializable;

/**
 *
 * @author tai-prg3
 */
public class Blood extends Effect implements Serializable {
    public final static double ANIM_SPEED = 7.5;
    private Vector2 direction;
    
    public Blood(Vector3 pos) {
        super(TextureBank.BLOOD, ANIM_SPEED, true, pos);
        
        double dir = Math.toRadians(Math.random() * 360.0);
        direction = new Vector2(Math.cos(dir), Math.sin(dir));
    }    
    
    @Override
    public void update() {
        super.update();
        double deltaTime = Gdx.graphics.getDeltaTime();
        
        Vector3 pos = getPos();
        setPos(pos.add(direction.mul(deltaTime * 0.5)));
        getPos().z -= deltaTime;
    }
}
