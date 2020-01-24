/*
 * Copyright 2019 .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vuvk.swinger.graphic;

import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.Object3D;
import java.io.Serializable;
import static java.lang.Math.PI;

/**
 *
 * @author tai-prg3
 */
public class Camera extends Object3D implements Serializable {
    transient private final static double DEG_TO_RAD = 1.0 / 180.0 * PI;
    transient private final static double RAD_TO_DEG = 180.0 / PI;
    
    private double direction = 180.0;
    private Vector2 view = new Vector2(-1, 0);   // see to 0
    /** the 2d raycaster version of camera plane */
    private Vector2 plane = new Vector2(0, 0.66);  // fov 66

    public double getDirection() {
        return direction;
    }

    public Vector2 getPlane() {
        return plane;
    }

    public Vector2 getView() {
        return view;
    }

    void setView(Vector2 view) {
        this.view = view;
    }

    void setPlane(Vector2 plane) {
        this.plane = plane;
    } 
    
    void setDirection(double degree) {
        this.direction = degree;
        while (direction < 0.0) {
            direction += 360.0;
        }
        while (direction >= 360.0) {
            direction -= 360.0;
        }
        /*
        double rad = degree * DEG_TO_RAD;
        double sin = Math.sin(rad);
        double cos = Math.cos(rad);
        view.set(cos, sin);
        
        //both camera direction and camera plane must be rotated        
        double oldDirX = view.x;
        view.x = view.x   * cos - view.y * sin;
        view.y = oldDirX * sin + view.y * cos;
        
        double oldPlaneX = plane.x;
        plane.x = plane.x   * cos - plane.y * sin;
        plane.y = oldPlaneX * sin + plane.y * cos;*/
    }
    
    /*
    public void setPlane(Vector2 plane) {
        this.plane = plane;
    }*/
    
    public void rotate(double rad) {        
        double sin = Math.sin(rad);
        double cos = Math.cos(rad);
        
        //both camera direction and camera plane must be rotated
        double oldDirX = view.x;
        view.x = view.x  * cos - view.y * sin;
        view.y = oldDirX * sin + view.y * cos;
        
        double oldPlaneX = plane.x;
        plane.x = plane.x   * cos - plane.y * sin;
        plane.y = oldPlaneX * sin + plane.y * cos;
        
        setDirection(direction + rad * RAD_TO_DEG);
    }
    
    public void duplicate(Camera another) {
        Vector3 pos   = new Vector3(another.getPos());
        Vector2 view  = new Vector2(another.getView());
        Vector2 plane = new Vector2(another.getPlane());
        double dir    = another.getDirection();
        
        this.setPos(pos);
        this.setPlane(plane);
        this.setView(view);
        this.setDirection(dir);
    }
}
