/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.objects.creatures.enemy;

import com.vuvk.retard_sound_system.Sound;
import com.vuvk.retard_sound_system.SoundSystem;
import java.awt.Point;
import com.vuvk.swinger.objects.creatures.enemy.EnemyState;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.vuvk.swinger.graphic.Renderer;
import com.vuvk.swinger.math.Ray;
import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.res.Material;
import com.vuvk.swinger.objects.Sprite;
import com.vuvk.swinger.objects.creatures.Creature;
import com.vuvk.swinger.objects.creatures.Player;
import com.vuvk.swinger.res.Map;
/**
 *
 * @author tai-prg3
 */
public abstract class Enemy extends Creature {   
    /** список всех вражин */
    //public final static List<Enemy> LIB = new ArrayList<>();
    /** список вражин на удаление */
    //private final static List<Enemy> FOR_DELETE_FROM_LIB = new ArrayList<>();
        
    //private Texture[][] curAnim;
    /*private Texture[][] idle;
    private Texture[][] walk;
    private Texture[][] pain;
    private Texture[][] die;
    private Texture     dead;*/
    private Material idle;
    private Material atk;
    private Material walk;
    private Material pain;
    private Material die;
    private Material dead;
    
    private Vector2 viewVector = new Vector2(1, 0);
    private Sprite sprite;
    //private int curFrame;
    //private double frameDelay;
    private double stateDelay = 0.0;
    private double shootDelay = 0.0;
    private double prevStateDelay;
    private EnemyState state     = EnemyState.IDLE;    
    private EnemyState prevState = state;
    
    /* знает ли о существовании игрока */
    private boolean noticed = false;
    
    //private Vector2 pos;
    //private double direction;
    
    /*
    public Enemy(Vector3 pos) {
        this(pos, 0.0);
    }
    */
    
    protected Enemy(/*final Texture[][] idle,
                 final Texture[][] walk,
                 final Texture[][] pain,
                 final Texture[][] die,
                 final Texture     dead,*/
                 final Material idle,
                 final Material atk,
                 final Material walk,
                 final Material pain,
                 final Material die,
                 final Material dead,
                 final Vector3 pos, 
                 double direction,
                 double health,
                 double radius
    ) {
        super(pos, health, radius);
        sprite = new Sprite(idle, pos);
        //sprite.markForAdd();
        rotate(direction);
        setIdleAnimation(idle);
        setAttackAnimation(atk);
        setWalkAnimation(walk);
        setPainAnimation(pain);
        setDieAnimation(die);
        setDeadAnimation(dead);
        setState(EnemyState.IDLE);
        
        //LIB.add(this);
    }
    
    @Override
    public void finalize() {
        super.finalize();
        sprite.markForDelete();
        //LIB.remove(this);
    }
    
    /*
    public static void deleteAll() {
        LIB.clear();
    }
    */
        
    @Override
    public void setPos(final Vector3 pos) {
        super.setPos(pos);
        if (sprite != null) {
            sprite.setPos(pos);
        }
    }
    
    public void setIdleAnimation(final Material animation) {
        idle = animation;
    }
    
    public void setWalkAnimation(final Material animation) {
        walk = animation;
    }    
    
    public void setAttackAnimation(final Material animation) {
        atk = animation;
    }    
    
    public void setPainAnimation(final Material animation) {
        pain = animation;
    }    
    
    public void setDieAnimation(final Material animation) {
        die = animation;
    }   
    
    public void setDeadAnimation(final Material animation) {
        dead = animation;
    }   
    
    public void setState(final EnemyState state) {
        prevState = this.state;
        this.state = state;
        //curFrame = 0;
        stateDelay = 0.0;
        shootDelay = 0.0;
        //frameDelay = 0.0;
        switch (state) {
            case IDLE:
                sprite.duplicate(idle);
                //sprite.setAnimSpeed(0.0);
                //curAnim = idle;
                break;
                
            case WALK:
                sprite.duplicate(walk);
                //sprite.setAnimSpeed(1.0);
                sprite.play();
                //curAnim = walk;
                break;
                
            case ATTACK:
                //Sound.playRandom(getAttackSounds());
                sprite.duplicate(atk);
                sprite.playOnce();
                break;
                
            case PAIN:
                SoundSystem.playRandom(true, getPainSounds());
                sprite.setFrames(pain.getFrames()[(int)(Math.random() * 2)][0]);
                //sprite.setAnimSpeed(0.0);
                sprite.playOnce();
                //curAnim = pain;
                break;
                
            case DIE:
                SoundSystem.playRandom(getDieSounds());
                sprite.duplicate(die);
                //sprite.setAnimSpeed(1.0);
                sprite.playOnce();
                //curAnim = die;
                break;                
        }
    }
    
    protected abstract Sound[] getAlarmSounds();
    protected abstract Sound[] getAttackSounds();
    protected abstract Sound[] getPainSounds();
    protected abstract Sound[] getDieSounds();
    
