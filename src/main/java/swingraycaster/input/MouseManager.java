/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.input;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tai-prg3
 */
public final class MouseManager extends MouseAdapter {
    /*class MouseTracker extends MouseMotionAdapter { 
        @Override
        public void mouseMoved(final MouseEvent e) {
            location = e.getLocationOnScreen();
        }        
    }
    
    class MouseInput extends MouseAdapter {        
        @Override
        public void mousePressed(final MouseEvent e) {
            switch (e.getButton()) {
                case MouseEvent.BUTTON1 :
                    leftClick = true;
                    break;
                case MouseEvent.BUTTON2 :
                    rightClick = true;
                    break;            
            }
        }

        @Override
        public void mouseReleased(final MouseEvent e) {
            switch (e.getButton()) {
                case MouseEvent.BUTTON1 :
                    leftClick = false;
                    break;
                case MouseEvent.BUTTON2 :
                    rightClick = false;
                    break;            
            }
        }   
    }*/
    
    
    private final static Logger LOG = Logger.getLogger(MouseManager.class.getName());  
    private final static MouseManager INSTANCE = new MouseManager();  
    //private static MouseTracker mouseTracker;  
    //private static MouseInput   mouseInput;    
    private static Robot robot;
    private static Point location;
    private static boolean leftClick  = false;
    private static boolean rightClick = false;
    
    private MouseManager () {
        //mouseTracker = new MouseTracker(); 
        //mouseInput   = new MouseInput(); 
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }   
    }     

    /*
    public static MouseTracker getMouseTracker() {
        return mouseTracker;
    }       
    
    public static MouseInput getMouseInput() {
        return mouseInput;
    } 
    */
    
    public static MouseManager getInstance() {
        return INSTANCE;
    }       
    
    @Override
    public void mouseDragged(final MouseEvent e){
        mouseMoved(e);
    }
    
    @Override
    public void mouseMoved(final MouseEvent e) {
        location = e.getLocationOnScreen();
    }    

    @Override
    public void mousePressed(final MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1 :
                leftClick = true;
                break;
            case MouseEvent.BUTTON2 :
                rightClick = true;
                break;            
        }
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1 :
                leftClick = false;
                break;
            case MouseEvent.BUTTON2 :
                rightClick = false;
                break;            
        }
    }  
        
    public static void setLocation(Point point) {
        robot.mouseMove(point.x, point.y);
        location = point;
    }

    public static Point getLocation() {
        return location;
    } 
    
    public static boolean isLeftClick() {
        return leftClick;
    }
    
    public static boolean isRightClick() {
        return rightClick;
    }    
}
