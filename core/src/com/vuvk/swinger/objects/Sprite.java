/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.objects;

import com.badlogic.gdx.utils.Array;
import com.vuvk.swinger.res.Material;
import com.vuvk.swinger.objects.creatures.Player;
import com.vuvk.swinger.math.Vector3;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.objects.creatures.Creature;
import com.vuvk.swinger.res.Image;
import com.vuvk.swinger.res.Texture;
import java.io.Serializable;

/**
 *
 * @author tai-prg3
 */
public class Sprite extends Material implements Comparable<Sprite>, Serializable {
    //private final static Player PLAYER = Player.getInstance();
    transient public final static List<Sprite> LIB = new ArrayList<>();
    //private final static List<Sprite> FOR_ADD_TO_LIB = new ArrayList<>();
    transient private final static List<Sprite> FOR_DELETE_FROM_LIB = new ArrayList<>();
    //public final static double DEPTH_FACTOR = Texture.HEIGHT * ((int)(Math.ceil((double)Window.HEIGHT / Texture.HEIGHT)) >> Config.quality);
    //public final static double DEPTH_FACTOR = Window.HEIGHT >> Config.quality;
    
    /** позиция в 3D-мире*/
    private Vector3 pos;
    /** текущая текстура, которая будет нарисована */
    private Texture curTex;
    /** набор текстур (8 углов поворота) */
    //private Texture[] textures;
    /** глубина - поправочное значение для */
    //private double depth;
    //private boolean isAnimate;
    //private boolean hasSides;
    protected Vector2 scale = new Vector2(1,1);
    
    private double direction = 0.0;
    private double distance = 0;  
    
    public Sprite(final Image frame) {
        this(frame, new Vector3());
    }
    
    public Sprite(final Image[] frames) {
        this(frames, new Vector3());
    }
    
    public Sprite(final Image[][] frames) {
        this(frames, new Vector3());
    }
    
    public Sprite(final Material animation) {
        this(animation, new Vector3());
    }
    
    /*
    public Sprite(final Vector3 pos, final Texture ... texture) {
        setPos(pos);
        setTexture(texture);
        //this.isAnimate = false;
        
        LIB.add(this);
    }*/
    
    public Sprite(final Image frame, final Vector3 pos) {
        this(frame, 0.0, pos);
    }
    
    public Sprite(final Image[] frames, final Vector3 pos) {
        this(frames, 0.0, pos);
    }
    
    public Sprite(final Image[][] frames, final Vector3 pos) {
        this(frames, 0.0, pos);
    }
    
    public Sprite(final Material animation, final Vector3 pos) {
        this(animation.getFrames(), animation.getAnimSpeed(), animation.isPlayOnce(), pos);
    }
    
    public Sprite(final Image frame, double animSpeed, final Vector3 pos) {
        this(frame, animSpeed, false, pos);
    }    
    
    public Sprite(final Image[] frames, double animSpeed, final Vector3 pos) {
        this(frames, animSpeed, false, pos);
    }    
    
    public Sprite(final Image[][] frames, double animSpeed, final Vector3 pos) {
        this(frames, animSpeed, false, pos);
    }    
    
    public Sprite(final Image frame, double animSpeed, boolean playOnce, final Vector3 pos) {
        super(frame, animSpeed, playOnce);
        setPos(pos);
        //synchronized(LIB) {
            LIB.add(this);
        //}
    }   
    
    public Sprite(final Image[] frames, double animSpeed, boolean playOnce, final Vector3 pos) {
        super(frames, animSpeed, playOnce);
        setPos(pos);
        //synchronized(LIB) {
            LIB.add(this);
        //}
    }   
    
    public Sprite(final Image[][] frames, double animSpeed, boolean playOnce, final Vector3 pos) {
        super(frames, animSpeed, playOnce);
        setPos(pos);
        //synchronized(LIB) {
            LIB.add(this);
        //}
    }    
    
    @Override
    public void finalize() {
        super.finalize();
        destroy();
    }
    
    @Override
    public void destroy() {
        super.destroy();
        //synchronized(LIB) {
            LIB.remove(this);
        //}
    }

    public Vector3 getPos() {
        return pos;
    }

    public void setPos(final Vector3 pos) {
        this.pos = pos;
    }

    public Vector2 getScale() {
        return scale;
    }

    public void setScale(Vector2 scale) {
        this.scale = scale;
    }
     
    
    /*
    public void setTexture(final Texture ... texture) {
        if (texture.length > 0) {
            curTex = texture[0];
            // если не переданы текстуры с углами
            if (texture.length < 8) { 
                hasSides = false;
                textures = new Texture[]{texture[0]};
            } else {
                hasSides = true;
                textures = texture;
            }
        }
    }*/
    /*
    public void setMaterial(final Material material) {
        setFrames(material.getFrames());
    }   
    */
    public Texture getTexture() {
        return curTex;
    }
        
