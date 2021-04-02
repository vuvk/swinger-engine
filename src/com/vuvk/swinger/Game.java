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

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Locale;

import com.vuvk.retard_sound_system.SoundSystem;
import com.vuvk.swinger.audio.SoundBank;
import com.vuvk.swinger.d3.Model;
import com.vuvk.swinger.graphic.Fog;
import com.vuvk.swinger.graphic.Renderer;
import com.vuvk.swinger.graphic.Sky;
import com.vuvk.swinger.graphic.gui.ScreenBlood;
import com.vuvk.swinger.graphic.gui.menu.Menu;
import com.vuvk.swinger.graphic.gui.text.FontBank;
import com.vuvk.swinger.graphic.gui.text.ScreenMessage;
import com.vuvk.swinger.graphic.gui.text.Text;
import com.vuvk.swinger.graphic.weapon_in_hand.AmmoPack;
import com.vuvk.swinger.graphic.weapon_in_hand.KnifeInHand;
import com.vuvk.swinger.graphic.weapon_in_hand.MinigunInHand;
import com.vuvk.swinger.graphic.weapon_in_hand.PistolInHand;
import com.vuvk.swinger.graphic.weapon_in_hand.RifleInHand;
import com.vuvk.swinger.graphic.weapon_in_hand.RocketLauncherInHand;
import com.vuvk.swinger.graphic.weapon_in_hand.ShotgunInHand;
import com.vuvk.swinger.input.KeyboardManager;
import com.vuvk.swinger.input.MouseManager;
import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.Camera;
import com.vuvk.swinger.objects.Door;
import com.vuvk.swinger.objects.LightSource;
import com.vuvk.swinger.objects.Sprite;
import com.vuvk.swinger.objects.mortals.Mortal;
import com.vuvk.swinger.objects.mortals.Player;
import com.vuvk.swinger.objects.weapon.AmmoType;
import com.vuvk.swinger.res.Map;
import com.vuvk.swinger.res.Material;
import com.vuvk.swinger.res.TextureBank;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class Game extends Frame {

    private boolean initialized = false;
    private Canvas canvas;
    private Graphics batch;
    private BufferStrategy backBuffer;
    private BufferedImage surface;
    private Renderer renderer;
    private BufferedImage consoleBackground;
    private Vector2 windowCenter = new Vector2();

    private double timeForFPS;
    //private String processors = "available render threads - " + Config.THREADS_COUNT;
    private Text fpsText;
    private Text playerPosText;
    private Text playerHpText;
    private Text playerAmmoText;
    public static ScreenMessage screenMsg;

    private BufferedImage lastFrame;

    int renderX,
        renderY,
        renderWidth,
        renderHeight;

    /*class KishPoint {
        Vector2 a;
        boolean onDown = false;

        KishPoint(Vector2 v) {
            a = v;
        }

        void update() {
            double speed = 0.5 * Gdx.graphics.getDeltaTime();
            Vector2 move = new Vector2(speed, 0);

            if (onDown) {
                a = a.sub(move);
                if (a.x <= 1.25) {
                    onDown = false;
                }
            } else {
                a = a.add(move);
                if (a.x >= 1.75) {
                    onDown = true;
                }
            }
        }
    }
    KishPoint[] kishPoints = new KishPoint[10];*/

    double angle = 0.0;

    public Game() {
        super();

        addKeyListener(KeyboardManager.getInstance());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Config.QUIT = true;
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                toFront();
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                toFront();
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                if (isVisible()) {
                    Point location = getLocationOnScreen();
                    int centerX = location.x + (getWidth() >> 1);
                    int centerY = location.y + (getHeight() >> 1); 
                    windowCenter.set(centerX, centerY);
                }
            }
        });

        canvas = new Canvas();
        canvas.setIgnoreRepaint(true);
        canvas.setSize(Config.WIDTH, Config.HEIGHT);
        canvas.setFocusable(false);
        canvas.addMouseListener(MouseManager.getInstance());
        canvas.addMouseMotionListener(MouseManager.getInstance());
        canvas.addMouseWheelListener(MouseManager.getInstance());
        add(canvas);
        pack();

        setTitle(Config.TITLE);
        setBackground(Color.BLACK);
        setIgnoreRepaint(true);
        setAutoRequestFocus(true);
        //setFocusTraversalKeysEnabled(true);
        setAlwaysOnTop(true);
        setMinimumSize(new Dimension(Config.WIDTH, Config.HEIGHT));
        setLocationRelativeTo(null);
