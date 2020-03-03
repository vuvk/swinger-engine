/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.res;

import com.badlogic.gdx.utils.Array;
import com.vuvk.swinger.graphic.Fog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author tai-prg3
 */
public final class TextureBank {
    public final static String PICS_FOLDER = "resources/pics/";    
    
    public static Texture   SKY;
    public static final List<Texture> WALLS = new ArrayList<>()/*   = new Texture[11]*/;/*
    public final static Texture[] FLOOR   = new Texture[6];
    public final static Texture[] CEIL    = new Texture[7];*/
    //public final static Texture[] SPRITES = new Texture[50];
    //public final static Texture[] DOORS   = new Texture[3];
    
    // для анимации массив двумерный, где x - кадры, а y - текстуры для углов поворота (1 или 8 штук)
    public final static Texture[][] GUARD_ATK   = new Texture[3][1];
    public final static Texture[][] GUARD_STAND = new Texture[1][8];
    public final static Texture[][] GUARD_WALK  = new Texture[4][8];
    public final static Texture[][] GUARD_PAIN  = new Texture[2][1];
    public final static Texture[][] GUARD_DIE   = new Texture[3][1];
    public static Texture     GUARD_DEAD;
    
    public final static Texture[][] ROCKET = new Texture[1][8];
    
    public final static Texture[] EXPLODE = new Texture[3];
    public final static Texture[] BLOOD = new Texture[3];
    public final static Texture[] PUFF = new Texture[4];
    public final static Texture[] SMOKE = new Texture[16];
    
    public static com.badlogic.gdx.graphics.Texture MAIN_MENU;
    
