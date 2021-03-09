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
package com.vuvk.swinger.utils;

import java.io.Serializable;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class MutablePair<T,V> implements Pair, Serializable {
    private T left;
    private V right;

    public MutablePair() {}

    public MutablePair(T left, V right) {
        this.left = left;
        this.right = right;
    }

    public T getLeft() {
        return left;
    }

    public V getRight() {
        return right;
    }

    public void setLeft(T left) {
        this.left = left;
    }

    public void setRight(V right) {
        this.right = right;
    }

    public void set(T left, V right) {
        setLeft(left);
        setRight(right);
    }
}
