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
public class Pair<T,V> implements Serializable {
    private T left;
    private V right; 
    
    public Pair() {
    }

    public Pair(T left, V right) {
        this.left = left;
        this.right = right;
    }

    public T getLeft() {
        return left;
    }

    public void setLeft(T left) {
        this.left = left;
    }

    public V getRight() {
        return right;
    }

    public void setRight(V right) {
        this.right = right;
    }    
}
