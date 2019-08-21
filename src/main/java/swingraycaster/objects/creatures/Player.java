/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.objects.creatures;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import swingraycaster.graphic.Renderer;
import swingraycaster.graphic.weapon_in_hand.KnifeInHand;
import swingraycaster.graphic.weapon_in_hand.PistolInHand;
import swingraycaster.graphic.weapon_in_hand.RifleInHand;
import swingraycaster.graphic.weapon_in_hand.RocketLauncherInHand;
import swingraycaster.graphic.weapon_in_hand.ShotgunInHand;
import swingraycaster.graphic.weapon_in_hand.WeaponInHand;
import swingraycaster.input.MouseManager;
import swingraycaster.math.Segment;
import swingraycaster.res.Map;
import swingraycaster.math.Vector2;
import swingraycaster.math.Vector3;
import swingraycaster.audio.Sound;
import swingraycaster.audio.SoundBank;
import swingraycaster.objects.Door;
import swingraycaster.objects.Key;
import swingraycaster.objects.weapon.Weapon;

/**
 *
 * @author tai-prg3
 */
public final class Player extends Creature {    
    private static Player instance = null;  
    
    private double direction = 180.0;
    private Vector2 view = new Vector2(-1, 0);   // see to 0
    /** the 2d raycaster version of camera plane */
    private Vector2 plane = new Vector2(0, 0.66);  // fov 66
    
    private WeaponInHand[] weaponsInHand = new WeaponInHand[5];
    private int curWeaponInHandNum = 0;
    
    private boolean moveL = false,
                    moveR = false,
                    moveF = false,
                    moveB = false;
    private boolean rotL = false,
                    rotR = false;
    
    private final static double HEALTH = 100.0;
    private final static double RADIUS = 0.25;
    public final static double  MOVE_SPEED = 5.0;
    public final static double  KEY_ROT_SPEED  = 3.0;
    public final static double  MOUSE_ROT_SPEED  = 15.0;
    public final static double  FOV = Math.toRadians(66.0);
    public final static double  FOV_2 = FOV / 2.0;
    
    private Sound[] soundsNeadKey = { new Sound(SoundBank.SOUND_BUFFER_NEAD_KEY1),
                                      new Sound(SoundBank.SOUND_BUFFER_NEAD_KEY2) };
    
    /** ключи, которые есть в наличии у игрока */
    private final Set<Integer> keys = new HashSet<>();
    
    public boolean isRot() {
        return (rotL || rotR);
    }

    public boolean isRotL() {
        return rotL;
    }

    public void setRotL(final boolean rotL) {
        this.rotL = rotL;
    }

    public boolean isRotR() {
        return rotR;
    }

    public void setRotR(final boolean rotR) {
        this.rotR = rotR;
    }
    
    public boolean isMove() {
        return (moveF || moveB || moveL || moveR);
    }

    public boolean isMoveL() {
        return moveL;
    }

    public void setMoveL(final boolean moveL) {
        this.moveL = moveL;
    }

    public boolean isMoveR() {
        return moveR;
    }

    public void setMoveR(final boolean moveR) {
        this.moveR = moveR;
    }

    public boolean isMoveF() {
        return moveF;
    }

    public void setMoveF(final boolean moveF) {
        this.moveF = moveF;
    }

    public boolean isMoveB() {
        return moveB;
    }

    public void setMoveB(final boolean moveB) {
        this.moveB = moveB;
    }
    