    public static void load() {
        SKY = new Texture(PICS_FOLDER + "sky.png");
        /*
        WALLS[0] = new Texture(PICS_FOLDER + "eagle.png");
        WALLS[1] = new Texture(PICS_FOLDER + "redbrick.png");
        WALLS[2] = new Texture(PICS_FOLDER + "purplestone.png");
        WALLS[3] = new Texture(PICS_FOLDER + "greystone.png");
        WALLS[4] = new Texture(PICS_FOLDER + "bluestone.png");
        WALLS[5] = new Texture(PICS_FOLDER + "mossy.png");
        WALLS[6] = new Texture(PICS_FOLDER + "wood.png");
        WALLS[7] = new Texture(PICS_FOLDER + "colorstone.png");
        WALLS[8] = new Texture(PICS_FOLDER + "iron_bars.png");
        WALLS[9]  = new Texture(PICS_FOLDER + "world/computer0.png");
        WALLS[10] = new Texture(PICS_FOLDER + "world/computer1.png");
        */
        /*
        FLOOR[0] = new Texture(PICS_FOLDER + "Ceiling1.png");   
        FLOOR[1] = new Texture(PICS_FOLDER + "Ceiling2.png");  
        FLOOR[2] = new Texture(PICS_FOLDER + "StoneFloor.png");  
        FLOOR[3] = new Texture(PICS_FOLDER + "Tiles1.png");  
        FLOOR[4] = new Texture(PICS_FOLDER + "Tiles2.png");       
        FLOOR[5] = new Texture(PICS_FOLDER + "Grass.png");    
        
        CEIL[0] = new Texture(PICS_FOLDER + "Ceiling1.png");   
        CEIL[1] = new Texture(PICS_FOLDER + "Ceiling2.png");  
        CEIL[2] = new Texture(PICS_FOLDER + "StoneFloor.png");  
        CEIL[3] = new Texture(PICS_FOLDER + "Tiles1.png");  
        CEIL[4] = new Texture(PICS_FOLDER + "Tiles2.png");       
        CEIL[5] = new Texture(PICS_FOLDER + "Grass.png");      
        CEIL[6] = new Texture(PICS_FOLDER + "iron_bars.png"); 
        */
        /*
        SPRITES[0] = new Texture(PICS_FOLDER + "barrel.png");
        SPRITES[1] = new Texture(PICS_FOLDER + "pillar.png");
        SPRITES[2] = new Texture(PICS_FOLDER + "greenlight.png");
        SPRITES[3] = new Texture(PICS_FOLDER + "mage.png");
        SPRITES[4] = new Texture(PICS_FOLDER + "Turrel1_1.png");
        SPRITES[5] = new Texture(PICS_FOLDER + "spears.png");
        SPRITES[6] = new Texture(PICS_FOLDER + "corpse.png");
        SPRITES[7] = new Texture(PICS_FOLDER + "corpse0.png");
        SPRITES[8] = new Texture(PICS_FOLDER + "blood.png");
        SPRITES[9] = new Texture(PICS_FOLDER + "skeleton.png");
        */
        //guard
        //attack
        /*GUARD_ATK[0][0] = new Texture(PICS_FOLDER + "guard/SPR_GRD_SHOOT1.png"); 
        GUARD_ATK[1][0] = new Texture(PICS_FOLDER + "guard/SPR_GRD_SHOOT2.png"); 
        GUARD_ATK[2][0] = new Texture(PICS_FOLDER + "guard/SPR_GRD_SHOOT3.png"); */
        GUARD_ATK[0][0] = new Texture(PICS_FOLDER + "enemies/guard/gard_shoot1.png"); 
        GUARD_ATK[1][0] = new Texture(PICS_FOLDER + "enemies/guard/gard_shoot2.png"); 
        GUARD_ATK[2][0] = new Texture(PICS_FOLDER + "enemies/guard/gard_shoot3.png");
        
        //idle
        /*GUARD_STAND[0][0] = new Texture(PICS_FOLDER + "guard/SPR_GRD_S_1.png");
        GUARD_STAND[0][1] = new Texture(PICS_FOLDER + "guard/SPR_GRD_S_2.png");
        GUARD_STAND[0][2] = new Texture(PICS_FOLDER + "guard/SPR_GRD_S_3.png");
        GUARD_STAND[0][3] = new Texture(PICS_FOLDER + "guard/SPR_GRD_S_4.png");
        GUARD_STAND[0][4] = new Texture(PICS_FOLDER + "guard/SPR_GRD_S_5.png");
        GUARD_STAND[0][5] = new Texture(PICS_FOLDER + "guard/SPR_GRD_S_6.png");
        GUARD_STAND[0][6] = new Texture(PICS_FOLDER + "guard/SPR_GRD_S_7.png");
        GUARD_STAND[0][7] = new Texture(PICS_FOLDER + "guard/SPR_GRD_S_8.png");*/
        GUARD_STAND[0][0] = new Texture(PICS_FOLDER + "enemies/guard/garda1.png");
        GUARD_STAND[0][1] = new Texture(PICS_FOLDER + "enemies/guard/garda2.png");
        GUARD_STAND[0][2] = new Texture(PICS_FOLDER + "enemies/guard/garda3.png");
        GUARD_STAND[0][3] = new Texture(PICS_FOLDER + "enemies/guard/garda4.png");
        GUARD_STAND[0][4] = new Texture(PICS_FOLDER + "enemies/guard/garda5.png");
        GUARD_STAND[0][5] = new Texture(PICS_FOLDER + "enemies/guard/garda6.png");
        GUARD_STAND[0][6] = new Texture(PICS_FOLDER + "enemies/guard/garda7.png");
        GUARD_STAND[0][7] = new Texture(PICS_FOLDER + "enemies/guard/garda8.png");
        //walk0
        /*GUARD_WALK[0][0] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W1_1.png");
        GUARD_WALK[0][1] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W1_2.png");
        GUARD_WALK[0][2] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W1_3.png");
        GUARD_WALK[0][3] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W1_4.png");
        GUARD_WALK[0][4] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W1_5.png");
        GUARD_WALK[0][5] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W1_6.png");
        GUARD_WALK[0][6] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W1_7.png");
        GUARD_WALK[0][7] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W1_8.png");*/
        GUARD_WALK[0][0] = new Texture(PICS_FOLDER + "enemies/guard/gardb1.png");
        GUARD_WALK[0][1] = new Texture(PICS_FOLDER + "enemies/guard/gardb2.png");
        GUARD_WALK[0][2] = new Texture(PICS_FOLDER + "enemies/guard/gardb3.png");
        GUARD_WALK[0][3] = new Texture(PICS_FOLDER + "enemies/guard/gardb4.png");
        GUARD_WALK[0][4] = new Texture(PICS_FOLDER + "enemies/guard/gardb5.png");
        GUARD_WALK[0][5] = new Texture(PICS_FOLDER + "enemies/guard/gardb6.png");
        GUARD_WALK[0][6] = new Texture(PICS_FOLDER + "enemies/guard/gardb7.png");
        GUARD_WALK[0][7] = new Texture(PICS_FOLDER + "enemies/guard/gardb8.png");
        //walk1
        /*GUARD_WALK[1][0] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W2_1.png");
        GUARD_WALK[1][1] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W2_2.png");
        GUARD_WALK[1][2] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W2_3.png");
        GUARD_WALK[1][3] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W2_4.png");
        GUARD_WALK[1][4] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W2_5.png");
        GUARD_WALK[1][5] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W2_6.png");
        GUARD_WALK[1][6] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W2_7.png");
        GUARD_WALK[1][7] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W2_8.png");*/
        GUARD_WALK[1][0] = new Texture(PICS_FOLDER + "enemies/guard/gardc1.png");
        GUARD_WALK[1][1] = new Texture(PICS_FOLDER + "enemies/guard/gardc2.png");
        GUARD_WALK[1][2] = new Texture(PICS_FOLDER + "enemies/guard/gardc3.png");
        GUARD_WALK[1][3] = new Texture(PICS_FOLDER + "enemies/guard/gardc4.png");
        GUARD_WALK[1][4] = new Texture(PICS_FOLDER + "enemies/guard/gardc5.png");
        GUARD_WALK[1][5] = new Texture(PICS_FOLDER + "enemies/guard/gardc6.png");
        GUARD_WALK[1][6] = new Texture(PICS_FOLDER + "enemies/guard/gardc7.png");
        GUARD_WALK[1][7] = new Texture(PICS_FOLDER + "enemies/guard/gardc8.png");
        //walk2
        /*GUARD_WALK[2][0] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W3_1.png");
        GUARD_WALK[2][1] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W3_2.png");
        GUARD_WALK[2][2] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W3_3.png");
        GUARD_WALK[2][3] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W3_4.png");
        GUARD_WALK[2][4] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W3_5.png");
        GUARD_WALK[2][5] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W3_6.png");
        GUARD_WALK[2][6] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W3_7.png");
        GUARD_WALK[2][7] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W3_8.png");*/
        GUARD_WALK[2][0] = new Texture(PICS_FOLDER + "enemies/guard/gardd1.png");
        GUARD_WALK[2][1] = new Texture(PICS_FOLDER + "enemies/guard/gardd2.png");
        GUARD_WALK[2][2] = new Texture(PICS_FOLDER + "enemies/guard/gardd3.png");
        GUARD_WALK[2][3] = new Texture(PICS_FOLDER + "enemies/guard/gardd4.png");
        GUARD_WALK[2][4] = new Texture(PICS_FOLDER + "enemies/guard/gardd5.png");
        GUARD_WALK[2][5] = new Texture(PICS_FOLDER + "enemies/guard/gardd6.png");
        GUARD_WALK[2][6] = new Texture(PICS_FOLDER + "enemies/guard/gardd7.png");
        GUARD_WALK[2][7] = new Texture(PICS_FOLDER + "enemies/guard/gardd8.png");
        //walk3
        /*GUARD_WALK[3][0] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W4_1.png");
        GUARD_WALK[3][1] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W4_2.png");
        GUARD_WALK[3][2] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W4_3.png");
        GUARD_WALK[3][3] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W4_4.png");
        GUARD_WALK[3][4] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W4_5.png");
        GUARD_WALK[3][5] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W4_6.png");
        GUARD_WALK[3][6] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W4_7.png");
        GUARD_WALK[3][7] = new Texture(PICS_FOLDER + "guard/SPR_GRD_W4_8.png"); */  
        GUARD_WALK[3][0] = new Texture(PICS_FOLDER + "enemies/guard/garde1.png");
        GUARD_WALK[3][1] = new Texture(PICS_FOLDER + "enemies/guard/garde2.png");
        GUARD_WALK[3][2] = new Texture(PICS_FOLDER + "enemies/guard/garde3.png");
        GUARD_WALK[3][3] = new Texture(PICS_FOLDER + "enemies/guard/garde4.png");
        GUARD_WALK[3][4] = new Texture(PICS_FOLDER + "enemies/guard/garde5.png");
        GUARD_WALK[3][5] = new Texture(PICS_FOLDER + "enemies/guard/garde6.png");
        GUARD_WALK[3][6] = new Texture(PICS_FOLDER + "enemies/guard/garde7.png");
        GUARD_WALK[3][7] = new Texture(PICS_FOLDER + "enemies/guard/garde8.png");        
        //pain
        /*GUARD_PAIN[0][0] = new Texture(PICS_FOLDER + "guard/SPR_GRD_PAIN_1.png");   
        GUARD_PAIN[1][0] = new Texture(PICS_FOLDER + "guard/SPR_GRD_PAIN_2.png"); */
        GUARD_PAIN[0][0] = new Texture(PICS_FOLDER + "enemies/guard/gard_pain1.png");   
        GUARD_PAIN[1][0] = new Texture(PICS_FOLDER + "enemies/guard/gard_pain2.png"); 
        //die
        /*GUARD_DIE[0][0] = new Texture(PICS_FOLDER + "guard/SPR_GRD_DIE_1.png"); 
        GUARD_DIE[1][0] = new Texture(PICS_FOLDER + "guard/SPR_GRD_DIE_2.png"); 
        GUARD_DIE[2][0] = new Texture(PICS_FOLDER + "guard/SPR_GRD_DIE_3.png"); */
        GUARD_DIE[0][0] = new Texture(PICS_FOLDER + "enemies/guard/gard_die1.png"); 
        GUARD_DIE[1][0] = new Texture(PICS_FOLDER + "enemies/guard/gard_die2.png"); 
        GUARD_DIE[2][0] = new Texture(PICS_FOLDER + "enemies/guard/gard_die3.png"); 
        //dead
        //GUARD_DEAD = new Texture(PICS_FOLDER + "guard/SPR_GRD_DEAD.png"); 
        GUARD_DEAD = new Texture(PICS_FOLDER + "enemies/guard/gard_dead.png"); 
        
        // projectiles
        // rocket
        for (int i = 0; i < 8; ++i) {
            ROCKET[0][i] = new Texture(PICS_FOLDER + "projectiles/rocket" + (i + 1) + ".png"); 
        }
        
        // effects
        // explode
        for (int i = 0; i < EXPLODE.length; ++i) {
            EXPLODE[i] = new Texture(PICS_FOLDER + "effects/explode" + i + ".png"); 
        }
        // blood
        for (int i = 0; i < BLOOD.length; ++i) {
            BLOOD[i] = new Texture(PICS_FOLDER + "effects/blood" + i + ".png"); 
        }
        // puff
        for (int i = 0; i < PUFF.length; ++i) {
            PUFF[i] = new Texture(PICS_FOLDER + "effects/puff" + i + ".png"); 
        }        
        // smoke
        for (int i = 0; i < SMOKE.length; ++i) {
            SMOKE[i] = new Texture(PICS_FOLDER + "effects/smoke/smoke_" + i + ".png"); 
        }        
        
        /*
        DOORS[0] = new Texture(PICS_FOLDER + "Door.png");
        DOORS[1] = new Texture(PICS_FOLDER + "Door2.png");
        DOORS[2] = new Texture(PICS_FOLDER + "door_iron_upper.png");   
        */
        
        MAIN_MENU = new com.badlogic.gdx.graphics.Texture(PICS_FOLDER + "gui/main_menu.png");
    }
    
    public static Texture[] getWalls() {
        Texture[] textures = new Texture[WALLS.size()];
        int i = 0;
        for (Iterator<Texture> it = WALLS.iterator(); it.hasNext(); ) {
            textures[i] = it.next();
            ++i;
        }
        return textures;
    }
    
    private TextureBank() {}
}
