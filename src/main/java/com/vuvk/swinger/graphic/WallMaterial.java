/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.graphic;

import com.vuvk.swinger.res.Material;
import com.vuvk.swinger.res.Image;
import com.vuvk.utils.Utils;

/**
 *
 * @author tai-prg3
 */
public class WallMaterial {
    // 4 для каждой стороны
    private final Material[] materials = new Material[4];
    private int sidesLoaded;
    
    public WallMaterial(Image ... materials) {     
        setMaterials(materials);
    }
    
    public WallMaterial(Material ... materials) {
        setMaterials(materials);
    }

    public int getSidesLoaded() {
        return sidesLoaded;
    }
    
    public Image getTexture(int side) {
        if (sidesLoaded > side) {
            return materials[side].getSideFrames()[0];
        } else {
            return materials[0].getSideFrames()[0];
        }
    }
    
    public Image getTexture(Side side) {
        return getTexture(side.getNum());
    }
    
    public Material getMaterial(int side) {
        if (sidesLoaded > side) {
            return materials[side];
        } else {
            return materials[0];
        }
    }

    public Material getMaterial(Side side) {
        return getMaterial(side.getNum());
    }
    
    public void setMaterials(Image ... materials) {
        Material[] temp = new Material[materials.length];
        for (int i = 0; i < temp.length; ++i) {
            temp[i] = new Material(materials[i]);
        }
        setMaterials(temp);
    }
    
    public void setMaterials(Material ... materials) {
        sidesLoaded = 0;
        Utils.arrayFastFill(this.materials, null);
        for (int i = 0; 
             i < this.materials.length && 
             i < materials.length; 
             ++i
            ) {
            this.materials[i] = materials[i];
            ++sidesLoaded;
        }        
    }
}
