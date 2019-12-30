/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.res;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.StreamUtils;
import com.vuvk.swinger.objects.Sprite;
/*
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vuvk.retard_sound_system.Music;
import com.vuvk.retard_sound_system.SoundSystem;
*/
import com.vuvk.swinger.Config;
import com.vuvk.swinger.audio.SoundBank;
import com.vuvk.swinger.audio.SoundSystem;
import java.io.FileReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.vuvk.swinger.graphic.TexturedSegment;
import com.vuvk.swinger.graphic.gui.text.FontBank;
import com.vuvk.swinger.graphic.gui.text.Text;
import com.vuvk.swinger.graphic.weapon_in_hand.AmmoPack;
import com.vuvk.swinger.objects.weapon.AmmoType;
import com.vuvk.swinger.graphic.weapon_in_hand.KnifeInHand;
import com.vuvk.swinger.graphic.weapon_in_hand.MinigunInHand;
import com.vuvk.swinger.graphic.weapon_in_hand.PistolInHand;
import com.vuvk.swinger.graphic.weapon_in_hand.RifleInHand;
import com.vuvk.swinger.graphic.weapon_in_hand.RocketLauncherInHand;
import com.vuvk.swinger.graphic.weapon_in_hand.ShotgunInHand;
import com.vuvk.swinger.js.Interpreter;
import com.vuvk.swinger.math.Segment;
import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.Clip;
import com.vuvk.swinger.objects.Door;
import com.vuvk.swinger.objects.Key;
import com.vuvk.swinger.objects.MedKit;
import com.vuvk.swinger.objects.creatures.Creature;
import com.vuvk.swinger.objects.creatures.Player;
import com.vuvk.swinger.objects.creatures.enemy.Guard;
import com.vuvk.swinger.objects.creatures.enemy.GuardRocketeer;
import com.vuvk.swinger.objects.weapon.Minigun;
import com.vuvk.swinger.objects.weapon.Pistol;
import com.vuvk.swinger.objects.weapon.Rifle;
import com.vuvk.swinger.utils.ArrayUtils;
import com.vuvk.swinger.utils.ImmutablePair;
import com.vuvk.swinger.utils.Pair;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tai-prg3
 */
public final class Map {
    private final static Logger LOG = Logger.getLogger(Map.class.getName());    
    
    public final static int WIDTH  = 24;
    public final static int HEIGHT = 24;
    public final static int LEVELS_COUNT = 3;
    
    public final static boolean[][] SOLIDS = new boolean[WIDTH][HEIGHT];
    public final static boolean[][] VISIBLE_CELLS = new boolean[WIDTH][HEIGHT];
    
    // уровень активен?
    public static boolean active = false;
    
    private static Music MUSIC = null;
    
    
    private Map() {}    
    