    public static Player getInstance() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }

    
    public void addKey(Key key) {
        keys.add(key.getKeyNum());
    }
    
    public boolean hasKey(Key key) {
        return keys.contains(key.getKeyNum());
    }
    
    /*
    public Vector3 getPos() {
        return pos;
    }

    public void setPos(final Vector3 pos) {
        this.pos = pos;
    }
    */

    public Vector2 getView() {
        return view;
    }

    public double getDirection() {
        return direction;
    } 
    /*
    public void setDirection(Vector2 dir) {
        this.direction = dir;
    }
    */
    
    public Vector2 getPlane() {
        return plane;
    }
    
    public WeaponInHand getWeaponInHand() {
        return weaponsInHand[curWeaponInHandNum];
    }
    
    public void setWeaponInHand(int number) {
        if (number >= 0 && number < weaponsInHand.length) {
            if (weaponsInHand[number] != null) {
                curWeaponInHandNum = number;   
                getWeaponInHand().pullUp();
            }
        }
    }

    private void setDirection(double degree) {
        this.direction = degree;
        while (direction < 0.0) {
            direction += 360.0;
        }
        while (direction >= 360.0) {
            direction -= 360.0;
        }/*
        
        double rad = Math.toRadians(degree);
        double sin = Math.sin(rad);
        double cos = Math.cos(rad);
        view.set(cos, sin);
        
        //both camera direction and camera plane must be rotated        
        double oldDirX = view.x;
        view.x = view.x   * cos - view.y * sin;
        view.y = oldDirX * sin + view.y * cos;
        
        double oldPlaneX = plane.x;
        plane.x = plane.x   * cos - plane.y * sin;
        plane.y = oldPlaneX * sin + plane.y * cos;*/
    }
    
    /*
    public void setPlane(Vector2 plane) {
        this.plane = plane;
    }*/
    
    public void rotate(double rad) {        
        double sin = Math.sin(rad);
        double cos = Math.cos(rad);
        
        //both camera direction and camera plane must be rotated
        double oldDirX = view.x;
        view.x = view.x  * cos - view.y * sin;
        view.y = oldDirX * sin + view.y * cos;
        
        double oldPlaneX = plane.x;
        plane.x = plane.x   * cos - plane.y * sin;
        plane.y = oldPlaneX * sin + plane.y * cos;
        
        setDirection(direction + Math.toDegrees(rad));
    }
    
    public void openDoor() {
        // ищем дверь перед игроком
        Vector2 checkPos = pos.add(view);
        int x = (int)checkPos.x,
            y = (int)checkPos.y;
        if (Map.DOORS_MAP[x][y] >= 0) {
            // чекаем пересечение взгляда с дверью
            Door door = (Door)Map.SEGMENTS[x][y];
            if (door != null && !door.isOpen()) {
                Segment viewLine = new Segment(pos, pos.add(view.mul(1.5)));
                if (viewLine.intersect(door) != null) {
                    // если ключ не нужен, то открыть
                    if (!door.isNeadKey()) {
                        door.open();
                    // ключ нужен
                    } else {
                        // ключ есть?
                        if (keys.contains(door.getKeyForOpen())) {
                            door.open();
                        } else {
                            // сказать фразу, если ещё не говорит
                            Sound.playRandom(true, soundsNeadKey);
                        }
                    }
                }
            }
        }
    }    
    
    public void createWeaponsInHand() {        
        weaponsInHand[0] = KnifeInHand.getInstance();
        /*weaponsInHand[1] = PistolInHand.getInstance();
        weaponsInHand[2] = ShotgunInHand.getInstance();
        weaponsInHand[3] = RifleInHand.getInstance();
        weaponsInHand[4] = RocketLauncherInHand.getInstance();*/
    }
    
    public void addWeaponInHand(Weapon weapon) {
        int weaponNum = weapon.getWeaponNum();
        boolean firstGrab = (weaponsInHand[weaponNum] == null);
        if (firstGrab) {
            weaponsInHand[weaponNum] = weapon.getInstance();
            setWeaponInHand(weaponNum);
        }
    }
    
    public void update() {          
        // обновляем оружие в руках
        if (getWeaponInHand() != null) {
            getWeaponInHand().update();
        }
        
        // rotation
        if (rotR || rotL) {
            double rotSpeed  = Renderer.getDeltaTime() * Player.KEY_ROT_SPEED; //the constant value is in radians/second
            
            // rotate to the right
            if (rotR) {
                rotate(-rotSpeed);
            }

            // rotate to the left
            if (rotL) {
                rotate( rotSpeed);
            }
        }
        
        // moving         
        if (moveF || moveB || moveR || moveL) {
            //speed modifiers
            double moveSpeed = Renderer.getDeltaTime() * Player.MOVE_SPEED; //the constant value is in squares/second  
            if (moveSpeed > radius) {
                moveSpeed = radius;
            }
            
            Vector2 moveVector = new Vector2();
            
            // move forward if no wall in front of you
            if (moveF) {           
                moveVector.set( view.x,
                                view.y);
            }

            // move backwards if no wall behind you
            if (moveB) {        
                moveVector.set(-view.x,
                               -view.y);
            }

            // Strafe right
            if (moveR) {
                moveVector.set( view.y,
                               -view.x);
            }

            // strafe left
            if (moveL) {
                moveVector.set(-view.y,
                                view.x);
            }
            
            moveVector = moveVector.mul(moveSpeed);
            
            Vector3 newPos = new Vector3(pos);

            // позиция проверки
            Vector3 check = new Vector3(pos.x + Math.signum(moveVector.x) * radius,
                                        pos.y + Math.signum(moveVector.y) * radius,
                                        0);
            int checkX = (int)check.x;
            int checkY = (int)check.y;
            
            boolean canMove = false;
            
            // по горизонтали
            if (checkX >= 0 && checkX < Map.WIDTH) {    
                // стены нет            
                if (!Map.SOLIDS[checkX][(int)pos.y]) {
                    boolean segmentCollision = false;
                    
                    // а есть ли сегмент?
                    Segment segment = Map.SEGMENTS[checkX][(int)pos.y];
                    if (segment != null) {
                        if (segment.intersect(new Segment(pos, new Vector2(check.x, pos.y))) != null) {
                            segmentCollision = true;
                        }
                    }
                    
                    // нет столкновения с сегментом
                    // а нет ли моба?
                    if (!segmentCollision) {
                        if (whoInPos(new Vector2(check.x, pos.y), this).isEmpty()) {                   
                            newPos.x += moveVector.x;
                            canMove = true;
                        }
                    }
                }
            }

            // по вертикали
            if (checkY >= 0 && checkY < Map.HEIGHT) {
                // стены нет            
                if (!Map.SOLIDS[(int)pos.x][checkY]) { 
                    boolean segmentCollision = false;
                    
                    // а есть ли сегмент?
                    Segment segment = Map.SEGMENTS[(int)pos.x][checkY];
                    if (segment != null) {
                        if (segment.intersect(new Segment(pos, new Vector2(pos.x, check.y))) != null) {
                            segmentCollision = true;
                        }
                    }
                    
                    // нет столкновения с сегментом
                    // а нет ли моба?
                    if (!segmentCollision) {
                        if (whoInPos(new Vector2(pos.x, check.y), this).isEmpty()) {                   
                            newPos.y += moveVector.y;
                            canMove = true;
                        }
                    }
                }
            }
            
            if (canMove) {
                setPos(newPos);
            }
        }
        
        if (MouseManager.isLeftClick()     && 
            //!getWeaponInHand().isAnimate() && 
            getWeaponInHand().isCanShoot()
           ) {
            getWeaponInHand().setAnimate(true);
        }
    }
    
    private Player() {
        super(new Vector3(20.5, 4.75, 0.0), HEALTH, RADIUS);
        //super(new Vector3(19.76, 20.55, 0.0), HEALTH, RADIUS);
        //super(new Vector3(20.5, 8.79, 0.0), HEALTH, RADIUS);
        //super(new Vector3(15.46, 5.3, 0.0), HEALTH, RADIUS);
        rotate(Math.toRadians(-90));        
    }
}
