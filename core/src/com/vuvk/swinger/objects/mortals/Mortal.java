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
package com.vuvk.swinger.objects.mortals;

import com.vuvk.swinger.math.BoundingBox;
import com.vuvk.swinger.math.Segment;
import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.Object3D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public abstract class Mortal extends Object3D implements Serializable {
    transient public final static List<Mortal> LIB = new CopyOnWriteArrayList<>();
    //transient private final static List<Mortal> FOR_DELETE_FROM_LIB = new ArrayList<>();

    protected double health;
    protected final double maxHealth;
    protected double radius;
    protected boolean live = false; // является ли объект живым - живой испускает кровь, неживой  пыль
    protected final BoundingBox bb;

    public Mortal(final Vector3 pos, double health, double radius) {
        bb = new BoundingBox(pos, radius);

        setHealth(health);
        setRadius(radius);
        setPos(pos);
        maxHealth = health;
    //  update();
        LIB.add(this);
    }

    @Override
    public void finalize() {
        destroy();
    }

    /**
     * Пометить объект на удаление
     */
    @Override
    public void destroy() {
        synchronized (LIB) {
            LIB.remove(this);
        }
    }

    public static void updateAll() {
        synchronized(LIB) {
            LIB.forEach(Mortal::update);
        }
    }

    public static void deleteAll() {
        LIB.clear();
    }

    public double getHealth() {
        return health;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public BoundingBox getBB() {
        return bb;
    }

    public double getRadius() {
        return radius;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void setPos(final Vector3 pos) {
        super.setPos(pos);
        bb.setPos(pos);
    }

    private void setRadius(double radius) {
        this.radius = radius;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    /**
     * Получить урон
     * @param damage величина урона
     */
    public void applyDamage(double damage) {
        //if (health > 0.0) {
            health -= damage;
        //}
    }

    /**
     * Проверка пересечения с квадратом
     * @param box Ограничивающая коробка для проверки
     * @return true, если пересекает
     */
    public boolean intersect(final BoundingBox box) {
        return bb.intersect(box);
    }

    /**
     * Проверка пересечения с кругом
     * @param center Центр круга
     * @param radius Радиус круга
     * @return true, если пересекает
     */
    public boolean intersect(final Vector2 center, double radius) {
        double dX = getPos().x - center.x;
        double dY = getPos().y - center.y;
        double distance = Math.sqrt(dX * dX + dY * dY);

        return ((distance < this.radius + radius) &&
                (distance > Math.abs(this.radius - radius)));
    }

    /**
     * Проверить есть ли какое-то создание в точке (учитывая радиус существ)
     * @param pos Проверяемая позиция
     * @return Список существ
     */
    public static List<Mortal> whoInPos(final Vector2 pos) {
        ArrayList<Mortal> mortals = new ArrayList<>(Mortal.LIB.size());
        for (Mortal mortal : LIB) {
            if (mortal.bb.hasPoint(pos)) {
                mortals.add(mortal);
            }
        }
        return mortals;
    }

    /**
     * Проверить есть ли какое-то создание в точке (учитывая радиус существ)
     * @param pos Проверяемая позиция
     * @param whoIgnore Какое существо игнорировать в проверке
     * @return Список существ
     */
    public static List<Mortal> whoInPos(final Vector2 pos, final Mortal whoIgnore) {
        List<Mortal> mortals = whoInPos(pos);
        mortals.remove(whoIgnore);
        return mortals;
    }

    /**
     * Проверить есть ли какое-то создание, которое пересекается с заданным квадратом
     * @param box Проверяемый квадрат
     * @return Список существ
     */
    public static List<Mortal> whoIntersectBox(final BoundingBox box) {
        ArrayList<Mortal> mortals = new ArrayList<>(Mortal.LIB.size());
        for (Mortal mortal : LIB) {
            if (mortal.intersect(box)) {
                mortals.add(mortal);
            }
        }
        return mortals;
    }

    /**
     * Проверить есть ли какое-то создание, которое пересекается с заданным квадратом
     * @param box Проверяемый квадрат
     * @param whoIgnore Какое существо игнорировать в проверке
     * @return Список существ
     */
    public static List<Mortal> whoIntersectBox(final BoundingBox box, final Mortal whoIgnore) {
        List<Mortal> mortals = whoIntersectBox(box);
        mortals.remove(whoIgnore);
        return mortals;
    }

    /**
     * Проверить есть ли какое-то создание, которое пересекается с текущим
     * @param center Центр круга
     * @param radius Радиус круга
     * @return Список существ
     */
    public static List<Mortal> whoIntersectCircle(final Vector2 center, double radius) {
        ArrayList<Mortal> mortals = new ArrayList<>(Mortal.LIB.size());
        for (Mortal mortal : LIB) {
            if (mortal.intersect(center, radius)) {
                mortals.add(mortal);
            }
        }
        return mortals;
    }

    /**
     * Проверить есть ли какое-то создание, которое пересекается с текущим
     * @param center Центр круга
     * @param radius Радиус круга
     * @param whoIgnore Какое существо игнорировать в проверке
     * @return Список существ
     */
    public static List<Mortal> whoIntersectCircle(final Vector2 center, double radius, final Mortal whoIgnore) {
        List<Mortal> mortals = whoIntersectCircle(center, radius);
        mortals.remove(whoIgnore);
        return mortals;
    }

    /**
     * Проверить есть ли какие-то создания, которые пересекаются с заданным сегментом
     * @param segment Проверяемый сегмент
     * @return Список существ
     */
    public static List<Mortal> whoIntersectSegment(final Segment segment) {
        ArrayList<Mortal> mortals = new ArrayList<>(Mortal.LIB.size());
        for (Mortal mortal : LIB) {
            if (mortal.bb.intersect(segment)) {
                mortals.add(mortal);
            }
        }
        return mortals;
    }

    /**
     * Проверить есть ли какие-то создания, которые пересекаются с заданным сегментом
     * @param segment Проверяемый сегмент
     * @param whoIgnore Какое существо игнорировать в проверке
     * @return Список существ
     */
    public static List<Mortal> whoIntersectSegment(final Segment segment, final Mortal whoIgnore) {
        List<Mortal> mortals = whoIntersectSegment(segment);
        for (Iterator<Mortal> it = mortals.iterator(); it.hasNext(); ) {
            Mortal mortal = it.next();
            if (mortal.equals(whoIgnore)) {
                it.remove();
            }
        }

        return mortals;
    }

    public static Mortal[] getLib() {
        return LIB.toArray(new Mortal[LIB.size()]);
    }
}