    /*public final static WallMaterial[] WALLS_MATERIALS = {
        new WallMaterial(TextureBank.WALLS[0]),
        new WallMaterial(TextureBank.WALLS[1]),
        new WallMaterial(TextureBank.WALLS[2]),
        new WallMaterial(TextureBank.WALLS[3]),
        new WallMaterial(TextureBank.WALLS[4]),
        new WallMaterial(TextureBank.WALLS[5]),
        new WallMaterial(TextureBank.WALLS[6]),
        new WallMaterial(TextureBank.WALLS[7]),
        new WallMaterial(TextureBank.WALLS[8]),
        
        new WallMaterial(new Material[]{new Material(TextureBank.WALLS[0]), 
                                         new Material(TextureBank.WALLS[1]), 
                                         new Material(new Image[]{TextureBank.WALLS[9], TextureBank.WALLS[10]}, 5.0), 
                                         new Material(TextureBank.WALLS[3])}),
    };*/
    public final static int[][][] WALLS_MAP = new int[LEVELS_COUNT][WIDTH][HEIGHT]/*{ 
        {
            {8,8,8,8,8,8,8,8,8,8,8,4,4,6,4,4,6,4,6,4,4,4,6,4},
            {8,0,0,0,0,0,0,0,0,0,8,4,0,0,0,0,0,0,0,0,0,0,0,4},
            {8,0,3,3,0,0,0,0,0,8,8,4,0,0,0,0,0,0,0,0,0,0,0,6},
            {8,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6},
            {8,0,3,3,0,0,0,0,0,8,8,4,0,0,0,0,0,0,0,0,0,0,0,4},
            {8,0,0,0,0,0,0,0,0,0,8,4,0,0,0,0,0,6,6,6,0,6,4,6},
            {8,8,8,8,0,8,8,8,8,8,8,4,4,4,4,4,4,6,0,0,0,0,0,6},
            {7,7,7,7,0,7,7,7,7,0,8,0,8,0,8,0,8,4,0,4,0,6,0,6},
            {7,7,0,0,0,0,0,0,7,8,0,8,0,8,0,8,8,6,0,0,0,0,0,6},
            {7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,6,0,0,0,0,0,4},
            {7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,6,0,6,0,6,0,6},
            {7,7,0,0,0,0,0,0,7,8,0,8,0,8,0,8,8,6,4,6,0,6,6,6},
            {7,7,7,7,0,7,7,7,7,8,8,4,0,6,8,4,8,2,2,2,0,2,2,3},
            {2,2,2,2,0,2,2,2,2,4,6,4,0,0,6,0,6,3,0,0,0,0,0,3},
            {2,2,0,0,0,0,0,2,2,4,0,0,0,0,0,0,4,3,0,0,0,0,0,3},
            {2,0,0,0,0,0,0,0,2,4,0,0,0,0,0,0,4,3,0,0,0,0,0,3},
            {1,0,0,0,0,0,0,0,1,4,4,4,4,4,6,0,6,3,0,0,0,0,0,3},
            {2,0,0,0,0,0,0,0,2,2,2,1,2,2,2,6,6,0,0,0,0,0,0,5},
            {2,10,0,0,0,0,0,10,2,2,0,0,0,2,2,0,5,0,5,0,0,0,5,5},
            {2,0,0,0,0,0,0,0,2,0,0,0,0,0,2,5,0,5,0,5,0,5,0,5},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5},
            {2,0,0,0,0,0,0,0,2,0,0,0,0,0,2,5,0,5,0,5,0,9,9,5},
            {2,2,0,0,0,0,0,2,2,2,0,0,0,2,2,0,5,0,5,0,0,9,5,5},
            {2,2,2,2,1,2,2,2,2,2,2,1,2,2,2,5,5,5,5,5,5,5,5,5}
        }, 
        { 
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {2,2,2,2,2,2,2,2,2,2,0,0,0,2,2,0,5,0,5,0,0,0,5,0},
            {3,0,0,0,0,0,0,2,2,0,0,0,0,0,2,5,0,5,0,0,0,0,0,0},
            {3,0,0,0,0,0,0,2,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0},
            {3,0,0,0,0,0,0,2,2,0,0,0,0,0,2,5,0,5,0,0,0,0,0,0},
            {2,2,2,2,2,2,2,2,2,2,2,1,2,2,2,5,5,5,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
        }, 
        { 
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,2,0,2,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,2,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,2,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {2,2,2,2,2,2,2,0,2,2,0,0,0,2,2,0,0,0,0,0,0,0,5,0},
            {2,0,0,0,0,0,0,0,2,0,0,0,0,0,2,0,0,5,0,0,0,0,0,0},
            {2,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {2,0,0,0,0,0,0,0,2,0,0,0,0,0,2,5,0,0,0,0,0,0,0,0},
            {2,2,2,2,2,2,2,2,2,2,2,1,2,2,2,0,5,5,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
        }
    }*/;
    public final static WallMaterial[][][] WALLS_MATERIALS_MAP = new WallMaterial[LEVELS_COUNT][WIDTH][HEIGHT];
    
    public final static int[][] DOORS = new int [WIDTH][HEIGHT];/* = {
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,2,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
    };*/
    public final static TexturedSegment[][] SEGMENTS = new TexturedSegment[WIDTH][HEIGHT];
    
    public final static int[][] FLOOR = new int[WIDTH][HEIGHT]/*{
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,4,4,4,4,4,4,4,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,4,4,4,4,4,4,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,4,4,4,4,4,4,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,4,4,4,4,4,4,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,4,4,4,4,4,4,4,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,5,5,5,5,5,5,5},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,5,5,5,5,5,5,5},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,5,5,5,5,5,5,5},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,5,5,5,5,5,5,5},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,5,5,5,5,5,5,5},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,5,5,5,5,5,5,5},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,5,5,5,5,5,5,5},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,5,5,5,5,5,5,5},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,5,5,5,5,5,5,5},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,5,5,5,5,5,5,5}
    }*/;
    
