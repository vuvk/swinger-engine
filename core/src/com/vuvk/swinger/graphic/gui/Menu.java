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
package com.vuvk.swinger.graphic.gui;

import com.vuvk.swinger.graphic.Renderer;
import com.vuvk.swinger.graphic.gui.text.FontBank;
import com.vuvk.swinger.graphic.gui.text.Text;
import com.vuvk.swinger.math.Vector2;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author tai-prg3
 */
public class Menu {
    
    public static class ButtonMenu {
        private Text text;
        private Runnable method;

        public ButtonMenu(Text text, Runnable method) {
            this.text = text;
            this.method = method;
        }
        
        void click() {
            method.run();
        }

        public Text getText() {
            return text;
        }

        public void setText(Text text) {
            this.text = text;
        }
    }
    
    public static class SubMenu {
        private final List<ButtonMenu> buttons = new ArrayList<>();
        private int pos = 0;
        
        public void activate() {
            setVisible(true);
            pos = 0;
        }
        
        public void deactivate() {
            setVisible(false);
            pos = 0;
        }
        
        public void decPos() {
            if (pos > 0) {
                --pos;
            }
        }
        
        public void incPos() {
            if (pos < getButtonsCount() - 1) {
                ++pos;
            }
        }
        
        public void addButton(ButtonMenu button) {
            buttons.add(button);
        }
        
        public ButtonMenu getButton(int index) {
            return buttons.get(index);
        }
        
        public int getButtonsCount() {
            return buttons.size();
        }
        
        public int getPos() {
            return pos;
        }
        
        public void setVisible(boolean visible) {
            for (ButtonMenu btn : buttons) {
                btn.text.setVisible(visible);
            }
        }
    }
    
    private final static Menu INSTANCE = new Menu();
    
    private static final Text cursor = new Text(FontBank.FONT_BUBBLA);
    private boolean active = false;
    public static SubMenu CURRENT;
    private static SubMenu[] subMenus;
            
    
    public static void init() {
        subMenus = new SubMenu[1];
        
        // меню при входе в игру
        subMenus[0] = new SubMenu();
        subMenus[0].addButton(new ButtonMenu(new Text(FontBank.FONT_OUTLINE, "New Game", 
                                                      new Vector2(Renderer.HALF_WIDTH - 50, Renderer.HALF_HEIGHT - 50)), 
                                             () -> JOptionPane.showMessageDialog(null, "NEW GAME!")));
        
        subMenus[0].addButton(new ButtonMenu(new Text(FontBank.FONT_OUTLINE, "Load Game", 
                                                      new Vector2(Renderer.HALF_WIDTH - 50, Renderer.HALF_HEIGHT)), 
                                             () -> JOptionPane.showMessageDialog(null, "loading here!")));
        subMenus[0].addButton(new ButtonMenu(new Text(FontBank.FONT_OUTLINE, "Quit Game", 
                                                      new Vector2(Renderer.HALF_WIDTH - 50, Renderer.HALF_HEIGHT + 50)), 
                                             () -> JOptionPane.showMessageDialog(null, "now quit :)")));
        
        CURRENT = subMenus[0];
        
        deactivate();
    }

    public static boolean isActive() {
        return INSTANCE.active;
    }
    
    public static void activate() {
        INSTANCE.active = true;
        CURRENT = subMenus[0];
        CURRENT.activate();
    }
    
    public static void deactivate() {
        INSTANCE.active = false;
        CURRENT.deactivate();
    }
}
