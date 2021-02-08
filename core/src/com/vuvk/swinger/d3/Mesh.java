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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class Mesh implements Serializable {
    transient public final static List<Mesh> LIB = new CopyOnWriteArrayList<>();

    private List<Polygon> polys = new ArrayList<>();

    public Mesh(List<Polygon> polys) {
        this.polys.addAll(polys);

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
        return LIB.toArray(new Mesh[LIB.size()]);
    }
}
