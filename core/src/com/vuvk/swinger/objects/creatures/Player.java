/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.objects.creatures;

//import com.vuvk.retard_sound_system.Sound;
//import com.vuvk.retard_sound_system.SoundSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.vuvk.swinger.Config;
import java.util.HashSet;
import java.util.Set;
import com.vuvk.swinger.graphic.Renderer;
import com.vuvk.swinger.graphic.weapon_in_hand.KnifeInHand;
import com.vuvk.swinger.graphic.weapon_in_hand.WeaponInHand;
import com.vuvk.swinger.input.InputManager;
import com.vuvk.swinger.math.Segment;
import com.vuvk.swinger.res.Map;
import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.audio.SoundBank;
import com.vuvk.swinger.audio.SoundSystem;
import com.vuvk.swinger.objects.Camera;
import com.vuvk.swinger.graphic.weapon_in_hand.AmmoPack;
import com.vuvk.swinger.objects.weapon.AmmoType;
import com.vuvk.swinger.objects.Door;
import com.vuvk.swinger.objects.Key;
import com.vuvk.swinger.objects.weapon.Weapon;
import java.io.Serializable;

/**
 *
 * @author tai-prg3
 */
public final class Player extends Creature implements Serializable {    
    private static Player instance = null;  
    
    /*
    private double direction = 180.0;
    private Vector2 view = new Vector2(-1, 0);   // see to 0
    private Vector2 plane = new Vector2(0, 0.66);  // fov 66
    */
    final private Camera camera = new Camera();
    final private WeaponInHand[] weaponsInHand = new WeaponInHand[5];
    private int curWeaponInHandNum = 0;
    
    /** ключи, которые есть в наличии у игрока */
    private final Set<Integer> keys = new HashSet<>();
    
    private boolean moveL = false,
                    moveR = false,
                    moveF = false,
                    moveB = false;
    private boolean rotL = false,
                    rotR = false;
    private boolean shooting = false;
    
    private final static double HEALTH = 100.0;
    private final static double RADIUS = 0.25;
    public  final static double MOVE_SPEED = 5.0;
    public  final static double KEY_ROT_SPEED  = 3.0;
    public  final static double MOUSE_ROT_SPEED  = 15.0;
    public  final static double FOV = Math.toRadians(66.0);
    public  final static double FOV_2 = FOV / 2.0;
    transient private final static Music[] SOUNDS_NEAD_KEY = { 
        SoundSystem.loadSound(SoundBank.FILE_NEED_KEY1),
        SoundSystem.loadSound(SoundBank.FILE_NEED_KEY2)
    };
    
    transient private Music[] soundsNeadKey;
    
    private void init() {
        soundsNeadKey = SOUNDS_NEAD_KEY;
    }
    
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

    public boolean isShooting() {
        return shooting;
    }
    
    public Camera getCamera() {
        return camera;
    }

    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }
    
    public void addKey(Key key) {
        keys.add(key.getKeyNum());
    }
    
    public boolean hasKey(Key key) {
        return keys.contains(key.getKeyNum());
    }
    
    public void shoot() {
        if (getWeaponInHand().isCanShoot()) {
            getWeaponInHand().setAnimate(true);
        }
    }
    
    /*
    public Vector3 getPos() {
        return pos;
    }

    public void setPos(final Vector3 pos) {
        this.pos = pos;
    }
    */
    /*
    public void setDirection(Vector2 dir) {
        this.direction = dir;
    }
    */
    
