/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.objects.weapon;

import swingraycaster.graphic.weapon_in_hand.RifleInHand;
import swingraycaster.graphic.weapon_in_hand.WeaponInHand;
import swingraycaster.math.Vector3;
import swingraycaster.objects.AmmoType;
import swingraycaster.res.Material;

/**
 *
 * @author vuvk
 */
public class Rifle extends Weapon {
    
    private final static int WEAPON_NUM = 2;
    private final static AmmoType AMMO_TYPE = AmmoType.PISTOL;
    
    public Rifle(Material material, Vector3 pos) {
        super(material, pos, WEAPON_NUM, AMMO_TYPE);
    }

    @Override
    public WeaponInHand getInstance() {
        return RifleInHand.getInstance();
    }    
}

