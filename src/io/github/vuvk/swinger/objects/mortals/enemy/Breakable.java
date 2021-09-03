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
package io.github.vuvk.swinger.objects.mortals.enemy;

import io.github.vuvk.audiosystem.AudioSystem;
import io.github.vuvk.audiosystem.Sound;
import io.github.vuvk.swinger.Engine;
import io.github.vuvk.swinger.math.Vector2;
import io.github.vuvk.swinger.math.Vector3;
import io.github.vuvk.swinger.objects.Sprite;
import io.github.vuvk.swinger.objects.mortals.Mortal;
import io.github.vuvk.swinger.res.Material;
import java.io.Serializable;
/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class Breakable extends Mortal implements Serializable {
    protected Material idle;
    protected Material pain;
    protected Material die;
    protected Material dead;

    protected Sound[] painSounds;
    protected Sound[] dieSounds;

    protected Vector2 viewVector = new Vector2(1, 0);
    protected Sprite sprite;

    protected double stateDelay = 0.0;
    protected EnemyState state     = EnemyState.IDLE;
    protected EnemyState prevState = state;

    public Breakable(
        final Material idle,
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

        rotate(direction);
        setIdleAnimation(idle);
        setPainAnimation(pain);
        setDieAnimation(die);
        setDeadAnimation(dead);
        setState(EnemyState.IDLE);
    }

    @Override
    public void finalize() throws Throwable {
        for (Sound snd : painSounds) { snd.dispose(); }
        for (Sound snd : dieSounds ) { snd.dispose(); }
        destroy();
        super.finalize();
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
        stateDelay = 0.0;

        switch (state) {
            case IDLE:
                sprite.duplicate(idle);
                break;

            case PAIN:
                AudioSystem.playRandom(getPainSounds());
                sprite.setFrames(pain.getFrames()[(int)(Math.random() * 2)][0]);
                sprite.playOnce();
                break;

            case DIE:
                AudioSystem.playRandom(getDieSounds());
                sprite.duplicate(die);
                sprite.playOnce();
                break;

            default:
                break;
        }
    }

    public void setPainSounds(Sound[] painSounds) {
        this.painSounds = painSounds;
    }
/*
    public void setPainSounds(String[] painSounds) {
        this.painSounds = new Sound[painSounds.length];
        for (int i = 0; i < painSounds.length; ++i) {
            this.painSounds[i] = new Sound(painSounds[i], true);
        }
    }
*/
    public void setDieSounds(Sound[] dieSounds) {
        this.dieSounds = dieSounds;
    }
/*
    public void setDieSounds(String[] dieSounds) {
        this.dieSounds = new Sound[dieSounds.length];
        for (int i = 0; i < dieSounds.length; ++i) {
            this.dieSounds[i] = new Sound(dieSounds[i], true);
        }
    }
*/
    protected Sound[] getPainSounds() {
        return painSounds;
    }

    protected Sound[] getDieSounds() {
        return dieSounds;
    }

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
        }
    }

    @Override
    public void destroy() {
        sprite.destroy();
        super.destroy();
    }

    @Override
    public void update() {
        super.update();

        float x = (float) getPos().x,
              y = (float) getPos().y,
              z = (float) getPos().z;

        for (Sound snd : painSounds) { snd.setRelative(false);  snd.setPosition(x, z, y); }
        for (Sound snd : dieSounds ) { snd.setRelative(false);  snd.setPosition(x, z, y); }

        // умирать?
        if (health <= 0.0 && state != EnemyState.DIE) {
            setState(EnemyState.DIE);
        }

        if (state == EnemyState.DIE) {
            if (!sprite.isAnimate()) {
                destroy();
                new Sprite(dead, pos);
            }
            return;
        }

        // обновляем состояние
        stateDelay += Engine.getDeltaTime();

        if (state == EnemyState.PAIN) {
            if (stateDelay >= 0.25) {
                setState(prevState);
            }
        } else {
            if (stateDelay < 3.0) {
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
