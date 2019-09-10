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
    public static boolean multithreading = false;
    public static boolean mouseLook = false;
    public static boolean console = false;
    public static String consoleCommand;
    public static boolean drawSky = true;    
    public static Fog fog = Fog.NOTHING;  
    public final static int THREADS_COUNT = Runtime.getRuntime().availableProcessors() + 1;
    public final static boolean STEP_BY_STEP_RENDERING = false;
    public final static int STEP_BY_STEP_DELAY = 10; 
    
    private Config() {}
}
