/**
    Copyright (C) 2021 Anton "Vuvk" Shcherbatykh <vuvk69@gmail.com>
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
package com.vuvk.swinger.objects;

import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.math.Vector3;
import java.awt.Color;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class LightSource extends Object3D implements Serializable {
    transient public final static List<LightSource> LIB = new CopyOnWriteArrayList<>();

    private Color color;
    private double radius;
    private double squareRadius; // for fast check point

    public LightSource() {
        this(Color.WHITE);
    }

    public LightSource(Color color) {
        this(color, 1.0);
    }

    public LightSource(Color color, double radius) {
        this(color, radius, new Vector3());
    }

    public LightSource(Color color, double radius, Vector3 pos) {
        setColor(color);
        setRadius(radius);
        setPos(pos);
        squareRadius = radius * radius;

        LIB.add(this);
    }

    @Override
    public void destroy() {
        synchronized (LIB) {
            LIB.remove(this);
        }
    }

    @Override
    public void finalize() {
        destroy();
    }

    public Color getColor() {
        return color;
    }

    public double getRadius() {
        return radius;
    }

    public boolean isPointInRadius(Vector2 point) {
        double x = point.x,
               y = point.y;
        return (x*x + y*y <= squareRadius);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public void update() {
        super.update();
    }

    public static LightSource[] getLib() {
        return LIB.toArray(new LightSource[LIB.size()]);
    }

    public static void updateAll() {
        synchronized (LIB) {
            LIB.forEach(LightSource::update);
        }
    }

    public static void deleteAll() {
        synchronized (LIB) {
            LIB.clear();
        }
    }

}
