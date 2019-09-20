/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger;

import com.vuvk.retard_sound_system.SoundSystem;
import com.vuvk.swinger.res.Map;
import java.awt.EventQueue;
import java.awt.event.WindowEvent;
import com.vuvk.swinger.graphic.Fog;

/**
 *
 * @author tai-prg3
 */
public class Main {
    
    static Window mainWindow;
    
    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {  
                
        System.out.println("Start engine...");
        
        System.out.println("Start sound system");
        SoundSystem.start();
                
        for (int i = 0; i < args.length; ++i) {
            switch (args[i].toLowerCase()) {
                case "-interlacing" :
                    Config.interlacing = true;
                    break;
                    
                case "-fast" :
                    Config.quality = 1;
                    break;
                    
                case "-fastest" :
                    Config.quality = 2;
                    break;
                    
                case "-antialiasing" :
                    Config.antialiasing = true;
                    break;
                    
                case "-multithreading" :
                    Config.multithreading = true;
                    break;
                    
                case "-w" :
                    if (i < args.length - 1) {
                        try {
                            Window.WIDTH = Integer.parseInt(args[i + 1]);                            
                        } catch (NumberFormatException ex) {}
                    }
                    break;
                    
                case "-h" :
                    if (i < args.length - 1) {
                        try {
                            Window.HEIGHT = Integer.parseInt(args[i + 1]);                            
                        } catch (NumberFormatException ex) {}
                    }
                    break;
                    
                case "-fog" :
                    if (i < args.length - 1) {
                        try {
                            int param = Integer.parseInt(args[i + 1]);
                            if (param == 1) {
                                Config.fog = Fog.OLD;
                            } else if (param == 2) {
                                Config.fog = Fog.SMOOTH;
                            }
                        } catch (NumberFormatException ex) {}
                    }
                    break;
            }
        }
                        
        Fog.init();  
        
        EventQueue.invokeLater(new Runnable() {  
            @Override
            public void run() {                
                mainWindow = new Window();
                System.out.println("Engine started.");                

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map.load();  
                        Config.draw = true;
                        
                        while (!Window.QUIT) {
                            mainWindow.update();
                        }   
                        // завершить цикл и закрыть окно
                        mainWindow.dispatchEvent(new WindowEvent(mainWindow, WindowEvent.WINDOW_CLOSING));     
                        SoundSystem.stop();
                    }
                }, "Main Loop").start();  
            }
        });
        
    }    
}
