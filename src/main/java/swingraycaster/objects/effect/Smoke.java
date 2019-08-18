/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.objects.effect;

import swingraycaster.graphic.Fog;
import swingraycaster.math.Vector3;
import swingraycaster.res.TextureBank;

/**
 *
 * @author tai-prg3
 */
public class Smoke extends Effect {
    private final static double ANIM_SPEED = 25.0;
    
    public Smoke(Vector3 pos) {
        super(TextureBank.SMOKE, ANIM_SPEED, true, pos);
        setBrightness(Fog.START);
    }    
}
