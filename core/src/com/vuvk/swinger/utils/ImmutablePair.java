/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.utils;

import java.io.Serializable;

/**
 *
 * @author tai-prg3
 */
public class ImmutablePair<T,V> implements Serializable {
    private final T left;
    private final V right;    

    public ImmutablePair(final T left, final V right) {
        this.left  = left;
        this.right = right;
    }

    public final T getLeft() {
        return left;
    }

    public final V getRight() {
        return right;
    }
}
