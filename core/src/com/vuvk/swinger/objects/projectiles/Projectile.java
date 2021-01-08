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
package com.vuvk.swinger.objects.projectiles;

import com.badlogic.gdx.Gdx;
import java.util.List;
import com.vuvk.swinger.math.BoundingBox;
import com.vuvk.swinger.math.Segment;
import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.Sprite;
import com.vuvk.swinger.objects.mortals.Mortal;
import com.vuvk.swinger.res.Map;
import com.vuvk.swinger.res.Texture;
import java.io.Serializable;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public abstract class Projectile extends Sprite implements Serializable {
    private Vector2 viewVector = new Vector2();
    private double radius;
    private double moveSpeed;
    private double damage;

    private BoundingBox bb;

    protected Projectile(Texture[][] frames, Vector3 pos/*, double radius, double direction, double moveSpeed, double animSpeed*/) {
        super(frames, pos);
    }

    public void setViewVector(Vector2 viewVector) {
        this.viewVector = viewVector;
    }

    @Override
    public void setPos(final Vector3 pos) {
        super.setPos(pos);
        if (bb == null) {
            bb = new BoundingBox(pos, radius);
        }

        bb.setLeft  (pos.x - radius);
        bb.setRight (pos.x + radius);
        bb.setTop   (pos.y - radius);
        bb.setBottom(pos.y + radius);
    }

    public void setMoveSpeed(double moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setDirection(double direction) {
        double rad = Math.toRadians(direction);
        viewVector = new Vector2(Math.cos(rad), Math.sin(rad));
        super.setDirection(direction);
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public Vector2 getViewVector() {
        return viewVector;
    }

    protected abstract void createDestroyEffect();

    @Override
    public void update() {
        super.update();

        Vector2 moveVector = viewVector.mul(moveSpeed * Gdx.graphics.getDeltaTime());
        Vector3 newPos = getPos().add(moveVector);
        int newPosX = (int)newPos.x;
        int newPosY = (int)newPos.y;
        boolean canMove = false;
        List<Mortal> targets = null;

        // если не столкнулся со стеной
        if (newPosX >= 0 && newPosX < Map.WIDTH &&
            newPosY >= 0 && newPosY < Map.HEIGHT &&
            !Map.SOLIDS[newPosX][newPosY]) {

            // если не столкнулся с сегментом
            Segment segment = Map.SEGMENTS[newPosX][newPosY];
            if (segment == null || (segment != null && segment.intersect(bb).isEmpty())) {
                // и не столкнулся с живым
                targets = Mortal.whoIntersectBox(bb);
                if (targets.isEmpty()) {
                    canMove = true;
                }
            }
        }

        if (canMove) {
            setPos(newPos);
        } else {
            // нанести урон всем жертвам
            if (targets != null && !targets.isEmpty()) {
                for (Mortal mortal : targets) {
                    mortal.applyDamage(damage);
                }
            }

            //FOR_DELETE_FROM_LIB.add(this);
            createDestroyEffect();
            destroy();
        }
    }
}
