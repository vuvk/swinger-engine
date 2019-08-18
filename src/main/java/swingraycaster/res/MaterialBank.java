/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.res;

import swingraycaster.res.Texture;
import swingraycaster.res.TextureBank;

/**
 *
 * @author tai-prg3
 */
public final class MaterialBank {
    // guard
    public final static Material GUARD_ATK   = new Material(TextureBank.GUARD_ATK,  10.0, true);
    public final static Material GUARD_STAND = new Material(TextureBank.GUARD_STAND, 0.0, true);
    public final static Material GUARD_WALK  = new Material(TextureBank.GUARD_WALK,  7.5, false);
    public final static Material GUARD_PAIN  = new Material(TextureBank.GUARD_PAIN,  0.0, true);
    public final static Material GUARD_DIE   = new Material(TextureBank.GUARD_DIE,   7.5, true);
    public final static Material GUARD_DEAD  = new Material(TextureBank.GUARD_DEAD,  0.0, true);    
    
    public static Material[] BANK;
    
    private MaterialBank() {};
}
