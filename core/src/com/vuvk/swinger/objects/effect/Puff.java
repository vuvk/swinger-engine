/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.objects.effect;

import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.res.TextureBank;
import java.io.Serializable;

/**
 *
 * @author tai-prg3
 */
public class Puff extends Effect implements Serializable {
    public final static double ANIM_SPEED = 10.0;
    
    public Puff(Vector3 pos) {
        super(TextureBank.PUFF, ANIM_SPEED, true, pos);
    }    
}
