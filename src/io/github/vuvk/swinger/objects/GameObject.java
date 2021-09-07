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
package io.github.vuvk.swinger.objects;

import java.io.Serializable;

import io.github.vuvk.swinger.Engine;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public abstract class GameObject implements Serializable {

    private static final Set<GameObject> LIB = new CopyOnWriteArraySet<>();    
    private static final long serialVersionUID = 1L;

    private boolean deferredDelete = false; // отложенное удаление
    private double delayForDelete = 0;      // время до отложенного удаления

    protected GameObject() {
        LIB.add(this);
    }
    
    /**
     * Пометить объект на удаление
     */
    public void destroy() {
        LIB.remove(this);
    }

    /**
     * Пометить объект на отложенное удаление
     */
    public void destroy(double delay) {
        deferredDelete = true;
        delayForDelete = delay;
    }

    public void update() {
        if (deferredDelete) {
            if (delayForDelete > 0.0) {
                delayForDelete -= Engine.getDeltaTime();
            } else {
                destroy();
            }
        }
    }
    
    public static void updateAll() {
        LIB.forEach(GameObject::update);
    }
    
    public static void destroyAll() {
        LIB.forEach(GameObject::destroy);
    }  
}
