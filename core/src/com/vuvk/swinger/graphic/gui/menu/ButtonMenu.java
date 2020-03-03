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
import com.vuvk.swinger.graphic.gui.text.Text;

/**
 *
 * @author tai-prg3
 */
public class ButtonMenu {
    private Text text;
    private Runnable method;

    public ButtonMenu(Text text, Runnable method) {
        this.text = text;
        this.method = method;
    }

    public void click() {
        SoundSystem.playOnce(SoundBank.FILE_MENU_TOGGLE);
        method.run();
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }    
}