    public final static int[][] CEIL = new int[WIDTH][HEIGHT]/*{
        {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},
        {2,3,3,3,3,3,3,3,3,3,2,2,2,2,2,2,2,2,2,2,2,2,2,2},
        {2,3,3,3,3,3,3,3,3,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},
        {2,3,3,3,3,3,3,3,3,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},
        {2,3,3,3,3,3,3,3,3,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},
        {2,3,3,3,3,3,3,3,3,3,2,2,2,2,2,2,2,2,2,2,2,2,2,2},
        {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},
        {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},
        {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},
        {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},
        {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},
        {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},
        {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},
        {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0},
        {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0},
        {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0},
        {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0},
        {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0},
        {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0},
        {2,0,0,0,0,0,2,2,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0},
        {2,0,0,0,0,0,2,2,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0},
        {2,0,0,0,0,0,2,2,2,2,2,2,2,2,2,0,0,0,0,0,0,7,7,0},
        {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,0,0,7,0,0},
        {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0}
    }*/;
    
    public final static double[][] SPRITES = {/*
        //{20.5, 11.5, 0.0, 2}, //green light in front of playerstart
        //green lights in every room
        {10.0, 4.5,  0.0, 2},
        {10.0, 12.5, 0.0, 2},
        {3.5,  6.5,  0.0, 2},
        {3.5,  20.5, 0.0, 2},
        {3.5,  14.5, 0.0, 2},

        //row of pillars in front of wall: fisheye test
        {18.5, 10.5, 0.0, 1},
        {18.5, 11.5, 0.0, 1},
        {18.5, 12.5, 0.0, 1},

        //some barrels around the map
        {20.5, 1.5, -0.1,  0},
        {19.5, 1.5, -0.25, 0},
        {21.5, 1.5, 0.0,   0},
        {21.5, 2.5, 0.25,  0},
        {21.5, 3.5, 0.5,   0},
        {21.5, 4.5, 0.75,  0},
        {21.5, 5.5, 1.0,   0},
        
        {15.5,  1.5, 0.0, 0},
        {16.0,  1.8, 0.0, 0},
        {16.2,  1.2, 0.0, 0},
        {3.5,   2.5, 0.0, 0},
        {9.5,  15.5, 0.0, 0},
        {10.0, 15.1, 0.0, 0},
        {10.5, 15.8, 0.0, 0},
        
        // security
        {20.5,  6.5, 1.0,  3},
        {19.5,  7.5, 2.0,  3},
        {21.5,  7.5, 2.0,  3},
        {12.5, 19.5, 2.0,  3},
        {12.5, 21.5, 2.0,  3},
        
        // turrel
        {16.5, 4.5,  0.0, 4},  
        
        // spears
        {19.5,  3.5, -0.1,  5},
        
        // corpse
        {17.5, 17.5, 0.0,  7},
        {17.5, 17.5, 0.9,  6},
        
        {16.5, 16.5, 0.0,  7},
        {16.5, 16.5, 0.9,  6},
        
        {14.5, 16.5, 0.0,  7},
        {14.5, 16.5, 0.9,  6},
        
        //blood
        {12.5, 20.5, 0.0,  8},
        {21.5, 22.5, 0.0,  8},
        
        //skeleton
        {15.5, 21.5, 0.0,  9},
        {22.5, 21.5, 0.0,  9},*/
    };
    
    private static void loadTexturesAndMaterials(JsonValue jsonLevel) {
        Json json = new Json();
        
        int texturesCount  = TextureBank.WALLS.size();        
        
        /* грузим текстуры */    
        ArrayList<String> txrArray = json.readValue(ArrayList.class, jsonLevel.get("textures"));        
        for (String path : txrArray) {
            TextureBank.WALLS.add(new Texture(path));
        }
        
        /* Формируем материалы */
        ArrayList<JsonValue> matArray = json.readValue(ArrayList.class, jsonLevel.get("materials"));
        for (JsonValue mat : matArray) {            
            int[] frmNum = mat.get("textures").asIntArray();
            Texture[] frames = new Texture[frmNum.length];
            for (int j = 0; j < frames.length; ++j) {
                frames[j] = TextureBank.WALLS.get(texturesCount + frmNum[j]);
            }
            
            double animSpeed = mat.getDouble("animation_speed");
            boolean playOnce = mat.getBoolean("play_once");
            
            Material material = new Material(frames, animSpeed, playOnce);
            JsonValue jsonBrigthness = mat.get("brigthness");
            if (jsonBrigthness != null) {
                material.setBrightness(jsonBrigthness.asDouble());
            }
            MaterialBank.BANK.add(material);
        }    
    }
    
