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
package io.github.vuvk.swinger.objects.mortals;

import io.github.vuvk.swinger.math.BoundingBox;
import io.github.vuvk.swinger.math.Segment;
import io.github.vuvk.swinger.math.Vector2;
import io.github.vuvk.swinger.math.Vector3;
import io.github.vuvk.swinger.objects.Object3D;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public abstract class Mortal extends Object3D implements Serializable {
    transient public final static Set<Mortal> LIB = new CopyOnWriteArraySet<>();

    protected double health;
    protected final double maxHealth;
    protected double radius;
    /** является ли объект живым - живой испускает кровь, неживой  пыль */
    protected boolean live = false;
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
    public void finalize() throws Throwable {
        destroy();
        super.finalize();
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
    public static Set<Mortal> whoInPos(final Vector2 pos) {
        Set<Mortal> mortals = new HashSet<>();
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
    public static Set<Mortal> whoInPos(final Vector2 pos, final Mortal whoIgnore) {
        Set<Mortal> mortals = whoInPos(pos);
        mortals.remove(whoIgnore);
        return mortals;
    }

    /**
     * Проверить есть ли какое-то создание, которое пересекается с заданным квадратом
     * @param box Проверяемый квадрат
     * @return Список существ
     */
    public static Set<Mortal> whoIntersectBox(final BoundingBox box) {
        Set<Mortal> mortals = new HashSet<>();
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
    public static Set<Mortal> whoIntersectBox(final BoundingBox box, final Mortal whoIgnore) {
        Set<Mortal> mortals = whoIntersectBox(box);
        mortals.remove(whoIgnore);
        return mortals;
    }

    /**
     * Проверить есть ли какое-то создание, которое пересекается с текущим
     * @param center Центр круга
     * @param radius Радиус круга
     * @return Список существ
     */
    public static Set<Mortal> whoIntersectCircle(final Vector2 center, double radius) {
        Set<Mortal> mortals = new HashSet<>();
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
    public static Set<Mortal> whoIntersectCircle(final Vector2 center, double radius, final Mortal whoIgnore) {
        Set<Mortal> mortals = whoIntersectCircle(center, radius);
        mortals.remove(whoIgnore);
        return mortals;
    }

    /**
     * Проверить есть ли какие-то создания, которые пересекаются с заданным сегментом
     * @param segment Проверяемый сегмент
     * @return Список существ
     */
    public static Set<Mortal> whoIntersectSegment(final Segment segment) {
        Set<Mortal> mortals = new HashSet<>();
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
    public static Set<Mortal> whoIntersectSegment(final Segment segment, final Mortal whoIgnore) {
        Set<Mortal> mortals = whoIntersectSegment(segment);
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
