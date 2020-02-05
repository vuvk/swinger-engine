package com.vuvk.swinger.d3;

import com.vuvk.swinger.math.Vector3;
import java.io.Serializable;

/**
 *
 * @author Shcherbatykh
 */
public class Model implements Serializable {
    private Mesh mesh;
    private Vector3 position;
    
    public Model(Mesh mesh) {
        this(mesh, new Vector3());
    }
    
    public Model(Mesh mesh, Vector3 position) {
        this.mesh = mesh;
        this.position = position;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }
}