    /*
    private void setTexture(Texture tex) {
        this.tex = tex;
    }
    */
    
    public double getDirection() {
        return direction;
    }

    public void setDirection(final double degree) {
        direction = degree;
        while (direction < 0.0) {
            direction += 360.0;
        }
        while (direction >= 360.0) {
            direction -= 360.0;
        }
    }
    
    public void rotate(final double degree) {
        setDirection(direction + degree);
    }
    
    /**
     * Пометить объект на добавление в очередь
     */
    /*public void markForAdd() {
        FOR_ADD_TO_LIB.add(this);
    }*/
    
    /**
     * Пометить объект на удаление
     */
    @Override
    public void markForDelete() {
        //synchronized(FOR_DELETE_FROM_LIB) {
            FOR_DELETE_FROM_LIB.add(this);
        //}
    }
    
    @Override
    public void update() {        
        super.update();
        
        Player player = Player.getInstance();
        
        final Vector2 plPos = player.getPos();
        final double x1 = pos.x,
                     y1 = pos.y,
                     x2 = plPos.x,
                     y2 = plPos.y;
        
        final double x0 = x1 - x2;
        final double y0 = y1 - y2;
        distance = x0*x0 + y0*y0; // sqrt not taken, unneeded
        
        //depth = pos.z * DEPTH_FACTOR;
        // если есть текстуры для сторон, то определить сторону и назначить текстуру
        if (hasSides()) {
            double degree = Math.toDegrees(Math.atan2(/*(int)*/y1 - /*(int)*/y2, 
                                                      /*(int)*/x1 - /*(int)*/x2)) - direction;
            while (degree < 0.0) {
                degree += 360.0;
            }
            while (degree >= 360.0) {
                degree -= 360.0;
            }
            
            int side = 0;
            if (degree >= 337.5 || degree < 22.5) {
                side = 0;
            } else if (/*degree >= 22.5 && */degree < 67.5) {
                side = 1;
            } else if (/*degree >= 67.5 && */degree < 112.5) {
                side = 2;
            } else if (/*degree >= 112.5 && */degree < 157.5) {
                side = 3;
            } else if (/*degree >= 157.5 && */degree < 202.5) {
                side = 4;
            } else if (/*degree >= 202.5 && */degree < 247.5) {
                side = 5;
            } else if (/*degree >= 247.5 && */degree < 292.5) {
                side = 6;
            } else if (/*degree >= 292.5 && */degree < 337.5) {
                side = 7;
            }
            
            //Texture nextTexture = textures[(int)(degree) / 45];
            Texture nextTexture = (Texture) getSideFrames()[side];
            if (nextTexture != null) {
                curTex = nextTexture;
            }
        } else {
            curTex = (Texture) getSideFrames()[0];
        }
    }
    
    /*
    public static void loadAll() {
        for (int i = 0; i < Map.SPRITES.length; ++i) {
            final double x = Map.SPRITES[i][0];
            final double y = Map.SPRITES[i][1];
            final double z = Map.SPRITES[i][2];
            final int num  = (int)Map.SPRITES[i][3];
            
            new Sprite(TextureBank.SPRITES[num], new Vector3(x, y, z));
            
            // твердость
            if (!Map.SOLIDS[(int)x][(int)y]) {
                 Map.SOLIDS[(int)x][(int)y] = num < 2 && z < 1.0;
            }
        }        
    }*/
    
    
    public static void updateAll() {
        /*for (Sprite spr : LIB) {
            spr.update();
        }
        
        Collections.sort(LIB);*/
        /*
        if (FOR_ADD_TO_LIB.size() > 0) {
            for (Sprite sprite : FOR_ADD_TO_LIB) {
                LIB.add(sprite);
            }
            FOR_ADD_TO_LIB.clear();
        }
        */
        if (!FOR_DELETE_FROM_LIB.isEmpty()) {
            synchronized(FOR_DELETE_FROM_LIB) {                
                for (Iterator<Sprite> it = FOR_DELETE_FROM_LIB.iterator(); it.hasNext(); ) {
                    /*it.next().finalize();*/
                    it.next().destroy();
                }
                FOR_DELETE_FROM_LIB.clear();
            }
        }
    }
        
    public static void deleteAll() {
        LIB.clear();
        FOR_DELETE_FROM_LIB.clear();
    }
    
    public static Sprite[] getLib() {
        Sprite[] sprites = new Sprite[LIB.size()];
        int i = 0;
        for (Iterator<Sprite> it = LIB.iterator(); it.hasNext(); ) {
            sprites[i] = it.next();
            ++i;
        }
        return sprites;
    }

    @Override
    public int compareTo(final Sprite another) {
        if (!another.equals(this)) {
            if (distance > another.distance) {
                return -1;
            } else if (distance < another.distance) {
                return 1;
            }
        }
        
        return 0;
    }
}
