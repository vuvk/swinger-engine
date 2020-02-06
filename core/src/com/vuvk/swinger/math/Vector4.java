package com.vuvk.swinger.math;

/**
 *
 * @author Shcherbatykh
 */
public class Vector4 extends Vector3 {
    public double w;    
    
    public Vector4() {
        this(0, 0, 0, 1);
    }    
    
    public Vector4(final Vector2 other) {
        this(other.x, other.y, 0.0, 1.0);
    }
    
    public Vector4(final Vector3 other) {
        this(other.x, other.y, other.z, 1.0);
    }
    
    public Vector4(final Vector4 other) {
        this(other.x, other.y, other.z, other.w);
    }
    
    public Vector4(final float[] components) {
        this(components[0], components[1], components[2], components[3]);
    }
    
    public Vector4(final double[] components) {
        this(components[0], components[1], components[2], components[3]);
    }
    
    public Vector4(double x, double y) {
        this(x, y, 0, 1.0);
    }
    
    public Vector4(double x, double y, double z) {
        this(x, y, z, 1.0);
    }
    
    public Vector4(double x, double y, double z, double w) {
        set(x, y, z, w);
    }
    
    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }
    
    public void set(double x, double y, double z, double w) {
        super.set(x, y, z);
        this.w = w;
    }   
    
    @Override
    public Vector4 add(final Vector2 other) {
        return new Vector4(x + other.x, y + other.y, z, w);
    }
    
    @Override
    public Vector4 add(final Vector3 other) {
        return new Vector4(x + other.x, y + other.y, z + other.z, w);
    }
    
    public Vector4 add(final Vector4 other) {
        return new Vector4(x + other.x, y + other.y, z + other.z, w + other.w);
    }
    
    @Override
    public Vector4 sub(final Vector2 other) {
        return new Vector4(x - other.x, y - other.y, z, w);
    }
    
    @Override
    public Vector4 sub(final Vector3 other) {
        return new Vector4(x - other.x, y - other.y, z - other.z, w);
    }
    
    public Vector4 sub(final Vector4 other) {
        return new Vector4(x - other.x, y - other.y, z - other.z, w - other.w);
    }
    
    @Override
    public Vector4 mul(double value) {
        return new Vector4(x * value, y * value, z * value, w * value);
    }
    
    @Override
    public Vector4 div(double value) {
        if (value != 0.0) {
            value = 1.0 / value;
            return new Vector4(x * value, y * value, z * value, w * value);
        } else {
            return new Vector4();
        }
    }
    
    @Override
    public Vector4 neg() {
        return new Vector4(-x, -y, -z, -w);
    }
    
    public double dot(Vector4 other) {
        return x * other.x + 
               y * other.y + 
               z * other.z + 
               w * other.w;
    }
    
    public double distance(final Vector4 another) {
        return (this.sub(another)).length();
    }
        
    @Override
    public double[] toArray() {
        return new double[]{x, y, z, w};
    }
    
    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            Vector4 v = (Vector4)other;
            return x == v.x &&
                   y == v.y &&
                   z == v.z &&
                   w == v.w;
        }        
        return false;
    }
}
