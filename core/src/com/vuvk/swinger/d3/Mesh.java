package com.vuvk.swinger.d3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Shcherbatykh
 */
public class Mesh implements Serializable {
    transient public final static List<Mesh> LIB = new ArrayList<>();
    
    private List<Polygon> polys = new ArrayList<>();    
    
    public Mesh(List<Polygon> polys) {
        for (Polygon poly : polys) {
            this.polys.add(poly);
        }
        
        LIB.add(this);
    }
    
    public List<Polygon> getPolygons() {
        return polys;
    }
    
    @Override
    public void finalize() {
        LIB.remove(this);
    }
    
    public static void deleteAll() {
        LIB.clear();
    }
    
    public static Mesh[] getLib() {
        Mesh[] meshes = new Mesh[LIB.size()];
        int i = 0;
        for (Iterator<Mesh> it = LIB.iterator(); it.hasNext(); ) {
            meshes[i] = it.next();
            ++i;
        }
        return meshes;
    }
}