    private static void loadSprites(int levelNum) {            
        System.out.println("\tSprites...");
        
        int materialsCount = MaterialBank.BANK.size();    
        
        Json json = new Json();
        JsonValue jsonLevel = new JsonReader().parse(Gdx.files.internal("resources/maps/" + levelNum + "/sprites.json"));        
         
        System.out.println("\t\tTextures and materials...");
        loadTexturesAndMaterials(jsonLevel);
        
        ArrayList<JsonValue> spritesArray = json.readValue(ArrayList.class, jsonLevel.get("map"));  
        for (JsonValue jsonSprite : spritesArray) {            
            int matNum = materialsCount + jsonSprite.getInt("material");
            Material mat = MaterialBank.BANK.get(matNum);
            
            float[] jsonPos = jsonSprite.get("position").asFloatArray();
            Vector3 pos = new Vector3(jsonPos[0], jsonPos[1], jsonPos[2]);
            
            boolean solid = jsonSprite.getBoolean("solid");  
            if (solid) {
                Map.SOLIDS[(int)pos.x][(int)pos.y] = true;
            }
            
            new Sprite(mat, pos);          
        }
    }
    
    private static void loadWeapons(int levelNum) {             
        System.out.println("\tWeapons...");
        
        int materialsCount = MaterialBank.BANK.size();
        
        Json json = new Json();
        JsonValue jsonLevel = new JsonReader().parse(Gdx.files.internal("resources/maps/" + levelNum + "/weapons.json"));
        
        System.out.println("\t\tTextures and materials...");
        loadTexturesAndMaterials(jsonLevel);
        
        ArrayList<JsonValue> weapArray = json.readValue(ArrayList.class, jsonLevel.get("config"));   
        Material[] weaponsMat = new Material[weapArray.size()];
        int[] weaponsClip = new int[weaponsMat.length];
        for (int i = 0; i < weapArray.size(); ++i) {
            JsonValue jsonWeapon = weapArray.get(i);
            int matNum = jsonWeapon.getInt("material");
            if (matNum >= 0) {
                weaponsMat[i] = MaterialBank.BANK.get(materialsCount + matNum);
            }
            weaponsClip[i] = jsonWeapon.getInt("ammo_in_clip");
        }
        
        System.out.println("\t\tWeapons placing...");
        ArrayList<JsonValue> weapMap = json.readValue(ArrayList.class, jsonLevel.get("map"));   
        for (JsonValue jsonWeapon : weapMap) {     
            int weaponNum = jsonWeapon.getInt("weapon");
                        
            float[] jsonPos = jsonWeapon.get("position").asFloatArray();
            Vector3 pos = new Vector3(jsonPos[0], jsonPos[1], jsonPos[2]);
            
            switch (weaponNum) {
                case 1 :
                    new Pistol(weaponsMat[1], pos)
                        .setAmmoInClip(weaponsClip[1]);
                    break;
                case 2 :
                    new Rifle(weaponsMat[2], pos)
                        .setAmmoInClip(weaponsClip[2]);
                    break;
                case 3 :
                    new Minigun(weaponsMat[3], pos)
                        .setAmmoInClip(weaponsClip[3]);
                    break;
            }
        }
    }    
    
    private static void loadClips(int levelNum) {
        System.out.println("\tClips...");
        
        int materialsCount = MaterialBank.BANK.size();    
        
        Json json = new Json();
        JsonValue jsonLevel = new JsonReader().parse(Gdx.files.internal("resources/maps/" + levelNum + "/clips.json"));        
         
        System.out.println("\t\tTextures and materials...");
        loadTexturesAndMaterials(jsonLevel);
        
        ArrayList<JsonValue> clipsArray = json.readValue(ArrayList.class, jsonLevel.get("config"));   
        Material[] clipsMat = new Material[clipsArray.size()];
        AmmoType[] clipsType = new AmmoType[clipsArray.size()];
        int[] clipsVol = new int[clipsArray.size()];
        for (int i = 0; i < clipsArray.size(); ++i) {
            JsonValue jsonClip = clipsArray.get(i);
            
            int matNum = jsonClip.getInt("material");
            if (matNum >= 0) {
                clipsMat[i] = MaterialBank.BANK.get(materialsCount + matNum);
            }
            int type = jsonClip.getInt("type");
            switch (type) {
                case 1:
                    clipsType[i] = AmmoType.PISTOL;
                    break;
                case 2:
                    clipsType[i] = AmmoType.SHOTGUN;
                    break;
                case 3:
                    clipsType[i] = AmmoType.ROCKET;
                    break;
                default:
                    clipsType[i] = AmmoType.NOTHING;
                    break;                
            }
            clipsVol[i] = jsonClip.getInt("volume");
        }        
        
        ArrayList<JsonValue> clipsMap = json.readValue(ArrayList.class, jsonLevel.get("map"));  
        for (JsonValue jsonClip : clipsMap) {
            int clipNum = jsonClip.getInt("clip");
            
            float[] jsonPos = jsonClip.get("position").asFloatArray();
            Vector3 pos = new Vector3(jsonPos[0], jsonPos[1], jsonPos[2]);
            
            new Clip(clipsMat[clipNum], pos, clipsType[clipNum], clipsVol[clipNum]);
        }
    }
    
