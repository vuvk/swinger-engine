/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.objects.effect;

import swingraycaster.math.Vector3;
import swingraycaster.res.Texture;
import swingraycaster.res.TextureBank;

/**
 *
 * @author tai-prg3
 */
public class Puff extends Effect {
    public final static double ANIM_SPEED = 10.0;
    
    public Puff(Vector3 pos) {
        super(TextureBank.PUFF, ANIM_SPEED, true, pos);
    }    
}
