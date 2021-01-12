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

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vuvk.swinger.audio.SoundBank;
import com.vuvk.swinger.audio.SoundSystem;
import com.vuvk.swinger.d3.Model;
import com.vuvk.swinger.graphic.Fog;
import com.vuvk.swinger.graphic.Renderer;
import com.vuvk.swinger.graphic.Sky;
import com.vuvk.swinger.graphic.gui.GuiBank;
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
import com.vuvk.swinger.input.InputManager;
import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.objects.Camera;
import com.vuvk.swinger.objects.Door;
import com.vuvk.swinger.objects.Sprite;
import com.vuvk.swinger.objects.mortals.Mortal;
import com.vuvk.swinger.objects.mortals.Player;
import com.vuvk.swinger.objects.weapon.AmmoType;
import com.vuvk.swinger.res.Map;
import com.vuvk.swinger.res.Material;
import com.vuvk.swinger.res.TextureBank;
import java.util.Locale;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class Game extends ApplicationAdapter {
    private SpriteBatch batch;
    private Renderer renderer;
    private Texture consoleBackground;
    private BitmapFont font;
    private OrthographicCamera cam;
    private Rectangle viewport;
    private Stage stage;
    private Viewport camViewport;
    private Vector2 windowCenter = new Vector2();
    private InputManager inputManager;

    private double timeForFPS;
    private String processors = "available render threads - " + Config.THREADS_COUNT;
    private Text fpsText;
    private Text playerPosText;
    private Text playerHpText;
    private Text playerAmmoText;
    public static ScreenMessage screenMsg;

    private Texture lastFrame;

    int renderX,
        renderY,
        renderWidth,
        renderHeight;

    class KishPoint {
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
    KishPoint[] kishPoints = new KishPoint[10];

    @Override
    public void create () {
        Config.load();
        Config.init();

        ScreenBlood.DROPS = new ScreenBlood[Config.WIDTH];

        Fog.init();
        Renderer.init();
        GuiBank.init();
        Menu.init();
        TextureBank.load();
        SoundSystem.load();
        FontBank.load();

        KnifeInHand.loadFrames();
        PistolInHand.loadFrames();
        ShotgunInHand.loadFrames();
        RifleInHand.loadFrames();
        MinigunInHand.loadFrames();
        RocketLauncherInHand.loadFrames();

        font  = new BitmapFont();
        batch = new SpriteBatch();
        consoleBackground = new Texture(Config.WIDTH, 40, Pixmap.Format.RGB888);
        lastFrame      = TextureBank.MAIN_MENU;
        fpsText        = new Text(FontBank.FONT_BUBBLA, "", new Vector2(10, 15));
        playerPosText  = new Text(FontBank.FONT_BUBBLA, "", new Vector2(10, 25));
        playerHpText   = new Text(FontBank.FONT_MIDDLE);
        playerAmmoText = new Text(FontBank.FONT_MIDDLE);
        screenMsg      = new ScreenMessage(FontBank.FONT_BUBBLA, new Vector2(10, 45));
        //new Text(FontBank.FONT_OUTLINE, "demo", new Vector2(250, 150));

        cam = new OrthographicCamera(Config.WIDTH, Config.HEIGHT);
        cam.setToOrtho(false);
        renderer = Renderer.getInstance();

        camViewport = new FitViewport(Config.WIDTH, Config.HEIGHT, cam);
        //camViewport.apply();

        stage = new Stage();
        stage.setViewport(new ScreenViewport());
        //stage.getViewport().apply();
        //stage.addListener(new InputManager());
        /*Actor actor = new Image();
        actor.setPosition(0, 0);
        actor.setBounds(0, 0, Config.WIDTH, Config.HEIGHT);
        actor.setColor(Color.BLACK);
        actor.addListener(new InputManager());
        stage.addActor(actor);*/
        if (Config.buildForMobiles) {
            stage.addActor(GuiBank.MOBILE_BUTTON_UP);
            stage.addActor(GuiBank.MOBILE_BUTTON_DOWN);
            stage.addActor(GuiBank.MOBILE_BUTTON_LEFT);
            stage.addActor(GuiBank.MOBILE_BUTTON_RIGHT);
            stage.addActor(GuiBank.MOBILE_BUTTON_USE);
            stage.addActor(GuiBank.MOBILE_BUTTON_SHOOT);
            stage.addActor(GuiBank.MOBILE_BUTTON_NEXT_WEAPON);
            stage.addActor(GuiBank.MOBILE_BUTTON_PREV_WEAPON);
        }

        //
        //inputManager = new InputManager();
        if (Config.buildForMobiles) {
            Gdx.input.setInputProcessor(stage);
        } else {
            Gdx.input.setInputProcessor(new InputManager());
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Renderer.getInstance().start();

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

        SoundSystem.playMusic(SoundBank.FILE_MUSIC_TITLE);
    }

    @Override
    public void render () {
        // clear previous frame
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Graphics graphics = Gdx.graphics;

        cam.update();

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

        timeForFPS += Gdx.graphics.getDeltaTime();
        if (timeForFPS > 0.5) {
            timeForFPS -= 0.5;
            fpsText.setMessage("FPS: " + Gdx.graphics.getFramesPerSecond());
        }

        if (Config.draw) {
            if (!Map.isLoaded() && !lastFrame.equals(TextureBank.MAIN_MENU)) {
                lastFrame = TextureBank.MAIN_MENU;
                playerPosText.setVisible(false);
                playerHpText.setVisible(false);
                playerAmmoText.setVisible(false);
            }

            camViewport.apply();
            batch.setProjectionMatrix(cam.combined);

            if (Map.isLoaded()) {
                Renderer renderer = Renderer.getInstance();
                Player player = Player.getInstance();
                Camera playerCamera = player.getCamera();

                if (Map.active) {

                    /*batch.begin();
                    if (lastFrame != null) {
                        batch.draw(lastFrame, 0, 0, Config.WIDTH + 1, Config.HEIGHT + 1);
                    }
                    batch.end();*/

                    if (player.getHealth() > 0.0) {
                        Material.updateAll();
                        Sprite.updateAll();
                        Door.updateAll();
                        Mortal.updateAll();
                        Model.updateAll();

                        for (Model mdl : Model.LIB) {
                            mdl.rotateX(Gdx.graphics.getDeltaTime());
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

                        /*
                        font.draw(batch, processors, 10, 450);
                        font.draw(batch, fpsString,  10, 470);
                        */

                        // GUI
                        if (!Config.STEP_BY_STEP_RENDERING) {
                            Vector2 pos = player.getPos();
                            playerPosText.setMessage(String.format(Locale.ENGLISH, "PLAYER POS: %.2f %.2f", pos.x, pos.y));

                            playerHpText.setLocation(new Vector2(16, Config.HEIGHT - 16));
                            playerHpText.setMessage(String.format(Locale.ENGLISH, "HP %.0f", player.getHealth()));

                            String ammoText = "";
                            AmmoType ammoType = player.getWeaponInHand().getAmmoType();
                            switch (ammoType) {
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
                            double deltaX = InputManager.getDeltaX();
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

                            InputManager.setLocation(windowCenter);
                        }

                        //batch.end();

                        //Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                        /*stage.getViewport().apply();
                        stage.draw();
                        stage.act();*/
                    }
                    // игрок умер
                    else {
                        for (int i = 0; i < ScreenBlood.DROPS.length; ++i) {
                            ScreenBlood.DROPS[i].update();
                            Vector2 pos = ScreenBlood.DROPS[i].getPos();
                            lastFrame.draw(ScreenBlood.DROP, (int)pos.x, (int)pos.y);
                        }
                    }
                }
            }

            batch.begin();
            if (lastFrame != null) {
                batch.draw(lastFrame, 0, 0, Config.WIDTH + 1, Config.HEIGHT + 1);
            }

            Text.drawAll(batch);

            if (!Menu.isActive() && Config.console) {
                batch.draw(consoleBackground, 0, 0);
                font.draw(batch, Config.consoleCommand, 10, 25);
            }

            batch.end();

            stage.getViewport().apply();
            stage.draw();
            stage.act();
        }
        InputManager.reset();

        if (Config.QUIT) {
            Config.save();
            Gdx.app.exit();
        }
    }

    @Override
    public void resize (int width, int height) {
        windowCenter.set(width >> 1, height >> 1);

        camViewport.update(width, height, true);
        stage.getViewport().update(width, height, true);

        // смещаем кнопки управления
        int btnSize = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) >> 3;
        GuiBank.MOBILE_BUTTON_USE  .setX(width - btnSize * 1.5f);
        GuiBank.MOBILE_BUTTON_SHOOT.setX(width - btnSize * 3.0f);

        GuiBank.MOBILE_BUTTON_NEXT_WEAPON.setX(width  - btnSize * 1.5f);
        GuiBank.MOBILE_BUTTON_NEXT_WEAPON.setY(height - btnSize * 1.5f);

        GuiBank.MOBILE_BUTTON_PREV_WEAPON.setX(width  - btnSize * 1.5f);
        GuiBank.MOBILE_BUTTON_PREV_WEAPON.setY(height - btnSize * 2.5f);
    }

    @Override
    public void dispose () {
        batch.dispose();
        font.dispose();
        Map.reset();
        GuiBank.deinit();
        SoundSystem.unload();
    }

    public static void setFullscreenMode(boolean fullscreen) {
        if (!Config.buildForMobiles) {
            Config.fullscreen = fullscreen;
            if (fullscreen) {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            } else {
                Gdx.graphics.setWindowedMode(Config.WIDTH, Config.HEIGHT);
            }
        }
    }

    public static void setVSync(boolean vSync) {
        Config.vSync = vSync;
        Gdx.graphics.setVSync(vSync);
    }
}