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
package io.github.vuvk.swinger.graphic.gui.text;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
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
