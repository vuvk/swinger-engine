package com.vuvk.swinger.d3;

import com.vuvk.swinger.math.Matrix4;
import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.Object3D;
import com.vuvk.swinger.objects.creatures.Player;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Shcherbatykh
 */
public class Model extends Object3D implements Comparable<Model>, Serializable {
    transient public final static List<Model> LIB = new ArrayList<>();
    
    private Matrix4 mdlMtx;
    private Mesh mesh;
    private boolean visible = true;
    private double distance = 0;  
    
    public Model(Mesh mesh) {
        this(mesh, new Vector3());
    }
    
    public Model(Mesh mesh, Vector3 position) {
        this.mesh = mesh;
        setModelMtx();
        LIB.add(this);
    }
    
    @Override
    public void finalize() {
        LIB.remove(this);
    }
    
    public static void deleteAll() {
        LIB.clear();
    }

    public Mesh getMesh() {
        return mesh;
    }

    public boolean isVisible() {
        return visible;
    }
    
    public Matrix4 getModelMtx() {
        return mdlMtx;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    private void setModelMtx() {
        mdlMtx = new Matrix4().translate(pos.x, -pos.z, pos.y);
    }
    
    @Override
    public void setPos(Vector3 pos) {
        super.setPos(pos);
        setModelMtx();
    }

    @Override
    public int compareTo(final Model another) {
        if (!another.equals(this)) {
            if (distance > another.distance) {
                return -1;
            } else if (distance < another.distance) {
                return 1;
            }
        }
        
        return 0;
    }
    
    public void update() {           
        Player player = Player.getInstance();
        
        final Vector2 plPos = player.getPos();
        final double x1 = pos.x,
                     y1 = pos.y,
                     x2 = plPos.x,
                     y2 = plPos.y;
        
        final double x0 = x1 - x2;
        final double y0 = y1 - y2;
        distance = x0*x0 + y0*y0; // sqrt not taken, unneeded
    }
    
    public static void updateAll() {
        for (Model mdl : LIB) {
            mdl.update();
        }
    }
        
    
    public static Model[] getLib() {
        Model[] models = new Model[LIB.size()];
        int i = 0;
        for (Iterator<Model> it = LIB.iterator(); it.hasNext(); ) {
            models[i] = it.next();
            ++i;
        }
        return models;
    }
}
