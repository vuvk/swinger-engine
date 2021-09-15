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

import io.github.vuvk.swinger.Engine;
import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public abstract class GameObject implements Serializable {

    transient private static final Set<GameObject> LIB = new CopyOnWriteArraySet<>();
    private static final long serialVersionUID = 1L;

    /** frame count in sec when go updating */
    private static final double UPDATES_PER_SEC = 30;
    /** max timeout between frames for update go */
    private static final double UPDATE_TIMEOUT = 1 / UPDATES_PER_SEC;

    private static double delayUpdate = 0;
    private static double accumulatedDeltaTime = 0;

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
                delayForDelete -= getAccumulatedDeltaTime();
            } else {
                destroy();
            }
        }
    }

    protected static double getAccumulatedDeltaTime() {
        return accumulatedDeltaTime;
    }

    public static void updateAll() {
        // timeout for update ai
        if (delayUpdate < UPDATE_TIMEOUT) {
            delayUpdate += Engine.getDeltaTime();
            return;
        } else {
            accumulatedDeltaTime = delayUpdate;
            delayUpdate = 0;

            LIB.forEach(GameObject::update);
        }
    }

    public static void destroyAll() {
        LIB.forEach(GameObject::destroy);
    }
}
