/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.objects.effect;

import swingraycaster.audio.Sound;
import swingraycaster.audio.SoundBank;
import swingraycaster.graphic.Fog;
import swingraycaster.math.Vector3;
import swingraycaster.res.TextureBank;

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
