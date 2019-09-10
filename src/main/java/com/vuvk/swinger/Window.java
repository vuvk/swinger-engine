/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.logging.Logger;
import javax.swing.JFrame;
import com.vuvk.swinger.Config;
import com.vuvk.swinger.res.Material;
import com.vuvk.swinger.graphic.Renderer;
import com.vuvk.swinger.graphic.Sky;
import com.vuvk.swinger.input.KeyboardManager;
import com.vuvk.swinger.input.MouseManager;
import com.vuvk.swinger.objects.creatures.Player;
import com.vuvk.swinger.objects.Door;
import com.vuvk.swinger.objects.Sprite;
import com.vuvk.swinger.objects.creatures.enemy.Enemy;
import com.vuvk.swinger.res.Map;
import com.vuvk.swinger.util.Utils;

/**
 *
 * @author tai-prg3
 */
public class Window extends JFrame {
    private final static Logger LOG = Logger.getLogger(Window.class.getName());    
    public /*final*/ static int WIDTH = 640;
    public /*final*/ static int HEIGHT = 480;
    public static int HALF_WIDTH;
    public static int HALF_HEIGHT;
    private final static String TITLE = "Swing Raycaster"; 
    public static boolean QUIT = false;
    
    private final Renderer RENDERER;
    private final Player PLAYER;
    private Point windowPos;
    private Point windowCenter;
    
    //private BufferStrategy bufferStrategy;
    //private Graphics graphics;
    
    public Window() {        
        //bufferStrategy = getBufferStrategy();
        //graphics = bufferStrategy.getDrawGraphics();
        
        HALF_WIDTH  = WIDTH >> 1;
        HALF_HEIGHT = HEIGHT >> 1;
                
        RENDERER = Renderer.getInstance();
        add(RENDERER);
        pack();
        PLAYER = Player.getInstance();
        
        init();
    }
    
    private void updateLocationInfo() {
        windowPos    = getLocationOnScreen();
        windowCenter = new Point(windowPos.x + HALF_WIDTH, 
                                 windowPos.y + HALF_HEIGHT);  
    }
    
    public void init() {
        addKeyListener(KeyboardManager.getInstance());
        addMouseMotionListener(MouseManager.getInstance());
        addMouseListener(MouseManager.getInstance());
        // слушалка событий окна
        addComponentListener(new ComponentListener() {
            @Override
            public void componentMoved(ComponentEvent e) { 
                updateLocationInfo();
            }
            
            @Override
            public void componentResized(ComponentEvent e) {}
            @Override
            public void componentShown(ComponentEvent e) {}
            @Override
            public void componentHidden(ComponentEvent e) {}
        });
        
        setTitle(TITLE);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIgnoreRepaint(true);
        setBackground(Color.BLACK);
        setForeground(Color.BLACK);
        //RepaintManager.currentManager(this).setDoubleBufferingEnabled(false);
       
        setResizable(false);
        //setUndecorated(true);
        
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 1, 0, 0));
        setVisible(true);
        // hide mouse cursor
        //setCursor(getToolkit().createCustomCursor(null, null, null));
        
        updateLocationInfo();
                
        //repaint();
    }
    
    public void update() {                
        // затираем видимость блоков
        //Utils.arrayFastFill(Map.VISIBLE_CELLS, false);
        for (boolean[] array : Map.VISIBLE_CELLS) {
            Utils.arrayFastFill(array, false);
        }
        /*
        for (int i = 0; i < Map.VISIBLE_CELLS.length; ++i) {
            Map.VISIBLE_CELLS[i] = false;      
        } */
        
        Material.updateAll();
        Sprite.updateAll();
        Door.updateAll();/*
        Projectile.updateAll();
        Effect.updateAll();*/
        Enemy.updateAll();
        PLAYER.update();
        
        // вращение мышкой и фиксация курсора в центре окна
        if (Config.mouseLook) {
            Point mousePos = MouseManager.getLocation();
            if (mousePos != null && mousePos.x != windowCenter.x) {
                int offsetX = windowCenter.x - mousePos.x;
                if (offsetX != 0) {
                    double mouseSpeed = (double)offsetX / WIDTH;
                    PLAYER.rotate(Math.toRadians(mouseSpeed * Player.MOUSE_ROT_SPEED));         
                }
                MouseManager.setLocation(windowCenter);
            } else if (mousePos == null) {
                MouseManager.setLocation(windowCenter);                
            }
        }
        
        Sky.getInstance().update();
        RENDERER.draw();
    }
}
