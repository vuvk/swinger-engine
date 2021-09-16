/**
    Copyright (C) 2019-2021 Anton "Vuvk" Shcherbatykh <vuvk69@gmail.com>
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
package io.github.vuvk.swinger;

import io.github.vuvk.audiosystem.AudioSystem;
import io.github.vuvk.swinger.audio.SoundBank;
import io.github.vuvk.swinger.graphic.Fog;
import io.github.vuvk.swinger.graphic.Renderer;
import io.github.vuvk.swinger.graphic.Sky;
import io.github.vuvk.swinger.graphic.gui.GuiBank;
import io.github.vuvk.swinger.graphic.gui.ScreenBlood;
import io.github.vuvk.swinger.graphic.gui.menu.Menu;
import io.github.vuvk.swinger.graphic.gui.text.FontBank;
import io.github.vuvk.swinger.graphic.gui.text.ScreenMessage;
import io.github.vuvk.swinger.graphic.gui.text.Text;
import io.github.vuvk.swinger.graphic.weapon_in_hand.AmmoPack;
import io.github.vuvk.swinger.graphic.weapon_in_hand.KnifeInHand;
import io.github.vuvk.swinger.graphic.weapon_in_hand.MinigunInHand;
import io.github.vuvk.swinger.graphic.weapon_in_hand.PistolInHand;
import io.github.vuvk.swinger.graphic.weapon_in_hand.RifleInHand;
import io.github.vuvk.swinger.graphic.weapon_in_hand.RocketLauncherInHand;
import io.github.vuvk.swinger.graphic.weapon_in_hand.ShotgunInHand;
import io.github.vuvk.swinger.input.KeyboardManager;
import io.github.vuvk.swinger.input.MouseManager;
import io.github.vuvk.swinger.math.Vector2;
import io.github.vuvk.swinger.objects.Camera;
import io.github.vuvk.swinger.objects.Door;
import io.github.vuvk.swinger.objects.GameObject;
import io.github.vuvk.swinger.objects.mortals.Player;
import io.github.vuvk.swinger.objects.weapon.AmmoType;
import io.github.vuvk.swinger.res.Map;
import io.github.vuvk.swinger.res.TextureBank;
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
import java.awt.image.BufferedImage;
import java.util.Locale;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class Game extends Frame {

    private static Game instance = null;

    private boolean initialized = false;
    private Canvas canvas;
    private Graphics batch;
    //private BufferStrategy backBuffer;
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

    private Game() {
        super();

        addKeyListener(KeyboardManager.getInstance());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Config.QUIT = true;
            }
/*
            @Override
            public void windowLostFocus(WindowEvent e) {
                toFront();
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                toFront();
            }*/

            @Override
            public void windowActivated(WindowEvent e) {
                updateWindowCenter();
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                updateWindowCenter();
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
//      setFocusTraversalKeysEnabled(true);
//      setAlwaysOnTop(true);
        setMinimumSize(new Dimension(Config.WIDTH, Config.HEIGHT));
        setLocationRelativeTo(null);
/*
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            public void eventDispatched(AWTEvent e) {
                System.err.println(e);
            }
        }, FocusEvent.FOCUS_EVENT_MASK | WindowEvent.WINDOW_FOCUS_EVENT_MASK | WindowEvent.WINDOW_EVENT_MASK);
*/

        //Config.load();
        Config.init();
        setShowCursor(!Config.mouseLook);

        ScreenBlood.drops = new ScreenBlood[Config.WIDTH];
        for (int i = 0; i < ScreenBlood.drops.length; ++i) {
            ScreenBlood.drops[i] = new ScreenBlood(new Vector2(i, 0));
        }

        Fog.init();
        Renderer.init();
        Menu.init();
        TextureBank.load();
        AudioSystem.init();
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
        //canvas.createBufferStrategy(2);
        //backBuffer = canvas.getBufferStrategy();
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

        //SoundBank.MUSIC_TITLE.setLooping(true).play();
        AudioSystem.newMusic(SoundBank.PATH_MUSIC_TITLE).setLooping(true).play();

        initialized = true;
        setVisible(true);
    }

    public static synchronized Game getInstance() {
        if (instance == null) {
            synchronized (Game.class) {
                if (instance == null) {
                    instance = new Game();
                }
            }
        }
        return instance;
    }

    @Override
    public void dispose() {
        Map.reset();
        initialized = false;
        Config.save();

        batch.dispose();
        super.dispose();

        AudioSystem.deinit();

        if (instance != null) {
            synchronized (instance) {
                synchronized (Game.class) {
                    instance = null;
                }
            }
        }

        System.exit(0);
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
            if ((!Map.isLoaded() || !Map.isActive()) && !TextureBank.MAIN_MENU.equals(lastFrame)) {
                lastFrame = TextureBank.MAIN_MENU;
                playerPosText.setVisible(false);
                playerHpText.setVisible(false);
                playerAmmoText.setVisible(false);
            }

            if (Map.isLoaded() && Map.isActive()) {
                Player player = Player.getInstance();
                Camera playerCamera = player.getCamera();

                angle += Engine.getDeltaTime();
                if (angle >= 360.0) {
                    angle -= 360.0;
                }

                /*
                double x = 11.5 + 2.0 * Math.cos(angle),
                       y =  4.5 + 2.0 * Math.sin(angle);

                Map.light1.setPos(new Vector3(x, y));


                x = 11.5 + Math.cos(-angle);
                y =  4.5 + Math.sin(-angle);

                Map.light2.setPos(new Vector3(x, y));
                */

                if (player.getHealth() > 0.0) {
                    /*
                    LightSource.updateAll();
                    Material.updateAll();
                    Sprite.updateAll();
                    Mortal.updateAll();
                    */

                    Door.updateAll();
                    GameObject.updateAll();
/*
                    Model.updateAll();
                    for (Model mdl : Model.LIB) {
                        mdl.rotateX(Engine.getDeltaTime());
                    }
*/
                    Sky.getInstance().update();

                    renderer.setActiveCamera(playerCamera);
                    lastFrame = renderer.getFrame();

                    // GUI
                    if (!Const.STEP_BY_STEP_RENDERING) {
                        Vector2 pos = player.getPos();
                        playerPosText.setMessage(String.format(Locale.ENGLISH, "PLAYER POS: %.2f %.2f", pos.x, pos.y));

                        playerHpText.setLocation(new Vector2(16, Config.HEIGHT - 32));
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
                                ammoText = "AMMO " + AmmoPack.getNum(ammoType);
                                break;
                        }
                        playerAmmoText.setLocation(new Vector2(16, Config.HEIGHT - 56));
                        playerAmmoText.setMessage(ammoText);

                        playerPosText.setVisible(true);
                        playerHpText.setVisible(true);
                        playerAmmoText.setVisible(true);

                        // crosshair
                        lastFrame.getGraphics().drawImage(
                            GuiBank.IMG_CROSSHAIR,
                            (Config.HALF_WIDTH ) - ((GuiBank.IMG_CROSSHAIR.getWidth())  >> 1),
                            (Config.HALF_HEIGHT) - ((GuiBank.IMG_CROSSHAIR.getHeight()) >> 1),
                            null
                        );
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
                            playerCamera.rotate(Math.toRadians(deltaX * Player.MOUSE_ROT_SPEED * Engine.getDeltaTime()));
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

                        if (getInstance().isFocused()) {
                            MouseManager.setLocation(windowCenter);
                        }
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

            if (lastFrame != null) {
                batch.drawImage(lastFrame, 0, 0, Config.WIDTH, Config.HEIGHT, null);
            }

            Text.drawAll(batch);

            if (!Menu.isActive()) {
                if (Config.console) {
                    int consoleY = Config.HEIGHT - consoleBackground.getHeight();
                    batch.drawImage(consoleBackground, 0, consoleY, null);
                    batch.setColor(Color.RED);
                    batch.drawString(Config.consoleCommand, 10, consoleY + 15);
                }
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
        while (!Config.QUIT) {
            update();
            Thread.yield();
        }
        dispose();
    }

    private void updateWindowCenter() {
        if (isVisible()) {
            Point location = getLocationOnScreen();
            int centerX = location.x + (getWidth() >> 1);
            int centerY = location.y + (getHeight() >> 1);
            windowCenter.set(centerX, centerY);
        }
    }

    public void setShowCursor(boolean show) {
        if (show) {
            setCursor(getToolkit().createCustomCursor(
                GuiBank.IMG_ARROW,
                new Point(),
                null)
            );
        } else {
            setCursor(getToolkit().createCustomCursor(
                GuiBank.IMG_NULL,
                new Point(),
                null)
            );
        }
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
