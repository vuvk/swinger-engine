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
package com.vuvk.swinger.objects.mortals.enemy;

//import com.vuvk.retard_sound_system.Sound;
//import com.vuvk.retard_sound_system.SoundSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.vuvk.swinger.audio.SoundSystem;
import com.vuvk.swinger.math.Ray;
import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.LightSource;
import com.vuvk.swinger.objects.Sprite;
import com.vuvk.swinger.objects.mortals.Mortal;
import com.vuvk.swinger.objects.mortals.Player;
import com.vuvk.swinger.res.Map;
import com.vuvk.swinger.res.Material;
import java.awt.Color;
import java.io.Serializable;
import java.util.logging.Logger;
/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public abstract class Enemy extends Breakable implements Serializable {

    private static final Logger LOG = Logger.getLogger(Enemy.class.getName());

    /** список всех вражин */
    //public final static List<Enemy> LIB = new ArrayList<>();

    private Material atk;
    private Material walk;

    protected String[] alarmSounds;
    protected String[] attackSounds;

//    private Vector2 viewVector = new Vector2(1, 0);
//    private Sprite sprite;
    //private int curFrame;
    //private double frameDelay;
//    private double stateDelay = 0.0;
    private double shootDelay = 0.0;
    private double prevStateDelay;
//    private EnemyState state     = EnemyState.IDLE;
//    private EnemyState prevState = state;

    /* знает ли о существовании игрока */
    private boolean noticed = false;
    /* создана вспышка от выстрела */
    private boolean flashCreated = false;


    protected double viewDistance;
    protected double viewAngle;

    protected double minAttackDistance;
    protected double maxAttackDistance;
    protected double damage;
    protected double accuracy;
    protected int    bulletsPerShoot;
    protected double shootSpeed;

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
        super(idle, pain, die, dead, pos, direction, health, radius);
        //sprite = new Sprite(idle, pos);
        //sprite.markForAdd();
        setAttackAnimation(atk);
        setWalkAnimation(walk);
    }
    
    public void setWalkAnimation(final Material animation) {
        walk = animation;
    }

    public void setAttackAnimation(final Material animation) {
        atk = animation;
    }

    @Override
    public void setState(final EnemyState state) {
        flashCreated = false; // не было создано вспышки от выстрела
        /*prevState = this.state;
        this.state = state;
        //curFrame = 0;
        stateDelay = 0.0;*/
        shootDelay = 0.0;
        //frameDelay = 0.0;
        super.setState(state);
        switch (state) {
            /*case IDLE:
                sprite.duplicate(idle);
                //sprite.setAnimSpeed(0.0);
                //curAnim = idle;
                break;*/

            case WALK:
                sprite.duplicate(walk);
                //sprite.setAnimSpeed(1.0);
                sprite.play();
                //curAnim = walk;
                break;

            case ATTACK:
                sprite.duplicate(atk);
                sprite.playOnce();
                break;

           /* case PAIN:
                SoundSystem.playOnceRandom(getPainSounds());
                sprite.setFrames(pain.getFrames()[(int)(Math.random() * 2)][0]);
                sprite.playOnce();
                break;

            case DIE:
                SoundSystem.playOnceRandom(getDieSounds());
                sprite.duplicate(die);
                sprite.playOnce();
                break;  */
        }
    }

    protected void setAlarmSounds(String[] painSounds) {
        this.alarmSounds = alarmSounds;
    }

    protected void setAlarmSounds(FileHandle[] alarmSounds) {
        this.alarmSounds = new String[alarmSounds.length];
        for (int i = 0; i < alarmSounds.length; ++i) {
            this.alarmSounds[i] = alarmSounds[i].file().getPath();
        }
    }

    protected void setAttackSounds(String[] attackSounds) {
        this.attackSounds = attackSounds;
    }

    protected void setAttackSounds(FileHandle[] attackSounds) {
        this.attackSounds = new String[attackSounds.length];
        for (int i = 0; i < attackSounds.length; ++i) {
            this.attackSounds[i] = attackSounds[i].file().getPath();
        }
    }

    protected void setViewDistance(double viewDistance) {
        this.viewDistance = viewDistance;
    }

    protected void setViewAngle(double viewAngle) {
        this.viewAngle = viewAngle;
    }

    protected void setMinAttackDistance(double minAttackDistance) {
        this.minAttackDistance = minAttackDistance;
    }

    protected void setMaxAttackDistance(double maxAttackDistance) {
        this.maxAttackDistance = maxAttackDistance;
    }

    protected void setShootSpeed(double shootSpeed) {
        this.shootSpeed = shootSpeed;
    }

    protected void setDamage(double damage) {
        this.damage = damage;
    }

    protected void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    protected void setBulletsPerShoot(int bulletsPerShoot) {
        this.bulletsPerShoot = bulletsPerShoot;
    }

    protected FileHandle[] getAlarmSounds() {
        FileHandle[] sounds = new FileHandle[alarmSounds.length];
        for (int i = 0; i < alarmSounds.length; ++i) {
            sounds[i] = Gdx.files.internal(alarmSounds[i]);
        }
        return sounds;
    }

    protected FileHandle[] getAttackSounds() {
        FileHandle[] sounds = new FileHandle[attackSounds.length];
        for (int i = 0; i < attackSounds.length; ++i) {
            sounds[i] = Gdx.files.internal(attackSounds[i]);
        }
        return sounds;
    }

    protected double getViewDistance() {
        return viewDistance;
    }

    protected double getViewAngle() {
        return viewAngle;
    }

    protected double getMinAttackDistance() {
        return minAttackDistance;
    }

    protected double getMaxAttackDistance() {
        return maxAttackDistance;
    }

    protected double getShootSpeed() {
        return shootSpeed;
    }

    protected double getDamage() {
        return damage;
    }

    protected double getAccuracy() {
        return accuracy;
    }

    protected int getBulletsPerShoot() {
        return bulletsPerShoot;
    }


    protected abstract void shoot();

    /*public EnemyState getState() {
        return state;
    }*/

    /**
     * Получить урон
     * @param damage величина урона
     */
    @Override
    public void applyDamage(double damage) {
        super.applyDamage(damage);
        if (health > 0.0) {
            /*if (state != EnemyState.PAIN) {
                setState(EnemyState.IDLE);
                setState(EnemyState.PAIN);
            }*/

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
        Mortal whoInView = viewRay.getMortal(this);
        // никого не видно или видно не игрока
        if (whoInView == null || !whoInView.equals(Player.getInstance())) {
            return false;
        }

        // окей, видно игрока. Но не перекрывает ли вид стена?
        Vector2 colPoint = new Vector2();
        double distToSolid = viewRay.getSolid(new Vector2(pos.x, pos.y), colPoint);
        return (distToSolid > distanceToPlayer);
    }

    private void notice() {
        noticed = true;
        SoundSystem.playOnceRandom(getAlarmSounds());

        Vector3 plPos = Player.getInstance().getPos();
        double angleToPlayer = Math.toDegrees(Math.atan2(plPos.y - pos.y, plPos.x - pos.x));
        setDirection(angleToPlayer);
    }

    @Override
    public void update() {
        // прям грязный хак, но надо вызвать update от GameObject
        super.updateDefferedDelete();

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
                destroy();
                new Sprite(dead, pos);
            }
            return;
        }

        // игрок в зоне видимости?
        boolean playerInFov = isPlayerInFov();
        // Игрок на линии и не пересекается ничем?
        boolean playerInLine = isPlayerInLine();

        // обновляем состояние
        stateDelay += Gdx.graphics.getDeltaTime();

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
                        if (shootDelay < getShootSpeed()) {
                            shootDelay += Gdx.graphics.getDeltaTime();
                        } else {
                            shootDelay = 0.0;
                            prevStateDelay = stateDelay;
                            setState(EnemyState.ATTACK);
                        }
                    }
                // стреляет
                } else if (state == EnemyState.ATTACK) {
                    if (sprite.isAnimate()) {
                        // на последнем кадре
                        if (sprite.getFrameNum() == sprite.getFramesCount() - 1) {
                            // если ещё не было вспышки от выстрела
                            if (!flashCreated) {
                                new LightSource(Color.WHITE, 2.0, pos).destroy(0.15);
                                flashCreated = true;
                            }
                        }
                    // уже выстрелил
                    } else {
                        SoundSystem.playOnceRandom(getAttackSounds());
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
            double moveSpeed = Gdx.graphics.getDeltaTime();
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
}
