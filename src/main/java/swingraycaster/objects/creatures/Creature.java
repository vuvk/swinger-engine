/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.objects.creatures;

import java.util.ArrayList;
import java.util.List;
import swingraycaster.math.BoundingBox;
import swingraycaster.math.Segment;
import swingraycaster.math.Vector2;
import swingraycaster.math.Vector3;

/**
 *
 * @author tai-prg3
 */
public abstract class Creature {
    public final static List<Creature> LIB = new ArrayList<>();
    
    protected double health;
    protected Vector3 pos;
    protected double radius;
    protected final BoundingBox bb = new BoundingBox();
    
    public Creature(final Vector3 pos, double health, double radius) {
        setHealth(health);
        setRadius(radius);
        setPos(pos);
    //  update();
        LIB.add(this);
    }
    
    @Override
    public void finalize() {
        LIB.remove(this);
    }

    public double getHealth() {
        return health;
    }

    public Vector3 getPos() {
        return pos;
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
        this.pos = pos;
        
        bb.setLeft  (pos.x - radius);
        bb.setRight (pos.x + radius);
        bb.setTop   (pos.y - radius);
        bb.setBottom(pos.y + radius);
    }   
    
    private void setRadius(double radius) {
        this.radius = radius;
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
        double dX = pos.x - center.x;
        double dY = pos.y - center.y;            
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
        creatures.remove(whoIgnore);        
        return creatures;
    }
}
