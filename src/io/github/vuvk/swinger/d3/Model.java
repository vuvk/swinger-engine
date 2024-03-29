/**
    Copyright (C) 2019-2021 Anton "Vuvk" Shcherbatykh <vuvk69@gmail.com>
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

import io.github.vuvk.swinger.math.Matrix4;
import io.github.vuvk.swinger.math.Vector2;
import io.github.vuvk.swinger.math.Vector3;
import io.github.vuvk.swinger.objects.Object3D;
import io.github.vuvk.swinger.objects.mortals.Player;
import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class Model extends Object3D implements Comparable<Model>, Serializable {
    transient public final static Set<Model> LIB = new CopyOnWriteArraySet<>();

    private Matrix4 mdlMtx = new Matrix4();
    private Mesh mesh;
    private boolean visible = true;
    private double distance = 0;
    private Vector3 rotation = new Vector3();

    public Model(Mesh mesh) {
        this(mesh, new Vector3());
    }

    public Model(Mesh mesh, Vector3 position) {
        this.mesh = mesh;
        setPos(position);
        setModelMtx();
        LIB.add(this);
    }

    @Override
    public void finalize() {
        LIB.remove(this);
    }

    public static void deleteAll() {
        LIB.clear();
    }

    public Mesh getMesh() {
        return mesh;
    }

    public boolean isVisible() {
        return visible;
    }

    public Matrix4 getModelMtx() {
        return mdlMtx;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    private void setModelMtx() {
        mdlMtx.identity();
        mdlMtx = mdlMtx.translate(pos.x, pos.z, pos.y);

        if (rotation.x != 0) {
            mdlMtx = mdlMtx.rotateX(rotation.x);
        }
    }

    @Override
    public void setPos(Vector3 pos) {
        super.setPos(pos);
        setModelMtx();
    }

    public void rotateX(double rad) {
        rotation.x += rad;
    }

    @Override
    public int compareTo(final Model another) {
        if (!another.equals(this)) {
            if (distance > another.distance) {
                return -1;
            } else if (distance < another.distance) {
                return 1;
            }
        }

        return 0;
    }

    public void update() {
        Player player = Player.getInstance();

        final Vector2 plPos = player.getPos();
        final double x1 = pos.x,
                     y1 = pos.y,
                     x2 = plPos.x,
                     y2 = plPos.y;

        final double x0 = x1 - x2;
        final double y0 = y1 - y2;
        distance = x0*x0 + y0*y0; // sqrt not taken, unneeded
    }
/*
    public static void updateAll() {
        synchronized(LIB) {
            LIB.forEach(Model::update);
        }
    }
*/

    public static Model[] getLib() {
        return LIB.toArray(new Model[LIB.size()]);
    }

    @Override
    public void destroy() {
        synchronized (LIB) {
            LIB.remove(this);
        }
    }
}
