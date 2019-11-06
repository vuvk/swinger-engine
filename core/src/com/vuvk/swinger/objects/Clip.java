/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.objects;

import com.vuvk.swinger.audio.SoundBank;
import com.vuvk.swinger.audio.SoundSystem;
import com.vuvk.swinger.graphic.weapon_in_hand.AmmoPack;
import com.vuvk.swinger.objects.weapon.AmmoType;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.creatures.Player;
import com.vuvk.swinger.res.Material;

/**
 * Валяющиеся обоймы
 * @author vuvk
 */
public class Clip extends Sprite {
    private final AmmoType ammoType;
    private final int volume;
    
    public Clip(Material material, Vector3 pos, AmmoType ammoType, int volume) {
        super(material, pos);
        this.ammoType = ammoType;
        this.volume = volume;
    }
    
    public void update() {     
        super.update();
        
        Player player = Player.getInstance();
        
        if (player != null && player.getPos().distance(getPos()) < 0.5) {
            int curAmmo = AmmoPack.PACK.get(ammoType);
            int maxAmmo = ammoType.getMax();
            
            if (curAmmo < maxAmmo) {
                curAmmo += volume;
                if (curAmmo > maxAmmo) {
                    curAmmo = maxAmmo;
                }
                AmmoPack.PACK.put(ammoType, curAmmo);
                
                SoundSystem.playOnce(SoundBank.FILE_GET_AMMO);

                markForDelete();
            }
        }
    }    
}
