/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.objects.effect;

import swingraycaster.graphic.Renderer;
import swingraycaster.math.Vector2;
import swingraycaster.math.Vector3;
import swingraycaster.res.Texture;
import swingraycaster.res.TextureBank;

/**
 *
 * @author tai-prg3
 */
public class Blood extends Effect {
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
        
        Vector3 pos = getPos();
        setPos(pos.add(direction.mul(Renderer.getDeltaTime() * 0.5)));
        getPos().z -= Renderer.getDeltaTime();
    }
}
