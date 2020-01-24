/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.objects;

//import com.vuvk.retard_sound_system.Sound;
import com.vuvk.swinger.audio.SoundBank;
import com.vuvk.swinger.audio.SoundSystem;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.creatures.Player;
import com.vuvk.swinger.res.Material;
import java.io.Serializable;

/**
 *
 * @author tai-prg3
 */
public class Key extends Sprite implements Serializable {
        
    //private Player PLAYER = Player.getInstance();
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
        
        Player player = Player.getInstance();
        
        if (player.getPos().distance(getPos()) < 0.5) {
            SoundSystem.playOnce(SoundBank.FILE_GET_KEY);
            player.addKey(this);
            
            markForDelete();
        }
    }
}