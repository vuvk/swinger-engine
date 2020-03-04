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
import com.vuvk.swinger.audio.SoundBank;
import com.vuvk.swinger.audio.SoundSystem;
import com.vuvk.swinger.graphic.Fog;
import com.vuvk.swinger.graphic.Renderer;
import com.vuvk.swinger.graphic.gui.text.FontBank;
import com.vuvk.swinger.graphic.gui.text.Text;
import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.res.Map;
import com.vuvk.swinger.utils.SavedGame;
import java.io.File;
import java.util.Date;

/**
 *
 * @author tai-prg3
 */
public class Menu {     
    public final static Text CURSOR = new Text(FontBank.FONT_MENU, "@", new Vector2(Renderer.HALF_WIDTH - 64,1));
    
    private static boolean active = false;
    /* для меню сохранения/загрузки. Если true, то загружать при выборе, если false, то сохранять */
    private static boolean loadSubMenu = true;  
    public static SubMenu CURRENT;
    private final static SubMenu MAIN_MENU = new SubMenu(), 
                                 IN_GAME = new SubMenu(), 
                                 CLOSE_GAME_CONFIRM = new SubMenu(),
                                 LOAD_SAVE_GAME = new SubMenu(),
                                 OPTIONS = new SubMenu(),
                                 EXIT_MAIN_CONFIRM = new SubMenu();
    