/*
    public Vector2 getView() {
        return view;
    }

    public double getDirection() {
        return direction;
    } 
    
    public Vector2 getPlane() {
        return plane;
    }
*/    
    public void createWeaponsInHand() {        
        weaponsInHand[0] = KnifeInHand.getInstance();
        /*weaponsInHand[1] = PistolInHand.getInstance();
        weaponsInHand[2] = ShotgunInHand.getInstance();
        weaponsInHand[3] = RifleInHand.getInstance();
        weaponsInHand[4] = RocketLauncherInHand.getInstance();*/
    }
    
    public void initWeaponsInHand() {
        for (int i = 0; i < weaponsInHand.length; ++i) {
            WeaponInHand weapon = weaponsInHand[i];
            if (weapon != null) {
                weapon.init();
            }
        }
    }
    
    public void addWeaponInHand(Weapon weapon) {
        int weaponNum = weapon.getWeaponNum();
        boolean firstGrab = (weaponsInHand[weaponNum] == null);
        if (firstGrab) {
            weaponsInHand[weaponNum] = weapon.getInstance();
            setWeaponInHand(weaponNum);
        }
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
/*
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
//    }
    
    /*
    public void setPlane(Vector2 plane) {
        this.plane = plane;
    }*/
    
/*    public void rotate(double rad) {        
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
 */   
    @Override
    public void setPos(final Vector3 pos) {
        super.setPos(pos);
        if (camera != null) {
            camera.setPos(pos);
        }
    }
    
    public void openDoor() {
        Vector2 view = camera.getView();
        // ищем дверь перед игроком
        Vector2 checkPos = pos.add(view);
        int x = (int)checkPos.x,
            y = (int)checkPos.y;
        if (Map.DOORS[x][y] >= 0) {
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
                            SoundSystem.playRandom(true, soundsNeadKey);
                        }
                    }
                }
            }
        }
    }    
    
    public void prevWeapon() {
        WeaponInHand weapon = null;
        while (weapon == null) {
            if (--curWeaponInHandNum < 0) {
                curWeaponInHandNum = weaponsInHand.length - 1;
            }
            weapon = weaponsInHand[curWeaponInHandNum];
        }
    }
        
    public void nextWeapon() {
        WeaponInHand weapon = null;
        while (weapon == null) {
            if (++curWeaponInHandNum >= weaponsInHand.length) {
                curWeaponInHandNum = 0;
            }
            weapon = weaponsInHand[curWeaponInHandNum];
        }
    }
    
    @Override
    public void update() {      
        final double deltaTime = Gdx.graphics.getDeltaTime();
        
        // обновляем оружие в руках
        if (getWeaponInHand() != null) {
            getWeaponInHand().update();
        }
        
        // rotation
        if (rotR || rotL) {
            double rotSpeed  = deltaTime * Player.KEY_ROT_SPEED; //the constant value is in radians/second
            
            // rotate to the right
            if (rotR) {
                camera.rotate(-rotSpeed);
            }

            // rotate to the left
            if (rotL) {
                camera.rotate( rotSpeed);
            }
        }
        
        // moving         
        if (moveF || moveB || moveR || moveL) {
            //speed modifiers
            double moveSpeed = deltaTime * Player.MOVE_SPEED; //the constant value is in squares/second  
            if (moveSpeed > radius) {
                moveSpeed = radius;
            }
            
            Vector2 moveVector = new Vector2();
            Vector2 view = camera.getView();
            
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
        
        if (!Config.buildForMobiles) {
            shooting = InputManager.isLeftClick();
        }
            
        if (InputManager.isScrollDown()) {
            prevWeapon();
        }

        if (InputManager.isScrollUp()) {
            nextWeapon();
        }
        
        if (shooting) {
            AmmoType ammoType = getWeaponInHand().getAmmoType();
            int curAmmo = AmmoPack.PACK.get(ammoType);
            if ((ammoType == AmmoType.NOTHING) || 
                (ammoType != AmmoType.NOTHING && curAmmo > 0)
               ) {
                shoot();
            } else {
                shooting = false;
            }
        }
    }
    
    public static Player getInstance() {
        if (instance == null) {
            createInstance(new Vector3(20.5, 4.75, 0.0));
        }
        return instance;
    }
    
    public static void createInstance(Vector3 pos) {
        if (instance != null) {
            //deleteInstance();
            instance.finalize();
        }
        
        instance = new Player(pos);
    }
    
    public static void setInstance(Player instance) {
        if (instance != null) {
            Player.instance = instance;
            Player.instance.init();
        }
    }
    
    public static void deleteInstance() {
        if (instance != null) {
            instance.markForDelete();
            instance = null;
        }
    }
    
    private Player(Vector3 pos) {
        super(pos, HEALTH, RADIUS);
        createWeaponsInHand();
        camera.setPos(pos);
        camera.rotate(Math.toRadians(-90));
        init();
    }
}
