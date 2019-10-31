/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger;

import com.vuvk.swinger.graphic.Fog;

/**
 *
 * @author tai-prg3
 */
public final class Config {
    public static boolean interlacing = false;
    public static boolean antialiasing = false;
    public static int quality = 0;  // чем больше, тем хуже качество
    public static boolean multithreading = true;
    public static boolean mouseLook = false;
    public static boolean console = false;
    public static String consoleCommand = "";
    public static boolean drawSky = true;    
    public static Fog fog = Fog.NOTHING;
    public final static int THREADS_COUNT = Runtime.getRuntime().availableProcessors() + 1;
    public final static boolean STEP_BY_STEP_RENDERING = false;
    public final static int STEP_BY_STEP_DELAY = 10; 
        
    public /*final*/ static int WIDTH  = 640;
    public /*final*/ static int HEIGHT = 480;
    public static float ASPECT_RATIO;
    public static int HALF_WIDTH;
    public static int HALF_HEIGHT;
    public final static String TITLE = "swinger engine"; 
    public static boolean QUIT = false;
    
    public static boolean buildForMobiles = false;
    
    public static boolean draw = false;
    
    
    private Config() {}
    
    public static void init() {
        HALF_WIDTH   = WIDTH  >> 1;
        HALF_HEIGHT  = HEIGHT >> 1;
        ASPECT_RATIO = (float)WIDTH / HEIGHT;
        
        if (buildForMobiles) {
            multithreading = true;
            mouseLook = false;
        }
    }
}