    protected abstract double getViewDistance();
    protected abstract double getViewAngle();
    
    protected abstract double getMinAttackDistance();
    protected abstract double getMaxAttackDistance();
    protected abstract double getShootDelay();
    protected abstract double getDamage();
    protected abstract double getAccuracy();    
    protected abstract int getBulletsPerShoot();
    protected abstract void shoot();
    
    /*
    public Vector2 getPos() {
        return sprite.getPos();
    }
    */

    public EnemyState getState() {
        return state;
    }   
    
    public double getDirection() {
        return sprite.getDirection();
    }
    
    public Vector2 getViewVector() {
        return viewVector;
    }
    
    public void setDirection(double degree) { 
        double rad = Math.toRadians(degree);
        double sin = Math.sin(rad);
        double cos = Math.cos(rad);
        
        viewVector.x = cos;
        viewVector.y = sin;
        //viewVector = viewVector.normalize();
        
        sprite.setDirection(degree);
    }
    
    public void rotate(double degree) {
        if (degree == 0.0) {
            return;
        }
        
        double rad = Math.toRadians(degree);
        double sin = Math.sin(rad);
        double cos = Math.cos(rad);
        
        double oldDirX = viewVector.x;
        viewVector.x = viewVector.x * cos - viewVector.y * sin;
        viewVector.y = oldDirX      * sin + viewVector.y * cos;
        //viewVector = viewVector.normalize();
        
        sprite.rotate(degree);
    }
    
    /**
     * Получить урон
     * @param damage величина урона 
     */
    @Override
    public void applyDamage(double damage) {  
        super.applyDamage(damage);
        if (health > 0.0) { 
            if (state != EnemyState.PAIN) {
                setState(EnemyState.IDLE);
                setState(EnemyState.PAIN);
            }
            
            if (!noticed) {
                notice();
            }
        }
    }
    
    /**
     * Игрок в области видимости?
     * @return true, если да
     */
    protected boolean isPlayerInFov() {
        Vector3 plPos = Player.getInstance().getPos();
        
        // если игрок слишком далеко, то не видит его
        double distanceToPlayer = pos.distance(plPos);
        if (distanceToPlayer > getViewDistance()) {
            return false;
        }
        
        // проверяем находится ли игрок в области видимости
        Vector2 toPlayerVector = plPos.sub(pos).normalize();
        double anglePlayerInView = Math.toDegrees(Math.acos(viewVector.dot(toPlayerVector)));
        return (anglePlayerInView <= getViewAngle());
    }
    
    /**
     * Игрок на линии и не пересекается ничем?
     * @return true, если да
     */
    protected boolean isPlayerInLine() {
        Vector3 plPos = Player.getInstance().getPos();
        
        // если игрок слишком далеко, то не видит его
        double distanceToPlayer = pos.distance(plPos);
        if (distanceToPlayer > getViewDistance()) {
            return false;
        }
        
        // проверяем доходит ли луч до игрока или спотыкается о кого-то
        double rad = Math.atan2(plPos.y - pos.y, plPos.x - pos.x);  
        double sin = Math.sin(rad);
        double cos = Math.cos(rad);
        
        Ray viewRay = new Ray(pos, new Vector2(cos, sin), getViewDistance());
        Creature whoInView = viewRay.getCreature(this);        
        // никого не видно или видно не игрока
        if (whoInView == null || !whoInView.equals(Player.getInstance())) {
            return false;
        }
        
        // окей, видно игрока. Но не перекрывает ли вид стена?
        Vector2 colPoint = new Vector2();
        double distToSolid = viewRay.getSolid(new Point((int)pos.x, (int)pos.y), colPoint);
        return (distToSolid > distanceToPlayer);
    }
    
    private void notice() {
        noticed = true;
        SoundSystem.playRandom(getAlarmSounds());
        
        Vector3 plPos = Player.getInstance().getPos();
        double angleToPlayer = Math.toDegrees(Math.atan2(plPos.y - pos.y, plPos.x - pos.x));
        setDirection(angleToPlayer);
    }
    
