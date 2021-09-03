/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.vuvk.swinger.res.prefab;

import io.github.vuvk.swinger.math.Vector2;
import io.github.vuvk.swinger.res.Material;

/**
 *
 * @author vuvk
 */
public class SpritePrefab {
    private Material material;
    private boolean solid;
    private Vector2 scale;
    
    public SpritePrefab(Material material) {
        this(material, false);
    }
    
    public SpritePrefab(Material material, boolean solid) {
        this(material, solid, new Vector2(1, 1));
    }
    
    public SpritePrefab(Material material, boolean solid, Vector2 scale) {
        this.material = material;
        this.solid = solid;
        this.scale = scale;
    }
}
