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
package io.github.vuvk.swinger.objects;

import io.github.vuvk.swinger.math.BoundingBox;
import io.github.vuvk.swinger.math.Vector2;
import io.github.vuvk.swinger.math.Vector3;
import io.github.vuvk.swinger.utils.ImmutablePair;
import io.github.vuvk.swinger.utils.Pair;
import io.github.vuvk.swinger.utils.Utils;
import java.awt.Color;
import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class LightSource extends Object3D implements Serializable {
    transient public final static Set<LightSource> LIB = new CopyOnWriteArraySet<>();

    private Color color;
    private double brightness;
    private double radius;
    private double squareRadius; // for fast check point
    private double invRadius;    // for fast div
    protected final BoundingBox bb;

    public LightSource() {
        this(Color.WHITE);
    }

    public LightSource(Color color) {
        this(color, 1.0);
    }

    public LightSource(Color color, double radius) {
        this(color, radius, Vector3.ZERO);
    }

    public LightSource(Color color, double radius, Vector3 pos) {
        this(color, radius, pos, 1.0);
    }

    public LightSource(Color color, double radius, Vector3 pos, double brightness) {
        bb = new BoundingBox(pos, radius);

        setColor(color);
        setRadius(radius);
        setPos(pos);
        setBrightness(brightness);
        updateRadius();

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

    public double getBrightness() {
        return brightness;
    }

    public double getRadius() {
        return radius;
    }

    /** возвращаемое значение в случае, если точка не в радиусе источника света */
    private final static ImmutablePair<Boolean, Double> POINT_NOT_IN_RADIUS = new ImmutablePair<>(false, Double.NaN);

    /**
     * Находится ли точка внутри радиуса источника освещения
     * @param point Точка для проверки
     * @return возвращает пару - true/false и яркость, если true
     */
    public final Pair<Boolean, Double> hasPoint(Vector2 point) {
        if (!bb.hasPoint(point)) {
            return POINT_NOT_IN_RADIUS;
        }

        double x = pos.x - point.x,
               y = pos.y - point.y;
        double squareLength = x*x + y*y;

        if (squareLength > squareRadius) {
            return POINT_NOT_IN_RADIUS;
        } else {
            // считаем расстояние до центра источника. Чем ближе к центру, тем ярче
            double distance = Math.sqrt(squareLength);
            double brightnessInPoint = 1.0 - distance * invRadius;

            return new ImmutablePair<>(true, brightnessInPoint);
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setBrightness(double brightness) {
        this.brightness = Utils.limit(brightness, 0.0, 1.0);
    }

    public void setRadius(double radius) {
        this.radius = radius;
        updateRadius();
    }

    @Override
    public void setPos(Vector3 pos) {
        super.setPos(pos);
        bb.setPos(pos);
    }

    private void updateRadius() {
        squareRadius = radius * radius;
        invRadius = 1.0 / radius;
        updateBb();
    }

    private void updateBb() {
        Vector2 center = bb.getCenter();

        bb.setLeft  (center.x - radius);
        bb.setRight (center.x + radius);
        bb.setTop   (center.y - radius);
        bb.setBottom(center.y + radius);
    }

    @Override
    public void update() {
        super.update();
        updateRadius();
    }

    public static LightSource[] getLib() {
        return LIB.toArray(new LightSource[LIB.size()]);
    }
/*
    public static void updateAll() {
        LIB.forEach(LightSource::update);
    }
*/
    public static void deleteAll() {
        LIB.clear();
    }

}
