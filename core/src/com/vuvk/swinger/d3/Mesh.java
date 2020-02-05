package com.vuvk.swinger.d3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Shcherbatykh
 */
public class Mesh implements Serializable {
    private List<Polygon> polys = new ArrayList<>();
    
    public static List<Mesh> LIB = new ArrayList<>();
    
    public Mesh(List<Polygon> polys) {
        for (Polygon poly : polys) {
            this.polys.add(poly);
        }
    }
    
    public List<Polygon> getPolygons() {
        return polys;
    }
}
