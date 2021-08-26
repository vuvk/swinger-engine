package com.vuvk.audiosystem;

/**
 *
 * @author vuvk
 */
public abstract class Disposable {
    abstract public void dispose();

    @Override
    public void finalize() {
        dispose();

        try {
            super.finalize();
        } catch (Throwable ignored) {}
    }
}
