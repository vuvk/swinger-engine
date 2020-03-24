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
import com.vuvk.swinger.graphic.gui.text.Text;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class ButtonMenu {
    private Text text;
    private Runnable click, left, right;

    public ButtonMenu(Text text, Runnable click) {
        this.text = text;
        this.click = click;
        this.left  = click;
        this.right = click;
    }
    
    public ButtonMenu(Text text, Runnable click, Runnable left, Runnable right) {
        this.text = text;
        this.click = click;
        this.left = left;
        this.right = right;
    }

    public void click() {
        SoundSystem.playOnce(SoundBank.FILE_MENU_TOGGLE);
        click.run();
    }
    
    public void left() {
        SoundSystem.playOnce(SoundBank.FILE_MENU_TOGGLE);
        if (left != null) {
            left.run();
        }        
    }
    
    public void right() {
        SoundSystem.playOnce(SoundBank.FILE_MENU_TOGGLE);
        if (right != null) {
            right.run();
        }        
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }    
}
