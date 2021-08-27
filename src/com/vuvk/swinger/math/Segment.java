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
package com.vuvk.swinger.math;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class Segment implements Serializable {
    protected Vector2 a;
    protected Vector2 b;

    public Segment() {
        this(new Vector2(), new Vector2());
    }

    public Segment(final Vector2 a, final Vector2 b) {
        this.a = a;
        this.b = b;
    }

    public Segment(double x1, double y1, double x2, double y2) {
        this(new Vector2(x1, y1), new Vector2(x2, y2));
    }

    public Vector2 getA() {
        return a;
    }

    public void setA(final Vector2 a) {
        this.a = a;
    }

    public Vector2 getB() {
        return b;
    }

    public void setB(final Vector2 b) {
        this.b = b;
    }

    /**
     * Проверка пересечения с другим отрезком
     * @param other Отрезок для проверки пересечения с ним
     * @return Вектор-точка пересечения или null, если пересечения нет
     */
    public Vector2 intersect(final Segment other) {
        return intersect(this, other);
    }

    /**
     * Проверка пересечения двух отрезков
     * @param s1 Первый отрезок
     * @param s2 Второй отрезок
     * @return Вектор-точка пересечения или null, если пересечения нет
     */
    public static Vector2 intersect(final Segment s1, final Segment s2) {
        return intersect(s1.a.x, s1.a.y,
                         s1.b.x, s1.b.y,
                         s2.a.x, s2.a.y,
                         s2.b.x, s2.b.y);
    }

    /**
     * Проверка пересечения двух отрезков
     * @return Вектор-точка пересечения или null, если пересечения нет
     */
    public static Vector2 intersect(final double x1, final double y1, final double x2, final double y2,
                                    final double x3, final double y3, final double x4, final double y4) {
        // предварительный расчет разницы
        final double x1_x3 = x1 - x3;
        final double y1_y3 = y1 - y3;
        final double x2_x1 = x2 - x1;
        final double y2_y1 = y2 - y1;
        final double x4_x3 = x4 - x3;
        final double y4_y3 = y4 - y3;

        // denominator - числитель одинаковый для Ua и Ub
        double den = y4_y3 * x2_x1 - x4_x3 * y2_y1;

        // если числитель = 0, значит прямые параллельны
        if (Math.abs(den) <= 0.00000001) {
            return null;
        }
        den = 1.0 / den;

        // Если и числитель и знаменатель равны нулю, то прямые совпадают
        // здесь не рассматривается
        //double Ua = ((x4 - x3)*(y1 - y3) - (y4 - y3)*(x1 - x3))/den;
        //double Ub = ((x2 - x1)*(y1 - y3) - (y2 - y1)*(x1 - x3))/den;
        final double Ua = (x4_x3 * y1_y3 - y4_y3 * x1_x3) * den;
        final double Ub = (x2_x1 * y1_y3 - y2_y1 * x1_x3) * den;

        // Если нужно найти пересечение отрезков, то нужно лишь проверить,
        // лежат ли ua и ub на промежутке [0,1]. Если какая-нибудь из этих двух
        // переменных 0 <= ui <= 1, то соответствующий отрезок содержит точку
        // пересечения. Если обе переменные приняли значения из [0,1], то точка
        // пересечения прямых лежит внутри обоих отрезков.
        if ((0.0 <= Ua) && (Ua <= 1.0) &&
            (0.0 <= Ub) && (Ub <= 1.0)) {
            return new Vector2(x1 + Ua * x2_x1,
                               y1 + Ua * y2_y1);
        }

        // не пересекаются
        return null;
    }

    /**
     * Пересекается ли сегмент с коробкой
     * @param bb Коробка для проверки
     * @return true, если есть
     */
    public boolean intersect(final BoundingBox bb) {
        return bb.intersect(this);
    }

    /**
     * Пересекается ли сегмент с коробкой
     * @param bb Коробка для проверки
     * @return В списке точки пересечения, если есть
     */
    public List<Vector2> getIntersections(final BoundingBox bb) {
        return bb.getIntersections(this);
    }
}
