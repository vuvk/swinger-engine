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

import com.vuvk.swinger.audio.SoundBank;
import com.vuvk.swinger.audio.SoundSystem;
import com.vuvk.swinger.math.Vector2;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tai-prg3
 */
public class SubMenu {
    private final List<ButtonMenu> buttons = new ArrayList<>();
    private int pos = 0;

    public void activate() {
        setVisible(true);
        pos = 0;
        updateCursorLocation();
    }

    public void deactivate() {
        setVisible(false);
    }

    public void prev() {
        if (pos > 0) {
            --pos;
            SoundSystem.playOnce(SoundBank.FILE_MENU_SELECT);
            updateCursorLocation();
        }
    }

    public void next() {
        if (pos < getButtonsCount() - 1) {
            ++pos;
            SoundSystem.playOnce(SoundBank.FILE_MENU_SELECT);
            updateCursorLocation();
        }
    }

    public void addButton(ButtonMenu button) {
        buttons.add(button);
    }
    
    public ButtonMenu getCurrentButton() {
        return buttons.get(pos);
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
            btn.getText().setVisible(visible);
        }
    }    
    
    private void updateCursorLocation() {            
        Vector2 cursorLoc = Menu.CURSOR.getLocation();
        cursorLoc.y = buttons.get(pos).getText().getLocation().y;        
    }
}
