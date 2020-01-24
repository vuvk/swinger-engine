/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.objects.weapon;

import com.vuvk.swinger.graphic.weapon_in_hand.MinigunInHand;
import com.vuvk.swinger.graphic.weapon_in_hand.WeaponInHand;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.weapon.AmmoType;
import com.vuvk.swinger.res.Material;
import java.io.Serializable;

/**
 *
 * @author vuvk
 */
public class Minigun extends Weapon implements Serializable {
    
    private final static int WEAPON_NUM = 3;
    private final static AmmoType AMMO_TYPE = AmmoType.PISTOL;
    
    public Minigun(Material material, Vector3 pos) {
        super(material, pos, WEAPON_NUM, AMMO_TYPE);
    }

    @Override
    public WeaponInHand getInstance() {
        return MinigunInHand.getInstance();
    }
}