/*
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            public void eventDispatched(AWTEvent e) {
                System.err.println(e);
            }
        }, FocusEvent.FOCUS_EVENT_MASK | WindowEvent.WINDOW_FOCUS_EVENT_MASK | WindowEvent.WINDOW_EVENT_MASK);
*/

        create();

        initialized = true;
        setVisible(true);
    }

    private void create() {
        Config.load();
        Config.init();

        ScreenBlood.drops = new ScreenBlood[Config.WIDTH];
        for (int i = 0; i < ScreenBlood.drops.length; ++i) {
            ScreenBlood.drops[i] = new ScreenBlood(new Vector2(i, 0));
        }

        Fog.init();
        Renderer.init();
        Menu.init();
        TextureBank.load();
        SoundSystem.start();
        FontBank.load();

        KnifeInHand.loadFrames();
        PistolInHand.loadFrames();
        ShotgunInHand.loadFrames();
        RifleInHand.loadFrames();
        MinigunInHand.loadFrames();
        RocketLauncherInHand.loadFrames();

        consoleBackground = new BufferedImage(Config.WIDTH, 40, BufferedImage.TYPE_INT_ARGB);
        Graphics cG = consoleBackground.getGraphics();
        cG.setColor(Color.DARK_GRAY);
        cG.fillRect(0, 0, consoleBackground.getWidth(), consoleBackground.getHeight());

        lastFrame      = TextureBank.MAIN_MENU;
        fpsText        = new Text(FontBank.FONT_BUBBLA, "", new Vector2(10, 15));
        playerPosText  = new Text(FontBank.FONT_BUBBLA, "", new Vector2(10, 25));
        playerHpText   = new Text(FontBank.FONT_MIDDLE);
        playerAmmoText = new Text(FontBank.FONT_MIDDLE);
        screenMsg      = new ScreenMessage(FontBank.FONT_BUBBLA, new Vector2(10, 45));
        //new Text(FontBank.FONT_OUTLINE, "demo", new Vector2(250, 150));

        renderer = Renderer.getInstance();
        canvas.createBufferStrategy(2);
        backBuffer = canvas.getBufferStrategy();
        surface = new BufferedImage(Config.WIDTH, Config.HEIGHT, BufferedImage.TYPE_INT_ARGB);
        //surface.getGraphics().setColor(Color.BLACK);
        //surface.getGraphics().fillRect(0, 0, surface.getWidth(), surface.getHeight());
        batch = surface.getGraphics();

        //
        //inputManager = new InputManager();
//        Renderer.getInstance().start();

        /*for (int i = 1; i < 6; ++i) {
            kishPoints[i - 1] = new KishPoint(Map.SEGMENTS[1][i].getA());
            kishPoints[(i - 1) * 2 + 1] = new KishPoint(Map.SEGMENTS[1][i].getB());
        }*/

        /*Map.load(1);

        kishPoints[0] = new KishPoint(Map.SEGMENTS[1][1].getA());
        kishPoints[1] = new KishPoint(Map.SEGMENTS[1][1].getB());
        kishPoints[2] = new KishPoint(Map.SEGMENTS[1][2].getA());
        kishPoints[3] = new KishPoint(Map.SEGMENTS[1][2].getB());
        kishPoints[4] = new KishPoint(Map.SEGMENTS[1][3].getA());
        kishPoints[5] = new KishPoint(Map.SEGMENTS[1][3].getB());
        kishPoints[6] = new KishPoint(Map.SEGMENTS[1][4].getA());
        kishPoints[7] = new KishPoint(Map.SEGMENTS[1][4].getB());
        kishPoints[8] = new KishPoint(Map.SEGMENTS[1][5].getA());
        kishPoints[9] = new KishPoint(Map.SEGMENTS[1][5].getB());*/

        Config.draw = true;
        Menu.activate();

        SoundBank.MUSIC_TITLE.play(true);
    }

    @Override
    public void dispose() {
        super.dispose();
        Map.reset();
        SoundSystem.stop();
        SoundSystem.deleteSoundBuffers();
        initialized = false;
        Config.save();
    }

    private void update() {
        if (!initialized) {
            return;
        }

        // clear previous frame
        //Graphics graphics = Gdx.graphics;
        //batch = surface.createGraphics();

        /*for (int i = 1; i < 6; ++i) {
            kishPoints[i - 1].update();
            kishPoints[(i - 1) * 2 + 1].update();

            Map.SEGMENTS[1][i].setA(kishPoints[i - 1].a);
            //Map.SEGMENTS[1][i].setB(kishPoints[(i - 1) * 2 + 1].a);
        }*/
        /*for (int i = 0; i < 10; ++i) {
            kishPoints[i].update();
        }

        Map.SEGMENTS[1][1].setA(kishPoints[0].a);
        Map.SEGMENTS[1][1].setB(kishPoints[1].a);
        Map.SEGMENTS[1][2].setA(kishPoints[2].a);
        Map.SEGMENTS[1][2].setB(kishPoints[3].a);
        Map.SEGMENTS[1][3].setA(kishPoints[4].a);
        Map.SEGMENTS[1][3].setB(kishPoints[5].a);
        Map.SEGMENTS[1][4].setA(kishPoints[6].a);
        Map.SEGMENTS[1][4].setB(kishPoints[7].a);
        Map.SEGMENTS[1][5].setA(kishPoints[8].a);
        Map.SEGMENTS[1][5].setB(kishPoints[9].a);

        Segment tree = Map.SEGMENTS[7][ 3];
        Vector2 a = tree.getA();
        Vector2 b = tree.getB();
        double angle = Math.toRadians(45 * Gdx.graphics.getDeltaTime());

        a.x -= 7.5;
        b.x -= 7.5;
        a.y -= 3.5;
        b.y -= 3.5;

        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        double x = a.x;
        a.x = a.x * cos - a.y * sin;
        a.y =   x * sin + a.y * cos;

        x = b.x;
        b.x = b.x * cos - b.y * sin;
        b.y =   x * sin + b.y * cos;

        a.x += 7.5;
        b.x += 7.5;
        a.y += 3.5;
        b.y += 3.5;


        tree = Map.SEGMENTS[7][ 5];
        a = tree.getA();
        b = tree.getB();

        a.x -= 7.5;
        b.x -= 7.5;
        a.y -= 5.5;
        b.y -= 5.5;

        x = a.x;
        a.x = a.x * cos - a.y * sin;
        a.y =   x * sin + a.y * cos;

        x = b.x;
        b.x = b.x * cos - b.y * sin;
        b.y =   x * sin + b.y * cos;

        a.x += 7.5;
        b.x += 7.5;
        a.y += 5.5;
        b.y += 5.5;    */
        Engine.update();

        timeForFPS += Engine.getDeltaTime();
        if (timeForFPS > 0.5) {
            timeForFPS -= 0.5;
            fpsText.setMessage("FPS: " + Engine.getFps());
        }

        if (Config.draw) {
            if (!Map.isLoaded() && !TextureBank.MAIN_MENU.equals(lastFrame)) {
                lastFrame = TextureBank.MAIN_MENU;
                playerPosText.setVisible(false);
                playerHpText.setVisible(false);
                playerAmmoText.setVisible(false);
            }

            if (Map.isLoaded()) {
                Player player = Player.getInstance();
                Camera playerCamera = player.getCamera();

                if (Map.active) {

                    angle += Engine.getDeltaTime();
                    if (angle >= 360.0) {
                        angle -= 360.0;
                    }

                    double x = 11.5 + 2.0 * Math.cos(angle),
                           y =  4.5 + 2.0 * Math.sin(angle);

                    Map.light1.setPos(new Vector3(x, y));


                    x = 11.5 + Math.cos(-angle);
                    y =  4.5 + Math.sin(-angle);

                    Map.light2.setPos(new Vector3(x, y));

                    /*batch.begin();
                    if (lastFrame != null) {
                        batch.draw(lastFrame, 0, 0, Config.WIDTH + 1, Config.HEIGHT + 1);
                    }
                    batch.end();*/

                    if (player.getHealth() > 0.0) {
                        LightSource.updateAll();
                        Material.updateAll();
                        Sprite.updateAll();
                        Door.updateAll();
                        Mortal.updateAll();
                        Model.updateAll();

                        for (Model mdl : Model.LIB) {
                            mdl.rotateX(Engine.getDeltaTime());
                        }

                        Sky.getInstance().update();

                        renderer.setActiveCamera(playerCamera);

                        // set viewport
                        //Gdx.gl.glViewport((int) viewport.x,     (int) viewport.y,
                        //                  (int) viewport.width, (int) viewport.height);

                        //Renderer.canRender = false;
                        //Renderer.SCREEN.draw(Renderer.getInstance().SCREEN_RASTER, 0, 0);

                        lastFrame = renderer.getFrame();
                        //batch.begin();
                        //batch.draw(renderer.getFrame(), 0, 0, Config.WIDTH + 1, Config.HEIGHT + 1);
                        //Renderer.canRender = true;

                        // GUI
                        if (!Config.STEP_BY_STEP_RENDERING) {
                            Vector2 pos = player.getPos();
                            playerPosText.setMessage(String.format(Locale.ENGLISH, "PLAYER POS: %.2f %.2f", pos.x, pos.y));

                            playerHpText.setLocation(new Vector2(16, Config.HEIGHT - 16));
                            playerHpText.setMessage(String.format(Locale.ENGLISH, "HP %.0f", player.getHealth()));

                            String ammoText;
                            AmmoType ammoType = player.getWeaponInHand().getAmmoType();
                            switch (ammoType) {
                                default:
                                case NOTHING:
                                    ammoText = "";
                                    break;
                                case PISTOL:
                                case SHOTGUN:
                                case ROCKET:
                                    ammoText = "AMMO " + AmmoPack.PACK.get(ammoType);
                                    break;
                            }
                            playerAmmoText.setLocation(new Vector2(16, Config.HEIGHT - 40));
                            playerAmmoText.setMessage(ammoText);

                            playerPosText.setVisible(true);
                            playerHpText.setVisible(true);
                            playerAmmoText.setVisible(true);
                        }

                        /*
                        if (Config.console) {
                            batch.begin();
                            batch.draw(consoleBackground, 0, 0);
                            font.draw(batch, Config.consoleCommand, 10, 25);
                            batch.end();
                        }*/

                        // вращение мышкой и фиксация курсора в центре окна
                        if (Config.mouseLook) {
                            double deltaX = MouseManager.getDeltaX();
                            if (deltaX != 0.0) {
                                double mouseSpeed = deltaX / Config.WIDTH;
                                playerCamera.rotate(Math.toRadians(mouseSpeed * Player.MOUSE_ROT_SPEED ));
                            }

                            /*if (deltaX < 0.0) {
                                player.setRotL(false);
                                player.setRotR(true);
                            } else if (deltaX > 0.0) {
                                player.setRotL(true);
                                player.setRotR(false);
                            } else {
                                player.setRotL(false);
                                player.setRotR(false);
                            }*/

                            MouseManager.setLocation(windowCenter);
                        }

                        //batch.end();

                        //Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                        /*stage.getViewport().apply();
                        stage.draw();
                        stage.act();*/
                    }
                    // игрок умер
                    else {
                        for (int i = 0; i < ScreenBlood.drops.length; ++i) {
                            ScreenBlood.drops[i].update();
                            Vector2 pos = ScreenBlood.drops[i].getPos();
                            lastFrame.getGraphics().drawImage(ScreenBlood.DROP, (int)pos.x, (int)pos.y, null);
                        }
                    }
                }
            }

            if (lastFrame != null) {
                batch.drawImage(lastFrame, 0, 0, Config.WIDTH, Config.HEIGHT, null);
            }

            Text.drawAll(batch);

            if (!Menu.isActive() && Config.console) {
                int consoleY = Config.HEIGHT - consoleBackground.getHeight();
                batch.drawImage(consoleBackground, 0, consoleY, null);
                batch.setColor(Color.RED);
                batch.drawString(Config.consoleCommand, 10, consoleY + 15);
            }
        }
        MouseManager.reset();

        //Graphics g = backBuffer.getDrawGraphics();
        Graphics g = canvas.getGraphics();
        g.drawImage(surface, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
        /*if (!backBuffer.contentsLost()) {
            backBuffer.show();
        }*/

        //g.dispose();
        //batch.dispose();
    }
/*
    @Override
    public void reshape(int x, int y, int width, int height) {
        super.reshape(x, y, width, height);
        setFocusable(true);
    }
*/
    public void gameLoop() {
        new Thread(() -> {
            while (!Config.QUIT) {
                update();
                Thread.yield();
            }
            dispose();
        }, "GameLoop Thread").start();
    }

    public static void setFullscreenMode(boolean fullscreen) {
        if (!Config.buildForMobiles) {
            Config.fullscreen = fullscreen;
            if (fullscreen) {
                //Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            } else {
                //Gdx.graphics.setWindowedMode(Config.WIDTH, Config.HEIGHT);
            }
        }
    }

    public static void setVSync(boolean vSync) {
        Config.vSync = vSync;
        //Gdx.graphics.setVSync(vSync);
    }
}
