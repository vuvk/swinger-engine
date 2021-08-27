/**
    Copyright (C) 2019-2021 Anton "Vuvk" Shcherbatykh <vuvk69@gmail.com>
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
package com.vuvk.swinger.objects;

import com.vuvk.audiosystem.AudioSystem;
import com.vuvk.audiosystem.Sound;
import com.vuvk.swinger.Engine;
import com.vuvk.swinger.audio.SoundBank;
import com.vuvk.swinger.graphic.TexturedSegment;
import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.objects.mortals.Mortal;
import com.vuvk.swinger.res.Map;
import com.vuvk.swinger.res.Material;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class Door extends TexturedSegment implements Serializable {
    private Sound openSound;
    private Sound closeSound;

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

    //private Player PLAYER = Player.getInstance();
    public final static List<Door> LIB = new CopyOnWriteArrayList<>();

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

        closeSound = (Sound) AudioSystem
            .newSound(SoundBank.SOUND_BUFFER_DOOR_CLOSE)
            .setPosition((float) center.x, 0f, (float) center.y)
            .setRelative(false);
        openSound = (Sound) AudioSystem
            .newSound(SoundBank.SOUND_BUFFER_DOOR_OPEN )
            .setPosition((float) center.x, 0f, (float) center.y)
            .setRelative(false);

        LIB.add(this);
    }

    @Override
    public void finalize() throws Throwable {
        openSound.dispose();
        closeSound.dispose();
        LIB.remove(this);
        super.finalize();
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

    public static void deleteAll() {
        LIB.clear();
    }

    public void update() {
        if (isOpen()) {
            final double deltaTime = Engine.getDeltaTime();
            final double moveSpeed = speed * deltaTime;

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
                    delay += deltaTime;
                } else {
                    // проверить можно ли закрывать дверь
                    boolean canClose = true;
                    if (!Map.SOLIDS[(int)openPos.x][(int)openPos.y]) {
                        for (Mortal creature : Mortal.LIB) {
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
        synchronized(LIB) {
            LIB.forEach(Door::update);
        }
    }

    public static Door[] getLib() {
        return LIB.toArray(new Door[LIB.size()]);
    }
}
