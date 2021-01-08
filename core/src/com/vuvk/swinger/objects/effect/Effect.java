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
package com.vuvk.swinger.objects.effect;

//import com.vuvk.retard_sound_system.Sound;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.Sprite;
import com.vuvk.swinger.res.Texture;
import java.io.Serializable;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public abstract class Effect extends Sprite implements Serializable {
//    private Sound soundEffect;
    /*
    private final static List<Effect> LIB = new ArrayList<>();
    private final static List<Effect> FOR_DELETE_FROM_LIB = new ArrayList<>();
    */
    /*private Texture[] frames;
    private Sprite sprite;
    private int curFrameNum = 0;
    private double animDelay = 0;
    private double animSpeed;
    private boolean playOnce;*/

    protected Effect(Texture[] frames, double animSpeed, boolean playOnce, Vector3 pos) {
        super(frames, animSpeed, playOnce, pos);
    //    LIB.add(this);
    }

    protected Effect(Texture[][] frames, double animSpeed, boolean playOnce, Vector3 pos) {
        super(frames, animSpeed, playOnce, pos);
    //    LIB.add(this);
    }
    /*
    protected void setSoundEffect(File audioFile) {
        soundEffect = new Sound(audioFile, true);
    }

    protected void setSoundEffect(String audioFilePath) {
        setSoundEffect(new File(audioFilePath));
    }

    public void setSoundEffect(Sound soundEffect) {
        this.soundEffect = soundEffect;
    }
    */
/*
    @Override
    public void finalize() {
        LIB.remove(this);
    }
    */
    /*
    public Vector3 getPos() {
        return sprite.getPos();
    }

    public void setPos(Vector3 pos) {
        sprite.setPos(pos);
    }
    */
    @Override
    public void update() {
        super.update();

        if (!isAnimate() && isPlayOnce()) {
            destroy();
        }

        /*if (animDelay < 1.0) {
            animDelay += animSpeed * Renderer.getDeltaTime();
        } else {
            animDelay = 0.0;
            ++curFrameNum;

            if (curFrameNum >= frames.length) {
                if (playOnce) {
                    FOR_DELETE_FROM_LIB.add(this);
                    return;
                } else {
                    curFrameNum = 0;
                }
            }

            sprite.setTexture(frames[curFrameNum]);
        }*/
    }
    /*
    public static void updateAll() {
        for (Effect effect : LIB) {
            effect.update();
        }

        if (FOR_DELETE_FROM_LIB.size() > 0) {
            for (Iterator<Effect> it = FOR_DELETE_FROM_LIB.iterator(); it.hasNext(); ) {
                it.next().finalize();
            }
            FOR_DELETE_FROM_LIB.clear();
        }
    }*/
}
