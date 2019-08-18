/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.graphic;

import swingraycaster.objects.creatures.Player;
import swingraycaster.res.Image;
import swingraycaster.res.TextureBank;

/**
 *
 * @author tai-prg3
 */
public final class Sky extends Image {    
    private static Sky instance = new Sky(TextureBank.PICS_FOLDER + "sky.png");
    private final static Player PLAYER = Player.getInstance();
    
    private double xStep;    
    private int xStart;
    
    public Sky(final String path) {
        super(path, Renderer.HEIGHT << 2, Renderer.HEIGHT);
        xStep = width / 360.0;
    }
    
    public int getXStart() {
        return xStart;
    }
    
    public static Sky getInstance() {
        return instance;
    }
    
    public void update() {
        xStart = (int)(PLAYER.getDirection() * xStep);
        if (xStart >= width) {
            xStart = 0;
        }
    }
}