    private static void loadMedkits(int levelNum) {
        System.out.println("\nMedkits...");
        
        int materialsCount = MaterialBank.BANK.size();    
        
        Json json = new Json();
        JsonValue jsonLevel = new JsonReader().parse(Gdx.files.internal("resources/maps/" + levelNum + "/medkits.json"));        
         
        System.out.println("\t\tTextures and materials...");
        loadTexturesAndMaterials(jsonLevel);
        
        ArrayList<JsonValue> medkitsArray = json.readValue(ArrayList.class, jsonLevel.get("config"));   
        Pair<Material, Double>[] presets = new Pair[medkitsArray.size()];
        for (int i = 0; i < medkitsArray.size(); ++i) {
            JsonValue jsonClip = medkitsArray.get(i);
            presets[i] = new Pair<>();
                    
            int matNum = jsonClip.getInt("material");
            if (matNum >= 0) {
                presets[i].setLeft(MaterialBank.BANK.get(materialsCount + matNum));
            }
            presets[i].setRight(jsonClip.getDouble("volume"));
        }        
        
        ArrayList<JsonValue> clipsMap = json.readValue(ArrayList.class, jsonLevel.get("map"));  
        for (JsonValue jsonClip : clipsMap) {
            int num = jsonClip.getInt("medkit");
            
            float[] jsonPos = jsonClip.get("position").asFloatArray();
            Vector3 pos = new Vector3(jsonPos[0], jsonPos[1], jsonPos[2]);
            
            new MedKit(presets[num].getLeft(), pos, presets[num].getRight());
        }
    }
    
