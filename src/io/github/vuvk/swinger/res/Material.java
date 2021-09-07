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
package io.github.vuvk.swinger.res;

import io.github.vuvk.swinger.Engine;
import io.github.vuvk.swinger.objects.Object3D;
import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class Material extends Object3D implements Serializable {
    transient public  final static Set<Material> LIB = new CopyOnWriteArraySet<>();

    // для анимации массив двумерный, где x - кадры, а y - текстуры для углов поворота (1 или 8 штук)
    private Image[][] frames;
    private double animSpeed;
    private double delay = 0;
    private int frameNum = 0;
    private boolean animate = true;
    private boolean playOnce;
//    protected double brightness = 0;

    public Material(Image frame) {
        this(new Image[]{frame});
    }

    public Material(Image[] frames) {
        this(frames, 0);
    }

    public Material(Image[][] frames) {
        this(frames, 0);
    }

    public Material(Image frame, double animSpeed) {
        this(new Image[]{frame}, animSpeed);
    }

    public Material(Image[] frames, double animSpeed) {
        this(frames, animSpeed, false);
    }

    public Material(Image[][] frames, double animSpeed) {
        this(frames, animSpeed, false);
    }

    public Material(Image frame, double animSpeed, boolean playOnce) {
        this(new Image[]{frame}, animSpeed, playOnce);
    }

    public Material(Image[] frames, double animSpeed, boolean playOnce) {
        setFrames(frames);
        setAnimSpeed(animSpeed);
        this.playOnce = playOnce;
        synchronized (LIB) {
            LIB.add(this);
        }
    }

    public Material(Image[][] frames, double animSpeed, boolean playOnce) {
        setFrames(frames);
        setAnimSpeed(animSpeed);
        this.playOnce = playOnce;
        synchronized (LIB) {
            LIB.add(this);
        }
    }

    @Override
    public void finalize() {
        destroy();
    }

    public static Material[] getLib() {
        return LIB.toArray(new Material[LIB.size()]);
    }

    public static void deleteAll() {
        //synchronized (LIB) {
            LIB.clear();
        //}
    }

    /**
     * Копировать параметры из подобного объекта
     * @param another
     */
    public void duplicate(Material another) {
        this.frames    = another.frames;
        this.animSpeed = another.animSpeed;
        this.frameNum  = another.frameNum;
        this.animate   = another.animate;
        this.playOnce  = another.playOnce;
//        this.brightness = another.brightness;
    }
/*
    public double getBrightness() {
        return brightness;
    }
*/

    public int getFrameNum() {
        return frameNum;
    }

    public int getFramesCount() {
        return frames.length;
    }

    public int getSideFramesCount() {
        return getSideFrames().length;
    }

    public Image[][] getFrames() {
        return frames;
    }

    public Image[] getSideFrames() {
        return frames[frameNum];
    }

    public double getAnimSpeed() {
        return animSpeed;
    }

    public boolean isAnimate() {
        return animate;
    }

    public boolean isPlayOnce() {
        return playOnce;
    }

    public boolean hasSides() {
        return (getSideFrames().length > 1);
    }
/*
    public void setBrightness(double brightness) {
        this.brightness = brightness;
    }
*/
    public void setFrames(Image[][] frames) {
        this.frames = frames;
        this.frameNum = 0;
    }

    public void setFrames(Image[] frames) {
        Image[][] temp = new Image[frames.length][1];
        for (int i = 0; i < frames.length; ++i) {
            temp[i][0] = frames[i];
        }
        setFrames(temp);
    }

    public void setFrames(Image frame) {
        setFrames(new Image[][] {new Image[] {frame}});
    }

    public void setAnimSpeed(double animSpeed) {
        this.animSpeed = animSpeed;
    }

    public void play() {
        playOnce = false;
        animate = true;
        frameNum = 0;
    }

    public void playOnce() {
        playOnce = true;
        animate = true;
        frameNum = 0;
    }

    @Override
    public void destroy() {
        super.destroy();
        LIB.remove(this);
    }

    @Override
    public void update() {
        super.update();

        if (animate) {
            if (animSpeed > 0.0 && frames.length > 1) {
                if (delay < 1.0) {
                    delay += animSpeed * Engine.getDeltaTime();
                } else {
                    delay -= 1.0;
                    ++frameNum;
                    if (frameNum >= frames.length) {
                        if (!playOnce) {
                            frameNum = 0;
                        // одноразовая анимация
                        } else {
                            frameNum = frames.length - 1;
                            animate = false;
                        }
                    }
                }
            } else {
                animate = false;
            }
        }
    }

    public static void updateAll() {
        synchronized (LIB) {
            LIB.forEach(Material::update);
        }
    }
}
