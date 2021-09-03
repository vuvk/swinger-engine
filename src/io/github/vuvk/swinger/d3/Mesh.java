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
package io.github.vuvk.swinger.d3;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class Mesh implements Serializable {
    transient public final static Set<Mesh> LIB = new CopyOnWriteArraySet<>();

    private Set<Polygon> polys = new HashSet<>();

    public Mesh(Set<Polygon> polys) {
        this.polys.addAll(polys);

        LIB.add(this);
    }

    public Set<Polygon> getPolygons() {
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
