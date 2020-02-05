package com.vuvk.swinger.d3;

import com.vuvk.swinger.math.Vector3;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Shcherbatykh
 */
public class Model implements Serializable {
    transient public final static List<Model> LIB = new ArrayList<>();
    
    private Mesh mesh;
    private Vector3 position;
    private boolean visible = true;
    
    public Model(Mesh mesh) {
        this(mesh, new Vector3());
    }
    
    public Model(Mesh mesh, Vector3 position) {
        this.mesh = mesh;
        this.position = position;
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

    public Vector3 getPosition() {
        return position;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
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
