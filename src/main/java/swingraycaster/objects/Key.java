/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.objects;

import swingraycaster.audio.Sound;
import swingraycaster.audio.SoundBank;
import swingraycaster.math.Vector3;
import swingraycaster.objects.creatures.Player;
import swingraycaster.res.Material;

/**
 *
 * @author tai-prg3
 */
public class Key extends Sprite {
        
    private Player PLAYER = Player.getInstance();
    //private final static List<Key> LIB = new ArrayList<>();
    
    private int keyNum;
    
    public Key(Material material, Vector3 pos, int keyNum) {
        super(material, pos);
        this.keyNum = keyNum;
    //    LIB.add(this);
    }

    public int getKeyNum() {
        return keyNum;
    }
    
    public void update() {     
        super.update();
        
        if (PLAYER.getPos().distance(getPos()) < 0.5) {
            new Sound(SoundBank.SOUND_BUFFER_GET_KEY).play();
            PLAYER.addKey(this);
            
            markForDelete();
        }
    }
}