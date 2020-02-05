package com.vuvk.swinger.d3;

import com.badlogic.gdx.math.Matrix4;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.Object3D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Shcherbatykh
 */
public class Model extends Object3D implements Serializable {
    transient public final static List<Model> LIB = new ArrayList<>();
    
    private Mesh mesh;
    private boolean visible = true;
    
    public Model(Mesh mesh) {
        this(mesh, new Vector3());
    }
    
    public Model(Mesh mesh, Vector3 position) {
        this.mesh = mesh;
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
        return new Matrix4().setToTranslation((float)pos.x, (float)pos.z, (float)pos.y);
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
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
