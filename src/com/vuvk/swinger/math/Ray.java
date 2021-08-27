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
package com.vuvk.swinger.math;

import com.vuvk.swinger.objects.mortals.Mortal;
import com.vuvk.swinger.objects.mortals.Player;
import com.vuvk.swinger.res.Map;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public final class Ray implements Serializable {
    private final Vector2 start;
    private final Vector2 dir;
    private final Segment segment;
    private final double length;

    public Ray(final Vector2 start, final Vector2 dir, double length) {
        this.start = start;
        this.dir = dir;
        this.length = length;
        this.segment = new Segment(start, start.add(dir.mul(length)));
    }

    /**
     * Получить дистанцию до точки на карте с твердым объектом, в которую попал луч
     * @param mapPoint В точку mapPoint записываются координаты на карте
     * @param collisionPoint В точку collisionPoint записываются мировые координаты точки столкновения
     * @return дистанция до твердого объекта.
     */
    public double getSolid(Vector2 mapPoint, Vector2 collisionPoint) {
        mapPoint.set((int)start.x, (int)start.y);

        int side; //was a NS or a EW wall hit?
        // what direction to step in x or y-direction (either +1 or -1)
        int stepX, stepY;

        Vector2 ray = new Vector2(dir);
        Vector2 invRay = new Vector2(1.0 / dir.x, 1.0 / dir.y);
        Vector2 deltaDist = new Vector2(Math.abs(invRay.x),
                                        Math.abs(invRay.y));

        // calculate step and initial sideDist
        Vector2 sideDist = new Vector2();
        if (ray.x < 0.0) {
            stepX = -1;
            sideDist.x = (start.x - mapPoint.x) * deltaDist.x;
        } else {
            stepX = 1;
            sideDist.x = (mapPoint.x + 1.0 - start.x) * deltaDist.x;
        }

        if (ray.y < 0.0) {
            stepY = -1;
            sideDist.y = (start.y - mapPoint.y) * deltaDist.y;
        } else {
            stepY = 1;
            sideDist.y = (mapPoint.y + 1.0 - start.y) * deltaDist.y;
        }

        while (true) {
            // jump to next map square, OR in x-direction, OR in y-direction
            if (sideDist.x < sideDist.y) {
                sideDist.x += deltaDist.x;
                mapPoint.x += stepX;
                side = 0;
            } else {
                sideDist.y += deltaDist.y;
                mapPoint.y += stepY;
                side = 1;
            }

            // луч долетел до края карты или столкнулся с чем-то твердым
            if ((mapPoint.x < 0 || mapPoint.x >= Map.WIDTH ||
                 mapPoint.y < 0 || mapPoint.y >= Map.HEIGHT) ||
                (Map.SOLIDS[(int)mapPoint.x][(int)mapPoint.y])/* ||
                Map.SEGMENTS[mapPoint.x][mapPoint.y] != null*/) {
                break;
            }

            // луч столкнулся с сегментом
            Segment mapSegment = Map.SEGMENTS[(int)mapPoint.x][(int)mapPoint.y];
            if (mapSegment != null) {
                Vector2 point = getSegment().intersect(mapSegment);
                if (point != null) {
                    collisionPoint.set(point.x, point.y);

                    Vector2 diff = point.sub(start);
                    Vector2 plane = Player.getInstance().getCamera().getPlane();
                    double distance = (-plane.y * diff.x + plane.x * diff.y) / (plane.x * dir.y - dir.x * plane.y);

                    return distance;
                }
            }
        }

        double distance = (side == 0) ? (mapPoint.x - start.x + ((1 - stepX) >> 1)) * invRay.x :
                                        (mapPoint.y - start.y + ((1 - stepY) >> 1)) * invRay.y;

        // определяем точку мира столкновения
        double wallX = (side == 0) ? start.y + distance * ray.y :
                                     start.x + distance * ray.x;
        wallX -= (int)wallX;

        if (side == 0 && ray.x > 0) {
            collisionPoint.x = mapPoint.x;
            collisionPoint.y = mapPoint.y + wallX;
        } else if (side == 0 && ray.x < 0) {
            collisionPoint.x = mapPoint.x + 1.0;
            collisionPoint.y = mapPoint.y + wallX;
        } else if (side == 1 && ray.y > 0) {
            collisionPoint.x = mapPoint.x + wallX;
            collisionPoint.y = mapPoint.y;
        } else {
            collisionPoint.x = mapPoint.x + wallX;
            collisionPoint.y = mapPoint.y + 1.0;
        }
        //collisionPoint = dir.mul(distance);

        return distance;
    }

    /**
     * Получить первую цель, попавшую в луч
     * @param whoIgnore Какое существо игнорировать в проверке
     * @return Создание, если есть попадание. Или null, если ни в кого не попал
     */
    public Mortal getMortal(Mortal whoIgnore) {
        List<Mortal> mortals = Mortal.whoIntersectSegment(segment, whoIgnore);
        Mortal target = null;

        if (mortals.size() > 0) {
            double targetDistance = Double.MAX_VALUE;

            // из всех созданий ищем ближайшее
            for (Mortal mortal : mortals) {
                double distance = start.distance(mortal.getPos());
                if (target == null || targetDistance > distance) {
                    target = mortal;
                    targetDistance = distance;
                }
            }
        }

        return target;
    }

    public Segment getSegment() {
        return segment;
    }

    public double getLength() {
        return length;
    }
}
