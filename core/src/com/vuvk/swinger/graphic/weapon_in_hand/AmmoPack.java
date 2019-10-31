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
package com.vuvk.swinger.graphic.weapon_in_hand;

import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author tai-prg3
 */
public final class AmmoPack {
    public final static Map<AmmoType, Integer> PACK = new HashMap<>();
        
    public static void reset() {
        PACK.clear();
        PACK.put(AmmoType.NOTHING, 0 );
        PACK.put(AmmoType.PISTOL,  20);
        PACK.put(AmmoType.SHOTGUN, 0 );
        PACK.put(AmmoType.ROCKET,  0 );
    }
    
    private AmmoPack() {}
}
