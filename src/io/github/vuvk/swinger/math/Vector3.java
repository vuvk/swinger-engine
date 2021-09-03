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
package io.github.vuvk.swinger.math;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class Vector3 extends Vector2 {

    public final static Vector3 ZERO = new Vector3();
    public final static Vector3 ONE  = new Vector3(1.0, 1.0, 1.0);

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

    public Vector3(final Vector4 other) {
        double w;
        if (other.w != 0.0)
            w = 1.0 / other.w;
        else
            w = 0.0;

        x = other.x * w;
        y = other.y * w;
        z = other.z * w;
    }

    public Vector3(final float[] components) {
        this(components[0], components[1], components[2]);
    }

    public Vector3(final double[] components) {
        this(components[0], components[1], components[2]);
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

    public Vector3 cross(Vector3 other) {
        return new Vector3(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        );
    }

    @Override
    public Vector3 normalize() {
        return div(length());
    }

    @Override
    public double length() {
        return Math.sqrt(x*x + y*y + z*z);
    }

    public double distance(final Vector3 another) {
        return (this.sub(another)).length();
    }

    @Override
    public double[] toArray() {
        return new double[]{x, y, z};
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
