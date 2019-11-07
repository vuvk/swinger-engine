/**
 * о подписи для андроида
 * https://stackoverflow.com/questions/47310006/android-studio-failed-to-create-keystore
 * 
 * о вьюпортах
 * https://www.gamefromscratch.com/post/2014/12/09/LibGDX-Tutorial-Part-17-Viewports.aspx
 */

package com.vuvk.swinger;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vuvk.swinger.audio.SoundBank;
import com.vuvk.swinger.audio.SoundSystem;
import com.vuvk.swinger.graphic.Camera;
import com.vuvk.swinger.graphic.Fog;
import com.vuvk.swinger.graphic.Renderer;
import com.vuvk.swinger.graphic.Sky;
import com.vuvk.swinger.graphic.gui.GuiBank;
import com.vuvk.swinger.graphic.gui.ScreenBlood;
import com.vuvk.swinger.graphic.gui.text.FontBank;
import com.vuvk.swinger.graphic.gui.text.Text;
import com.vuvk.swinger.graphic.weapon_in_hand.AmmoPack;
import com.vuvk.swinger.input.InputManager;
import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.objects.Door;
import com.vuvk.swinger.objects.Sprite;
import com.vuvk.swinger.objects.creatures.Creature;
import com.vuvk.swinger.objects.creatures.Player;
import com.vuvk.swinger.res.Map;
import com.vuvk.swinger.res.Material;
import com.vuvk.swinger.res.TextureBank;
import com.vuvk.swinger.objects.weapon.AmmoType;
import java.util.Locale;

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
    
    private Texture deathScreen;
    private ScreenBlood[] screenBloods = new ScreenBlood[Config.WIDTH];
    
    int renderX,
        renderY,
        renderWidth, 
        renderHeight;

    @Override
    public void create () {                 
        Config.init();
        
        Fog.init();
        Renderer.init();
        GuiBank.init();
        TextureBank.load();
        SoundSystem.load();
        FontBank.load();
        Map.load(1);
        Config.draw = true;
        
        font = new BitmapFont();
        batch = new SpriteBatch();
        consoleBackground = new Texture(Config.WIDTH, 40, Pixmap.Format.RGB888);
        fpsText        = new Text(FontBank.FONT_BUBBLA, "", new Vector2(10, 15));
        playerPosText  = new Text(FontBank.FONT_BUBBLA, "", new Vector2(10, 25));
        playerHpText   = new Text(FontBank.FONT_MIDDLE);
        playerAmmoText = new Text(FontBank.FONT_MIDDLE);
        
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
    }

    @Override
    public void render () { 
        // clear previous frame
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Graphics graphics = Gdx.graphics;
        
        cam.update();
                
        timeForFPS += Gdx.graphics.getDeltaTime();
        if (timeForFPS > 0.5) {
            timeForFPS -= 0.5;
            fpsText.setMessage("FPS: " + Gdx.graphics.getFramesPerSecond());
        }

        if (Config.draw) {            
            Renderer renderer = Renderer.getInstance();
            Player player = Player.getInstance();
            Camera playerCamera = player.getCamera();
            
            camViewport.apply();
            batch.setProjectionMatrix(cam.combined);
            
            if (player.getHealth() > 0.0) {
                Material.updateAll();
                Sprite.updateAll();
                Door.updateAll();
                Creature.updateAll();

                Sky.getInstance().update();

                renderer.setActiveCamera(playerCamera);

                // set viewport
                //Gdx.gl.glViewport((int) viewport.x,     (int) viewport.y,
                //                  (int) viewport.width, (int) viewport.height);

                //Renderer.canRender = false;
                //Renderer.SCREEN.draw(Renderer.getInstance().SCREEN_RASTER, 0, 0);
                
                batch.begin();
                batch.draw(renderer.getFrame(), 0, 0, Config.WIDTH + 1, Config.HEIGHT + 1);
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

                    Text.drawAll(batch);
                }

                if (Config.console) {
                    batch.draw(consoleBackground, 0, 0);
                    font.draw(batch, Config.consoleCommand, 10, 25);                      
                }

                batch.end();

                //Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                stage.getViewport().apply();
                stage.draw();
                stage.act();

                // вращение мышкой и фиксация курсора в центре окна
                if (Config.mouseLook) {
                    double deltaX = InputManager.getDeltaX();
                    if (deltaX != 0.0) {
                        double mouseSpeed = deltaX / Config.WIDTH;
                        playerCamera.rotate(Math.toRadians(mouseSpeed * Player.MOUSE_ROT_SPEED ));                      
                    }  
                    /*
                    if (deltaX < 0.0) {
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
            }
            // игрок умер
            else {
                if (deathScreen == null) {
                    deathScreen = renderer.getFrame();
                    SoundSystem.playOnceRandom(new FileHandle[]{SoundBank.FILE_DIE1, SoundBank.FILE_DIE2});
                    
                    for (int i = 0; i < screenBloods.length; ++i) {
                        screenBloods[i] = new ScreenBlood(new Vector2(i, 0));
                    }
                }
                
                for (int i = 0; i < screenBloods.length; ++i) {
                    screenBloods[i].update();
                    Vector2 pos = screenBloods[i].getPos();
                    if (pos.y < Renderer.HEIGHT) {
                        deathScreen.draw(ScreenBlood.DROP, (int)pos.x, (int)pos.y);
                    }
                }
                batch.begin();
                batch.draw(deathScreen, 0, 0, Config.WIDTH + 1, Config.HEIGHT + 1);
                batch.end();                
            }
        }
        InputManager.reset();
        
        if (Config.QUIT) {
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
}