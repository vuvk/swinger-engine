package com.vuvk.swinger.d3;

import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.math.Vector3;
import java.io.Serializable;

/**
 *
 * @author Shcherbatykh
 */
public class Polygon implements Serializable {
    private Vector3[] verticiesV = new Vector3[3];
    private Vector2[] texCoordsV = new Vector2[3];
    private Vector3 normalV;
    
    private double[] verticies    = new double[9];
    private double[] texCoords = new double[6];
    private double[] normal    = new double[3];
    
    public Polygon(Vector3[] verticies, Vector3 normal) {
        setPoints(verticies);
        setNormal(normal);
    }
    
    public Polygon(Vector3[] verticies, Vector2[] texCoords, Vector3 normal) {
        setPoints(verticies);
        setTexCoords(texCoords);
        setNormal(normal);
    }
    
    public Polygon(double[] verticies, double[] texCoords, double[] normal) {
        setPoints(verticies);
        setTexCoords(texCoords);
        setNormal(normal);
    }

    public Vector3[] getVerticiesV() {
        return verticiesV;
    }

    public Vector2[] getTexCoordsV() {
        return texCoordsV;
    }

    public Vector3 getNormalV() {
        return normalV;
    }

    public double[] getVerticies() {
        return verticies;
    }

    public double[] getTexCoords() {
        return texCoords;
    }

    public double[] getNormal() {
        return normal;
    }
    
    public void setPoints(Vector3[] verticies) {
        this.verticiesV = verticies;
        
        this.verticies[0] = verticies[0].x;
        this.verticies[1] = verticies[0].y;
        this.verticies[2] = verticies[0].z;
        
        this.verticies[3] = verticies[1].x;
        this.verticies[4] = verticies[1].y;
        this.verticies[5] = verticies[1].z;
        
        this.verticies[6] = verticies[2].x;
        this.verticies[7] = verticies[2].y;
        this.verticies[8] = verticies[2].z;    
    }
    
    public void setPoints(double[] verticies) {
        this.verticies = verticies;
        
        this.verticiesV[0] = new Vector3(verticies[0], verticies[1], verticies[2]);
        this.verticiesV[1] = new Vector3(verticies[3], verticies[4], verticies[5]);
        this.verticiesV[2] = new Vector3(verticies[6], verticies[7], verticies[8]);
    }
    
    public void setTexCoords(Vector2[] texCoords) {
        this.texCoordsV = texCoords;        
        
        this.texCoords[0] = texCoords[0].x;
        this.texCoords[1] = texCoords[0].y;
        
        this.texCoords[2] = texCoords[1].x;
        this.texCoords[3] = texCoords[1].y;
        
        this.texCoords[4] = texCoords[2].x;
        this.texCoords[5] = texCoords[2].y;      
    }
    
    public void setTexCoords(double[] texCoords) {
        this.texCoords = texCoords;
        
        this.texCoordsV[0] = new Vector2(texCoords[0], texCoords[1]);
        this.texCoordsV[1] = new Vector2(texCoords[2], texCoords[3]);
        this.texCoordsV[2] = new Vector2(texCoords[4], texCoords[5]);        
    }
    
    public void setNormal(Vector3 normal) {
        this.normalV = normal;        
        this.normal = normal.toArray();
    }
    
    public void setNormal(double[] normal) {
        this.normal = normal;        
        this.normalV = new Vector3(normal);
    }
}