    @Override
    public void update() {
        // умирать?
        if (health <= 0.0 && state != EnemyState.DIE) {
            setState(EnemyState.DIE);
        }
        
        // обновляем кадр
        /*if (state != EnemyState.PAIN) {
            if (frameDelay < 1.0) {
                frameDelay += Renderer.getDeltaTime() * 8;
            } else {
                frameDelay -= 1.0;
                ++curFrame;                
                
                if (curFrame >= curAnim.length) {
                    // если он умирал, то создать дохлый труп
                    if (state == EnemyState.DIE) {
                        new Sprite(dead, pos);
                        FOR_DELETE_FROM_LIB.add(this);
                        return;
                    } else {
                        curFrame = 0;                        
                    }
                }
 
                // передаем кадры углов поворота
                sprite.setFrames(curAnim[curFrame]);
            }
        }*/
        
        if (state == EnemyState.DIE) {
            if (!sprite.isAnimate()) {
                new Sprite(dead, pos)/*.markForAdd()*/;
                //FOR_DELETE_FROM_LIB.add(this);       
                //finalize();
                markForDelete();
                return;
            }
            return;
        }
        
        // игрок в зоне видимости?
        boolean playerInFov = isPlayerInFov();
        // Игрок на линии и не пересекается ничем?
        boolean playerInLine = isPlayerInLine();
        
        // обновляем состояние
        stateDelay += Renderer.getDeltaTime();
        
        // если ещё не видел, то проверить не заметил ли
        if (!noticed) {
            if (playerInFov && playerInLine) {
                notice();
            }
        }
        
        Vector3 plPos = Player.getInstance().getPos();
        double angleToPlayer = Math.toDegrees(Math.atan2(plPos.y - pos.y, plPos.x - pos.x)); 
        double distanceToPlayer = pos.distance(plPos);
        
        if (state == EnemyState.PAIN) {
            if (stateDelay >= 0.25) {
                setState(prevState);
            }
        } else {
            if (stateDelay < 3.0) {
                // стоит
                if (state == EnemyState.IDLE) {
                    // знает о существовании игрока и видит его
                    if (noticed && playerInLine) {
                        // поворачиваться на игрока
                        setDirection(angleToPlayer);
                        
                        // пришло время стрелять? Стрельни
                        if (shootDelay < getShootDelay()) {
                            shootDelay += Renderer.getDeltaTime();
                        } else {
                            shootDelay = 0.0;
                            prevStateDelay = stateDelay;
                            setState(EnemyState.ATTACK);
                        }
                    }
                // стреляет
                } else if (state == EnemyState.ATTACK) {
                    // уже выстрелил
                    if (!sprite.isAnimate()) {
                        SoundSystem.playRandom(getAttackSounds());
                        for (int i = 0; i < getBulletsPerShoot(); ++i) {
                            shoot();
                        }
                        setState(prevState);
                        stateDelay = prevStateDelay;
                    }
                }                
            } else {
                if (state == EnemyState.IDLE) {
                    // не знает о существовании игрока или не видит его
                    if (!noticed || !playerInLine) {
                        setDirection(Math.random() * 360.0);
                    } else {
                        setDirection(angleToPlayer);
                    }
                    setState(EnemyState.WALK);
                } else {
                    setState(EnemyState.IDLE);
                }
            }
        }
        /*
        if (stateDelay < 3.0) {
            stateDelay += Renderer.getDeltaTime();
        } else {
            stateDelay = 0.0;
            if (state == EnemyState.IDLE) {
                rotate(Math.random() * 360.0);
                setState(EnemyState.WALK);
            } else {
                setState(EnemyState.IDLE);
            }
        }*/
        
        if (state == EnemyState.WALK) {
            double moveSpeed = Renderer.getDeltaTime();
            if (moveSpeed > radius) {
                moveSpeed = radius;
            }
            
            // если знаешь о существовании игрока, то идти на игрока (если видишь его)
            if (noticed) {
                if (playerInLine) {
                    setDirection(angleToPlayer);
                }
            }
            
            Vector3 moveVector = new Vector3(viewVector.mul(moveSpeed));             
            boolean canMove = false;   
            // позиция проверки
            Vector3 check = new Vector3(pos.x + Math.signum(moveVector.x) * radius,
                                        pos.y + Math.signum(moveVector.y) * radius,
                                        0);
            int checkX = (int)check.x;
            int checkY = (int)check.y;         
            
            // не утыкается в стены
            if (checkX >= 0 && checkX < Map.WIDTH   &&
                checkY >= 0 && checkY < Map.HEIGHT  &&
                !Map.SOLIDS[checkX][(int)pos.y]     &&
                !Map.SOLIDS[(int)pos.x][checkY]) {
                // не утыкается в ячейки с сегментами
                if (Map.SEGMENTS[checkX][(int)pos.y] == null &&
                    Map.SEGMENTS[(int)pos.x][checkY] == null) {
                    // и не пересекается с другими существами
                    if (whoIntersectBox(bb.move(moveVector), this).isEmpty()) {
                        canMove = true;
                    }
                }
            }
            
            // расстояние до игрока меньше расстояния атаки
            if (playerInLine && distanceToPlayer <= getMinAttackDistance()) {
                canMove = false;
            }
            
            if (canMove) {
                setPos(pos.add(moveVector));
            } else {
                setState(EnemyState.IDLE);
            }
        }
    }
    
    /*
    public static void updateAll() {        
        if (FOR_DELETE_FROM_LIB.size() > 0) {
            for (Iterator<Enemy> it = FOR_DELETE_FROM_LIB.iterator(); it.hasNext(); ) {
                it.next().finalize();
            }
            FOR_DELETE_FROM_LIB.clear();
        }
        
        for (Enemy enemy : LIB) {
            enemy.update();
        }
    }*/
}
