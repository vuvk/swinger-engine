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
package com.vuvk.swinger.res;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public final class Texture extends Image implements Serializable {
    public final static int WIDTH  = 128;
    public final static int HEIGHT = 128;
    public final static int WIDTH_POT  = 7;
    public final static int HEIGHT_POT = 7;
        
    /*
    public Texture(final BufferedImage image) {
        super(image, WIDTH, HEIGHT);
    }
    
    public Texture(final File file) {
        super(file, WIDTH, HEIGHT);
    }
    */
    public Texture(final String path) {
        super(path, WIDTH, HEIGHT);
    }
}
