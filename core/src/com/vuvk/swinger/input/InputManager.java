/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.vuvk.swinger.Config;
import com.vuvk.swinger.graphic.Fog;
import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.objects.Door;
import com.vuvk.swinger.objects.Sprite;
import com.vuvk.swinger.objects.creatures.Creature;
import com.vuvk.swinger.objects.creatures.Player;
import com.vuvk.swinger.res.Map;
import com.vuvk.swinger.res.Material;
import com.vuvk.swinger.res.TextureBank;
import com.vuvk.swinger.res.MaterialBank;
import com.vuvk.swinger.res.Texture;
import com.vuvk.swinger.res.WallMaterial;
import com.vuvk.swinger.res.WallMaterialBank;
import com.vuvk.swinger.utils.SavedGame;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tai-prg3
 */
public final class InputManager extends InputAdapter {
    private static final Logger LOG = Logger.getLogger(InputManager.class.getName());    
    
    private static Vector2 location = new Vector2();
    private static int scrollAmount = 0;
    private static boolean leftClick  = false;
    private static boolean rightClick = false;
    
    public InputManager () {}     
    
    public static double getDeltaX() {
        return Gdx.input.getDeltaX();
    }
    
    public static double getDeltaY() {
        return Gdx.input.getDeltaY();
    }
    
    public static Vector2 getDelta() {
        return new Vector2(getDeltaX(), getDeltaY());
    }
    
    public static void setLocation(int x, int y) {
        location.set(x, y);
        Gdx.input.setCursorPosition(x, y);
    }
    
    public static void setLocation(Vector2 point) {
        setLocation((int)point.x, (int)point.y);
    }

    public static Vector2 getLocation() {
        return location;
    } 
    
    public static boolean isScrollDown() {
        return (scrollAmount > 0);
    }
    
    public static boolean isScrollUp() {
        return (scrollAmount < 0);
    }
    
    public static boolean isLeftClick() {
        return leftClick;
    }
    
    public static boolean isRightClick() {
        return rightClick;
    }
    
    public static void reset() {
        //leftClick = rightClick = false;
        //location.set(0, 0);
        scrollAmount = 0;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        switch (button) {
            case Buttons.LEFT : leftClick  = true; break;
            case Buttons.RIGHT: rightClick = true; break;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        switch (button) {
            case Buttons.LEFT : leftClick  = false; break;
            case Buttons.RIGHT: rightClick = false; break;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
        
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        location.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        scrollAmount = amount;
        return false;
    }
    
    @Override
    public boolean keyDown (int keycode) {
        if (!Config.console) {
            Player player = Player.getInstance();
            switch (keycode) {
                case Input.Keys.UP:
                case Input.Keys.W : player.setMoveF(true); break;

                case Input.Keys.DOWN:
                case Input.Keys.S : player.setMoveB(true); break;

                case Input.Keys.A : player.setMoveL(true); break;
                case Input.Keys.D : player.setMoveR(true); break;

                case Input.Keys.Q : player.getPos().z -= Gdx.graphics.getDeltaTime() * 15; break;
                case Input.Keys.Z : player.getPos().z += Gdx.graphics.getDeltaTime() * 15; break;

                case Input.Keys.LEFT : player.setRotL(true); break;
                case Input.Keys.RIGHT: player.setRotR(true); break;
            }
        } else {
            switch (keycode) {
                case Input.Keys.BACKSPACE :
                    if (Config.consoleCommand.length() > 0) {
                        char[] cmd = Config.consoleCommand.toCharArray();
                        Config.consoleCommand = "";
                        StringBuilder sb = new StringBuilder(cmd.length - 1);                        
                        for (int i = 0; i < cmd.length - 1; ++i) {
                            sb.append(cmd[i]);
                        }
                        Config.consoleCommand = sb.toString();
                        //Config.consoleCommand = Config.consoleCommand.substring(0, Config.consoleCommand.length() - 1);
                        //System.out.println("backspace");
                    } else {
                        Config.consoleCommand = "";
                    }
                    break;
            }
        }

        switch (keycode) {
            case Input.Keys.ENTER :  
                if (Config.console) {
                    switch (Config.consoleCommand.trim().toLowerCase()) {
                        case "interlacing 0" : Config.interlacing = false; break;
                        case "interlacing 1" : Config.interlacing = true;  break;

                        case "fog 0" : Config.fog = Fog.NOTHING; break;
                        case "fog 1" : Config.fog = Fog.OLD;     break;
                        case "fog 2" : Config.fog = Fog.SMOOTH;  break;

                        case "antialiasing 0" : Config.antialiasing = false; break;
                        case "antialiasing 1" : Config.antialiasing = true;  break;

                        case "multithreading 0" : Config.multithreading = false; break;
                        case "multithreading 1" : Config.multithreading = true;  break;

                        case "mouselook 0" : Config.mouseLook = false; break;
                        case "mouselook 1" : Config.mouseLook = true;  break; 

                        case "sky 0" : Config.drawSky = false; break;
                        case "sky 1" : Config.drawSky = true;  break;
                    } 
                    Config.consoleCommand = "";
                }
                Config.console = !Config.console;
                break;

            case Input.Keys.ESCAPE : 
                Config.QUIT = true;
                break;
        }

        return false;
    }

    /** Called when a key goes up. When true is returned, the event is {@link Event#handle() handled}. */
    @Override
    public boolean keyUp (int keycode) {
        Player player = Player.getInstance();

        if (!Config.console) {
            switch (keycode) {
                case Input.Keys.UP:
                case Input.Keys.W : player.setMoveF(false); break;

                case Input.Keys.DOWN:
                case Input.Keys.S : player.setMoveB(false); break;

                case Input.Keys.A : player.setMoveL(false); break;
                case Input.Keys.D : player.setMoveR(false); break;

                case Input.Keys.LEFT : player.setRotL(false); break;
                case Input.Keys.RIGHT: player.setRotR(false); break;

                case Input.Keys.NUM_1 : player.setWeaponInHand(0); break;
                case Input.Keys.NUM_2 : player.setWeaponInHand(1); break;
                case Input.Keys.NUM_3 : player.setWeaponInHand(2); break;
                case Input.Keys.NUM_4 : player.setWeaponInHand(3); break;
                case Input.Keys.NUM_5 : player.setWeaponInHand(4); break;

                case Input.Keys.SPACE : player.openDoor(); break;

                case Input.Keys.R : 
                    Map.load(1); 
                    Config.draw = true; 
                    break;

                case Input.Keys.M : 
                    Config.mouseLook = !Config.mouseLook; 
                    break;
                
                case Input.Keys.F5 :
                    if (player.getHealth() > 0.0) {
                        //Map.save();      
                        File savesDir = new File("saves");
                        if (!savesDir.exists() || !savesDir.isDirectory()) {
                            savesDir.mkdir();
                        }

                        new SavedGame().saveToFile("saves/game.gam");
                        System.out.println("Game saved.");
                    }
                    break;
                
                case Input.Keys.F9 :
                    new SavedGame().loadFromFile("saves/game.gam");
                    break;
            }
        }

        return false;
    }

    /** Called when a key is typed. When true is returned, the event is {@link Event#handle() handled}.
     * @param character May be 0 for key typed events that don't map to a character (ctrl, shift, etc). */
    @Override
    public boolean keyTyped (char character) {
        if (Config.console) {
            if (Character.isLetterOrDigit(character) ||                
                character == ' ' || 
                character == ',' || 
                character == '.' 
               ) {
                Config.consoleCommand += character;
            }
        }
        return false;
    }
}
