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
package com.vuvk.swinger.objects;

import com.badlogic.gdx.Gdx;
import java.io.Serializable;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public abstract class GameObject implements Serializable {

    private boolean deferredDelete = false; // отложенное удаление
    private double delayForDelete = 0;      // время до отложенного удаления

    /**
     * Пометить объект на удаление
     */
    public abstract void destroy();

    /**
     * Пометить объект на отложенное удаление
     */
    public void destroy(double delay) {
        deferredDelete = true;
        delayForDelete = delay;
    }

    // почему так? Потому что в Java нельзя вызвать метод супер.супер.класса
    public void update() {
        updateDefferedDelete();
    }

    protected void updateDefferedDelete() {
        if (deferredDelete) {
            if (delayForDelete > 0.0) {
                delayForDelete -= Gdx.graphics.getDeltaTime();
            } else {
                destroy();
            }
        }
    }
}
