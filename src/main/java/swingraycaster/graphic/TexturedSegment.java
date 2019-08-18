/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.graphic;

import swingraycaster.math.Segment;
import swingraycaster.math.Vector2;
import swingraycaster.res.Material;
import swingraycaster.res.Texture;

/**
 *
 * @author tai-prg3
 */
public class TexturedSegment extends Segment {
    private Material material;
    
    public TexturedSegment(final Vector2 a, final Vector2 b, final Material material) {
        super(a, b);
        this.material = material;
    }
    
    public TexturedSegment(double x1, double y1, double x2, double y2, Material material) {
        this(new Vector2(x1, y1), new Vector2(x2, y2), material);
    }

    public Texture getTexture() {
        return (Texture) material.getSideFrames()[0];
    }
    
    public Material getMaterial() {
        return material;
    }
}