    private static void loadKeysDoors(int levelNum) {
        System.out.println("\tKeys and Doors...");
        
        int materialsCount = MaterialBank.BANK.size();
        
        Json json = new Json();
        JsonValue jsonLevel = new JsonReader().parse(Gdx.files.internal("resources/maps/" + levelNum + "/keys_doors.json"));
        
        // грузим текстуры      
        System.out.println("\t\tTextures and materials...");
        loadTexturesAndMaterials(jsonLevel);
        
        ArrayList<JsonValue> keysArray = json.readValue(ArrayList.class, jsonLevel.get("keys_config"));   
        Material[] keysMat = new Material[keysArray.size()];
        for (int i = 0; i < keysArray.size(); ++i) {
            JsonValue jsonKey = keysArray.get(i);
            int matNum = jsonKey.getInt("material");
            if (matNum >= 0) {
                keysMat[i] = MaterialBank.BANK.get(materialsCount + matNum);
            }
        }
        
        System.out.println("\t\tKeys placing...");
        ArrayList<JsonValue> keysMap = json.readValue(ArrayList.class, jsonLevel.get("keys_map"));   
        for (JsonValue jsonKey : keysMap) {  
            int keyNum = jsonKey.getInt("key");
            
            float[] jsonPos = jsonKey.get("position").asFloatArray();
            Vector3 pos = new Vector3(jsonPos[0], jsonPos[1], jsonPos[2]);
            
            new Key(keysMat[keyNum], pos, keyNum);
        }        
                 
        
        ArrayList<JsonValue> doorArray = json.readValue(ArrayList.class, jsonLevel.get("doors_config"));  
        ImmutablePair<Material, Integer>[] doorsInfo = new ImmutablePair[doorArray.size()];
        for (int i = 0; i < doorArray.size(); ++i) {
            JsonValue jsonDoor = doorArray.get(i);            
            int matNum = materialsCount + jsonDoor.get("material").asInt();
            Material mat = MaterialBank.BANK.get(matNum);
            
            int keyNum = -1;            
            JsonValue jsonKey = jsonDoor.get("key");
            if (jsonKey != null) {
                keyNum = jsonKey.asInt();
            }
            
            doorsInfo[i] = new ImmutablePair<>(mat, keyNum);
        }
        
        System.out.println("\t\tDoors placing...");             
        for (int x = 0; x < WIDTH; ++x) {
            for (int y = 0; y < HEIGHT; ++y) {
                DOORS[x][y] = -1;
            }
        }
        ArrayList<JsonValue> doorsMap = json.readValue(ArrayList.class, jsonLevel.get("doors_map"));   
        for (JsonValue jsonDoor : doorsMap) {  
            int doorNum = jsonDoor.getInt("door");  
            int[] doorPos = jsonDoor.get("position").asIntArray();
            int x = doorPos[0],
                y = doorPos[1];
            
            DOORS[x][y] = doorNum;
            
            // расставляем твердые объекты там, где двери
            if (doorNum >= 0) {
                // точки сегмента двери
                Vector2 a = null,
                        b = null;

                // и создаем сегмент двери, если она размещена правильно
                // горизонтальная дверь?
                if (x > 0 && x < WIDTH - 1) {
                    if (WALLS_MAP[0][x - 1][y] >= 0 && WALLS_MAP[0][x + 1][y] >= 0) {
                        a = new Vector2(x + 0, y + 0.5);
                        b = new Vector2(x + 1, y + 0.5);
                    }
                }

                // вертикальная дверь?
                if (y > 0 && y < HEIGHT - 1) {
                    if (WALLS_MAP[0][x][y - 1] >= 0 && WALLS_MAP[0][x][y + 1] >= 0) {
                        a = new Vector2(x + 0.5, y + 0);
                        b = new Vector2(x + 0.5, y + 1);
                    }
                }

                // нужно ставить дверь
                if (a != null && b != null) {
                    Door door = new Door(a, b, doorsInfo[doorNum].getLeft(), doorsInfo[doorNum].getRight());
                    SEGMENTS[x][y] = door;
                    SOLIDS[x][y] = true;
                // дверь не была размещена
                } else {
                    DOORS[x][y] = -1;
                }                  
            } 
        }
    }
    
    public static void reset() {
        Config.draw = false;
        
//        SoundSystem.stopAll();
        Door.deleteAll();
        Creature.deleteAll();
        Sprite.deleteAll();
        Material.deleteAll();
        Player.deleteInstance();
        
        if (MUSIC != null) {
            MUSIC.stop();
            MUSIC.dispose();
            MUSIC = null;
        }        
        
        active = false;
    }
    
