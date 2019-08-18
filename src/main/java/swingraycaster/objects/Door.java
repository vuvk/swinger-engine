/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.objects;

import swingraycaster.objects.creatures.Creature;
import swingraycaster.objects.creatures.Player;
import java.util.ArrayList;
import java.util.List;
import swingraycaster.graphic.Renderer;
import swingraycaster.graphic.TexturedSegment;
import swingraycaster.math.Vector2;
import swingraycaster.res.Map;
import swingraycaster.res.Material;
import swingraycaster.audio.Sound;
import swingraycaster.audio.SoundBank;

/**
 *
 * @author tai-prg3
 */
public class Door extends TexturedSegment {  
    private final Sound openSound  = new Sound(SoundBank.SOUND_BUFFER_DOOR_OPEN );
    private final Sound closeSound = new Sound(SoundBank.SOUND_BUFFER_DOOR_CLOSE);
    
    //private int side;
    /** открытая позиция */
    private Vector2 openPos;
    /** закрытая позиция */
    private Vector2 closePos;
    /** центр двери */
    private Vector2 center;
    /** вектор движения на открытую позицию */
    private Vector2 openDir;
    /** вектор движения на закрытую позицию */
    private Vector2 closeDir;
    /** скорость движения двери */
    private double speed = 3.0;
    /** простой в открытом состоянии */
    private double delay = 0.0;
    /** закрыта? */
    private boolean closed = true;
    /** открывается? */
    private boolean open = false;
    /** нужен ли ключ для открывания */
    private boolean neadKey;
    private int keyForOpen;
    
    private Player PLAYER = Player.getInstance();    
    private final static List<Door> LIB = new ArrayList<>();
    
    public Door(final Vector2 a, final Vector2 b, final Material material) {
        this(a, b, material, -1);
    }
        
    public Door(final Vector2 a, final Vector2 b, final Material material, int keyForOpen) {
        super(a, b, material);
        //this.side = side;
        
        openPos  = a;
        closePos = b;
                
        openDir  = a.sub(b).normalize();
        closeDir = b.sub(a).normalize();
        
        center   = a.add(openDir.mul(a.sub(b).length() * 0.5));
        
        this.keyForOpen = keyForOpen;
        this.neadKey = (keyForOpen >= 0);
        
        LIB.add(this);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean isOpen() {
        return open;
    }

    public boolean isNeadKey() {
        return neadKey;
    }

    public int getKeyForOpen() {
        return keyForOpen;
    }
        
    public void open() {
        open = true;
        openSound.play();
    }
    
    @Override
    public void finalize() {
        LIB.remove(this);
    }
    
    public void update() {        
        if (isOpen()) {
            final double moveSpeed = speed * Renderer.getDeltaTime();
            
            // закрытое состояние?
            if (closed) {
                /*if (!neadKey || 
                    (neadKey && keyForOpen != -1 && PLAYER.keys.contains(keyForOpen)))*/ {                
                    if (a.distance(closePos) > moveSpeed) {
                        Vector2 moveVector = closeDir.mul(moveSpeed);
                        a = a.add(moveVector);
                        b = b.add(moveVector);
                    } else {
                        a = closePos;
                        closed = false;
                        delay = 0.0;
                        Map.SOLIDS[(int)openPos.x][(int)openPos.y] = false;
                    }
                }
            } else {
                if (delay < 3.0) {
                    delay += Renderer.getDeltaTime();   
                } else {
                    // проверить можно ли закрывать дверь
                    boolean canClose = true;
                    if (!Map.SOLIDS[(int)openPos.x][(int)openPos.y]) {
                        for (Creature creature : Creature.LIB) {
                            if (center.distance(creature.getPos()) <= 1.5) {
                                canClose = false;
                                break;
                            }
                        }
                    }

                    // закрываться, только если пройти сквозь ячейку нельзя
                    if (canClose) {
                        Map.SOLIDS[(int)openPos.x][(int)openPos.y] = true;
                        if (a.distance(openPos) > moveSpeed) {
                            Vector2 moveVector = openDir.mul(moveSpeed);
                            a = a.add(moveVector);
                            b = b.add(moveVector);
                        } else {
                            a = openPos;
                            b = closePos;
                            closed = true;
                            open   = false;
                            delay  = 0.0;
        
                            closeSound.play();
                        }
                    }
                }
            }            
        }
    }
    
    public static void updateAll() {
        for (Door door : LIB) {
            door.update();
        }
    }
}
