/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.objects.projectiles;

import com.badlogic.gdx.Gdx;
import com.vuvk.swinger.graphic.Renderer;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.effect.Explode;
import com.vuvk.swinger.objects.effect.Smoke;
import com.vuvk.swinger.res.TextureBank;

/**
 *
 * @author tai-prg3
 */
public class Rocket extends Projectile {
    private final static double DAMAGE = 100.0;
    private final static double RADIUS = 0.1;
    private final static double MOVE_SPEED = 5.0;
    private final static double ANIM_SPEED = 0.0;
    private double timeToSmoke = 0.0;
    
    public Rocket(Vector3 pos, double direction) {
        super(TextureBank.ROCKET, pos);
        setDirection(direction);
        setRadius(RADIUS);
        setMoveSpeed(MOVE_SPEED);
        setAnimSpeed(ANIM_SPEED);
        setDamage(DAMAGE);
    }

    @Override
    protected void createDestroyEffect() {
        new Explode(getPos())/*.markForAdd()*/;
    }
    
    @Override
    public void update() {
        super.update();
        
        if (timeToSmoke < 0.25) {
            timeToSmoke += Gdx.graphics.getDeltaTime();
        } else {
            timeToSmoke -= 0.25;
            new Smoke(getPos());
        }
    }
}
