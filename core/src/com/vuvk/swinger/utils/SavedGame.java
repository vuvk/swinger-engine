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
package com.vuvk.swinger.utils;

import com.vuvk.swinger.Config;
import com.vuvk.swinger.Game;
import com.vuvk.swinger.audio.SoundBank;
import com.vuvk.swinger.audio.SoundSystem;
import com.vuvk.swinger.d3.Mesh;
import com.vuvk.swinger.d3.Model;
import com.vuvk.swinger.graphic.Renderer;
import com.vuvk.swinger.graphic.TexturedSegment;
import com.vuvk.swinger.graphic.weapon_in_hand.AmmoPack;
import com.vuvk.swinger.objects.Door;
import com.vuvk.swinger.objects.Sprite;
import com.vuvk.swinger.objects.mortals.Mortal;
import com.vuvk.swinger.objects.mortals.Player;
import com.vuvk.swinger.objects.weapon.AmmoType;
import com.vuvk.swinger.res.Map;
import com.vuvk.swinger.res.Material;
import com.vuvk.swinger.res.MaterialBank;
import com.vuvk.swinger.res.Texture;
import com.vuvk.swinger.res.TextureBank;
import com.vuvk.swinger.res.WallMaterial;
import com.vuvk.swinger.res.WallMaterialBank;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class SavedGame implements Serializable {

    private static final Logger LOG = Logger.getLogger(SavedGame.class.getName());

    private static final long serialVersionUID = 1L;

    public Texture[]  textureWalls;
    public Material[] materialsLib;
    public Material[] materialsBank;
    public Sprite[]   spritesLib;
    public Mortal[]   mortalsLib;
    public Door[]     doorsLib;
    public Mesh[]     meshesLib;
    public Model[]    modelsLib;
    public WallMaterial[] wallMaterialsBank;

    public boolean[][] mapSolids;
    public boolean[][] mapVisibleCells;
    public int[][][] mapWallsMap;
    public int[][]   mapDoors;
    public int[][]   mapFloor;
    public int[][]   mapCeil;
    public TexturedSegment[][] mapSegments;
    public WallMaterial[][][]  mapWallsMaterialsMap;

    public java.util.Map<AmmoType, Integer> ammoPack;


    public SavedGame() {
        textureWalls  = TextureBank.getWalls();
        materialsBank = MaterialBank.getBank();
        materialsLib  = Material.getLib();
        spritesLib    = Sprite.getLib();
        mortalsLib    = Mortal.getLib();
        doorsLib      = Door.getLib();
        meshesLib     = Mesh.getLib();
        modelsLib     = Model.getLib();
        wallMaterialsBank = WallMaterialBank.BANK;

        mapSolids   = Map.SOLIDS;
        mapWallsMap = Map.WALLS_MAP;
        mapDoors    = Map.DOORS;
        mapSegments = Map.SEGMENTS;
        mapFloor    = Map.FLOOR;
        mapCeil     = Map.CEIL;
        mapVisibleCells = Map.VISIBLE_CELLS;
        mapWallsMaterialsMap = Map.WALLS_MATERIALS_MAP;

        ammoPack = AmmoPack.PACK;
    }

    public void saveToFile(String path) {
        Map.active  = false;
        Config.draw = false;

        try (FileOutputStream fileOutputStream = new FileOutputStream(path)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
        } catch (IOException ex) {
           LOG.log(Level.SEVERE, null, ex);
        } finally {
        }

        Map.active  = true;
        Config.draw = true;
    }

    public void loadFromFile(String path) {
        Config.draw = false;
        Map.reset();
        Renderer.getInstance().stopRenderTasks();

        SavedGame game = null;
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            game = (SavedGame) objectInputStream.readObject();

            TextureBank.WALLS.clear();
            TextureBank.WALLS.addAll(Arrays.asList(game.textureWalls));

            MaterialBank.BANK.clear();
            MaterialBank.BANK.addAll(Arrays.asList(game.materialsBank));

            Material.deleteAll();
            Material.LIB.addAll(Arrays.asList(game.materialsLib));

            WallMaterialBank.BANK = game.wallMaterialsBank;

            Sprite.deleteAll();
            Sprite.LIB.addAll(Arrays.asList(game.spritesLib));

            Mortal.deleteAll();
            for (Mortal mortal : game.mortalsLib) {
                if (mortal instanceof Player) {
                    Player.setInstance((Player) mortal);
                    Player.getInstance().initWeaponsInHand();
                }
                Mortal.LIB.add(mortal);
            }

            Door.deleteAll();
            Door.LIB.addAll(Arrays.asList(game.doorsLib));

            Mesh.deleteAll();
            Mesh.LIB.addAll(Arrays.asList(game.meshesLib));

            Model.deleteAll();
            Model.LIB.addAll(Arrays.asList(game.modelsLib));

            for (int x = 0; x < Map.WIDTH; ++x) {
                for (int y = 0; y < Map.HEIGHT; ++y) {
                    Map.SOLIDS[x][y]   = game.mapSolids[x][y];
                    Map.DOORS[x][y]    = game.mapDoors[x][y];
                    Map.SEGMENTS[x][y] = game.mapSegments[x][y];
                    Map.FLOOR[x][y]    = game.mapFloor[x][y];
                    Map.CEIL[x][y]     = game.mapCeil[x][y];
                    Map.VISIBLE_CELLS[x][y] = game.mapVisibleCells[x][y];
                }
            }

            for (int lvl = 0; lvl < Map.LEVELS_COUNT; ++lvl) {
                for (int x = 0; x < Map.WIDTH; ++x) {
                    for (int y = 0; y < Map.HEIGHT; ++y) {
                        Map.WALLS_MAP[lvl][x][y] = game.mapWallsMap[lvl][x][y];
                        Map.WALLS_MATERIALS_MAP[lvl][x][y] = game.mapWallsMaterialsMap[lvl][x][y];
                    }
                }
            }

            AmmoPack.PACK.clear();
            AmmoPack.PACK.putAll(game.ammoPack);

            Map.active = true;
            Config.draw = true;
        } catch (ClassNotFoundException | IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    public static void save(String name) {
        if (Player.getInstance().getHealth() > 0.0) {
            File savesDir = new File("saves");
            if (!savesDir.exists() || !savesDir.isDirectory()) {
                savesDir.mkdir();
            }

            try {
                new SavedGame().saveToFile("saves/" + name);
                Game.screenMsg.setMessage("GAME SAVED");
            } catch (Exception ex) {
                Game.screenMsg.setMessage("GAME NOT SAVED");
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void load(String name) {
        if (!Files.exists(Paths.get("saves/" + name))) {
            return;
        }

        try {
            new SavedGame().loadFromFile("saves/" + name);
            Game.screenMsg.setMessage("GAME LOADED");
        } catch (Exception ex) {
            Game.screenMsg.setMessage("GAME NOT LOADED");
            LOG.log(Level.SEVERE, null, ex);
        }

        SoundSystem.playMusic(SoundBank.FILE_MUSIC1);
        Map.setLoaded(true);
    }
}
