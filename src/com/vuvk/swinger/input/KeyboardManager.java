/**
    Copyright (C) 2019-2021 Anton "Vuvk" Shcherbatykh <vuvk69@gmail.com>
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
package com.vuvk.swinger.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Scanner;

import com.vuvk.swinger.Config;
import com.vuvk.swinger.Engine;
import com.vuvk.swinger.Game;
import com.vuvk.swinger.SavedGame;
import com.vuvk.swinger.graphic.Fog;
import com.vuvk.swinger.graphic.Renderer;
import com.vuvk.swinger.graphic.gui.menu.Menu;
import com.vuvk.swinger.objects.mortals.Player;
import com.vuvk.swinger.res.Map;
import com.vuvk.swinger.utils.Utils;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public final class KeyboardManager extends KeyAdapter {
    private static KeyboardManager instance = null;       
    
    private KeyboardManager () {}    

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
                case KeyEvent.VK_W :
                    if (Map.active && player.getHealth() > 0.0) {
                        player.setMoveF(false);
                    }
                    break;

                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S : 
                    if (Map.active && player.getHealth() > 0.0) {
                        player.setMoveB(false);
                    }
                    break;

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
                    Config.draw = false;
                    Renderer.getInstance().stopRenderTasks();

                    Map.load(1);

                    Config.draw = true;
                    Game.screenMsg.setMessage("LEVEL RESTARTED");
                    break;
                
                case KeyEvent.VK_M :
                    Config.mouseLook = !Config.mouseLook;
                    Game.screenMsg.setMessage("MOUSELOOK " + ((Config.mouseLook) ? "ON" : "OFF"));
                    break;

                case KeyEvent.VK_F5 :
                    SavedGame.save("quick.sav");
                    break;

                case KeyEvent.VK_F9 :
                    SavedGame.load("quick.sav");
                    break;
            }
        }
    }

    @Override
    public void keyTyped(final KeyEvent e) {
        char character = e.getKeyChar();
        if (Config.console) {
            if (Character.isLetterOrDigit(character) ||
                Character.isWhitespace(character) ||
                Character.isSpaceChar(character) ||
                character == ',' ||
                character == '.' ||
                character == '_' ||
                character == '-'
               ) {
                Config.consoleCommand += character;
            }
        }
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        Player player = Player.getInstance();
        int keycode = e.getKeyCode();
        
        if (!Config.console) {
            switch (keycode) {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W :
                    if (Map.isLoaded() &&
                        Map.active     &&
                        player.getHealth() > 0.0
                       ) {
                        player.setMoveF(true);
                    }
                    if (Menu.isActive()) {
                        Menu.currentMenu.prev();
                    }
                    break;

                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S : 
                    if (Map.isLoaded() &&
                        Map.active     &&
                        player.getHealth() > 0.0
                       ) {
                        player.setMoveB(true);
                    }
                    if (Menu.isActive()) {
                        Menu.currentMenu.next();
                    }
                    break;

                case KeyEvent.VK_A :
                    if (Map.isLoaded() &&
                        Map.active     &&
                        player.getHealth() > 0.0
                       ) {
                        player.setMoveL(true);
                    }
                    if (Menu.isActive()) {
                        Menu.currentMenu.getCurrentButton().left();
                    }
                    break;
                case KeyEvent.VK_D :
                    if (Map.isLoaded() &&
                        Map.active     &&
                        player.getHealth() > 0.0
                       ) {
                        player.setMoveR(true);
                    }
                    if (Menu.isActive()) {
                        Menu.currentMenu.getCurrentButton().right();
                    }
                    break;

                case KeyEvent.VK_Q : player.getPos().z -= Engine.getDeltaTime() * 15; break;
                case KeyEvent.VK_Z : player.getPos().z += Engine.getDeltaTime() * 15; break;

                case KeyEvent.VK_LEFT :
                    if (Map.isLoaded() &&
                        Map.active     &&
                        player.getHealth() > 0.0
                       ) {
                        player.setRotL(true);
                    }
                    if (Menu.isActive()) {
                        Menu.currentMenu.getCurrentButton().left();
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (Map.isLoaded() &&
                        Map.active     &&
                        player.getHealth() > 0.0
                       ) {
                        player.setRotR(true);
                    }
                    if (Menu.isActive()) {
                        Menu.currentMenu.getCurrentButton().right();
                    }
                    break;

            }
        } else {
            switch (keycode) {
                case KeyEvent.VK_BACK_SPACE :
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
            case KeyEvent.VK_ENTER :
                if (!Menu.isActive()) {
                    if (Config.console) {
                        String fullCommand = Config.consoleCommand.trim().toLowerCase();
                        Scanner scanCommand = new Scanner(fullCommand);

                        if (scanCommand.hasNext()) {
                            switch (scanCommand.next()) {
                                case "quit" :
                                    Config.QUIT = true;
                                    break;
                                        
                                case "interlacing" :
                                    Config.interlacing = (scanCommand.hasNextInt() && scanCommand.nextInt() == 1);
                                    Config.init();
                                    break;

                                case "fog" :
                                    if (scanCommand.hasNextInt()) {
                                        Config.fog = Fog.getByNum(scanCommand.nextInt());
                                    }
                                    break;

                                case "fog_start" :
                                    if (scanCommand.hasNextDouble()) {
                                        double start = scanCommand.nextDouble();
                                        if (start < 0.1f) {
                                            start = 0.1f;
                                        }
                                        if (start > Fog.END) {
                                            start = Fog.END;
                                        }
                                        Fog.START = start;
                                        Fog.init();
                                    }
                                    break;

                                case "fog_end" :
                                    if (scanCommand.hasNextDouble()) {
                                        double end = scanCommand.nextDouble();
                                        if (end < 0.1f) {
                                            end = 0.1f;
                                        }
                                        if (end < Fog.START) {
                                            end = Fog.START;
                                        }
                                        Fog.END = end;
                                        Fog.init();
                                    }
                                    break;

                                case "fog_color" :
                                    int red, green, blue;

                                    // получаем компоненты R G B
                                    if (scanCommand.hasNextInt()) {
                                        red = scanCommand.nextInt();
                                        red = Utils.limit(red, 0, 255);
                                    } else {
                                        break;
                                    }

                                    if (scanCommand.hasNextInt()) {
                                        green = scanCommand.nextInt();
                                        green = Utils.limit(green, 0, 255);
                                    } else {
                                        break;
                                    }

                                    if (scanCommand.hasNextInt()) {
                                        blue = scanCommand.nextInt();
                                        blue = Utils.limit(blue, 0, 255);
                                    } else {
                                        break;
                                    }

                                    // все компоненты введены - установить цвет тумана
                                    Fog.COLOR = 0xFF000000 |
                                                (red   << 16) |
                                                (green <<  8) |
                                                (blue  <<  0);

                                    Fog.init();
                                    break;

                                case "antialiasing" :
                                    Config.antialiasing = (scanCommand.hasNextInt() && scanCommand.nextInt() == 1);
                                    Config.init();
                                    break;

                                case "multithreading" :
                                    Config.multithreading = (scanCommand.hasNextInt() && scanCommand.nextInt() == 1);
                                    break;

                                case "mouselook" :
                                    Config.mouseLook = (scanCommand.hasNextInt() && scanCommand.nextInt() == 1);
                                    break;

                                case "sky" :
                                    Config.drawSky = (scanCommand.hasNextInt() && scanCommand.nextInt() == 1);
                                    break;

                                case "fullscreen" :
                                    if (!Config.buildForMobiles) {
                                        if (scanCommand.hasNextInt()) {
                                            Game.setFullscreenMode(scanCommand.nextInt() == 1);
                                        }
                                    }
                                    break;

                                case "vsync" :
                                    if (scanCommand.hasNextInt()) {
                                        Game.setVSync(scanCommand.nextInt() == 1);
                                    }
                                    break;
                            }
                        }
                        Config.consoleCommand = "";
                        scanCommand.close();
                    }
                    Config.console = !Config.console;
                } else {
                    Menu.currentMenu.getCurrentButton().click();
                }
                break;

            case KeyEvent.VK_ESCAPE :
                //Config.QUIT = true;

                if (Menu.isActive()) {
                    Menu.deactivate();
                    //Map.active = true;
                } else {
                    Menu.setMainSubMenu();
                    Menu.activate();
                    //Map.active = false;

                    if (player.getHealth() > 0.0) {
                        player.setMoveB(false);
                        player.setMoveF(false);
                        player.setMoveL(false);
                        player.setMoveR(false);

                        player.setRotL(false);
                        player.setRotR(false);
                    }
                }

                break;
        }
    }
}
