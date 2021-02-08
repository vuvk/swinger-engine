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
package com.vuvk.swinger.graphic.gui.text;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class Symbol {
    transient private static final Logger LOG = Logger.getLogger(Symbol.class.getName());

    private Texture texture;

    public Symbol(Pixmap pixmap) {
        setTexture(new Texture(pixmap));
    }

    public Symbol(Texture texture) {
        setTexture(texture);
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void finalize() {
        texture.dispose();
        try {
            super.finalize();
        } catch (Throwable ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
}
