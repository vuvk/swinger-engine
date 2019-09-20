/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import com.vuvk.swinger.Config;
import com.vuvk.swinger.objects.creatures.Player;
import com.vuvk.swinger.graphic.Fog;
import com.vuvk.swinger.graphic.Renderer;
import com.vuvk.swinger.Window;
import com.vuvk.swinger.res.Map;

/**
 *
 * @author tai-prg3
 */
public final class KeyboardManager extends KeyAdapter {
    private static KeyboardManager instance = null;       
    //private static Player player;
    
    private KeyboardManager () {
    //    player = Player.getInstance();
    }    

    public static KeyboardManager getInstance() {
        if (instance == null) {
            instance = new KeyboardManager();
        }
        return instance;
    }
    
    @Override
    public void keyReleased(final KeyEvent e) {
        Player player = Player.getInstance();
        
        if (!Config.console) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W : player.setMoveF(false); break;

                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S : player.setMoveB(false); break;

                case KeyEvent.VK_A : player.setMoveL(false); break;
                case KeyEvent.VK_D : player.setMoveR(false); break;

                case KeyEvent.VK_LEFT : player.setRotL(false); break;
                case KeyEvent.VK_RIGHT: player.setRotR(false); break;
                
                case KeyEvent.VK_1 : player.setWeaponInHand(0); break;
                case KeyEvent.VK_2 : player.setWeaponInHand(1); break;
                case KeyEvent.VK_3 : player.setWeaponInHand(2); break;
                case KeyEvent.VK_4 : player.setWeaponInHand(3); break;
                case KeyEvent.VK_5 : player.setWeaponInHand(4); break;
                
                case KeyEvent.VK_SPACE : player.openDoor(); break;
                
                case KeyEvent.VK_R : 
                    Map.load(); 
                    Config.draw = true; 
                    break;
                
                case KeyEvent.VK_M : Config.mouseLook = !Config.mouseLook; break;
            }
        }
        
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER : { 
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
                } else {
                    Config.consoleCommand = "";
                }
                Config.console = !Config.console;
            } break;
            case KeyEvent.VK_ESCAPE : Window.QUIT = true; break;            
        }
    }

    @Override
    public void keyTyped(final KeyEvent e) {
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        Player player = Player.getInstance();
        
        if (!Config.console) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W : player.setMoveF(true); break;

                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S : player.setMoveB(true); break;

                case KeyEvent.VK_A : player.setMoveL(true); break;
                case KeyEvent.VK_D : player.setMoveR(true); break;

                case KeyEvent.VK_Q : player.getPos().z -= Renderer.getDeltaTime() * 15; break;
                case KeyEvent.VK_Z : player.getPos().z += Renderer.getDeltaTime() * 15; break;

                case KeyEvent.VK_LEFT : player.setRotL(true); break;
                case KeyEvent.VK_RIGHT: player.setRotR(true); break;
            }
        } else {
            int code = e.getKeyCode();
            if (code != KeyEvent.VK_ENTER) {
                if ((code >= KeyEvent.VK_A && code <= KeyEvent.VK_Z) || 
                    (code >= KeyEvent.VK_0 && code <= KeyEvent.VK_9) ||
                    (code == KeyEvent.VK_SPACE)) {
                    Config.consoleCommand += e.getKeyChar();
                } else if (code == KeyEvent.VK_BACK_SPACE) {
                    if (Config.consoleCommand.length() > 0) {
                        Config.consoleCommand = Config.consoleCommand.substring(0, Config.consoleCommand.length() - 1);
                    }
                }
            }
        }
    }
}
