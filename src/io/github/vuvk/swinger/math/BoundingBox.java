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
package io.github.vuvk.swinger.math;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class BoundingBox implements Serializable {
    private double left,
                   right,
                   top,
                   bottom;
    private Vector2 center;

    public BoundingBox() {
        this(0, 0, 0, 0);
    }

    public BoundingBox(final Vector2 center, double radius) {
        this(center.x - radius,
             center.x + radius,
             center.y - radius,
             center.y + radius);
    }

    public BoundingBox(double left, double right, double top, double bottom) {
        this.left   = left;
        this.right  = right;
        this.top    = top;
        this.bottom = bottom;

        this.center = new Vector2(left + (right - left) / 2, top + (bottom - top) / 2);
    }

    public double getLeft() {
        return left;
    }

    public double getRight() {
        return right;
    }

    public double getTop() {
        return top;
    }

    public double getBottom() {
        return bottom;
    }

    public Vector2 getCenter() {
        return center;
    }
    
    /**
     * Находится ли точка внутри существа
     * @param point точка для проверки
     * @return true, если точка внутри
     */
    public boolean hasPoint(final Vector2 point) {
        return (
            point.x >= left  &&
            point.x <= right &&
            point.y >= top   &&
            point.y <= bottom
        );
    }

    /**
     * Пересекается ли коробка с другой
     * @param other Другая коробка
     * @return true, если перескается
     */
    public boolean intersect(BoundingBox other) {
        if (this.equals(other)) {
            return true;
        }

        // фигура левее или правее другой - не пересекаются
        if (right < other.getLeft() || left > other.getRight()) {
            return false;
        }

        // фигура ниже или выше другой - не пересекаются
        if (bottom < other.getTop() || top > other.getBottom()) {
            return false;
        }

        return true;
    }

    /**
     * Проверка пересечения с отрезком
     * @param segment Отрезок для проверки
     * @return true, если пересекает
     */
    public boolean intersect(final Segment segment) {
        return (
            // пересекается с верхом?
            (segment.intersect(new Segment(left,  top,    right, top))    != null) ||
            // пересекается с низом?
            (segment.intersect(new Segment(left,  bottom, right, bottom)) != null) ||
            // пересекается с левым?
            (segment.intersect(new Segment(left,  top,    left,  bottom)) != null) ||
            // пересекается с правым?
            (segment.intersect(new Segment(right, top,    right, bottom)) != null)
        );
    }

    /**
     * Пересекается ли коробка с сегментом
     * @param segment Сегмент для проверки
     * @return В списке точки пересечения, если есть
     */
    public List<Vector2> getIntersections(final Segment segment) {
        List<Vector2> intersections = new ArrayList<>(4);
        // пересекается с верхом?
        Vector2 top = segment.intersect(new Segment(getLeft(), getTop(), getRight(), getTop()));
        // пересекается с низом?
        Vector2 bottom = segment.intersect(new Segment(getLeft(), getBottom(), getRight(), getBottom()));
        // пересекается с левым?
        Vector2 left = segment.intersect(new Segment(getLeft(), getTop(), getLeft(), getBottom()));
        // пересекается с правым?
        Vector2 right = segment.intersect(new Segment(getRight(), getTop(), getRight(), getBottom()));

        if (top    != null) { intersections.add(top);    }
        if (bottom != null) { intersections.add(bottom); }
        if (left   != null) { intersections.add(left);   }
        if (right  != null) { intersections.add(right);  }

        return intersections;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public void setRight(double right) {
        this.right = right;
    }

    public void setTop(double top) {
        this.top = top;
    }

    public void setBottom(double bottom) {
        this.bottom = bottom;
    }

    /**
     * Сдвинуть коробку на вектор движения
     * @param moveVector вектор движения
     */
    public void setPos(final Vector2 pos) {
        double width  = right - left;
        double height = bottom - top;

        center.set(pos);

        left   = center.x - width * 0.5;
        right  = center.x + width * 0.5;
        top    = center.y - height * 0.5;
        bottom = center.y + height * 0.5;
    }

    /**
     * Получить новую коробку сдвинутую на вектор движения
     * @param moveVector вектор движения
     * @return Новая коробка после сдвига
     */
    public BoundingBox move(final Vector2 moveVector) {
        return new BoundingBox(left   + moveVector.x,
                               right  + moveVector.x,
                               top    + moveVector.y,
                               bottom + moveVector.y);
    }

    /**
     * Сдвинуть коробку на вектор движения
     * @param moveVector вектор движения
     */
    public void translate(final Vector2 moveVector) {
        setPos(center.add(moveVector));
    }
}
