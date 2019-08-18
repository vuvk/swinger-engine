/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.graphic.weapon_in_hand;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import swingraycaster.audio.Sound;
import swingraycaster.audio.SoundBuffer;
import swingraycaster.graphic.Renderer;
import swingraycaster.math.Vector2;
import swingraycaster.objects.creatures.Player;
import swingraycaster.res.Image;

/**
 *
 * @author tai-prg3
 */
public abstract class WeaponInHand {    
    private static final Logger LOG = Logger.getLogger(WeaponInHand.class.getName());
    
    
    private Vector2 startPos;
    private Vector2 pos;
    private double angle = 0;
    private double moveSpeed;
    private double moveDistance;
    
    private Image[] frames;
    private int curFrameNum = 0;
    private int frameForShoot = 0;
    
    private int bulletPerShoot = 1;
    private double accuracy;
    private double distance;
    private double damage;
    
    private boolean animate = false;
    private double animDelay = 0.0;
    private double animSpeed;
    
    private boolean climbing;
    
    private boolean canShoot = true;
    private double shootDelay = 0;
    private double _shootDelay = 0;
    
    private Sound soundShoot;
    
    protected WeaponInHand() {                
        int rW = Renderer.WIDTH;
        int rH = Renderer.HEIGHT;
        int min = Math.min(rW, rH) >> 1;   
        moveSpeed = min * (2.0 / 3.0);
        moveDistance = min / 12.0;
        
        startPos = new Vector2(Renderer.HALF_WIDTH, Renderer.HEIGHT - 1 + moveDistance * 0.5);
        pos = new Vector2(startPos);  
    }
    
    public double getDistance() {
        return distance;
    }

    public Image getFrame() {
        return frames[curFrameNum];
    }

    public int getFrameNum() {
        return curFrameNum;
    }

    public boolean isAnimate() {
        return animate;
    }

    public double getDamage() {
        return damage;
    }

    public Vector2 getPos() {
        return pos;
    }
    
    public boolean isCanShoot() {
        return canShoot;
    }
    
    protected void setDamage(double damage) {
        this.damage = damage;
    }

    protected void setAnimSpeed(double animSpeed) {
        this.animSpeed = animSpeed;
    }

    protected void setBulletPerShoot(int bulletPerShoot) {
        this.bulletPerShoot = bulletPerShoot;
    }

    protected void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }
    
    protected void setDistance(double distance) {
        this.distance = distance;
    }

    protected void setFrameForShoot(int frameForShoot) {
        this.frameForShoot = frameForShoot;
    }

    protected void setShootDelay(double shootDelay) {
        this.shootDelay = shootDelay;
    }
    
    protected void setFrames(final Image[] images) {
        this.frames = images;
    }

    public void setAnimate(boolean animate) {
        this.animate = animate;
        this.canShoot = !animate;
    }
    
    protected void setSoundShoot(SoundBuffer audioBuffer) {
        soundShoot = new Sound(audioBuffer);
    }
    
    protected void playSoundShoot() {
        if (soundShoot != null) {
            soundShoot.play();
        }
    }
    
    public void pullUp() {
        //pos.set(startPos.x + moveDistance * 4, startPos.y + moveDistance * 4);
        angle = 0;
        climbing = false;
    }
    
    protected abstract void shoot(double direction);
    
    public void update() {
        Player player = Player.getInstance();
        
        // если игрок движется, то двигать ствол по восьмерке
        if (player.isMove()) {
            climbing = false;
            
            // определяем скорость движения ствола в зависимости от направления игрока
            double speed = moveSpeed;
            if (player.isMoveF()) {
                speed = moveSpeed;
            } else if (player.isMoveB()) {
                speed = moveSpeed * 0.66666;
            } else if (player.isMoveL() || player.isMoveR()) {
                speed = moveSpeed * 0.5;
            }

            // двигаем ствол по восьмерке
            angle += speed * Renderer.getDeltaTime();
            while (angle >= 360.0) {
                angle -= 360.0;
            }
            pos.set(startPos.x + moveDistance * Math.cos(Math.toRadians(angle)),
                    startPos.y + moveDistance * Math.sin(Math.toRadians(2 * angle)));
        // игрок не движется
        } else {
            if (!climbing) {
                double speed = moveSpeed * Renderer.getDeltaTime();
                if (pos.distance(startPos) > speed) {
                    angle = 0.0;
                    Vector2 moveVector = startPos.sub(pos).normalize().mul(speed);
                    pos = pos.add(moveVector);
                } else {
                    //angle = 0.0;
                    //pos = new Vector2(startPos);
                    climbing = true;
                }
            }
        }
        
        // игрок стоит и оружие можно качать
        if (climbing) {
            double speed = moveSpeed * Renderer.getDeltaTime() * 0.25;
            angle += speed;
            while (angle >= 360.0) {
                angle -= 360.0;
            }
            pos.set(startPos.x/* + moveDistance * Math.cos(Math.toRadians(angle))*/,
                    startPos.y + moveDistance * Math.cos(Math.toRadians(angle)));
        }
        
        // может стрелять только если задержка выдержана
        if (!canShoot) {
            if (_shootDelay < shootDelay) {
                _shootDelay += Renderer.getDeltaTime();
            } else {
                _shootDelay = 0.0;
                canShoot = true;
            }
        }
        
        if (animate) {                    
            if (animDelay < 1.0) {
                animDelay += animSpeed * Renderer.getDeltaTime();
            } else {
                ++curFrameNum;
                if (curFrameNum >= frames.length) {
                    curFrameNum = 0;
                    animDelay = 0.0;
                    animate = false;
                } else {                    
                    animDelay -= 1.0;
                }
                
                if (curFrameNum == frameForShoot) {
                    playSoundShoot();
                    
                    // стреляем столько раз, сколько пуль
                    for (int i = 0; i < bulletPerShoot; ++i) {
                        double direction = player.getDirection();
                                     
                        // делаем смещение направления с учетом кучности
                        if (accuracy != 0.0) {
                            direction += -accuracy * 0.5 + Math.random() * accuracy;
                        }
                        
                        shoot(direction);
                    }
                }
            }
        }        
    }
}
