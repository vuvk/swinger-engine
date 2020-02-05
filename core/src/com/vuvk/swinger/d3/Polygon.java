package com.vuvk.swinger.d3;

import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.math.Vector3;
import java.io.Serializable;

/**
 *
 * @author Shcherbatykh
 */
public class Polygon implements Serializable {
    private Vector3[] pointsV = new Vector3[3];
    private Vector2[] texCoordsV = new Vector2[3];
    private Vector3 normalV;
    
    private double[] points    = new double[9];
    private double[] texCoords = new double[6];
    private double[] normal    = new double[3];
    
    public Polygon(Vector3[] points, Vector2[] texCoords, Vector3 normal) {
        setPoints(points);
        setTexCoords(texCoords);
        setNormal(normal);
    }
    
    public Polygon(double[] points, double[] texCoords, double[] normal) {
        setPoints(points);
        setTexCoords(texCoords);
        setNormal(normal);
    }

    public Vector3[] getPointsV() {
        return pointsV;
    }

    public Vector2[] getTexCoordsV() {
        return texCoordsV;
    }

    public Vector3 getNormalV() {
        return normalV;
    }

    public double[] getPoints() {
        return points;
    }

    public double[] getTexCoords() {
        return texCoords;
    }

    public double[] getNormal() {
        return normal;
    }
    
    public void setPoints(Vector3[] points) {
        this.pointsV = points;
        
        this.points[0] = points[0].x;
        this.points[1] = points[0].y;
        this.points[2] = points[0].z;
        
        this.points[3] = points[1].x;
        this.points[4] = points[1].y;
        this.points[5] = points[1].z;
        
        this.points[6] = points[2].x;
        this.points[7] = points[2].y;
        this.points[8] = points[2].z;    
    }
    
    public void setPoints(double[] points) {
        this.points = points;
        
        this.pointsV[0] = new Vector3(points[0], points[1], points[2]);
        this.pointsV[1] = new Vector3(points[3], points[4], points[5]);
        this.pointsV[2] = new Vector3(points[6], points[7], points[8]);
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
