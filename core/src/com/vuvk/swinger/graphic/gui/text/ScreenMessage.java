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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.vuvk.swinger.math.Vector2;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class ScreenMessage extends Text {
    private static double MAX_TIME_FOR_DRAW = 3.0;
    private double timeForDraw = 0.0;
    
    public ScreenMessage(Font font, Vector2 location) {
        super(font, "", location);
    }

    @Override
    public void setMessage(String message) {
        super.setMessage(message);
        
        timeForDraw = 0;
    }    
    
    @Override
    public void draw(Batch batch) {  
        super.draw(batch);
        
        if (timeForDraw <= MAX_TIME_FOR_DRAW) {
            timeForDraw += Gdx.graphics.getDeltaTime();
        } else {
            if (message.length() > 0) {
                message = "";
            }
        }
    }
}