    public static void load(int levelNum) {
        reset();
        //Config.draw = false;
                                
        System.out.println("Loading map...");
        
        //Interpreter.clearListing();
        //Interpreter.addListing(new File("resources/maps/loader.js"));
              
        Json json = new Json();
        JsonValue jsonlevel = new JsonReader().parse(Gdx.files.internal("resources/maps/" + levelNum + "/map.json"));
                
        /* убираем метку твердости */
        for (int x = 0; x < WIDTH; ++x) {
            for (int y = 0; y < HEIGHT; ++y) {
                SOLIDS[x][y] = false;
            }
        }
        /* грузим текстуры */     
        System.out.println("\tTextures and materials...");
        TextureBank.WALLS.clear();
        MaterialBank.BANK.clear();
        loadTexturesAndMaterials(jsonlevel);
        
        /* формируем материалы стен */
        System.out.println("\tWalls materials...");
        ArrayList<JsonValue> sidesArray = json.readValue(ArrayList.class, jsonlevel.get("wall_sides_materials"));
        WallMaterialBank.BANK = new WallMaterial[sidesArray.size()];
        for (int i = 0; i < sidesArray.size(); ++i) {
            JsonValue mat = sidesArray.get(i);
            int[] jsonSides = mat.get("materials").asIntArray();
            Material[] sides = new Material[jsonSides.length];
            for (int j = 0; j < sides.length; ++j) {
                sides[j] = MaterialBank.BANK.get(jsonSides[j]);
            }
            
            WallMaterialBank.BANK[i] = new WallMaterial(sides);
        }
        
        /* грузим расположение стен */
        System.out.println("\tWalls...");
        Array<Array<Float>> wallsArray = json.readValue(Array.class, jsonlevel.get("walls"));        
        for (int level = 0; level < LEVELS_COUNT; ++level) {
            Array<Float> map = wallsArray.get(level);
            for (int i = 0; i < map.size; ++i) {
                int x = i / HEIGHT;
                int y = i % WIDTH;
                int cell = map.get(i).intValue();
                WALLS_MAP[level][x][y] = cell;                
                
                /* формируем карту материалов стен */
                if (cell < 0) {
                    WALLS_MATERIALS_MAP[level][x][y] = null;
                } else {
                    WALLS_MATERIALS_MAP[level][x][y] = WallMaterialBank.BANK[cell];
                }
            }
        }  
        
        /* грузим пол */
        System.out.println("\tFloor...");
        int[] floor = jsonlevel.get("floor").asIntArray();
        for (int i = 0; i < floor.length; ++i) {
            int x = i / HEIGHT;
            int y = i % WIDTH;
            FLOOR[x][y] = floor[i];
        }
        
        /* грузим потолок */  
        System.out.println("\tCeil...");
        int[] ceil = jsonlevel.get("ceil").asIntArray();
        for (int i = 0; i < ceil.length; ++i) {
            int x = i / HEIGHT;
            int y = i % WIDTH;
            CEIL[x][y] = ceil[i];
        }
        
        for (Segment[] array : SEGMENTS) {
            Arrays.fill(array, null);
        }

        for (int x = 0; x < WIDTH; ++x) {
            for (int y = 0; y < HEIGHT; ++y) {
                // расставляем твердые объекты там, где стены
                if (WALLS_MAP[0][x][y] >= 0) {
                    SOLIDS[x][y] = true;
                }
            }
        }
                        
        /* грузим спрайты */  
        loadSprites(levelNum);
        //Interpreter.addListing(new File("resources/maps/" + levelNum + "/sprites.js"));
        
        /* грузим оружие */
        loadWeapons(levelNum);  
        //Interpreter.addListing(new File("resources/maps/" + levelNum + "/weapons.js"));
        
        /* грузим патроны */
        loadClips(levelNum);
        //Interpreter.addListing(new File("resources/maps/" + levelNum + "/clips.js"));
        
        /* грузим аптечки */
        loadMedkits(levelNum);
        
        /* грузим ключи и двери */
        loadKeysDoors(levelNum);
        //Interpreter.addListing(new File("resources/maps/" + levelNum + "/keys_doors.js"));
               
        
        //Interpreter.runListing();
        
        System.out.println("\tWeapons in player's hand...");
        //Sprite.loadAll();
        KnifeInHand.loadFrames();
        PistolInHand.loadFrames();
        ShotgunInHand.loadFrames();
        RifleInHand.loadFrames();
        MinigunInHand.loadFrames();
        RocketLauncherInHand.loadFrames();
        AmmoPack.reset();
        //Player.getInstance().createWeaponsInHand();
        
        System.out.println("\tEnemies...");
        new Guard(new Vector3(15.5, 21.5, 0.0), 
                  90);
        
        new Guard(new Vector3(15.5, 19.5, 0.0), 
                  0);    
        
        new Guard(new Vector3(15.5, 20.5, 0.0), 
                  0);      
        
        new Guard(new Vector3(14.5, 20.5, 0.0), 
                  0);
        
        new GuardRocketeer(new Vector3(12.5, 18.5, 0.0), 
                            0);
        
        new Text(FontBank.FONT_OUTLINE, "demo", new Vector2(250, 150));
        
        
        /*MUSIC = SoundSystem.loadSound(SoundBank.FILE_MUSIC1);
        MUSIC.setVolume(0.6f);
        MUSIC.setLooping(true);
        MUSIC.play();*/
        
        new Sprite(TextureBank.GUARD_STAND, 0, new Vector3(17.1, 14.3, 1.0)).rotate(180);
        
        //new Music("resources/snd/music/music.mp3").play(true);
        
                
        // кастомные сегменты
        Texture kishka = new Texture("resources/pics/world/flat/zeltum33.jpg");
        Material kishkaMat = new Material(kishka);
        
        SEGMENTS[1][ 1] = new TexturedSegment(2, 1, 1, 2, kishkaMat);
        SEGMENTS[1][ 2] = new TexturedSegment(1, 2, 2, 3, kishkaMat);
        SEGMENTS[1][ 3] = new TexturedSegment(2, 3, 1, 4, kishkaMat);
        SEGMENTS[1][ 4] = new TexturedSegment(1, 4, 2, 5, kishkaMat);
        SEGMENTS[1][ 5] = new TexturedSegment(2, 5, 1, 6, kishkaMat);
        
        Texture tree = new Texture("resources/pics/sprites/nature/tree0.png");
        Material treeMat = new Material(tree);
                
        SEGMENTS[7][ 3] = new TexturedSegment(7, 3, 7, 4, treeMat);
        SEGMENTS[7][ 5] = new TexturedSegment(7.5, 5, 7.5, 6, treeMat);
        
                        
        /*  
        SEGMENTS[21][ 9] = new TexturedSegment(21,    9, 22, 9.5, TextureBank.WALLS[1]);
        SEGMENTS[22][10] = new TexturedSegment(22.5, 10, 23, 11 , TextureBank.WALLS[1]);        
        SEGMENTS[22][12] = new TexturedSegment(22,   13, 23, 12 , TextureBank.WALLS[1]);        
        SEGMENTS[21][13] = new TexturedSegment(21,   14, 22, 13 , TextureBank.WALLS[1]);
        */
        
        active = true;
    }
    
    
    private static void saveTexturesAndMaterials(Json jsonLevel) {
        Json json = new Json();      
        
        /* грузим текстуры */    
        List<String> txrArray = new ArrayList<>(TextureBank.WALLS.size());
        for (Texture txr : TextureBank.WALLS) {
            txrArray.add(txr.getPath());
        }
        
        json.writeValue("textures", txrArray);
        
        /* Формируем материалы */
        /*ArrayList<JsonValue> matArray = json.readValue(ArrayList.class, jsonLevel.get("materials"));
        for (JsonValue mat : matArray) {            
            int[] frmNum = mat.get("textures").asIntArray();
            Texture[] frames = new Texture[frmNum.length];
            for (int j = 0; j < frames.length; ++j) {
                frames[j] = TextureBank.WALLS.get(texturesCount + frmNum[j]);
            }
            
            double animSpeed = mat.getDouble("animation_speed");
            boolean playOnce = mat.getBoolean("play_once");
            
            Material material = new Material(frames, animSpeed, playOnce);
            JsonValue jsonBrigthness = mat.get("brigthness");
            if (jsonBrigthness != null) {
                material.setBrightness(jsonBrigthness.asDouble());
            }
            MaterialBank.BANK.add(material);
        }  */  
    }
    
