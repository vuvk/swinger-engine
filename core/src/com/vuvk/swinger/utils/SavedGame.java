package com.vuvk.swinger.utils;

import com.vuvk.swinger.Config;
import com.vuvk.swinger.d3.Mesh;
import com.vuvk.swinger.d3.Model;
import com.vuvk.swinger.graphic.TexturedSegment;
import com.vuvk.swinger.objects.Door;
import com.vuvk.swinger.objects.Sprite;
import com.vuvk.swinger.objects.creatures.Creature;
import com.vuvk.swinger.objects.creatures.Player;
import com.vuvk.swinger.res.Map;
import com.vuvk.swinger.res.Material;
import com.vuvk.swinger.res.MaterialBank;
import com.vuvk.swinger.res.Texture;
import com.vuvk.swinger.res.TextureBank;
import com.vuvk.swinger.res.WallMaterial;
import com.vuvk.swinger.res.WallMaterialBank;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tai-prg3
 */
public class SavedGame implements Serializable {
    
    private static final long serialVersionUID = 1L;
   
    public Texture[]  textureWalls;  
    public Material[] materialsLib;
    public Material[] materialsBank;
    public Sprite[]   spritesLib;
    public Creature[] creaturesLib;
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
    
    
    public SavedGame() {
        textureWalls  = TextureBank.getWalls();   
        materialsBank = MaterialBank.getBank(); 
        materialsLib  = Material.getLib();
        spritesLib    = Sprite.getLib(); 
        creaturesLib  = Creature.getLib();
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
    }
    
    public void saveToFile(String path) {
        Map.active  = false;
        Config.draw = false; 
                
        try (FileOutputStream fileOutputStream = new FileOutputStream(path)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);  
            objectOutputStream.close();          
        } catch (IOException ex) {
            Logger.getLogger(SavedGame.class.getName()).log(Level.SEVERE, null, ex);
        } finally {            
        }
        
        Map.active  = true;
        Config.draw = true; 
    }
    
    public void loadFromFile(String path) {
        SavedGame game = null;
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            game = (SavedGame) objectInputStream.readObject();   
                    
            Map.reset(); 

            TextureBank.WALLS.clear();
            for (Texture txr : game.textureWalls) {
                TextureBank.WALLS.add(txr);
            }

            MaterialBank.BANK.clear();
            for (Material mat : game.materialsBank) {
                MaterialBank.BANK.add(mat);
            }

            Material.deleteAll();
            for (Material mat : game.materialsLib) {
                Material.LIB.add(mat);
            }

            WallMaterialBank.BANK = game.wallMaterialsBank;

            Sprite.deleteAll();
            for (Sprite spr : game.spritesLib) {
                Sprite.LIB.add(spr);
            }

            Creature.deleteAll();
            for (Creature creature : game.creaturesLib) {
                if (creature instanceof Player) {                            
                    Player.setInstance((Player) creature);
                    Player.getInstance().initWeaponsInHand();
                }
                Creature.LIB.add(creature);                        
            }

            Door.deleteAll();
            for (Door door : game.doorsLib) {
                Door.LIB.add(door);
            }

            Mesh.deleteAll();
            for (Mesh mesh : game.meshesLib) {
                Mesh.LIB.add(mesh);
            }

            Model.deleteAll();
            for (Model model : game.modelsLib) {
                Model.LIB.add(model);
            }

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

            Map.active = true;
            Config.draw = true; 
        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(SavedGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
