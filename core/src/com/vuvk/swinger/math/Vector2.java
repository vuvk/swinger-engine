/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.math;

import java.io.Serializable;

/**
 *
 * @author tai-prg3
 */
public class Vector2 implements Serializable {
    public double x;
    public double y;    
    
    public Vector2() {
        this(0, 0);
    }
    
    public Vector2(double x, double y) {
        set(x, y);
    }
    
    public Vector2(final Vector2 other) {
        this(other.x, other.y);
    }    
    
    public Vector2(final Vector3 other) {        
        double z;
        if (other.z != 0.0)
            z = 1.0 / other.z;
        else
            z = 0.0;

        x = other.x * z;
        y = other.y * z;
    }
    
    public Vector2(final float[] components) {
        this(components[0], components[1]);
    }
    
    public Vector2(final double[] components) {
        this(components[0], components[1]);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }    
    
    public void set(double x, double y) {
        this.x = x;
        this.y = y;        
    }
    
    public void set(final Vector2 other) {
        set(other.x, other.y);        
    }
    
    public Vector2 add(final Vector2 other) {
        return new Vector2(x + other.x, y + other.y);
    }
    
    public Vector2 sub(final Vector2 other) {
        return new Vector2(x - other.x, y - other.y);
    }
    
    public Vector2 mul(double value) {
        return new Vector2(x * value, y * value);
    }
    
    public Vector2 div(double value) {
        if (value != 0.0) {
            value = 1.0 / value;
            return new Vector2(x * value, y * value);
        } else {
            return new Vector2();
        }
    }
    
    public Vector2 neg() {
        return new Vector2(-x, -y);
    }
    
    /*
    public Vector2 subtract(final Vector2 other) {
        return new Vector2(x - other.x, y - other.y);
    }
    
    public Vector2 multiply(double value) {
        return new Vector2(x * value, y * value);
    }
    
    public Vector2 divide(double value) {
        if (value != 0.0) {
            value = 1.0 / value;
            return new Vector2(x * value, y * value);
        } else {
            return new Vector2();
        }
    }
    */
    
    public double dot(final Vector2 other) {
        return x * other.x + y * other.y;
    }
    
    public Vector2 normalize() {
        return div(length());
    }
    
    public double length() {
        return Math.sqrt(x*x + y*y);
    }
    
    public double distance(final Vector2 other) {
        return (this.sub(other)).length();
    }
        
    public double[] toArray() {
        return new double[]{x, y};
    }
    
    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            Vector2 v = (Vector2)other;
            return x == v.x &&
                   y == v.y;
        }
        
        return false;
    }
}
