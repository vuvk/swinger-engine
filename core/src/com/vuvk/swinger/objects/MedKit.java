/*
 * Copyright 2019 .
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
package com.vuvk.swinger.objects;

import com.badlogic.gdx.files.FileHandle;
import com.vuvk.swinger.audio.SoundBank;
import com.vuvk.swinger.audio.SoundSystem;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.creatures.Player;
import com.vuvk.swinger.res.Material;

/**
 * Валяющиеся аптечки
 * @author vuvk
 */
public class MedKit extends Sprite {
    private final double volume;
    
    public MedKit(Material material, Vector3 pos, double volume) {
        super(material, pos);
        this.volume = volume;
    }
    
    public void update() {
        super.update();
        
        Player player = Player.getInstance();
        
        if (player != null && player.getPos().distance(getPos()) < 0.5) {
            double hp = player.getHealth();
            double maxHp = player.getMaxHealth();
            if (hp < maxHp) {
                hp += volume;
                if (hp > maxHp) {
                    hp = maxHp;
                }
                
                player.setHealth(hp);
                SoundSystem.playOnceRandom(new FileHandle[] { SoundBank.FILE_GET_MEDKIT1, SoundBank.FILE_GET_MEDKIT2 });

                markForDelete();
            }
        }
    }
}