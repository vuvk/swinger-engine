/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.graphic;

import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.res.Texture;

/**
 *
 * @author tai-prg3
 */  
public class RenderTarget {
    int x, y;
    //double wallX;
    int texX = 0;
    //int side;
    //double wallDist;
    // draw wall or segment?
    //boolean drawSegment = false;
    // is door side wall ?
    //boolean doorWall = false;
    Texture texture;
    double wallDist;
    Vector2 collisionPoint = new Vector2();
}