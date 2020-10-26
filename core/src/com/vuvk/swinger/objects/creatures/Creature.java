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
package com.vuvk.swinger.objects.creatures;

import java.util.ArrayList;
import java.util.List;
import com.vuvk.swinger.math.BoundingBox;
import com.vuvk.swinger.math.Segment;
import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.Object3D;
import java.io.Serializable;
import java.util.Iterator;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public abstract class Creature extends Object3D implements Serializable {
    transient public  final static List<Creature> LIB = new ArrayList<>();
    transient private final static List<Creature> FOR_DELETE_FROM_LIB = new ArrayList<>();
    
    protected double health;
    protected final double maxHealth;
    protected double radius;
    protected boolean live = false; // является ли объект живым - живой испускает кровь, неживой  пыль
    protected final BoundingBox bb = new BoundingBox();
    
    public Creature(final Vector3 pos, double health, double radius) {
        setHealth(health);
        setRadius(radius);
        setPos(pos);
        maxHealth = health;
    //  update();
        LIB.add(this);
    }
    
    @Override
    public void finalize() {
        LIB.remove(this);
    }
    
    abstract public void update();
    
    /**
     * Пометить объект на удаление
     */
    public void markForDelete() {
        FOR_DELETE_FROM_LIB.add(this);
    }
    
    public static void updateAll() {
        if (!FOR_DELETE_FROM_LIB.isEmpty()) {
            for (Creature creature : FOR_DELETE_FROM_LIB) {
                creature.finalize();
            }
            FOR_DELETE_FROM_LIB.clear();
        }
        
        for (Creature creature : LIB) {
            creature.update();
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
        
        bb.setLeft  (pos.x - radius);
        bb.setRight (pos.x + radius);
        bb.setTop   (pos.y - radius);
        bb.setBottom(pos.y + radius);
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
     * Находится ли точка внутри существа
     * @param point точка для проверки
     * @return true, если точка внутри
     */
    public boolean hasPoint(final Vector2 point) {
        return (
            point.x >= bb.getLeft()  && 
            point.x <= bb.getRight() && 
            point.y >= bb.getTop()   && 
            point.y <= bb.getBottom()
        );
    }
    
    /**
     * Проверка пересечения с отрезком
     * @param segment Отрезок для проверки
     * @return true, если пересекает
     */
    public boolean intersect(final Segment segment) {
        return (
            // пересекается с верхом?
            (segment.intersect(new Segment(bb.getLeft(),  bb.getTop(),    bb.getRight(), bb.getTop()))    != null) ||
            // пересекается с низом?
            (segment.intersect(new Segment(bb.getLeft(),  bb.getBottom(), bb.getRight(), bb.getBottom())) != null) ||
            // пересекается с левым?
            (segment.intersect(new Segment(bb.getLeft(),  bb.getTop(),    bb.getLeft(),  bb.getBottom())) != null) ||
            // пересекается с правым?
            (segment.intersect(new Segment(bb.getRight(), bb.getTop(),    bb.getRight(), bb.getBottom())) != null)
        );
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
        
    /*
    public static void updateAll() {
        for (Creature creature : LIB) {
            creature.update();
        }
    }
    */   
    
    
    /**
     * Проверить есть ли какое-то создание в точке (учитывая радиус существ) 
     * @param pos Проверяемая позиция
     * @return Список существ
     */
    public static List<Creature> whoInPos(final Vector2 pos) {
        ArrayList<Creature> creatures = new ArrayList<>(Creature.LIB.size());
        for (Creature creature : LIB) {
            if (creature.hasPoint(pos)) {
                creatures.add(creature);
            }
        }        
        return creatures;
    }
    
    /**
     * Проверить есть ли какое-то создание в точке (учитывая радиус существ) 
     * @param pos Проверяемая позиция
     * @param whoIgnore Какое существо игнорировать в проверке
     * @return Список существ
     */
    public static List<Creature> whoInPos(final Vector2 pos, final Creature whoIgnore) {
        List<Creature> creatures = whoInPos(pos);
        creatures.remove(whoIgnore);        
        return creatures;
    }
    
    /**
     * Проверить есть ли какое-то создание, которое пересекается с заданным квадратом
     * @param box Проверяемый квадрат
     * @return Список существ
     */
    public static List<Creature> whoIntersectBox(final BoundingBox box) {
        ArrayList<Creature> creatures = new ArrayList<>(Creature.LIB.size());
        for (Creature creature : LIB) {
            if (creature.intersect(box)) {
                creatures.add(creature);
            }
        }        
        return creatures;
    }
    
    /**
     * Проверить есть ли какое-то создание, которое пересекается с заданным квадратом
     * @param box Проверяемый квадрат
     * @param whoIgnore Какое существо игнорировать в проверке
     * @return Список существ
     */
    public static List<Creature> whoIntersectBox(final BoundingBox box, final Creature whoIgnore) {
        List<Creature> creatures = whoIntersectBox(box);
        creatures.remove(whoIgnore);        
        return creatures;
    }
    
    /**
     * Проверить есть ли какое-то создание, которое пересекается с текущим
     * @param center Центр круга
     * @param radius Радиус круга
     * @return Список существ
     */
    public static List<Creature> whoIntersectCircle(final Vector2 center, double radius) {
        ArrayList<Creature> creatures = new ArrayList<>(Creature.LIB.size());
        for (Creature creature : LIB) {
            if (creature.intersect(center, radius)) {
                creatures.add(creature);
            }
        }        
        return creatures;
    }
    
    /**
     * Проверить есть ли какое-то создание, которое пересекается с текущим
     * @param center Центр круга
     * @param radius Радиус круга
     * @param whoIgnore Какое существо игнорировать в проверке
     * @return Список существ
     */
    public static List<Creature> whoIntersectCircle(final Vector2 center, double radius, final Creature whoIgnore) {
        List<Creature> creatures = whoIntersectCircle(center, radius);
        creatures.remove(whoIgnore);        
        return creatures;
    }
    
    /**
     * Проверить есть ли какие-то создания, которые пересекаются с заданным сегментом
     * @param segment Проверяемый сегмент
     * @return Список существ
     */
    public static List<Creature> whoIntersectSegment(final Segment segment) {
        ArrayList<Creature> creatures = new ArrayList<>(Creature.LIB.size());
        for (Creature creature : LIB) {
            if (creature.intersect(segment)) {
                creatures.add(creature);
            }            
        }
        return creatures;
    }
    
    /**
     * Проверить есть ли какие-то создания, которые пересекаются с заданным сегментом
     * @param segment Проверяемый сегмент
     * @param whoIgnore Какое существо игнорировать в проверке
     * @return Список существ
     */
    public static List<Creature> whoIntersectSegment(final Segment segment, final Creature whoIgnore) {
        List<Creature> creatures = whoIntersectSegment(segment);        
        for (Iterator<Creature> it = creatures.iterator(); it.hasNext(); ) {
            Creature creature = it.next();
            if (creature.equals(whoIgnore)) {
                it.remove();
            }
        }
        
        return creatures;
    }
    
    public static Creature[] getLib() {
        Creature[] creatures = new Creature[LIB.size()];
        int i = 0;
        for (Iterator<Creature> it = LIB.iterator(); it.hasNext(); ) {
            creatures[i] = it.next();
            ++i;
        }
        return creatures;
    }
}
