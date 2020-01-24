/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.res;

import com.badlogic.gdx.utils.Array;
import com.vuvk.swinger.res.TextureBank;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author tai-prg3
 */
public final class MaterialBank {
    // guard
    public final static Material GUARD_ATK   = new Material(TextureBank.GUARD_ATK,  10.0f, true);
    public final static Material GUARD_STAND = new Material(TextureBank.GUARD_STAND, 0.0f, true);
    public final static Material GUARD_WALK  = new Material(TextureBank.GUARD_WALK,  7.5f, false);
    public final static Material GUARD_PAIN  = new Material(TextureBank.GUARD_PAIN,  0.0f, true);
    public final static Material GUARD_DIE   = new Material(TextureBank.GUARD_DIE,   7.5f, true);
    public final static Material GUARD_DEAD  = new Material(TextureBank.GUARD_DEAD,  0.0f, true);    
    
    public static List<Material> BANK = new ArrayList<>();
    
    public static Material[] getBank() {
        Material[] materials = new Material[BANK.size()];
        int i = 0;
        for (Iterator<Material> it = BANK.iterator(); it.hasNext(); ) {
            materials[i] = it.next();
            ++i;
        }
        return materials;
    }
    
    private MaterialBank() {};
}