    private Menu() {}
                
    
    public static void init() {             
        // меню при входе в игру
        MAIN_MENU.addButton(new ButtonMenu(new Text(FontBank.FONT_MENU, "NEW GAME", 
                                                    new Vector2(Renderer.HALF_WIDTH - 32, Renderer.HALF_HEIGHT - 32)), 
                                           () -> {
                                               Map.load(1); 
                                               changeSubMenu(IN_GAME);
                                               deactivate();
                                           }));        
        MAIN_MENU.addButton(new ButtonMenu(new Text(FontBank.FONT_MENU, "LOAD GAME", 
                                                    new Vector2(Renderer.HALF_WIDTH - 32, Renderer.HALF_HEIGHT)), 
                                           () -> {                 
                                               loadSubMenu = true;
                                               updateLoadSaveSubMenu();
                                               changeSubMenu(LOAD_SAVE_GAME);
                                           })); 
        MAIN_MENU.addButton(new ButtonMenu(new Text(FontBank.FONT_MENU, "OPTIONS", 
                                                    new Vector2(Renderer.HALF_WIDTH - 32, Renderer.HALF_HEIGHT + 32)), 
                                           () -> {
                                               updateOptionsSubMenu();
                                               changeSubMenu(OPTIONS);
                                           }));
        MAIN_MENU.addButton(new ButtonMenu(new Text(FontBank.FONT_MENU, "HELP", 
                                                    new Vector2(Renderer.HALF_WIDTH - 32, Renderer.HALF_HEIGHT + 64)), 
                                           () -> {}));
        MAIN_MENU.addButton(new ButtonMenu(new Text(FontBank.FONT_MENU, "QUIT GAME", 
                                                    new Vector2(Renderer.HALF_WIDTH - 32, Renderer.HALF_HEIGHT + 96)), 
                                           () -> changeSubMenu(CLOSE_GAME_CONFIRM) ));
        
        // меню внутри игры
        IN_GAME.addButton(new ButtonMenu(new Text(FontBank.FONT_MENU, "BACK", 
                                                  new Vector2(Renderer.HALF_WIDTH - 32, Renderer.HALF_HEIGHT - 32)), 
                                         () -> deactivate())); 
        IN_GAME.addButton(new ButtonMenu(new Text(FontBank.FONT_MENU, "LOAD GAME", 
                                                  new Vector2(Renderer.HALF_WIDTH - 32, Renderer.HALF_HEIGHT)), 
                                         () -> {                
                                             loadSubMenu = true;
                                             updateLoadSaveSubMenu();
                                             changeSubMenu(LOAD_SAVE_GAME);
                                         }));
        IN_GAME.addButton(new ButtonMenu(new Text(FontBank.FONT_MENU, "SAVE GAME", 
                                                  new Vector2(Renderer.HALF_WIDTH - 32, Renderer.HALF_HEIGHT + 32)), 
                                         () -> {
                                             loadSubMenu = false;
                                             updateLoadSaveSubMenu();
                                             changeSubMenu(LOAD_SAVE_GAME);
                                         }));
        IN_GAME.addButton(new ButtonMenu(new Text(FontBank.FONT_MENU, "OPTIONS", 
                                                  new Vector2(Renderer.HALF_WIDTH - 32, Renderer.HALF_HEIGHT + 64)), 
                                         () -> {
                                             updateOptionsSubMenu();
                                             changeSubMenu(OPTIONS);
                                         }));
        IN_GAME.addButton(new ButtonMenu(new Text(FontBank.FONT_MENU, "EXIT TO MAIN", 
                                                  new Vector2(Renderer.HALF_WIDTH - 32, Renderer.HALF_HEIGHT + 96)), 
                                         () -> changeSubMenu(EXIT_MAIN_CONFIRM) ));
        
        // меню со слотами сохранения/загрузки
        LOAD_SAVE_GAME.addButton(new ButtonMenu(new Text(FontBank.FONT_MENU, "BACK", 
                                                         new Vector2(Renderer.HALF_WIDTH - 32, Renderer.HALF_HEIGHT - 32)), 
                                                () -> setMainSubMenu() ));
        // слоты
        for (int i = 1; i < 6; ++i) {
            addSlotToLoadSaveSubMenu(i);
        }
        updateLoadSaveSubMenu();
        
        // меню опций
        OPTIONS.addButton(new ButtonMenu(new Text(FontBank.FONT_MENU, "BACK", 
                                                  new Vector2(Renderer.HALF_WIDTH - 32, Renderer.HALF_HEIGHT - 96)), 
                                         () -> setMainSubMenu() )); 
        OPTIONS.addButton(new ButtonMenu(new Text(FontBank.FONT_MENU, "MUSIC          100%", 
                                                  new Vector2(Renderer.HALF_WIDTH - 32, Renderer.HALF_HEIGHT - 64)), 
                                         () -> {} ));  
        OPTIONS.addButton(new ButtonMenu(new Text(FontBank.FONT_MENU, "VOLUME         100%", 
                                                  new Vector2(Renderer.HALF_WIDTH - 32, Renderer.HALF_HEIGHT - 32)), 
                                         () -> {} ));
        OPTIONS.addButton(new ButtonMenu(new Text(FontBank.FONT_MENU, "FOG            LOW", 
                                                  new Vector2(Renderer.HALF_WIDTH - 32, Renderer.HALF_HEIGHT)), 
                                         () -> {
                                             Config.fog = (Config.fog == Fog.SMOOTH) ? Fog.OLD : Fog.SMOOTH;
                                             updateOptionsSubMenu();
                                         } ));
        OPTIONS.addButton(new ButtonMenu(new Text(FontBank.FONT_MENU, "ANTIALIASING   OFF", 
                                                  new Vector2(Renderer.HALF_WIDTH - 32, Renderer.HALF_HEIGHT + 32)), 
                                         () -> {
                                             Config.antialiasing = !Config.antialiasing;
                                             if (Config.interlacing && Config.antialiasing) {
                                                 Config.interlacing = false;
                                             }
                                             updateOptionsSubMenu();                                             
                                         } ));
        OPTIONS.addButton(new ButtonMenu(new Text(FontBank.FONT_MENU, "INTERLACING    OFF", 
                                                  new Vector2(Renderer.HALF_WIDTH - 32, Renderer.HALF_HEIGHT + 64)), 
                                         () -> {
                                             Config.interlacing = !Config.interlacing;
                                             if (Config.interlacing && Config.antialiasing) {
                                                 Config.antialiasing = false;
                                             }
                                             updateOptionsSubMenu();                                             
                                         } ));
        OPTIONS.addButton(new ButtonMenu(new Text(FontBank.FONT_MENU, "MULTITHREADING ON", 
                                                  new Vector2(Renderer.HALF_WIDTH - 32, Renderer.HALF_HEIGHT + 96)), 
                                         () -> {
                                             Config.multithreading = !Config.multithreading;
                                             updateOptionsSubMenu();
                                         } ));
        OPTIONS.addButton(new ButtonMenu(new Text(FontBank.FONT_MENU, "MOUSELOOK      ON", 
                                                  new Vector2(Renderer.HALF_WIDTH - 32, Renderer.HALF_HEIGHT + 128)), 
                                         () -> {
                                             Config.mouseLook = !Config.mouseLook;
                                             updateOptionsSubMenu();
                                         } ));
        
        
        // меню подтверждения выхода в меню
        EXIT_MAIN_CONFIRM.addButton(new ButtonMenu(new Text(FontBank.FONT_MENU, "NO", 
                                                            new Vector2(Renderer.HALF_WIDTH - 32, Renderer.HALF_HEIGHT - 32)), 
                                                   () -> setMainSubMenu() ));
        EXIT_MAIN_CONFIRM.addButton(new ButtonMenu(new Text(FontBank.FONT_MENU, "YES", 
                                                            new Vector2(Renderer.HALF_WIDTH - 32, Renderer.HALF_HEIGHT)), 
                                                   () -> {
                                                       Map.reset();
                                                       Map.MUSIC = SoundSystem.loadSound(SoundBank.FILE_MUSIC_TITLE);
                                                       Map.MUSIC.setLooping(true);
                                                       Map.MUSIC.play();
                                                       changeSubMenu(MAIN_MENU);
                                                   }));
        
        // меню подтверждения выхода из игры
        CLOSE_GAME_CONFIRM.addButton(new ButtonMenu(new Text(FontBank.FONT_MENU, "NO", 
                                                             new Vector2(Renderer.HALF_WIDTH - 32, Renderer.HALF_HEIGHT - 32)), 
                                                    () -> setMainSubMenu() ));
        CLOSE_GAME_CONFIRM.addButton(new ButtonMenu(new Text(FontBank.FONT_MENU, "YES", 
                                                             new Vector2(Renderer.HALF_WIDTH - 32, Renderer.HALF_HEIGHT)), 
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
    
    private static void updateLoadSaveSubMenu() {
        for (int i = 1; i < 6; ++i) {
            ButtonMenu button = LOAD_SAVE_GAME.getButton(i);
            if (button == null) {
                continue;
            }
            
            File save = new File("saves/save" + i + ".sav");
            if (save.exists()) {
                Date dt = new Date(save.lastModified());
                String btnText = String.format("%02d/%02d/%02d %02d:%02d:%02d", 
                                               dt.getYear() - 100, dt.getMonth(), dt.getDate(),
                                               dt.getHours(), dt.getMinutes(), dt.getSeconds());
                button.getText().setMessage(btnText);
            } else {
                button.getText().setMessage("FREE SLOT " + i);
            }
        }
    }
    
    private static void updateOptionsSubMenu() {
        ButtonMenu btn;
        // fog
        btn = OPTIONS.getButton(3);
        btn.getText().setMessage("FOG            " + ((Config.fog == Fog.SMOOTH) ? "HIGH" : "LOW"));
        // antialising
        btn = OPTIONS.getButton(4);
        btn.getText().setMessage("ANTIALIASING   " + ((Config.antialiasing) ? "ON" : "OFF"));   
        // interlacing
        btn = OPTIONS.getButton(5);
        btn.getText().setMessage("INTERLACING    " + ((Config.interlacing) ? "ON" : "OFF"));  
        // multithreading
        btn = OPTIONS.getButton(6);
        btn.getText().setMessage("MULTITHREADING " + ((Config.multithreading) ? "ON" : "OFF"));    
        // mouselook
        btn = OPTIONS.getButton(7);
        btn.getText().setMessage("MOUSELOOK      " + ((Config.mouseLook) ? "ON" : "OFF"));        
    }
    
    private static void addSlotToLoadSaveSubMenu(final int num) {
        LOAD_SAVE_GAME.addButton(new ButtonMenu(new Text(FontBank.FONT_MENU, "FREE SLOT " + num, 
                                                         new Vector2(Renderer.HALF_WIDTH - 32, Renderer.HALF_HEIGHT + 32 * (num - 1))), 
                                 () -> {
                                     if (loadSubMenu) {
                                         SavedGame.load("save" + num + ".sav"); 
                                         setMainSubMenu();
                                     } else {
                                         SavedGame.save("save" + num + ".sav");
                                         updateLoadSaveSubMenu();
                                     }
                                     deactivate();
                                 }));        
    }
    
    public static void setMainSubMenu() {
        changeSubMenu(Map.isLoaded() ? IN_GAME : MAIN_MENU);        
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
        LOAD_SAVE_GAME.deactivate();
        OPTIONS.deactivate();
        CLOSE_GAME_CONFIRM.deactivate();
        
        if (Map.isLoaded()) {
            Map.active = true;
        }
    }
}
