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
package com.vuvk.swinger.graphic;

import com.vuvk.swinger.math.Segment;
import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.res.Material;
import com.vuvk.swinger.res.Texture;
import java.io.Serializable;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class TexturedSegment extends Segment implements Serializable {
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
