/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.graphic.gui.text;

/**
 *
 * @author vuvk
 */
public final class Font {    
    private final Symbol[] symbols = new Symbol[128];
    
    public Font() {}
    
    public Font(final Symbol[] symbols) {
        setSymbols(symbols);
    }
    
    public void setSymbols(final Symbol[] symbols) {
        System.arraycopy(symbols, 0, this.symbols, 0, symbols.length);
    }

    public final Symbol[] getSymbols() {
        return symbols;
    }
    
    public void setSymbol(int ascii, final Symbol symbol) {
        symbols[ascii] = symbol;
    }
    
    public Symbol getSymbol(int ascii) {
        return symbols[ascii];
    }
}
