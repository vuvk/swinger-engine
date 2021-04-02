/**
    Copyright (C) 2019-2020 Anton "Vuvk" Shcherbatykh <vuvk69@gmail.com>
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package com.vuvk.swinger;

//import com.vuvk.retard_sound_system.SoundSystem;
//import java.awt.EventQueue;
//import java.awt.event.WindowEvent;
import com.vuvk.swinger.graphic.Fog;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class Main {
    
//    static Window mainWindow;
    
    /**
     * @param args the command line arguments
     */
    public void main(final String[] args) {  
                
        System.out.println("Start engine...");
        
        System.out.println("Start sound system");
        //SoundSystem.start();
                
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
                            //Window.WIDTH = Integer.parseInt(args[i + 1]);                            
                        } catch (NumberFormatException ex) {}
                    }
                    break;
                    
                case "-h" :
                    if (i < args.length - 1) {
                        try {
                            //Window.HEIGHT = Integer.parseInt(args[i + 1]);                            
                        } catch (NumberFormatException ex) {}
                    }
                    break;
                    
                case "-fog" :
                    if (i < args.length - 1) {
                        try {
                            int param = Integer.parseInt(args[i + 1]);
                            if (param == 1) {
                                Config.fog = Fog.OLDSCHOOL;
                            } else if (param == 2) {
                                Config.fog = Fog.LINEAR;
                            }
                        } catch (NumberFormatException ex) {}
                    }
                    break;
            }
        }

        Fog.init();  
        /*
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
        });*/
        
    }    
}
