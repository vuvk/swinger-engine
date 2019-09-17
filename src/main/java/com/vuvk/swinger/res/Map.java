/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.res;

import com.vuvk.swinger.objects.Sprite;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vuvk.retard_sound_system.Music;
import java.awt.Point;
import java.io.FileReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.vuvk.swinger.graphic.TexturedSegment;
import com.vuvk.swinger.graphic.WallMaterial;
import com.vuvk.swinger.graphic.WallMaterialBank;
import com.vuvk.swinger.graphic.gui.text.FontBank;
import com.vuvk.swinger.graphic.gui.text.Text;
import com.vuvk.swinger.graphic.weapon_in_hand.KnifeInHand;
import com.vuvk.swinger.graphic.weapon_in_hand.PistolInHand;
import com.vuvk.swinger.graphic.weapon_in_hand.RifleInHand;
import com.vuvk.swinger.graphic.weapon_in_hand.RocketLauncherInHand;
import com.vuvk.swinger.graphic.weapon_in_hand.ShotgunInHand;
import com.vuvk.swinger.math.Segment;
import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.objects.Door;
import com.vuvk.swinger.objects.Key;
import com.vuvk.swinger.objects.creatures.Player;
import com.vuvk.swinger.objects.creatures.enemy.Guard;
import com.vuvk.swinger.objects.creatures.enemy.GuardRocketeer;
import com.vuvk.swinger.objects.weapon.Pistol;
import com.vuvk.swinger.objects.weapon.Rifle;
import com.vuvk.swinger.util.ImmutablePair;

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
    
    public final static int[][] DOORS_MAP = new int [WIDTH][HEIGHT];/* = {
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
    
    public static void load() {
        System.out.println("Loading map...");
        
        JsonObject jsonlevel = new JsonObject();        
        try (Reader reader = new FileReader("resources/level1/map.json")){
            Gson gson = new GsonBuilder().create();  
            jsonlevel = gson.fromJson(reader, JsonObject.class);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return;
        }
        
        /* убираем метку твердости */
        for (int x = 0; x < WIDTH; ++x) {
            for (int y = 0; y < HEIGHT; ++y) {
                SOLIDS[x][y] = false;
            }        
        }
        
        JsonArray   jsonArray;
        
        /* грузим текстуры */     
        System.out.println("\tTextures...");
        jsonArray = jsonlevel.get("textures").getAsJsonArray();
        TextureBank.WALLS = new Texture[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); ++i) {
            TextureBank.WALLS[i] = new Texture(jsonArray.get(i).getAsString());
        }
        
        /* Формируем материалы */
        System.out.println("\tMaterials...");
        jsonArray = jsonlevel.get("materials").getAsJsonArray();
        MaterialBank.BANK = new Material[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); ++i) {
            JsonObject mat = jsonArray.get(i).getAsJsonObject();
            JsonArray jsonFrames = mat.getAsJsonArray("textures");
            Texture[] frames = new Texture[jsonFrames.size()];
            for (int j = 0; j < frames.length; ++j) {
                frames[j] = TextureBank.WALLS[jsonFrames.get(j).getAsInt()];
            }
            
            double animSpeed = mat.get("animation_speed").getAsDouble();
            boolean playOnce = mat.get("play_once").getAsBoolean();  
            
            MaterialBank.BANK[i] = new Material(frames, animSpeed, playOnce);
            JsonElement jsonBrigthness = mat.get("brigthness");
            if (jsonBrigthness != null) {
                MaterialBank.BANK[i].setBrightness(jsonBrigthness.getAsDouble());
            }
        }    
        
        /* формируем материалы стен */
        System.out.println("\tWalls materials...");
        jsonArray = jsonlevel.get("wall_sides_materials").getAsJsonArray();
        WallMaterialBank.BANK = new WallMaterial[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); ++i) {
            JsonObject mat = jsonArray.get(i).getAsJsonObject();
            JsonArray jsonSides = mat.getAsJsonArray("materials");
            Material[] sides = new Material[jsonSides.size()];
            for (int j = 0; j < sides.length; ++j) {
                sides[j] = MaterialBank.BANK[jsonSides.get(j).getAsInt()];
            }
            
            WallMaterialBank.BANK[i] = new WallMaterial(sides);
        }
        
        /* грузим расположение стен */
        System.out.println("\tWalls...");
        jsonArray = jsonlevel.get("walls").getAsJsonArray();
        for (int level = 0; level < LEVELS_COUNT; ++level) {
            JsonArray map = jsonArray.get(level).getAsJsonArray();
            for (int i = 0; i < map.size(); ++i) {
                int x = i / HEIGHT;
                int y = i % WIDTH;
                int cell = map.get(i).getAsInt();
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
        jsonArray = jsonlevel.get("floor").getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); ++i) {
            int x = i / HEIGHT;
            int y = i % WIDTH;
            FLOOR[x][y] = jsonArray.get(i).getAsInt();
        }
        
        /* грузим потолок */  
        System.out.println("\tCeil...");
        jsonArray = jsonlevel.get("ceil").getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); ++i) {
            int x = i / HEIGHT;
            int y = i % WIDTH;
            CEIL[x][y] = jsonArray.get(i).getAsInt();
        }
        
        /* грузим ключи */
        System.out.println("\tKeys...");
        jsonArray = jsonlevel.get("keys").getAsJsonArray();
        Material[] keysMat = new Material[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); ++i) {
            JsonObject jsonKey = jsonArray.get(i).getAsJsonObject();            
            int matNum = jsonKey.get("material").getAsInt();
            keysMat[i] = MaterialBank.BANK[matNum];
        }
        
        /* расставляем ключи */
        System.out.println("\t\tKeys placing...");
        jsonArray = jsonlevel.get("keys_map").getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); ++i) {
            int keyNum = jsonArray.get(i).getAsInt();
            if (keyNum < 0) {
                continue;
            }
            
            double x = i / HEIGHT + 0.5;
            double y = i % WIDTH  + 0.5;
                 
            new Key(keysMat[keyNum], new Vector3(x, y), keyNum);
        }        
        
        /* грузим оружие */
        System.out.println("\tWeapons...");
        jsonArray = jsonlevel.get("weapons").getAsJsonArray();
        Material[] weaponsMat = new Material[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); ++i) {
            JsonObject jsonWeapon = jsonArray.get(i).getAsJsonObject();            
            int matNum = jsonWeapon.get("material").getAsInt();
            if (matNum >= 0) {
                weaponsMat[i] = MaterialBank.BANK[matNum];
            }
        }
        
        /* расставляем оружие */
        System.out.println("\t\tWeapons placing...");
        jsonArray = jsonlevel.get("weapons_map").getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); ++i) {
            int weaponNum = jsonArray.get(i).getAsInt();
            if (weaponNum < 1) {
                continue;
            }
            
            double x = i / HEIGHT + 0.5;
            double y = i % WIDTH  + 0.5;
            
            switch (weaponNum) {
                case 1 :
                    new Pistol(weaponsMat[1], new Vector3(x, y));
                    break;
                case 2 :
                    new Rifle(weaponsMat[2], new Vector3(x, y));
                    break;
            }
        }
        
        /* грузим двери */
        System.out.println("\tDoors...");
        jsonArray = jsonlevel.get("doors").getAsJsonArray();
        ImmutablePair<Material, Integer>[] doorsInfo = new ImmutablePair[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); ++i) {
            JsonObject jsonDoor = jsonArray.get(i).getAsJsonObject();            
            int matNum = jsonDoor.get("material").getAsInt();
            Material mat = MaterialBank.BANK[matNum];
            
            int keyNum = -1;            
            JsonElement jsonKey = jsonDoor.get("key");
            if (jsonKey != null) {
                keyNum = jsonKey.getAsInt();
            }
            
            doorsInfo[i] = new ImmutablePair<>(mat, keyNum);
        }
                
        /* читаем позицию дверей (расставляется чуть позже) */  
        System.out.println("\t\tDoors placing...");
        jsonArray = jsonlevel.get("doors_map").getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); ++i) {
            int x = i / HEIGHT;
            int y = i % WIDTH;
            
            DOORS_MAP[x][y] = jsonArray.get(i).getAsInt();
        }
                
        /* грузим спрайты */  
        System.out.println("\tSprites...");
        jsonArray = jsonlevel.get("sprites").getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); ++i) {
            JsonObject jsonSprite = jsonArray.get(i).getAsJsonObject();
            
            int matNum = jsonSprite.get("material").getAsInt();
            Material mat = MaterialBank.BANK[matNum];
            
            JsonArray jsonPos = jsonSprite.get("position").getAsJsonArray();
            Vector3 pos = new Vector3(jsonPos.get(0).getAsDouble(),
                                      jsonPos.get(1).getAsDouble(),
                                      jsonPos.get(2).getAsDouble());
            
            boolean solid = jsonSprite.get("solid").getAsBoolean();  
            if (solid) {
                Map.SOLIDS[(int)pos.x][(int)pos.y] = true;
            }
            
            new Sprite(mat, pos);          
        }
        
        for (Segment[] array : SEGMENTS) {
            Arrays.fill(array, null);
        }
                
        // кастомные сегменты
        /*  
        SEGMENTS[21][ 9] = new TexturedSegment(21,    9, 22, 9.5, TextureBank.WALLS[1]);
        SEGMENTS[22][10] = new TexturedSegment(22.5, 10, 23, 11 , TextureBank.WALLS[1]);        
        SEGMENTS[22][12] = new TexturedSegment(22,   13, 23, 12 , TextureBank.WALLS[1]);        
        SEGMENTS[21][13] = new TexturedSegment(21,   14, 22, 13 , TextureBank.WALLS[1]);
        */

        for (int x = 0; x < WIDTH; ++x) {
            for (int y = 0; y < HEIGHT; ++y) {
                //int arrayPos = y * WIDTH + x;
                // расставляем твердые объекты там, где стены
                if (WALLS_MAP[0][x][y] >= 0) {
                    SOLIDS[x][y] = true;
                }
                
                // расставляем твердые объекты там, где двери
                int doorNum = DOORS_MAP[x][y];
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
                        DOORS_MAP[x][y] = -1;
                    }                  
                }             
            }
        }
        
        System.out.println("\tWeapons...");
        //Sprite.loadAll();
        KnifeInHand.loadFrames();
        PistolInHand.loadFrames();
        ShotgunInHand.loadFrames();
        RifleInHand.loadFrames();
        RocketLauncherInHand.loadFrames();
        Player.getInstance().createWeaponsInHand();
        
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
        
        new Text(FontBank.FONT_OUTLINE, "demo", new Point(250, 150));
        
        /*
        new Sound(SoundBank.SOUND_FILE_MUSIC1)
            .setVolume(0.6f)
            .loop();
        */
        
        //new Music("resources/snd/music/music.mp3").play(true);
    }
}
