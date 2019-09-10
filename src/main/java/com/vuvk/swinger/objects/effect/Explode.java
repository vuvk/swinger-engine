/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.objects.effect;

import com.vuvk.retard_sound_system.Sound;
import com.vuvk.swinger.audio.SoundBank;
import com.vuvk.swinger.graphic.Fog;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.res.TextureBank;

/**
 *
 * @author tai-prg3
 */
public class Explode extends Effect {
    private final static double ANIM_SPEED = 10.0;
    private final static Sound SOUND_EXPLODE = new Sound(SoundBank.SOUND_BUFFER_EXPLODE);
    
    public Explode(Vector3 pos) {
        super(TextureBank.EXPLODE, ANIM_SPEED, true, pos);
        setBrightness(Fog.END);
        SOUND_EXPLODE.play();
    }
}
