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
package com.vuvk.swinger.graphic.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.vuvk.swinger.Config;
import com.vuvk.swinger.objects.creatures.Player;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class GuiBank {
    private final static String PATH_MOBILE_BUTTON_UP    = "resources/pics/gui/button_up.png";
    private final static String PATH_MOBILE_BUTTON_DOWN  = "resources/pics/gui/button_down.png";
    private final static String PATH_MOBILE_BUTTON_LEFT  = "resources/pics/gui/button_left.png";
    private final static String PATH_MOBILE_BUTTON_RIGHT = "resources/pics/gui/button_right.png";
    private final static String PATH_MOBILE_BUTTON_USE   = "resources/pics/gui/button_use.png";
    private final static String PATH_MOBILE_BUTTON_SHOOT = "resources/pics/gui/button_shoot.png";
    
    private static Texture TXR_MOBILE_BUTTON_UP;
    private static Texture TXR_MOBILE_BUTTON_DOWN;
    private static Texture TXR_MOBILE_BUTTON_LEFT;
    private static Texture TXR_MOBILE_BUTTON_RIGHT;
    private static Texture TXR_MOBILE_BUTTON_USE;
    private static Texture TXR_MOBILE_BUTTON_SHOOT;
    
    public static Actor MOBILE_BUTTON_UP;
    public static Actor MOBILE_BUTTON_DOWN;
    public static Actor MOBILE_BUTTON_LEFT;
    public static Actor MOBILE_BUTTON_RIGHT;
    public static Actor MOBILE_BUTTON_USE;
    public static Actor MOBILE_BUTTON_SHOOT;
    public static Actor MOBILE_BUTTON_NEXT_WEAPON;
    public static Actor MOBILE_BUTTON_PREV_WEAPON;
    
    public static void init() {
        //int btnSize = Math.min(Config.WIDTH, Config.HEIGHT) / 8;
        int btnSize = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) / 8;


        TXR_MOBILE_BUTTON_UP    = new Texture(Gdx.files.internal(PATH_MOBILE_BUTTON_UP   ));
        TXR_MOBILE_BUTTON_DOWN  = new Texture(Gdx.files.internal(PATH_MOBILE_BUTTON_DOWN ));
        TXR_MOBILE_BUTTON_LEFT  = new Texture(Gdx.files.internal(PATH_MOBILE_BUTTON_LEFT ));
        TXR_MOBILE_BUTTON_RIGHT = new Texture(Gdx.files.internal(PATH_MOBILE_BUTTON_RIGHT));
        TXR_MOBILE_BUTTON_USE   = new Texture(Gdx.files.internal(PATH_MOBILE_BUTTON_USE  ));
        TXR_MOBILE_BUTTON_SHOOT = new Texture(Gdx.files.internal(PATH_MOBILE_BUTTON_SHOOT));
        
        MOBILE_BUTTON_UP = new Image(TXR_MOBILE_BUTTON_UP);
        MOBILE_BUTTON_UP.setSize(btnSize, btnSize);
        MOBILE_BUTTON_UP.setPosition(btnSize * 2.0f, btnSize * 2.0f);
        MOBILE_BUTTON_UP.addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Player.getInstance().setMoveF(true);
                return false;
            }       
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Player.getInstance().setMoveF(false);
            } 
            @Override  
            public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
                Player.getInstance().setMoveF(false);          
            }  
        });
        
        MOBILE_BUTTON_DOWN = new Image(TXR_MOBILE_BUTTON_DOWN);
        MOBILE_BUTTON_DOWN.setSize(btnSize, btnSize);
        MOBILE_BUTTON_DOWN.setPosition(btnSize * 2.0f, btnSize * 0.5f);
        MOBILE_BUTTON_DOWN.addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Player.getInstance().setMoveB(true);
                return false;
            }        
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Player.getInstance().setMoveB(false);
            }    
            @Override  
            public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) { 
                Player.getInstance().setMoveB(false);          
            }  
        });
        
        MOBILE_BUTTON_LEFT = new Image(TXR_MOBILE_BUTTON_LEFT);
        MOBILE_BUTTON_LEFT.setSize(btnSize, btnSize);
        MOBILE_BUTTON_LEFT.setPosition(btnSize * 0.5f, btnSize * 0.5f);
        MOBILE_BUTTON_LEFT.addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Player.getInstance().setRotL(true);
                return false;
            }           
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Player.getInstance().setRotL(false);
            } 
            @Override  
            public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) { 
                Player.getInstance().setRotL(false);          
            }  
        });
        
        MOBILE_BUTTON_RIGHT = new Image(TXR_MOBILE_BUTTON_RIGHT);
        MOBILE_BUTTON_RIGHT.setSize(btnSize, btnSize);
        MOBILE_BUTTON_RIGHT.setPosition(btnSize * 3.5f, btnSize * 0.5f);
        MOBILE_BUTTON_RIGHT.addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Player.getInstance().setRotR(true);
                return false;
            }              
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Player.getInstance().setRotR(false);
            } 
            @Override  
            public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) { 
                Player.getInstance().setRotR(false);          
            }  
        });
                
        MOBILE_BUTTON_USE = new Image(TXR_MOBILE_BUTTON_USE);
        MOBILE_BUTTON_USE.setSize(btnSize, btnSize);
        MOBILE_BUTTON_USE.setPosition(Config.WIDTH - btnSize, btnSize * 0.5f);
        MOBILE_BUTTON_USE.addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Player.getInstance().openDoor();
                return false;
            }       
        });
                
        MOBILE_BUTTON_SHOOT = new Image(TXR_MOBILE_BUTTON_SHOOT);
        MOBILE_BUTTON_SHOOT.setSize(btnSize, btnSize);
        MOBILE_BUTTON_SHOOT.setPosition(Config.WIDTH - btnSize * 2, btnSize * 0.5f);
        MOBILE_BUTTON_SHOOT.addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Player.getInstance().setShooting(true);
                return false;
            }           
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Player.getInstance().setShooting(false);    
            } 
            @Override  
            public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) { 
                Player.getInstance().setShooting(false);          
            }      
        });
                
        MOBILE_BUTTON_NEXT_WEAPON = new Image(TXR_MOBILE_BUTTON_UP);
        MOBILE_BUTTON_NEXT_WEAPON.setSize(btnSize, btnSize);
        MOBILE_BUTTON_NEXT_WEAPON.setPosition(Config.WIDTH - btnSize * 0.5f, Config.HEIGHT - btnSize * 0.5f);
        MOBILE_BUTTON_NEXT_WEAPON.addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Player.getInstance().nextWeapon();
                return false;
            }
        });
                
        MOBILE_BUTTON_PREV_WEAPON = new Image(TXR_MOBILE_BUTTON_DOWN);
        MOBILE_BUTTON_PREV_WEAPON.setSize(btnSize, btnSize);
        MOBILE_BUTTON_PREV_WEAPON.setPosition(Config.WIDTH - btnSize * 0.5f, Config.HEIGHT - btnSize * 1.5f);
        MOBILE_BUTTON_PREV_WEAPON.addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Player.getInstance().prevWeapon();
                return false;
            }
        });
    }
    
    public static void deinit() {
        TXR_MOBILE_BUTTON_UP   .dispose();
        TXR_MOBILE_BUTTON_DOWN .dispose();
        TXR_MOBILE_BUTTON_LEFT .dispose();
        TXR_MOBILE_BUTTON_RIGHT.dispose();
        TXR_MOBILE_BUTTON_USE  .dispose();
        TXR_MOBILE_BUTTON_SHOOT.dispose();
        
        TXR_MOBILE_BUTTON_UP    = null;
        TXR_MOBILE_BUTTON_DOWN  = null;
        TXR_MOBILE_BUTTON_LEFT  = null;
        TXR_MOBILE_BUTTON_RIGHT = null;
        TXR_MOBILE_BUTTON_USE   = null;
        TXR_MOBILE_BUTTON_SHOOT  = null;
    }
}
