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
package io.github.vuvk.swinger.res;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.vuvk.audiosystem.AudioSystem;
import io.github.vuvk.audiosystem.Sound;
import io.github.vuvk.audiosystem.SoundBuffer;
import io.github.vuvk.swinger.audio.SoundBank;
import io.github.vuvk.swinger.graphic.Fog;
import io.github.vuvk.swinger.graphic.TexturedSegment;
import io.github.vuvk.swinger.graphic.weapon_in_hand.AmmoPack;
import io.github.vuvk.swinger.js.Interpreter;
import io.github.vuvk.swinger.math.Segment;
import io.github.vuvk.swinger.math.Vector2;
import io.github.vuvk.swinger.math.Vector3;
import io.github.vuvk.swinger.objects.Door;
import io.github.vuvk.swinger.objects.GameObject;
import io.github.vuvk.swinger.objects.LightSource;
import io.github.vuvk.swinger.objects.Sprite;
import io.github.vuvk.swinger.objects.items.Key;
import io.github.vuvk.swinger.objects.items.MedKit;
import io.github.vuvk.swinger.objects.mortals.enemy.Breakable;
import io.github.vuvk.swinger.objects.mortals.enemy.Guard;
import io.github.vuvk.swinger.objects.mortals.enemy.GuardRocketeer;
import io.github.vuvk.swinger.objects.weapon.Minigun;
import io.github.vuvk.swinger.objects.weapon.Pistol;
import io.github.vuvk.swinger.objects.weapon.Rifle;
import io.github.vuvk.swinger.utils.ImmutablePair;
import io.github.vuvk.swinger.utils.MutablePair;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public final class Map {
    private final static Logger LOGGER = Logger.getLogger(Map.class.getName());

    public final static int WIDTH  = 24;
    public final static int HEIGHT = 24;
    public final static int LEVELS_COUNT = 3;

    public final static boolean[][] SOLIDS = new boolean[WIDTH][HEIGHT];
    public final static boolean[][] VISIBLE_CELLS = new boolean[WIDTH][HEIGHT];

    // уровень загружен?
    private static boolean loaded = false;
    // уровень активен?
    private static boolean active = false;

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
    /*
    public final static double[][] SPRITES = {
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
        {22.5, 21.5, 0.0,  9},
    };*/
    //public final static Model[][] MODELS = new Model[WIDTH][HEIGHT];
    public static LightSource light1, light2;

    private static Set<SoundBuffer> mapSoundBuffers = new CopyOnWriteArraySet<>();

    private static void loadTexturesAndMaterials(JsonObject jsonLevel) {
        int texturesCount  = TextureBank.WALLS.size();

        /* грузим текстуры */
        JsonArray txrArray = jsonLevel.get("textures").getAsJsonArray();
        txrArray.forEach(path -> TextureBank.WALLS.add(new Texture(path.getAsString())));

        /* Формируем материалы */
        JsonArray matArray = jsonLevel.get("materials").getAsJsonArray();
        for (JsonElement matElement : matArray) {
            JsonObject mat = matElement.getAsJsonObject();

            JsonArray frmNums = mat.get("textures").getAsJsonArray();
            Texture[] frames = new Texture[frmNums.size()];
            for (int j = 0; j < frames.length; ++j) {
                frames[j] = TextureBank.WALLS.get(texturesCount + frmNums.get(j).getAsInt());
            }

            double animSpeed = mat.get("animation_speed").getAsDouble();
            boolean playOnce = mat.get("play_once").getAsBoolean();

            Material material = new Material(frames, animSpeed, playOnce);
/*            JsonValue jsonBrigthness = mat.get("brigthness");
            if (jsonBrigthness != null) {
                material.setBrightness(jsonBrigthness.asDouble());
            }
*/
            MaterialBank.BANK.add(material);
        }
    }

    private static void loadSprites(int levelNum) {
        System.out.println("\tSprites...");

        int materialsCount = MaterialBank.BANK.size();

        JsonObject json = null;
        try {
            json = JsonParser.parseReader(
                new FileReader("resources/maps/" + levelNum + "/sprites.json")
            ).getAsJsonObject();
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        Gson gson = new Gson();

        System.out.println("\t\tTextures and materials...");
        loadTexturesAndMaterials(json);

        JsonArray presetsArray = json.get("config").getAsJsonArray();
        List<Object>[] presets = new ArrayList[presetsArray.size()];
        for (int i = 0; i < presetsArray.size(); ++i) {
            JsonObject jsonValue = presetsArray.get(i).getAsJsonObject();
            presets[i] = new ArrayList<>();

            int materialNum = jsonValue.get("material").getAsInt();
            presets[i].add(MaterialBank.BANK.get(materialsCount + materialNum));

            boolean solid = jsonValue.get("solid").getAsBoolean();
            presets[i].add(solid);

            // по умолчанию масштабирования нет
            float[] scale = {1, 1};
            if (jsonValue.has("scale")) {
                JsonArray scaleValue = jsonValue.get("scale").getAsJsonArray();
                scale[0] = scaleValue.get(0).getAsFloat();
                scale[1] = scaleValue.get(1).getAsFloat();
            }
            presets[i].add(scale[0]);
            presets[i].add(scale[1]);
        }


        JsonArray spritesArray = json.get("map").getAsJsonArray();
        for (int i = 0; i < spritesArray.size(); ++i) {
            JsonObject jsonValue = spritesArray.get(i).getAsJsonObject();
            int num = jsonValue.get("sprite").getAsInt();
            List<Object> preset = presets[num];

            Material mat  = (Material) preset.get(0);
            boolean solid = (Boolean)  preset.get(1);
            float scaleX  = (Float)    preset.get(2);
            float scaleY  = (Float)    preset.get(3);
            Vector2 scale = new Vector2(scaleX, scaleY);

            float[] jsonPos = gson.fromJson(jsonValue.get("position"), float[].class);
            Vector3 pos = new Vector3(jsonPos);

            if (solid) {
                Map.SOLIDS[(int)pos.x][(int)pos.y] = true;
            }

            new Sprite(mat, pos).setScale(scale);
        }
    }

    private static void loadWeapons(int levelNum) {
        System.out.println("\tWeapons...");

        int materialsCount = MaterialBank.BANK.size();

        JsonObject json = null;
        try {
            json = JsonParser.parseReader(
                new FileReader("resources/maps/" + levelNum + "/weapons.json")
            ).getAsJsonObject();
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        Gson gson = new Gson();

        System.out.println("\t\tTextures and materials...");
        loadTexturesAndMaterials(json);

        JsonArray weapArray = json.get("config").getAsJsonArray();
        Material[] weaponsMat = new Material[weapArray.size()];
        int[] weaponsClip = new int[weaponsMat.length];
        for (int i = 0; i < weapArray.size(); ++i) {
            JsonObject jsonWeapon = weapArray.get(i).getAsJsonObject();
            int matNum = jsonWeapon.get("material").getAsInt();
            if (matNum >= 0) {
                weaponsMat[i] = MaterialBank.BANK.get(materialsCount + matNum);
            }
            weaponsClip[i] = jsonWeapon.get("ammo_in_clip").getAsInt();
        }

        System.out.println("\t\tWeapons placing...");
        JsonArray weapMap = json.get("map").getAsJsonArray();
        for (int i = 0; i < weapMap.size(); ++i) {
            JsonObject jsonValue = weapMap.get(i).getAsJsonObject();
            int weaponNum = jsonValue.get("weapon").getAsInt();

            float[] jsonPos = gson.fromJson(jsonValue.get("position"), float[].class);
            Vector3 pos = new Vector3(jsonPos);

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
        Interpreter.evalScript(new File("resources/maps/" + levelNum + "/clips.js"));
    }

    private static void loadMedkits(int levelNum) {
        System.out.println("\nMedkits...");

        int materialsCount = MaterialBank.BANK.size();

        JsonObject json = null;
        try {
            json = JsonParser.parseReader(
                new FileReader("resources/maps/" + levelNum + "/medkits.json")
            ).getAsJsonObject();
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        Gson gson = new Gson();

        System.out.println("\t\tTextures and materials...");
        loadTexturesAndMaterials(json);

        JsonArray medkitsArray = json.get("config").getAsJsonArray();
        MutablePair<Material, Double>[] presets = new MutablePair[medkitsArray.size()];
        for (int i = 0; i < medkitsArray.size(); ++i) {
            JsonObject jsonValue = medkitsArray.get(i).getAsJsonObject();
            presets[i] = new MutablePair<>();

            int matNum = jsonValue.get("material").getAsInt();
            if (matNum >= 0) {
                presets[i].setLeft(MaterialBank.BANK.get(materialsCount + matNum));
            }
            presets[i].setRight(jsonValue.get("volume").getAsDouble());
        }

        JsonArray medkitsMap = json.get("map").getAsJsonArray();
        for (int i = 0; i < medkitsMap.size(); ++i) {
            JsonObject jsonValue = medkitsMap.get(i).getAsJsonObject();
            int num = jsonValue.get("medkit").getAsInt();

            float[] jsonPos = gson.fromJson(jsonValue.get("position"), float[].class);
            Vector3 pos = new Vector3(jsonPos);

            new MedKit(presets[num].getLeft(), pos, presets[num].getRight());
        }
    }

    private static void loadKeysDoors(int levelNum) {
        System.out.println("\tKeys and Doors...");

        int materialsCount = MaterialBank.BANK.size();

        JsonObject json = null;
        try {
            json = JsonParser.parseReader(
                new FileReader("resources/maps/" + levelNum + "/keys_doors.json")
            ).getAsJsonObject();
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        Gson gson = new Gson();

        // грузим текстуры
        System.out.println("\t\tTextures and materials...");
        loadTexturesAndMaterials(json);

        JsonArray keysArray = json.get("keys_config").getAsJsonArray();
        Material[] keysMat = new Material[keysArray.size()];
        for (int i = 0; i < keysArray.size(); ++i) {
            JsonObject jsonKey = keysArray.get(i).getAsJsonObject();
            int matNum = jsonKey.get("material").getAsInt();
            if (matNum >= 0) {
                keysMat[i] = MaterialBank.BANK.get(materialsCount + matNum);
            }
        }

        System.out.println("\t\tKeys placing...");
        JsonArray keysMap = json.get("keys_map").getAsJsonArray();
        for (int i = 0; i < keysMap.size(); ++i) {
            JsonObject jsonValue = keysMap.get(i).getAsJsonObject();
            int keyNum = jsonValue.get("key").getAsInt();

            float[] jsonPos = gson.fromJson(jsonValue.get("position"), float[].class);
            Vector3 pos = new Vector3(jsonPos);

            new Key(keysMat[keyNum], pos, keyNum);
        }


        JsonArray doorArray = json.get("doors_config").getAsJsonArray();
        ImmutablePair<Material, Integer>[] doorsInfo = new ImmutablePair[doorArray.size()];
        for (int i = 0; i < doorArray.size(); ++i) {
            JsonObject jsonDoor = doorArray.get(i).getAsJsonObject();
            int matNum = materialsCount + jsonDoor.get("material").getAsInt();
            Material mat = MaterialBank.BANK.get(matNum);

            int keyNum = -1;
            if (jsonDoor.has("key")) {
                keyNum = jsonDoor.get("key").getAsInt();
            }

            doorsInfo[i] = new ImmutablePair<>(mat, keyNum);
        }

        System.out.println("\t\tDoors placing...");
        for (int x = 0; x < WIDTH; ++x) {
            for (int y = 0; y < HEIGHT; ++y) {
                DOORS[x][y] = -1;
            }
        }

        JsonArray doorsMap = json.get("doors_map").getAsJsonArray();
        for (int i = 0; i < doorsMap.size(); ++i) {
            JsonObject jsonDoor = doorsMap.get(i).getAsJsonObject();
            int doorNum = jsonDoor.get("door").getAsInt();
            JsonArray doorPos = jsonDoor.get("position").getAsJsonArray();
            int x = doorPos.get(0).getAsInt(),
                y = doorPos.get(1).getAsInt();

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

    private static void loadBreakables(int levelNum) {
        System.out.println("\nBreakables...");

        int materialsCount = MaterialBank.BANK.size();

        JsonObject json = null;
        try {
            json = JsonParser.parseReader(
                new FileReader("resources/maps/" + levelNum + "/breakables.json")
            ).getAsJsonObject();
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        Gson gson = new Gson();

        System.out.println("\t\tTextures and materials...");
        loadTexturesAndMaterials(json);

        System.out.println("\t\tSounds...");
        JsonArray sndArray = json.get("sounds").getAsJsonArray();
        // создаем и запоминаем локальные буферы
        SoundBuffer[] soundBuffers = new SoundBuffer[sndArray.size()];
        for (int s = 0; s < soundBuffers.length; ++s) {
            String path = sndArray.get(s).getAsString();
            soundBuffers[s] = AudioSystem.newSoundBuffer(path);
        }
        mapSoundBuffers.addAll(Arrays.asList(soundBuffers));

        JsonArray breakablesArray = json.get("config").getAsJsonArray();
        List<Object>[] presets = new ArrayList[breakablesArray.size()];
        for (int i = 0; i < breakablesArray.size(); ++i) {
            JsonObject jsonValue = breakablesArray.get(i).getAsJsonObject();
            presets[i] = new ArrayList<>();

            int idleNum = jsonValue.get("idle").getAsInt();
            presets[i].add(MaterialBank.BANK.get(materialsCount + idleNum));

            int painNum = jsonValue.get("pain").getAsInt();
            presets[i].add(MaterialBank.BANK.get(materialsCount + painNum));

            int dieNum = jsonValue.get("die").getAsInt();
            presets[i].add(MaterialBank.BANK.get(materialsCount + dieNum));

            int deadNum = jsonValue.get("dead").getAsInt();
            presets[i].add(MaterialBank.BANK.get(materialsCount + deadNum));

            double health = jsonValue.get("health").getAsDouble();
            presets[i].add(health);

            boolean live = jsonValue.get("live").getAsBoolean();
            presets[i].add(live);

            double radius = jsonValue.get("radius").getAsDouble();
            presets[i].add(radius);

            // сформируем массив буфферов звуков боли для конкретного типа Breakable
            int[] painSoundsIdx = gson.fromJson(jsonValue.get("pain sounds"), int[].class);
            SoundBuffer[] painSoundBuffers = new SoundBuffer[painSoundsIdx.length];
            for (int p = 0; p < painSoundsIdx.length; ++p) {
                int idx = painSoundsIdx[p];
                painSoundBuffers[p] = soundBuffers[idx];
            }
            presets[i].add(painSoundBuffers);

            // сформируем массив буфферов звуков смерти для конкретного типа Breakable
            int[] dieSoundsIdx = gson.fromJson(jsonValue.get("die sounds"), int[].class);
            SoundBuffer[] dieSoundBuffers = new SoundBuffer[dieSoundsIdx.length];
            for (int p = 0; p < dieSoundsIdx.length; ++p) {
                int idx = dieSoundsIdx[p];
                dieSoundBuffers[p] = soundBuffers[idx];
            }
            presets[i].add(dieSoundBuffers);
        }

        JsonArray breakablesMap = json.get("map").getAsJsonArray();
        for (int i = 0; i < breakablesMap.size(); ++i) {
            JsonObject jsonValue = breakablesMap.get(i).getAsJsonObject();
            int num = jsonValue.get("breakable").getAsInt();
            List<Object> preset = presets[num];

            Material idle = (Material) preset.get(0);
            Material pain = (Material) preset.get(1);
            Material die  = (Material) preset.get(2);
            Material dead = (Material) preset.get(3);
            double health = (Double)   preset.get(4);
            boolean live  = (Boolean)  preset.get(5);
            double radius = (Double)   preset.get(6);
            SoundBuffer[] painSoundBuffers = (SoundBuffer[]) preset.get(7);
            SoundBuffer[] dieSoundBuffers  = (SoundBuffer[]) preset.get(8);

            Sound[] painSounds = new Sound[painSoundBuffers.length];
            for (int p = 0; p < painSounds.length; ++p) {
                painSounds[p] = AudioSystem.newSound(painSoundBuffers[p]);
            }

            Sound[] dieSounds = new Sound[dieSoundBuffers.length];
            for (int p = 0; p < dieSounds.length; ++p) {
                dieSounds[p] = AudioSystem.newSound(dieSoundBuffers[p]);
            }

            float[] jsonPos = gson.fromJson(jsonValue.get("position"), float[].class);
            Vector3 pos = new Vector3(jsonPos);

            double direction = jsonValue.get("direction").getAsDouble();

            Breakable breakable = new Breakable(idle, pain, die, dead, pos, direction, health, radius);
            breakable.setLive(live);
            breakable.setPainSounds(painSounds);
            breakable.setDieSounds(dieSounds);
        }
    }

    /*
    private static void loadMeshesAndModels(int levelNum) {
        System.out.println("\tMeshes...");

        Json json = new Json();
        JsonValue jsonLevel = new JsonReader().parse(Gdx.files.internal("resources/maps/" + levelNum + "/models.json"));

        List<Mesh> meshes = new ArrayList<>();
        // читаем инфу по мешам
        ArrayList<JsonValue> meshesArray = json.readValue(ArrayList.class, jsonLevel.get("meshes"));
        for (JsonValue jsonMesh : meshesArray) {
            // собираем вершины
            List<Vector3> verticies = new ArrayList<>();
            ArrayList<Array<Float>> jsonVerticies = json.readValue(ArrayList.class, jsonMesh.get("verticies"));
            for(Array<Float> jsonVertex : jsonVerticies) {
                verticies.add(new Vector3(jsonVertex.get(0), jsonVertex.get(1), jsonVertex.get(2)));
            }

            // собираем вершины
            List<Vector3> normals = new ArrayList<>();
            ArrayList<Array<Float>> jsonNormals = json.readValue(ArrayList.class, jsonMesh.get("normals"));
            for(Array<Float> jsonNormal : jsonNormals) {
                normals.add(new Vector3(jsonNormal.get(0), jsonNormal.get(1), jsonNormal.get(2)));
            }

            // собираем полигоны
            List<Polygon> polys = new ArrayList<>();
            ArrayList<JsonValue> jsonFaces = json.readValue(ArrayList.class, jsonMesh.get("faces"));
            for(JsonValue jsonFace : jsonFaces) {
                int idxNormal = jsonFace.getInt("normal");
                int[] idxVertex = jsonFace.get("verticies").asIntArray();

                Vector3[] verts = new Vector3[3];
                verts[0] = verticies.get(idxVertex[0]);
                verts[1] = verticies.get(idxVertex[1]);
                verts[2] = verticies.get(idxVertex[2]);

                Vector3 normal = normals.get(idxNormal);

                polys.add(new Polygon(verts, normal));
            }

            // создаем меш
            meshes.add(new Mesh(polys));
        }

        System.out.println("\tModels placing...");
        // читаем инфу по моделям и создаём
        ArrayList<JsonValue> modelsArray = json.readValue(ArrayList.class, jsonLevel.get("models"));
        for (JsonValue jsonModel : modelsArray) {
            int idxMesh = jsonModel.getInt("mesh");

            float[] jsonPos = jsonModel.get("position").asFloatArray();
            Vector3 pos = new Vector3(jsonPos[0], jsonPos[1], jsonPos[2]);

            new Model(meshes.get(idxMesh), pos);
        }
    }*/

    public static boolean isActive() {
        return active;
    }

    public static boolean isLoaded() {
        return loaded;
    }

    public static void setLoaded(boolean loaded) {
        Map.loaded = loaded;
    }

    public static void setActivated(boolean active) {
        Map.active = active;
    }

    public static void reset() {
        active = false;
        loaded = false;
        //Config.draw = false;

        AudioSystem.disposeAllMusics();
        AudioSystem.disposeAllSoundSources();
        for (SoundBuffer buffer : mapSoundBuffers) {
            buffer.dispose();
        }
        mapSoundBuffers.clear();

        GameObject.destroyAll();

        //LightSource.deleteAll();
        //Player.deleteInstance();
        //Mortal.deleteAll();
        Door.deleteAll();
        //Sprite.deleteAll();
        //Material.deleteAll();
        TextureBank.deleteWalls();
        //MaterialBank.deleteBank();
        //Mesh.deleteAll();
        //Model.deleteAll();


        System.gc();
    }

    public static void load(int levelNum) {
        reset();
        //Config.draw = false;

        System.out.println("Loading map...");

        //Interpreter.clearListing();
        //Interpreter.addListing(new File("resources/maps/loader.js"));

        File config = new File("resources/maps/" + levelNum + "/map.json");
        if (config.exists()) {
            try {
                JsonObject jsonlevel = JsonParser.parseReader(new FileReader(config)).getAsJsonObject();
                // получаем общие настройки
                if (jsonlevel.has("name")) {
                    String name = jsonlevel.get("name").getAsString(); // пока бесполезно
                }
                if (jsonlevel.has("author")) {
                    String author = jsonlevel.get("author").getAsString(); // пока бесполезно
                }

                Gson gson = new Gson();

//              String fogColorString = (jsonlevel.has("fog_color")) ? jsonlevel.get("fog_color").getAsString() : "0xFF";
//              Fog.COLOR = Integer.parseUnsignedInt(fogColorString, 16);
                Fog.START = (jsonlevel.has("fog_start")) ? jsonlevel.get("fog_start").getAsDouble() : 2.0;
                Fog.END   = (jsonlevel.has("fog_end"))   ? jsonlevel.get("fog_end").getAsDouble()   : 8.0;
                Fog.init();

                /* убираем метку твердости */
                for (int x = 0; x < WIDTH; ++x) {
                    for (int y = 0; y < HEIGHT; ++y) {
                        SOLIDS[x][y] = false;
                    }
                }
                /* грузим текстуры */
                System.out.println("\tTextures and materials...");
                loadTexturesAndMaterials(jsonlevel);

                /* формируем материалы стен */
                System.out.println("\tWalls materials...");
                JsonArray sidesArray = jsonlevel.get("wall_sides_materials").getAsJsonArray();
                WallMaterialBank.BANK = new WallMaterial[sidesArray.size()];
                for (int i = 0; i < sidesArray.size(); ++i) {
                    JsonObject mat = sidesArray.get(i).getAsJsonObject();
                    int[] jsonSides = gson.fromJson(mat.get("materials"), int[].class);
                    Material[] sides = new Material[jsonSides.length];
                    for (int j = 0; j < sides.length; ++j) {
                        sides[j] = MaterialBank.BANK.get(jsonSides[j]);
                    }

                    WallMaterialBank.BANK[i] = new WallMaterial(sides);
                }

                /* грузим расположение стен */
                System.out.println("\tWalls...");
                int[][] wallsArray = gson.fromJson(jsonlevel.get("walls"), int[][].class);
                for (int level = 0; level < LEVELS_COUNT; ++level) {
                    int[] map = wallsArray[level];
                    for (int i = 0; i < map.length; ++i) {
                        int x = i / HEIGHT;
                        int y = i % WIDTH;
                        int cell = map[i];
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
                int[] floor = gson.fromJson(jsonlevel.get("floor"), int[].class);
                for (int i = 0; i < floor.length; ++i) {
                    int x = i / HEIGHT;
                    int y = i % WIDTH;
                    FLOOR[x][y] = floor[i];
                }

                /* грузим потолок */
                System.out.println("\tCeil...");
                int[] ceil = gson.fromJson(jsonlevel.get("ceil"), int[].class);
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

                /* грузим меши и модели */
                //loadMeshesAndModels(levelNum);

                //Interpreter.runListing();

                System.out.println("\tWeapons in player's hand...");
                //Sprite.loadAll();
                /*
                KnifeInHand.loadFrames();
                PistolInHand.loadFrames();
                ShotgunInHand.loadFrames();
                RifleInHand.loadFrames();
                MinigunInHand.loadFrames();
                RocketLauncherInHand.loadFrames();
                */
                AmmoPack.reset();
                //Player.getInstance().createWeaponsInHand();

                /* грузим разрушаемое */
                loadBreakables(levelNum);

                System.out.println("\tEnemies...");
                new Guard(new Vector3(15.5, 21.5, 0.0), 90);

                new Guard(new Vector3(15.5, 19.5, 0.0));

                new Guard(new Vector3(15.5, 20.5, 0.0));

                new Guard(new Vector3(14.5, 20.5, 0.0));

                new GuardRocketeer(new Vector3(12.5, 18.5, 0.0));


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

                light1 = new LightSource();
                light2 = new LightSource();

                // lighters
                new LightSource(Color.WHITE, 1.5, new Vector3(10.5, 14.5, 0.0));
                new LightSource(Color.WHITE, 1.5, new Vector3( 8.5, 17.5, 0.0));
                new LightSource(Color.WHITE, 1.5, new Vector3( 6.5, 13.5, 0.0));
                new LightSource(Color.WHITE, 1.5, new Vector3( 8.5, 13.5, 0.0));

                // fire
                new LightSource(Color.WHITE, 1.0, new Vector3(10.5,  4.5, 0.0));

                //SoundBank.MUSIC1.setLooping(true).play();
                AudioSystem.newMusic(SoundBank.PATH_MUSIC1).setLooping(true).play();

                active = true;
                loaded = true;
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }

        System.gc();
    }

    /*
    public static void save() {
        File savesDir = new File("saves");
        if (!savesDir.exists() || !savesDir.isDirectory()) {
            savesDir.mkdir();
        }

        SavedGame game = new SavedGame();
        game.saveToFile("saves/game.gam");
    }*/
}
