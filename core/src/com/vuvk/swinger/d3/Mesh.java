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
package com.vuvk.swinger.d3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class Mesh implements Serializable {
    transient public final static List<Mesh> LIB = new ArrayList<>();
    
    private List<Polygon> polys = new ArrayList<>();    
    
    public Mesh(List<Polygon> polys) {
        for (Polygon poly : polys) {
            this.polys.add(poly);
        }
        
        LIB.add(this);
    }
    
    public List<Polygon> getPolygons() {
        return polys;
    }
    
    @Override
    public void finalize() {
        LIB.remove(this);
    }
    
    public static void deleteAll() {
        LIB.clear();
    }
    
    public static Mesh[] getLib() {
        Mesh[] meshes = new Mesh[LIB.size()];
        int i = 0;
        for (Iterator<Mesh> it = LIB.iterator(); it.hasNext(); ) {
            meshes[i] = it.next();
            ++i;
        }
        return meshes;
    }
}
