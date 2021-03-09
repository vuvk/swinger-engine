/**
    Copyright (C) 2019-2020 Anton "Vuvk" Shcherbatykh <vuvk69@gmail.com>
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package com.vuvk.swinger.res;

import com.vuvk.swinger.graphic.Side;
import com.vuvk.swinger.res.Material;
import com.vuvk.swinger.res.Image;
import com.vuvk.swinger.utils.ArrayUtils;
import java.io.Serializable;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class WallMaterial implements Serializable{
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
        ArrayUtils.fill(this.materials, null);
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
