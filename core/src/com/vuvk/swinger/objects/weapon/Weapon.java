/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.objects.weapon;

//import com.vuvk.retard_sound_system.Sound;
import com.vuvk.swinger.audio.SoundBank;
import com.vuvk.swinger.audio.SoundSystem;
import com.vuvk.swinger.graphic.weapon_in_hand.AmmoPack;
import com.vuvk.swinger.graphic.weapon_in_hand.WeaponInHand;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.graphic.weapon_in_hand.AmmoType;
import com.vuvk.swinger.objects.Sprite;
import com.vuvk.swinger.objects.creatures.Player;
import com.vuvk.swinger.res.Material;

/**
 *
 * @author vuvk
 */
public abstract class Weapon extends Sprite {
    //private Player PLAYER = Player.getInstance();
    //private final static List<Key> LIB = new ArrayList<>();
    
    private int weaponNum;
    private AmmoType ammoType;
    private int ammoInClip;
    
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
    
    public Weapon setAmmoInClip(int ammoInClip) {
        this.ammoInClip = ammoInClip;
        return this;
    }
    
    /**
     * Получить количество патронов в обойме
     * @return число патронов
     */
    public int getAmmoInClip() {
        return ammoInClip;
    };
    
    public void update() {     
        super.update();
        
        Player player = Player.getInstance();
        
        if (player.getPos().distance(getPos()) < 0.5) {
            SoundSystem.playOnce(SoundBank.FILE_GET_WEAPON);
            player.addWeaponInHand(this);
            
            // добавить игроку патронов того же типа, что и оружие
            int curAmmo = AmmoPack.PACK.get(ammoType);
            curAmmo += ammoInClip;
            int maxAmmo = ammoType.getMax();
            if (curAmmo > maxAmmo) {
                curAmmo = maxAmmo;
            }
            AmmoPack.PACK.put(ammoType, curAmmo);
            
            markForDelete();
        }
    }
}