/*
 * Copyright 2020 .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vuvk.swinger.graphic.gui.menu;

import com.vuvk.swinger.Config;
import static com.vuvk.swinger.Game.screenMsg;
import com.vuvk.swinger.audio.SoundBank;
import com.vuvk.swinger.audio.SoundSystem;
import com.vuvk.swinger.graphic.Renderer;
import com.vuvk.swinger.graphic.gui.text.FontBank;
import com.vuvk.swinger.graphic.gui.text.Text;
import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.res.Map;
import com.vuvk.swinger.utils.SavedGame;
import javax.swing.JOptionPane;

/**
 *
 * @author tai-prg3
 */
public class Menu {     
    public final static Text CURSOR = new Text(FontBank.FONT_OUTLINE, ">", new Vector2(Renderer.HALF_WIDTH - 125,1));
    
    private static boolean active = false;
    public static SubMenu CURRENT;
    private final static SubMenu MAIN_MENU = new SubMenu(), 
                                 IN_GAME = new SubMenu(), 
                                 CLOSE_GAME_CONFIRM = new SubMenu(),
                                 EXIT_MAIN_CONFIRM = new SubMenu();
    
    private Menu() {}
                
    
    public static void init() {             
        // меню при входе в игру
        MAIN_MENU.addButton(new ButtonMenu(new Text(FontBank.FONT_OUTLINE, "New Game", 
                                                    new Vector2(Renderer.HALF_WIDTH - 100, Renderer.HALF_HEIGHT - 50)), 
                                           () -> {
                                               Map.load(1); 
                                               changeSubMenu(IN_GAME);
                                               deactivate();
                                           }));        
        MAIN_MENU.addButton(new ButtonMenu(new Text(FontBank.FONT_OUTLINE, "Load Game", 
                                                    new Vector2(Renderer.HALF_WIDTH - 100, Renderer.HALF_HEIGHT)), 
                                           () -> {                                                       
                                               SavedGame.load();
                                               changeSubMenu(IN_GAME);
                                               deactivate();
                                           }));
        MAIN_MENU.addButton(new ButtonMenu(new Text(FontBank.FONT_OUTLINE, "Quit Game", 
                                                    new Vector2(Renderer.HALF_WIDTH - 100, Renderer.HALF_HEIGHT + 50)), 
                                           () -> changeSubMenu(CLOSE_GAME_CONFIRM) ));
        
        // меню внутри игры
        IN_GAME.addButton(new ButtonMenu(new Text(FontBank.FONT_OUTLINE, "Back", 
                                                  new Vector2(Renderer.HALF_WIDTH - 100, Renderer.HALF_HEIGHT - 50)), 
                                         () -> deactivate())); 
        IN_GAME.addButton(new ButtonMenu(new Text(FontBank.FONT_OUTLINE, "Load Game", 
                                                  new Vector2(Renderer.HALF_WIDTH - 100, Renderer.HALF_HEIGHT)), 
                                         () -> {                                    
                                             SavedGame.load();
                                             deactivate();
                                         }));
        IN_GAME.addButton(new ButtonMenu(new Text(FontBank.FONT_OUTLINE, "Save Game", 
                                                  new Vector2(Renderer.HALF_WIDTH - 100, Renderer.HALF_HEIGHT + 50)), 
                                         () -> {                                    
                                             SavedGame.save();
                                             deactivate();
                                         }));
        IN_GAME.addButton(new ButtonMenu(new Text(FontBank.FONT_OUTLINE, "Exit To Main", 
                                                  new Vector2(Renderer.HALF_WIDTH - 100, Renderer.HALF_HEIGHT + 100)), 
                                         () -> changeSubMenu(EXIT_MAIN_CONFIRM) ));
        
        // меню подтверждения выхода в меню
        EXIT_MAIN_CONFIRM.addButton(new ButtonMenu(new Text(FontBank.FONT_OUTLINE, "NO", 
                                                            new Vector2(Renderer.HALF_WIDTH - 100, Renderer.HALF_HEIGHT - 50)), 
                                                   () -> changeSubMenu(Map.isLoaded() ? IN_GAME : MAIN_MENU)));
        EXIT_MAIN_CONFIRM.addButton(new ButtonMenu(new Text(FontBank.FONT_OUTLINE, "YES", 
                                                            new Vector2(Renderer.HALF_WIDTH - 100, Renderer.HALF_HEIGHT)), 
                                                   () -> {
                                                       Map.reset();
                                                       Map.MUSIC = SoundSystem.loadSound(SoundBank.FILE_MUSIC_TITLE);
                                                       Map.MUSIC.setLooping(true);
                                                       Map.MUSIC.play();
                                                       changeSubMenu(MAIN_MENU);
                                                   }));
        
        // меню подтверждения выхода из игры
        CLOSE_GAME_CONFIRM.addButton(new ButtonMenu(new Text(FontBank.FONT_OUTLINE, "NO", 
                                                             new Vector2(Renderer.HALF_WIDTH - 100, Renderer.HALF_HEIGHT - 50)), 
                                                    () -> changeSubMenu(Map.isLoaded() ? IN_GAME : MAIN_MENU) ));
        CLOSE_GAME_CONFIRM.addButton(new ButtonMenu(new Text(FontBank.FONT_OUTLINE, "YES", 
                                                             new Vector2(Renderer.HALF_WIDTH - 100, Renderer.HALF_HEIGHT)), 
                                                    () -> {
                                                        System.out.println("Bye-bye.");
                                                        Config.QUIT = true;
                                                    }));
        
        CURRENT = MAIN_MENU;
        
        deactivate();
    }

    private static void changeSubMenu(SubMenu menu) {
        deactivate();
        CURRENT = menu;
        activate();
    }
    
    public static boolean isActive() {
        return active;
    }
    
    public static void activate() {
        CURSOR.setVisible(true);
        active = true;
        CURRENT.activate();
        
        if (Map.isLoaded()) {
            Map.active = false;
        }
    }
    
    public static void deactivate() {
        CURSOR.setVisible(false);
        active = false;
        
        MAIN_MENU.deactivate();
        IN_GAME.deactivate();
        EXIT_MAIN_CONFIRM.deactivate();
        CLOSE_GAME_CONFIRM.deactivate();
        
        if (Map.isLoaded()) {
            Map.active = true;
        }
    }
}
