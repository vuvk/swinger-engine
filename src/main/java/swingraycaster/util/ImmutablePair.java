/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.util;

/**
 *
 * @author tai-prg3
 */
public class ImmutablePair<T,V> {
    private final T left;
    private final V right;    

    public ImmutablePair(T left, V right) {
        this.left = left;
        this.right = right;
    }

    public T getLeft() {
        return left;
    }

    public V getRight() {
        return right;
    }
}
