/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.graphic.weapon_in_hand;

/**
 *
 * @author vuvk
 */
public enum AmmoType {   
    NOTHING (0  ),
    PISTOL  (250),
    SHOTGUN (50 ),
    ROCKET  (25 );  
    
    private final int max;
    
    AmmoType(int max) {
        this.max = max;
    }
    
    public int getMax() {
        return max;
    }
}