    public static void save() {        
        File savesDir = new File("saves");
        if (!savesDir.exists() || !savesDir.isDirectory()) {
            savesDir.mkdir();
        }
        
        StringWriter buffer = new StringWriter();
        Json json = new Json();
        json.setWriter(new JsonWriter(buffer));
        
        try {
            // добавляем в файл информацию по текстурам
            json.writeObjectStart();
            String[] txrArray = new String[TextureBank.WALLS.size()];
            for (int t = 0; t < TextureBank.WALLS.size(); ++t) {
                txrArray[t] = "'" + TextureBank.WALLS.get(t).getPath() + "'";
            }
            json.writeValue("'" + "textures" + "'", txrArray);
            json.writeObjectEnd();     
            
            // добавляем в файл информацию по материалам
            class MaterialInfo {
                int textures[];
                double animation_speed;
                boolean play_once;
            }
            MaterialInfo[] matArray = new MaterialInfo[MaterialBank.BANK.size()];
            for (int m = 0; m < MaterialBank.BANK.size(); ++m) {
                Material mat = MaterialBank.BANK.get(m);
                MaterialInfo info = new MaterialInfo();
                
                Image[][] frames = mat.getFrames();
                info.textures = new int[frames.length];
                for (int f = 0; f < frames.length; ++f) {
                    Image frame = frames[f][0];
                    info.textures[f] = TextureBank.WALLS.indexOf(frame);
                }
                info.animation_speed = mat.getAnimSpeed();
                info.play_once = mat.isPlayOnce();
                
                matArray[m] = info;
            }
            json.writeObjectStart();
            json.writeValue("'" + "materials" + "'", matArray);
            json.writeObjectEnd();       
        } finally {
            //StreamUtils.closeQuietly(buffer);
        }
        
        FileHandle saveFile = Gdx.files.local("saves/save.json");
        saveFile.writeString(buffer.toString(), false);
        
        
        
        
                /*
                Door.deleteAll();
                Creature.deleteAll();
                Sprite.deleteAll();
                Material.deleteAll();
                Player.deleteInstance();
                */
        System.out.println("Game saved.");
            
    }
}
