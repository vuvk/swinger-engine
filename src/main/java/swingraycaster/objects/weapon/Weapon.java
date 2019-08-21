/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.objects.weapon;

import swingraycaster.audio.Sound;
import swingraycaster.audio.SoundBank;
import swingraycaster.graphic.weapon_in_hand.WeaponInHand;
import swingraycaster.math.Vector3;
import swingraycaster.objects.AmmoType;
import swingraycaster.objects.Sprite;
import swingraycaster.objects.creatures.Player;
import swingraycaster.res.Material;

/**
 *
 * @author vuvk
 */
public abstract class Weapon extends Sprite {
    private Player PLAYER = Player.getInstance();
    //private final static List<Key> LIB = new ArrayList<>();
    
    private int weaponNum;
    private AmmoType ammoType;
    
    public Weapon(Material material, Vector3 pos, int weaponNum, AmmoType ammoType) {
        super(material, pos);
        this.weaponNum = weaponNum;
        this.ammoType = ammoType;
    }

    public int getWeaponNum() {
        return weaponNum;
    }

    public AmmoType getAmmoType() {
        return ammoType;
    }   
    
    abstract public WeaponInHand getInstance();
    
    public void update() {     
        super.update();
        
        if (PLAYER.getPos().distance(getPos()) < 0.5) {
            new Sound(SoundBank.SOUND_BUFFER_GET_WEAPON).play();
            PLAYER.addWeaponInHand(this);
            
            markForDelete();
        }
    }
}