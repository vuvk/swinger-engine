/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.math;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tai-prg3
 */
public class BoundingBox implements Serializable {
    double left, 
           right, 
           top, 
           bottom;

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
     * Пересекается ли коробка с сегментом
     * @param segment Сегмент для проверки
     * @return В списке точки пересечения, если есть
     */
    public List<Vector2> intersect(final Segment segment) {
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
    
    /**
     * Сдвинуть коробку на вектор движения
     * @param moveVector вектор движения
     * @return Новая коробка после сдвига
     */
    public BoundingBox move(final Vector2 moveVector) {
        return new BoundingBox(left   + moveVector.x,
                               right  + moveVector.x,
                               top    + moveVector.y,
                               bottom + moveVector.y);
    }
}
