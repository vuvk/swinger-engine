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
package com.vuvk.swinger.graphic.gui.menu;

import com.vuvk.swinger.audio.SoundBank;
import com.vuvk.swinger.audio.SoundSystem;
import com.vuvk.swinger.math.Vector2;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
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
