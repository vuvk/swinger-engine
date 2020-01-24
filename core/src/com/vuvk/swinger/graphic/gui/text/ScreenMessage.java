package com.vuvk.swinger.graphic.gui.text;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.vuvk.swinger.math.Vector2;

/**
 *
 * @author tai-prg3
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
