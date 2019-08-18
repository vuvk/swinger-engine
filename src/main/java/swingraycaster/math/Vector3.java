/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.math;

/**
 *
 * @author tai-prg3
 */
public class Vector3 extends Vector2 {
    public double z;
    
    public Vector3() {
        this(0, 0, 0);
    }    
    
    public Vector3(final Vector2 other) {
        this(other.x, other.y, 0.0);
    }
    
    public Vector3(final Vector3 other) {
        this(other.x, other.y, other.z);
    }
    
    public Vector3(double x, double y) {
        this(x, y, 0);
    }
    
    public Vector3(double x, double y, double z) {
        set(x, y, z);
    }
    
    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
    
    public void set(double x, double y, double z) {
        super.set(x, y);
        this.z = z;
    }   
    
    @Override
    public Vector3 add(final Vector2 other) {
        return new Vector3(x + other.x, y + other.y, z);
    }
    
    public Vector3 add(final Vector3 other) {
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }
    
    @Override
    public Vector3 sub(final Vector2 other) {
        return new Vector3(x - other.x, y - other.y, z);
    }
    
    public Vector3 sub(final Vector3 other) {
        return new Vector3(x - other.x, y - other.y, z - other.z);
    }
    
    @Override
    public Vector3 mul(double value) {
        return new Vector3(x * value, y * value, z * value);
    }
    
    @Override
    public Vector3 div(double value) {
        if (value != 0.0) {
            value = 1.0 / value;
            return new Vector3(x * value, y * value, z * value);
        } else {
            return new Vector3();
        }
    }
    
    @Override
    public Vector3 neg() {
        return new Vector3(-x, -y, -z);
    }
    
    public double dot(Vector3 other) {
        return x * other.x + 
               y * other.y + 
               z * other.z;
    }
    
    public double distance(final Vector3 another) {
        return (this.sub(another)).length();
    }
    
    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            Vector3 v = (Vector3)other;
            return x == v.x &&
                   y == v.y &&
                   z == v.z;
        }
        
        return false;
    }
}
