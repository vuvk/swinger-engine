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
package com.vuvk.swinger.graphic;

import com.vuvk.swinger.objects.Camera;
import com.vuvk.swinger.res.WallMaterial;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Filter;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.utils.Array;
import com.vuvk.swinger.objects.Sprite;
import com.vuvk.swinger.res.Texture;
import com.vuvk.swinger.res.Map;
import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.math.Vector3;
import com.vuvk.swinger.math.Segment;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
//import javax.swing.JPanel;
import com.vuvk.swinger.Config;
import com.vuvk.swinger.d3.Mesh;
import com.vuvk.swinger.d3.Model;
import com.vuvk.swinger.d3.Polygon;
import com.vuvk.swinger.graphic.weapon_in_hand.WeaponInHand;
import com.vuvk.swinger.math.Matrix4;
import com.vuvk.swinger.math.Vector4;
import com.vuvk.swinger.objects.mortals.Player;
import com.vuvk.swinger.res.Image;
import com.vuvk.swinger.res.MaterialBank;
import com.vuvk.swinger.utils.ArrayUtils;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public final class Renderer/* extends JPanel*/ {
    private final static Logger LOG = Logger.getLogger(Renderer.class.getName());
    private static Renderer instance = null;
    //private final static Player PLAYER = Player.getInstance();
    /*private final static RepaintManager REPAINT_MANAGER;
    static {        
        REPAINT_MANAGER = RepaintManager.currentManager(INSTANCE);
    };*/
    
    public /*final*/ static int WIDTH/*  = Window.WIDTH  >> 2*/;
    public /*final*/ static int HEIGHT/* = Window.HEIGHT >> 2*/;
    public /*final*/ static int HALF_WIDTH/*  = WIDTH  >> 1*/;
    public /*final*/ static int HALF_HEIGHT/* = HEIGHT >> 1*/;
    public /*final*/ static int QUARTER_WIDTH/* = HALF_WIDTH >> 1*/;
    
    private /*final*/ static double RAY_STEP/* = 1.0 / WIDTH*/;
    //private/* final*/ static double ANG_STEP/* = Player.FOV / WIDTH*/;
    
    //private final /*static*/ BufferedImage SCREEN/* = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB)*/;
    private static com.badlogic.gdx.graphics.Texture SCREEN;
    //private/* final*/ static BufferedImage SCREEN_SMALL;
    private final /*static*/ IntBuffer SCREEN_BUFFER/* = new int[WIDTH * HEIGHT]*/;
    private final /*static*/ IntBuffer TEMP_BUFFER/* = new int[WIDTH * HEIGHT]*/;
    //private final WritableRaster SCREEN_RASTER;
    private Pixmap TEMP_RASTER;
    private Pixmap SCREEN_RASTER;
    //private final DataBuffer SCREEN_BUFFER;
    private final /*static*/ double[][] ZBUFFER/* = new double[WIDTH]*/;
    
    /** time of current frame */
//    private long time = System.currentTimeMillis(); 
    /** time of previous frame */
//    private long oldTime = time; 
    /** */
//    private static double deltaTime = 0;
    /** */
/*    private static int fps = 0;
    private int fpsCounter = 0;
    private double fpsTimer = 0;*/
    
    private int evenFactor = 0;
    private int xStep;
    private Camera nextActiveCamera = null;
    private Camera activeCamera = new Camera();
/*    
    private Text fpsText = new Text(FontBank.FONT_BUBBLA, "", new Point(10, 15));
    private Text playerPosText = new Text(FontBank.FONT_BUBBLA, "", new Point(10, 25));
    private Text playerHpText = new Text(FontBank.FONT_MIDDLE, "", new Point());
  */  
/*    
    @Struct
    private class RenderTarget {
        int x, y;
        double wallX;
        int texX = 0;
        int side;
        //double wallDist;
        // draw door or wall?
        boolean drawDoor = false;
        // is door side wall ?
        //boolean doorWall = false;
        Texture texture;
        double wallDist;
    }
*/
    private class RenderTarget {
        int x, y;
        //double wallX;
        int texX = 0;
        //int side;
        //double wallDist;
        // draw wall or segment?
        //boolean drawSegment = false;
        // is door side wall ?
        //boolean doorWall = false;
        Texture texture;
        double wallDist;
        Vector2 collisionPoint = new Vector2();
    }
    
    private class RenderTask implements Runnable {
        CountDownLatch latch;
        final private int fromX, toX;
        
        RenderTask(final String name, int fromX, int toX) {
            this.fromX = fromX;
            this.toX   = toX;
            //setName(name);
        }
        
        public void setLatch(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            renderWorld(fromX, toX);
            latch.countDown();
        }
    }
    
    private class AntialiasingTask implements Runnable {
        CountDownLatch latch;
        final private int fromX, toX;
        
        AntialiasingTask(final String name, int fromX, int toX) {
            this.fromX = fromX;
            this.toX   = toX;
            //setName(name);
        }
        
        public void setLatch(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            antialiasing(fromX, toX);
            latch.countDown();
        }
    }
    
    // ПОТОКИ РЕНДЕРИНГА
    private final /*static*/ RenderTask[] RENDER_TASKS = new RenderTask[Config.THREADS_COUNT];
    private final /*static*/ AntialiasingTask[] ANTIALIASING_TASKS = new AntialiasingTask[Config.THREADS_COUNT];
    //private /*final static*/ Thread[] RENDER_THREADS = new Thread[4];
    private final /*static*/ ExecutorService EXECUTOR = Executors.newFixedThreadPool(Config.THREADS_COUNT);
    //private boolean[] render = {false,false,false,false};
    private final /*static*/ List<Sprite> SPRITES_FOR_DRAW = new ArrayList<>(50);
    private final /*static*/ List<Model> MODELS_FOR_DRAW = new ArrayList<>(50);
    
    private static boolean started = false;     // рендерер запущен
    private boolean canRender = true;           // можно ли рендерить в итоговую пиксельную карту 
    private boolean alreadyRendered = false;    // уже отрендерил в память
    
    
    public static Renderer getInstance() {
        if (instance == null) {
            instance = new Renderer();
            /*instance.setDoubleBuffered(false);
            instance.setIgnoreRepaint(true);
            instance.setOpaque(false);*/
        }
        
        return instance;
    }
/*
    public static double getDeltaTime() {
        return deltaTime;
    }

    public static double getFps() {
        return fps;
    }
*/
    /*
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(SCREEN, 0, 0, Window.WIDTH, Window.HEIGHT, null);
        
        if (!Config.STEP_BY_STEP_RENDERING) {       
            g.setColor(Color.WHITE);
            //g.drawString("FPS: " + fps, 10, 15); //FPS counter
            fpsText.setMessage("FPS: " + fps);
                    
            Player player = Player.getInstance();
            Vector2 pos = player.getPos();
            //g.drawString(String.format(Locale.ENGLISH, "Player pos: %.2f %.2f", pos.x, pos.y), 10, 30);
            playerPosText.setMessage(String.format(Locale.ENGLISH, "PLAYER POS: %.2f %.2f", pos.x, pos.y));
            //g.drawString(String.format(Locale.ENGLISH, "PLAYER HEALTH: %.2f", PLAYER.getHealth()), 10, 90);
            playerHpText.setLocation(new Point(16, Window.HEIGHT - 48));
            playerHpText.setMessage(String.format(Locale.ENGLISH, "HP %.0f", player.getHealth()));
        }
        
        Text.drawAll(g);
        
        // рисуем консоль
        if (Config.console) {
            int y = Window.HEIGHT - 50;
            g.setColor(Color.BLACK);
            g.fillRect(0, y, Window.WIDTH, Window.HEIGHT);
            
            g.setColor(Color.WHITE);
            g.drawString("Enter command: " + Config.consoleCommand, 10, y + 15); //FPS counter            
        }
    } 
    */
    
    /*
    protected void putPixel(int x, int y, int color) {
        int row;
        int step = 1 << Config.quality;
        for (int pY = y; (pY < y + step) && (pY < HEIGHT); ++pY) {
            row = pY * WIDTH;
            for (int pX = x; (pX < x + step) && (pX < WIDTH); ++pX) {
                SCREEN_BUFFER[row + pX] = color;
            }
        }
    }
    */
    
    /**
     * Сделать пиксель темнее
     * @param color цвет пикселя в формате ARGB
     * @param brightness яркость (0.0 - 1.0)
     * @return Цвет результат
     */
    private int makeColorDarker(int color, double brightness) {
        if (brightness < 1.0) {
            int a = (color/* >> 24*/) & 0xFF;
            if (a > 0 && a < 255) {
                a += brightness;
            }
            int r = (int)(((color >> 24) & 0xFF) * brightness);
            int g = (int)(((color >> 16) & 0xFF) * brightness);
            int b = (int)(((color >>  8) & 0xFF) * brightness);
            
            return (a/* << 24*/) | (r << 24) | (g << 16) | (b << 8);
            //return (color & 0xFF000000) | (r << 16) | (g << 8) | b;
            //return (0xFF000000) | (r << 16) | (g << 8) | b;
        }
        return color;
    }
    
    /* минимальное видмое значение полупрозрачности */
    final static int MIN_VISIBLE_TRANSPARENT  = 25; 
    /* значение полупрозрачности, выше которого уже 100% непрозрачность */
    final static int MAX_ADDITIVE_TRANSPARENT = 230;
    final static double INV_255 = 1.0 / 255.0;
    /**
     * Наложить на пиксель пиксель с полупрозрачностью
     * @param pixel исходный пиксель
     * @param additive добавляемый пиксель
     * @return то, что получилось в ходе сложения
     */
    private int makeAdditive(int pixel, int additive) {
        /* альфа полупрозрачного пикселя */
        int aA = (additive/* >> 24*/) & 0xFF;
        //int aP = (pixel >> 24) & 0xFF;
                
        /* добавляемый пиксель вообще не виден */
        if (aA <= MIN_VISIBLE_TRANSPARENT) {
            return pixel;
        /* исходный пиксель перекрывается полностью */
        } else if (aA >= MAX_ADDITIVE_TRANSPARENT) {
            return additive;
        /* надо смешивать */
        } else {        
            /* считаем яркость добавляемого и исходного пикселя с учетом накладываемого */
            double aBrightness = aA * INV_255;
            double pBrightness = 1.0 - aBrightness;

            /* раскладываем */        
            int rA = (int)(((additive >> 24) & 0xFF) * aBrightness);
            int gA = (int)(((additive >> 16) & 0xFF) * aBrightness);
            int bA = (int)(((additive >>  8) & 0xFF) * aBrightness);

            int rP = (int)(((pixel >> 24) & 0xFF) * pBrightness);
            int gP = (int)(((pixel >> 16) & 0xFF) * pBrightness);
            int bP = (int)(((pixel >>  8) & 0xFF) * pBrightness);

            /* получаем результат */
            return 0xFF |
                   ((rA + rP) << 24) |
                   ((gA + gP) << 16) |
                   ((bA + bP) <<  8); 
        }
    }
    
    /**
     * Нарисовать на полотне пиксель
     * @param x координата X на полотне
     * @param y координата Y на полотне
     * @param pixel рисуемый пиксель
//     * @param brightness яркость пикселя
     */
    private void drawPixel(int x, int y, int pixel/*, double brightness*/) {
        int arrayPos = y * WIDTH + x;
        int oldPixel = TEMP_BUFFER.get(arrayPos);
        int newPixel = makeAdditive(oldPixel, pixel/*makeColorDarker(pixel, brightness)*/);
        if (oldPixel != newPixel)  {
            TEMP_BUFFER.put(arrayPos, newPixel);
        }
    }
    
    /**
     * Применить туман к пикселю (проверка расстояния)
     * @param color Исходный цвет пикселя в формате ARGB
     * @param distance Дистанция до пикселя
     * @return полученный цвет
     */
    private int applyFog(int color, double distance) {
        // близко - вернуть оригинальный пиксель
        if (distance < Fog.START) {
            return color;
        // дистанция между началом и концом тумана
        } else if (distance < Fog.END) {
            if (Config.fog == Fog.OLD) {
                int pos = (int)((distance - Fog.START) * Fog.INV_SIMPLE_DISTANCE_STEP);
                color = makeColorDarker(color, Fog.SIMPLE_BRIGHTNESS[pos]); 
                //color = makeColorDarker(color, Fog.SIMPLE_BRIGHTNESS[(int)(distance - Fog.START)]);      
            } else {
                color = makeColorDarker(color, 1.0 - (distance - Fog.START) * Fog.FACTOR);
            }
            return color;
        }
        // очень далеко - просто черный цвет
        return 0xFF;
    }
    
    /**
     * Избавиться от острых краев, размазав граничные пиксели
     * @param fromX с какой позиции обрабатывать
     * @param toX по какую
     */
    private void antialiasing(int fromX, int toX) {        
        for (int i = fromX; i < toX; ++i) {
            /*if (i % WIDTH == 1) {
                continue;
            }*/
            
            int pixel = TEMP_BUFFER.get(i);
            int next  = TEMP_BUFFER.get(i + 1);
            if (pixel != next) {
                int r = (((pixel >> 24) & 0xFF) + ((next >> 24) & 0xFF)) >> 1, 
                    g = (((pixel >> 16) & 0xFF) + ((next >> 16) & 0xFF)) >> 1, 
                    b = (((pixel >>  8) & 0xFF) + ((next >>  8) & 0xFF)) >> 1;
                TEMP_BUFFER.put(i, 0xFF | 
                                     (r << 24)  | 
                                     (g << 16)  | 
                                     (b <<  8));
                //++i;
            }
        }
    }
    
    /**
     * Задача рендеринга мира (стен и дверей).
     * @param fromX Позиция X с которой начинать
     * @param toX Позиция X до которой рендерить
     */
    private void renderWorld(int fromX, int toX) {   
        if (activeCamera == null) {
            return;
        }
        
        /*Player player = Player.getInstance();
        
        final Vector3 pos    = player.getPos();
        final Vector2 dir    = player.getCamera().getView();
        final Vector2 plane  = player.getCamera().getPlane();*/
        final Vector3 pos    = activeCamera.getPos();
        final Vector2 dir    = activeCamera.getView();
        final Vector2 plane  = activeCamera.getPlane();
        final Vector2 ray    = new Vector2(); 
        final Vector2 invRay = new Vector2();
            
        // length of ray from current position to next x or y-side
        final Vector2 sideDist = new Vector2();
        
        // length of ray from one x or y-side to next x or y-side
        final Vector2 deltaDist = new Vector2();
        double wallDist = 0.0;

        // what direction to step in x or y-direction (either +1 or -1)
        int stepX, stepY;
        
        // which box of the map we're in
        int mapX, mapY;
        
        // coordinates on the texture
        int texX = 0,
            texY = 0;
                            
        double wallX; //where exactly the wall was hit
        
        // draw door or wall?
        //boolean drawDoor;
        // is door side wall ?
        //boolean doorWall; 
        
        //double ang = -Player.FOV_2;
        //int step = 1 << Config.quality;   
        final Array<RenderTarget>[] renderTargets = new Array[Map.LEVELS_COUNT];
        for (int level = 0; level < Map.LEVELS_COUNT; ++level) { 
            renderTargets[level] = new Array<>(false, 50);    
        }
        /*
        RenderTargetList[] renderTargets = new RenderTargetList[Map.LEVELS_COUNT];
        for (int level = 0; level < Map.LEVELS_COUNT; ++level) { 
            renderTargets[level] = new RenderTargetList(Map.WIDTH*Map.HEIGHT);    
        }*/
            
        /*
        int skyX = Map.SKY.getXStart();
        int skyWidth = Map.SKY.getWidth();
        for (int x = evenFactor; x < WIDTH; x += xStep) {      
            int[] pixels = Map.SKY.getColumn(skyX);
            for (int y = 0; y < HEIGHT; ++y) {
                TEMP_BUFFER[y * WIDTH + x] = pixels[y];
            }
            
            skyX -= xStep;
            if (skyX < 0) {
                skyX = skyWidth - 1;
            }   
        }
        */
        //int skyX = fromX + Map.SKY.getXStart();
        //int skyWidth = Map.SKY.getWidth();
        
        for (int x = fromX + evenFactor; x < toX; x += xStep) {
            // сколько пикселей нарисовано в столбце (если меньше высоты, значит есть пустые области под небо)
            int pixelsInColumn = 0;
                    
            mapX = (int)pos.x;
            mapY = (int)pos.y;
            
            // calculate ray position and direction
            final double cameraX = (x << 1) * RAY_STEP - 1; // x-coordinate in camera space
            
            ray.set(dir.x + plane.x * cameraX,
                    dir.y + plane.y * cameraX);
            invRay.set(1.0 / ray.x, 1.0 / ray.y);

            deltaDist.set(Math.abs(invRay.x),
                          Math.abs(invRay.y));

            
            // calculate step and initial sideDist
            if (ray.x < 0.0) {
                stepX = -1;
                sideDist.x = (pos.x - mapX) * deltaDist.x;
            } else {
                stepX = 1;
                sideDist.x = (mapX + 1.0 - pos.x) * deltaDist.x;
            }
      
            if (ray.y < 0.0) {
                stepY = -1;
                sideDist.y = (pos.y - mapY) * deltaDist.y;
            } else {
                stepY = 1;
                sideDist.y = (mapY + 1.0 - pos.y) * deltaDist.y;
            }
            
            //was a NS or a EW wall hit?
            int side = (sideDist.x < sideDist.y) ? 0 : 1;
      
            // perform DDA
            //drawDoor = false;
            //boolean doorWall = false;
                        
            for (Array<RenderTarget> list : renderTargets) {
                list.clear();
            }/*
            for (RenderTargetList list : renderTargets) {
                list.clear();
            }*/
            
            
            // пока луч не долетел до края карты
            while (mapX >= 0 && mapX < Map.WIDTH &&
                   mapY >= 0 && mapY < Map.HEIGHT) {
                
                Map.VISIBLE_CELLS[mapX][mapY] = true;
                
                for (int level = 0; level < Map.LEVELS_COUNT; ++level) {
                    int[][] levelMap = Map.WALLS_MAP[level];
                    WallMaterial[][] wallsMat = Map.WALLS_MATERIALS_MAP[level];
                    boolean visibleWall = false;                    
                    Texture left  = null, 
                            right = null, 
                            front = null,
                            back  = null;
                    int cell = levelMap[mapX][mapY];
                    Texture texture = null;
                    RenderTarget target = null;
                    
                    if (level == 0 && cell < 0) {
                        // проверяем столкновение с сегментом
                        TexturedSegment segment = Map.SEGMENTS[mapX][mapY];

                        // сегмента впереди нет
                        if (segment != null) {
                            Segment viewRay = new Segment(pos, pos.add(ray.mul(Map.WIDTH)));

                            // есть пересечение?
                            Vector2 point = viewRay.intersect(segment);
                            // есть и в точке пересечения не стена (иначе пробивает насквозь)
                            if (point != null && levelMap[(int)point.x][(int)point.y] == -1) {
                                target = new RenderTarget();
                                //target.drawSegment = true;
                                target.texX = (int)(point.distance(segment.getA()) * Texture.WIDTH) % Texture.WIDTH;

                                texture = segment.getTexture();

                                //Vector2 doorPos = point.sub(pos);
                                Vector2 doorPos = point.sub(pos);
                                target.collisionPoint = point;
                                wallDist = (-plane.y * doorPos.x + plane.x * doorPos.y) / (plane.x * dir.y - dir.x * plane.y);
                            }
                        }
                    }
                            
                    if (cell >= 0 && target == null) {
                        Side boxSide;
                        // добавить в очередь только потенциально видимые стены
                        if (side == 0) {
                            if (ray.x > 0) {                // луч летит влево
                                boxSide = Side.LEFT;
                            } else {                        // луч летит вправо
                                boxSide = Side.RIGHT;
                            }
                        } else {                         
                            if (ray.y > 0) {                // луч летит вверх
                                boxSide = Side.FRONT;
                            } else {
                                boxSide = Side.BACK;
                            }
                        }
                        
                        //texture = TextureBank.WALLS[cell];
                        texture = (Texture) wallsMat[mapX][mapY].getTexture(boxSide);
                        if (mapX > 0) {
                            WallMaterial leftMat = wallsMat[mapX - 1][mapY];
                            if (leftMat != null) {
                                left = (Texture) leftMat.getTexture(Side.RIGHT);
                            }
                        }
                        if (mapX < Map.WIDTH - 1) {
                            //right = levelMap[mapX + 1][mapY] - 1;
                            WallMaterial rightMat = wallsMat[mapX + 1][mapY];
                            if (rightMat != null) {
                                right = (Texture) rightMat.getTexture(Side.LEFT);
                            }
                        }
                        if (mapY > 0) {
                            //top = levelMap[mapX][mapY - 1] - 1;
                            WallMaterial frontMat = wallsMat[mapX][mapY - 1];
                            if (frontMat != null) {
                                front = (Texture) frontMat.getTexture(Side.BACK);
                            }
                        }
                        if (mapY < Map.HEIGHT - 1) {
                            //bottom = levelMap[mapX][mapY + 1] - 1;
                            WallMaterial backMat = wallsMat[mapX][mapY + 1];
                            if (backMat != null) {
                                back = (Texture) backMat.getTexture(Side.FRONT);
                            }
                        }

                        // добавить в очередь только потенциально видимые стены
                        if (side == 0) {
                            if (ray.x > 0) {                // луч летит влево
                                if (left == null) {         // слева нет стены?
                                    visibleWall = true;
                                } else {
                                    //visibleWall = TextureBank.WALLS[left].hasAlphaChannel(); // или может быть стена, но с прозрачной текстурой?
                                    visibleWall = left.hasAlphaChannel();
                                }
                            } else {                        // луч летит вправо
                                if (right == null) {        // справа нет стены?
                                    visibleWall = true;
                                } else {
                                    visibleWall = right.hasAlphaChannel();// или может быть стена, но с прозрачной текстурой?
                                }
                            }
                        } else {                         
                            if (ray.y > 0) {                // луч летит вверх
                                if (front == null) {        // вверху нет стены?
                                    visibleWall = true;
                                } else {
                                    visibleWall = front.hasAlphaChannel();// или может быть стена, но с прозрачной текстурой?
                                }
                            } else {
                                if (back == null) {         // луч летит вниз
                                    visibleWall = true;     // внизу нет стены?
                                } else {
                                    visibleWall = back.hasAlphaChannel();// или может быть стена, но с прозрачной текстурой?
                                }
                            }
                        }

                        // если текущий блок с прозрачной текстурой, то
                        // добавить в очередь противоположную грань
                        /*if (texture.isAlphaChannel()) {
                            visibleWall = true;
                            //
                            int mX = 0, mY = 0, sd; 
                            if (sideDist.x < sideDist.y) {
                                mX = mapX + stepX;
                                sd = 0;
                            } else {
                                mY = mapY + stepY;
                                sd = 1;
                            }

                            target = new RenderTarget();
                            target.x = mX;
                            target.y = mY;
                            target.side = sd;
                            target.doorWall = false;
                            renderTargets[level].add(target);                                
                        }*/

                        if (visibleWall) {
                            target = new RenderTarget();
                            //target.doorWall = doorWall;
                            if (side == 0) {
                                wallDist = (mapX - pos.x + ((1 - stepX) >> 1)) * invRay.x;
                                wallX = pos.y + wallDist * ray.y;
                            } else {
                                wallDist = (mapY - pos.y + ((1 - stepY) >> 1)) * invRay.y;
                                wallX = pos.x + wallDist * ray.x;
                            }
                            wallX -= (int)wallX;
                                                        
                            texX = (int)(wallX * Texture.WIDTH);
                            if ((side == 0 && ray.x > 0) ||
                                (side == 1 && ray.y < 0)
                               ) {
                                texX = Texture.WIDTH - texX - 1;
                            }
                            target.texX = texX;                                

                            if (side == 0 && ray.x > 0) {
                                target.collisionPoint.x = mapX;
                                target.collisionPoint.y = mapY + wallX;
                            } else if (side == 0 && ray.x < 0) {
                                target.collisionPoint.x = mapX + 1.0;
                                target.collisionPoint.y = mapY + wallX;
                            } else if (side == 1 && ray.y > 0) {
                                target.collisionPoint.x = mapX + wallX;
                                target.collisionPoint.y = mapY;
                            } else {
                                target.collisionPoint.x = mapX + wallX;
                                target.collisionPoint.y = mapY + 1.0;
                            }
                        }
                    // Check if ray has hit a segment
                    }

                    // была найдена цель для рендеринга
                    if (target != null) {                            
                        /*if (side == 0) {
                            wallX = pos.y + wallDist * ray.y;
                        }
                        else {
                            wallX = pos.x + wallDist * ray.x;
                        }
                        wallX -= (int)wallX;*/

                        target.x = mapX;
                        target.y = mapY;
                        //target.side = side;
                        target.wallDist = wallDist;
                        target.texture = texture;
                        //target.wallX = wallX;

                        /*if (!target.drawSegment) {
                            texX = (int)(wallX * Texture.WIDTH);
                            if ((side == 0 && ray.x > 0) ||
                                (side == 1 && ray.y < 0)
                               ) {
                                texX = Texture.WIDTH - texX - 1;
                            }

                            target.texX = texX;                                

                            if (side == 0 && ray.x > 0) {
                                target.collisionPoint.x = mapX;
                                target.collisionPoint.y = mapY + wallX;
                            } else if (side == 0 && ray.x < 0) {
                                target.collisionPoint.x = mapX + 1.0;
                                target.collisionPoint.y = mapY + wallX;
                            } else if (side == 1 && ray.y > 0) {
                                target.collisionPoint.x = mapX + wallX;
                                target.collisionPoint.y = mapY;
                            } else {
                                target.collisionPoint.x = mapX + wallX;
                                target.collisionPoint.y = mapY + 1.0;
                            }
                        }*/

                        renderTargets[level].add(target);
                    }                 
                }
                
                // jump to next map square, OR in x-direction, OR in y-direction
                if (sideDist.x < sideDist.y) {
                    sideDist.x += deltaDist.x;
                    mapX += stepX;
                    side = 0;
                } else {
                    sideDist.y += deltaDist.y;
                    mapY += stepY;
                    side = 1;
                }
            }
            
            // максимальная длина на предыдущем этаже
            double prevMaxWallDist = 0.0;
            for (int level = 0; level < Map.LEVELS_COUNT; ++level) {
                Array<RenderTarget> renderLevelTargets = renderTargets[level];
                //RenderTargetList renderLevelTargets = renderTargets[level];
                for (Iterator<RenderTarget> it = renderLevelTargets.iterator(); it.hasNext(); ) {
                    RenderTarget target = it.next();
                //for (int t = 0; t < renderLevelTargets.getSize(); ++t) {
                    /*if (target == null) {
                        continue;
                    }*/
                //    RenderTarget target = renderLevelTargets.get(t);
                
                    texX = target.texX;
                    if (texX < 0 || texX >= Texture.WIDTH) {
                        continue;
                    }
                    
                    mapX = target.x;
                    mapY = target.y;
                    //side = target.side;
                    //wallX = target.wallX;                    

                    /*if (!target.drawDoor) {
                        // Calculate distance projected on camera direction (Euclidean distance will give fisheye effect!)
                        if (side == 0) {
                            wallDist = (mapX - pos.x + ((1 - stepX) >> 1)) * invRay.x;
                        } else {
                            wallDist = (mapY - pos.y + ((1 - stepY) >> 1)) * invRay.y;
                        }
                    } else {
                        // средняя дистанция между передней и задней стенками
                        double wD1, wD2;
                        double u;
                        if (side == 0) {
                            u = (mapX - pos.x + ((1 - stepX) >> 1));
                            wD1 = u * invRay.x;
                            wD2 = (u + stepX) * invRay.x;
                        } else {
                            u = (mapY - pos.y + ((1 - stepY) >> 1));
                            wD1 = u * invRay.y;
                            wD2 = (u + stepY) * invRay.y;
                        }
                        wallDist = (wD1 + wD2) * 0.5;
                    }*/
                    wallDist = target.wallDist;
                    double invWallDist = 1.0 / wallDist;

                    // Calculate height of line to draw on screen
                    int lineHeight = (int)(HEIGHT * invWallDist);

                    // calculate lowest and highest pixel to fill in current stripe
                    int drawStart = HALF_HEIGHT - (lineHeight >> 1) - (int)(lineHeight * level - lineHeight * pos.z);
                    int dS = drawStart;
                    int drawEnd = dS + lineHeight/*HALF_HEIGHT + (lineHeight >> 1)*/;

                    if ((drawStart < 1 && drawEnd < 1) ||
                        (drawStart >= HEIGHT && drawEnd >= HEIGHT)) {
                        continue;
                    } else {
                        //int dE = drawEnd;
                        if (drawStart < 1) {
                            drawStart = 1;
                        }
                        if (drawEnd >= HEIGHT) {
                            drawEnd =  HEIGHT - 1;
                        }
                    }

                    // texturing calculations
                    //Texture txr = null;
                    //if (!target.drawDoor) {
                    //    // дверной проём?
                    //    /*if (target.doorWall) {
                    //        txr = Texture.DOORS[2];
                    //    // не, просто стена
                    //    } else */{
                    //        int texNum = Map.WALLS[level][mapX][mapY] - 1;
                    //        txr = Texture.WALLS[texNum]; // 1 subtracted from it so that texture 0 can be used!
                    //    }
                    //// дверь
                    //} else {
                    //    txr = Texture.DOORS[Map.DOORS[mapY * Map.WIDTH + mapX] - 1];
                    //}
                    Texture txr = target.texture;
                    int[] pixelsColumn = txr.getColumn(texX);

                    /*Texture tex = Texture.WALLS.get(texNum);
                    BufferedImage img = tex.getImage();
                    int texWidth  = img.getWidth(),
                        texHeight = img.getHeight();*/

                    //calculate value of wallX
                    /*double wallX; //where exactly the wall was hit
                    if (side == 0) {
                        wallX = pos.y + wallDist * ray.y;
                    }
                    else {
                        wallX = pos.x + wallDist * ray.x;
                    }
                    wallX -= (int)wallX;*/

                    // x coordinate on the texture
                    /*if (!target.drawDoor) {
                        texX = (int)(wallX * Texture.WIDTH);
                        if ((side == 0 && ray.x > 0) ||
                            (side == 1 && ray.y < 0)
                           ) {
                            texX = Texture.WIDTH - texX - 1;
                        }
                    }*/

                    //double invLineHeight = 1.0 / lineHeight;
                    //int factorLineHeight = lineHeight << 7;
                    //int factorHeight = HEIGHT << 7;
                    double yFactor = (double)Texture.HEIGHT / lineHeight;
                    //double fogFactor = 1.0 - (wallDist - Config.FOG_START) * Config.FOG_FACTOR;
                    int pixelsForDraw = drawEnd - drawStart;
                    int pixelsDrawed = 0;
                    //int[] pixelsColumn = txr.getCol(texX);
                    for (int y = drawStart; y < drawEnd; ++y) {
                        //int arrayPos = y * WIDTH + x;
                        // первый уровень однозначно рисуется и забивает Z-BUFFER
                        /*if (level > 0)*/ {
                            /*if (ZBUFFER[arrayPos] < wallDist) {
                                continue;
                            }*/
                            if (ZBUFFER[x][y] <= wallDist) {
                                continue;
                            }
                        }

                        /*
                        int d = ((y) << 8) - factorHeight + factorLineHeight;  // 256 and 128 factors to avoid floats
                        texY = (int)((d << Texture.HEIGHT_POT) * invLineHeight) >> 8;
                        if (f.level == 1) {
                            texY = -texY;
                        }
                        */
                        texY = (int)((y - dS) * yFactor);
                        if (texY < 0 || texY >= Texture.HEIGHT) {
                            continue;
                        }
                        
                        /*int color = txr.getPixel(texX, texY); 
                        if ((color >> 24) == 0) {
                            continue;
                        }*/
                        int color = pixelsColumn[texY]; 
                        int a = (color/* >> 24*/) & 0xFF;
                        if (a <= MAX_ADDITIVE_TRANSPARENT) {
                            continue;
                        }
                        
                        if (Config.fog != Fog.NOTHING) {
                            color = applyFog(color, wallDist);
                        }
                        
                        //Color color = new Color(Texture.WALLS[texNum].getPixel(texX, texY));
                        //make color darker for y-sides: R, G and B byte each divided through two with a "shift" and an "and"
                        /*if (side == 1) {
                            color = color.darker();
                        }*/
                        //TEMP_BUFFER.setRGB(x, y, color);
                        //TEMP_BUFFER_RASTER.setPixel(x, y, new int[]{color});

                        //drawPixel(x, y, color);
                        TEMP_BUFFER.put(y * WIDTH + x, color);
                        //TEMP_BUFFER.setElem(arrayPos, color);
                        //putPixel(x, y, color);
                        //ZBUFFER[arrayPos] = wallDist;
                        /*if (a > MAX_ADDITIVE_TRANSPARENT)*/ {
                            ZBUFFER[x][y] = wallDist;
                            ++pixelsDrawed;
                            ++pixelsInColumn;
                        }
                    }
                    
                    /*    
                    if (Config.STEP_BY_STEP_RENDERING) {
                        try { 
                            Thread.sleep(Config.STEP_BY_STEP_DELAY); 
                        } catch (InterruptedException ex) {}
                        SCREEN_RASTER.setDataElements(0, 0, WIDTH, HEIGHT, TEMP_BUFFER);
                        repaint();
                    }
                    */
                    
                    if (level > 0) {
                        // если нарисована полная строка 
                        // И расстояние на предыдущем этаже меньше или равно
                        // то обход по очереди прекратить 
                        if ((pixelsDrawed == pixelsForDraw) &&
                            (wallDist >= prevMaxWallDist)) {
                            prevMaxWallDist = wallDist;
                            break;
                        }
                        // рисовать пол и потолок только на первом этаже
                        continue;
                    }

                    //SET THE ZBUFFER FOR THE SPRITE CASTING
                    //ZBUFFER[x] = wallDist; //perpendicular distance is used
                    // FLOOR CASTING
                    double floorXWall, floorYWall; //x, y position of the floor texel at the bottom of the wall

                    //4 different wall directions possible
                    /*if (!target.drawSegment) {
                        if (side == 0 && ray.x > 0) {
                            floorXWall = mapX;
                            floorYWall = mapY + wallX;
                        } else if (side == 0 && ray.x < 0) {
                            floorXWall = mapX + 1.0;
                            floorYWall = mapY + wallX;
                        } else if (side == 1 && ray.y > 0) {
                            floorXWall = mapX + wallX;
                            floorYWall = mapY;
                        } else {
                            floorXWall = mapX + wallX;
                            floorYWall = mapY + 1.0;
                        }
                    } else {
                        floorXWall = target.collisionPoint.x;
                        floorYWall = target.collisionPoint.y;
                    }*/
                    floorXWall = target.collisionPoint.x;
                    floorYWall = target.collisionPoint.y;

                    // коррекция расстояния, если это рисуется дверь
                    /*if (target.drawDoor) {
                        if (side == 0) {
                            floorXWall += 0.5 * stepX;
                        } else {
                            floorYWall += 0.5 * stepY;
                        }
                    }*/

                    if (drawEnd < 1) {
                        continue;
                        //drawEnd = HEIGHT; //becomes < 0 when the integer overflows
                    }

                    //draw the floor from drawEnd to the bottom of the screen
                    Texture floorTex = null, 
                            ceilTex  = null;
                    /*int[][] floorPixels = null, 
                            ceilPixels  = null;*/
                    int prevFloorX = -1,
                        prevFloorY = -1;
                    int floorCell = -1,
                        ceilCell  = -1;
                    for (int y = /*++*/drawEnd; y < HEIGHT; ++y) {
                        double currentDist = (double)HEIGHT / ((y << 1) - HEIGHT); //you could make a small lookup table for this instead

                        double weight = currentDist * invWallDist;

                        double currentFloorX = weight * floorXWall + (1.0 - weight) * pos.x;
                        double currentFloorY = weight * floorYWall + (1.0 - weight) * pos.y;
                        if (currentFloorX < 0 || currentFloorY < 0) {
                            continue;
                        }

                        // position in floor/ceil map
                        int floorX = (int)currentFloorX;
                        int floorY = (int)currentFloorY;
                        if (floorX < 0 || floorX >= Map.WIDTH ||
                            floorY < 0 || floorY >= Map.HEIGHT) {
                            continue;
                        }                  

                        int floorTexX, floorTexY;
                        floorTexX = (int)(currentFloorX * Texture.WIDTH ) % Texture.WIDTH;
                        floorTexY = (int)(currentFloorY * Texture.HEIGHT) % Texture.HEIGHT;

                        if (floorX != prevFloorX ||
                            floorY != prevFloorY) {
                            prevFloorX = floorX;
                            prevFloorY = floorY; 
                            
                            floorCell = Map.FLOOR[floorX][floorY];
                            if (floorCell >= 0) {
                                //floorTex = TextureBank.FLOOR[floorCell];
                                //floorPixels = floorTex.getPixels();
                                floorTex = (Texture) MaterialBank.BANK.get(floorCell).getSideFrames()[0];
                            }
                            
                            ceilCell = Map.CEIL[floorX][floorY];
                            if (ceilCell >= 0) {
                                //ceilTex = TextureBank.CEIL[ceilCell];
                                //ceilPixels = ceilTex.getPixels();
                                ceilTex = (Texture) MaterialBank.BANK.get(ceilCell).getSideFrames()[0];
                            }
                        }
                        
                        int color;
                        
                        // floor
                        //int arrayPos = (y - 1) * WIDTH + x;
                        //int pixelPos = (floorTexY << Texture.WIDTH_POT) + floorTexX;
                        //if (ZBUFFER[arrayPos] > currentDist) {
                        if (floorCell >= 0) {                            
                            if (ZBUFFER[x][y - 1] > currentDist) {
                                //TEMP_BUFFER.setRGB(x, y, Texture.WALLS[3].getPixel(floorTexX, floorTexY));
                                //buffer[(y - 1) * WIDTH + x] = Texture.WALLS[3].getPixel(floorTexX, floorTexY);
                                //TEMP_BUFFER_RASTER.setPixel(x, y, new int[]{Texture.WALLS[3].getPixel(floorTexX, floorTexY)});
                                color = floorTex.getPixel(floorTexX, floorTexY);
                                //color = floorTex.pixels[(floorTexY << Texture.WIDTH_POT) + floorTexX];
                                //color = floorPixels[pixelPos];
                                //color = floorPixels[floorTexX][floorTexY];
                                if (Config.fog != Fog.NOTHING) {
                                    color = applyFog(color, currentDist);
                                }
                                TEMP_BUFFER.put((y - 1) * WIDTH + x, color);
                                //TEMP_BUFFER.setElem(arrayPos, color);
                                //putPixel(x, y - 1, Texture.FLOOR[Map.FLOOR[floorX][floorY]].getPixel(floorTexX, floorTexY));
                                //ZBUFFER[arrayPos] = currentDist;
                                ZBUFFER[x][y - 1] = currentDist;
                                ++pixelsInColumn;
                            }
                        }

                        // ceiling (symmetrical!)
                        if (ceilCell >= 0) {
                            //arrayPos = (HEIGHT - y) * WIDTH + x;
                            //if (ZBUFFER[arrayPos] > currentDist) {
                            if (ZBUFFER[x][HEIGHT - y] > currentDist) {
                                //TEMP_BUFFER.setRGB(x, HEIGHT - y, Texture.WALLS[6].getPixel(floorTexX, floorTexY));
                                //buffer[(HEIGHT - y) * WIDTH + x] = Texture.WALLS[6].getPixel(floorTexX, floorTexY);
                                //TEMP_BUFFER_RASTER.setPixel(x, HEIGHT - y, new int[]{Texture.WALLS[6].getPixel(floorTexX, floorTexY)});
                                color = ceilTex.getPixel(floorTexX, floorTexY);
                                //color = ceilTex.pixels[(floorTexY << Texture.WIDTH_POT) + floorTexX];
                                //color = ceilPixels[pixelPos];
                                //color = ceilPixels[floorTexX][floorTexY];
                                if (((color/* >> 24*/) & 0xFF) != 0) {
                                    if (Config.fog != Fog.NOTHING) {
                                        color = applyFog(color, currentDist);
                                    }
                                    TEMP_BUFFER.put((HEIGHT - y) * WIDTH + x, color);
                                    //TEMP_BUFFER.setElem(arrayPos, color);
                                    //putPixel(x, HEIGHT - y, Texture.CEIL[Map.CEIL[floorX][floorY]].getPixel(floorTexX, floorTexY));
                                    //ZBUFFER[arrayPos] = currentDist;
                                    ZBUFFER[x][HEIGHT - y] = currentDist;
                                    ++pixelsInColumn;
                                }
                            }
                        }
                    }
                            
                    /*
                    if (Config.STEP_BY_STEP_RENDERING) {
                        try { 
                            Thread.sleep(Config.STEP_BY_STEP_DELAY); 
                        } catch (InterruptedException ex) {}
                        SCREEN_RASTER.setDataElements(0, 0, WIDTH, HEIGHT, TEMP_BUFFER);
                        repaint();
                    }*/
                    
                    // нарисована вся высота на первом этаже, дальнейший обход не нужен
                    if (level == 0 && pixelsDrawed == pixelsForDraw) {
                        prevMaxWallDist = wallDist;
                        break;
                    }
                }
            }
                
            // после рисовки стен и пола/потолка, закрашиваем небо, если есть что закрашивать
            if (pixelsInColumn < HEIGHT - 1) {
                if (Config.drawSky) {                      
                        
                    if (Config.STEP_BY_STEP_RENDERING) {
                        try { 
                            Thread.sleep(5); 
                        } catch (InterruptedException ex) {}
                    }
                    
                    Sky sky = Sky.getInstance();
                    int skyX = sky.getXStart() - x;
                    if (skyX < 0) {
                        skyX += sky.getWidth()/* - xStep*/;
                    }
                    int[] pixels = sky.getColumn(skyX);

                    for (int y = 1; y < HEIGHT && (pixelsInColumn < HEIGHT - 1); ++y) {
                        //int arrayPos = y * WIDTH + x;
                        //if (ZBUFFER[arrayPos] == Double.MAX_VALUE) {
                        if (ZBUFFER[x][y] == Double.MAX_VALUE) {
                            TEMP_BUFFER.put(y * WIDTH + x, pixels[y]);
                            //TEMP_BUFFER.setElem(arrayPos, pixels[y]);
                            ++pixelsInColumn;
                        }
                    }
                } else {
                    for (int y = 1; y < HEIGHT && (pixelsInColumn < HEIGHT - 1); ++y) {
                        //int arrayPos = y * WIDTH + x;
                        //if (ZBUFFER[arrayPos] == Double.MAX_VALUE) {
                        if (ZBUFFER[x][y] == Double.MAX_VALUE) {
                            TEMP_BUFFER.put(y * WIDTH + x, 0xFF000000);
                            //TEMP_BUFFER.setElem(arrayPos, 0xFF000000);
                            ++pixelsInColumn;  
                        }
                    }                    
                }
                
                /*
                if (Config.STEP_BY_STEP_RENDERING) {
                    try { 
                        Thread.sleep(Config.STEP_BY_STEP_DELAY); 
                    } catch (InterruptedException ignored) {}
                    SCREEN_RASTER.setDataElements(0, 0, WIDTH, HEIGHT, TEMP_BUFFER);
                    repaint();
                }
                */
            }
        }
    }
    
    private void render() {
        while (alreadyRendered || nextActiveCamera == null) {
            Thread.yield();
        }
        activeCamera.duplicate(nextActiveCamera);
        
        //Graphics g = SCREEN.getGraphics();
        //final WritableRaster raster = SCREEN.getRaster();
        //DataBuffer buf = raster.getDataBuffer();
        //WritableRaster rasterSmall = SCREEN_SMALL.getRaster();
        for (boolean[] array : Map.VISIBLE_CELLS) {
            ArrayUtils.fill(array, false);
        }
        /*
        Player player = Player.getInstance();
        final Vector3 pos = player.getPos();
        final Vector2 dir = player.getCamera().getView();
        final Vector2 plane = player.getCamera().getPlane();
        //Vector2 ray = new Vector2();  
        */
        final Vector3 pos   = activeCamera.getPos();
        final Vector2 dir   = activeCamera.getView();
        final Vector2 plane = activeCamera.getPlane();
        
        // calc distance to player
        //Sprite.updateAll();   
        
        // прыгаем на четный/нечетный столбец, если чересстрочный режим        
        if (Config.interlacing) {
            evenFactor ^= 1;
            xStep = 2;
        } else {      
            evenFactor = 0;
            xStep = 1;
        }
        
        //g.setColor(Color.BLACK);
        //g.fillRect(0, 0, WIDTH, HEIGHT);
        /*if (Config.STEP_BY_STEP_RENDERING) {
            ArrayUtils.fill(TEMP_BUFFER, 0);   
        }*/
        //Arrays.fill(TEMP_BUFFER, Color.BLACK.getRGB());   
                
        // забиваем Z-BUFFER и полотно
        /*if (Config.interlacing) {
            for (int y = 0; y < HEIGHT; ++y) {
                int col = y * WIDTH;
                for (int x = evenFactor; x < WIDTH; x += xStep) { 
                    TEMP_BUFFER[col + x] = 0xFF000000;
                    ZBUFFER[col + x] = Double.MAX_VALUE;
                }
            }
        } else {
            for (int i = 0; i < TEMP_BUFFER.length; ++i) {
                TEMP_BUFFER[i] = 0xFF000000;
                ZBUFFER      [i] = Double.MAX_VALUE;
            }
        }*/
        
        // забиваем Z-BUFFER
        /*for (int i = 0; i < ZBUFFER.length; ++i) {
            ZBUFFER[i] = Double.MAX_VALUE;
        }*/
        //Utils.arrayFastFill(ZBUFFER, Double.MAX_VALUE);
        for (double[] array : ZBUFFER) {
            ArrayUtils.fill(array, Double.MAX_VALUE);
        }
        
        // WORLD RENDERING
        if (Config.multithreading) {
            CountDownLatch cdl = new CountDownLatch(RENDER_TASKS.length);
            for (RenderTask task : RENDER_TASKS) {
                task.setLatch(cdl);
                EXECUTOR.execute(task);
            }
            //try {
            //    cdl.await();
            //} catch(InterruptedException ignored) {} 
            while (cdl.getCount() > 0) {
                Thread.yield();
            }
        } else {
            renderWorld(0, WIDTH);
        }
        
        
        // SPRITE CASTING
        SPRITES_FOR_DRAW.clear();
        for (Sprite sprite : Sprite.LIB) {
            //sprite.update();
            int x = (int)sprite.getPos().x;
            int y = (int)sprite.getPos().y;
            
            if (x >= 0 && x < Map.WIDTH  &&
                y >= 0 && y < Map.HEIGHT &&
                Map.VISIBLE_CELLS[x][y]) {
                SPRITES_FOR_DRAW.add(sprite);
            } 
        }
        // сортируем в порядке от меньшего расстояния к большему
        //Collections.sort(SPRITES_FOR_DRAW, Collections.reverseOrder());
        Collections.sort(SPRITES_FOR_DRAW);
        //SPRITES_FOR_DRAW.sort();
        
        for (Sprite sprite : SPRITES_FOR_DRAW) {
            Texture txr = sprite.getTexture();
            if (txr == null) {
                continue;
            }
            
            //translate sprite position to relative to camera
            Vector3 sprPos = sprite.getPos().sub(pos);
            Vector2 scale = sprite.getScale();

            //transform sprite with the inverse camera matrix
            // [ planeX   dirX ] -1                                       [ dirY      -dirX ]
            // [               ]       =  1/(planeX*dirY-dirX*planeY) *   [                 ]
            // [ planeY   dirY ]                                          [ -planeY  planeX ]

            double invDet = 1.0 / (plane.x * dir.y - dir.x * plane.y); //required for correct matrix multiplication

            Vector2 transform = new Vector2(invDet * (dir.y    * sprPos.x - dir.x   * sprPos.y),
                                            invDet * (-plane.y * sprPos.x + plane.x * sprPos.y)); //this is actually the depth inside the screen, that what Z is in 3D
            
            if (transform.y > 0.0) {
                double invTransformY = 1.0 / transform.y;
                int spriteScreenX = (int)(HALF_WIDTH * (1.0 + transform.x * invTransformY));

                //calculate height of the sprite on screen
                int spriteHeight = /*Math.abs*/((int)(HEIGHT * invTransformY * scale.y)); //using "transformY" instead of the real distance prevents fisheye
                double invSpriteHeight = 1.0 / spriteHeight;
                int halfSpriteHeight = spriteHeight >> 1;
                //calculate lowest and highest pixel to fill in current stripe
                int depth = (int)((sprPos.z * invTransformY * (Config.HEIGHT >> Config.quality)));    // поправка по высоте (глубина)
                int dSY = HALF_HEIGHT - (int)((halfSpriteHeight + depth));
                int drawStartY = dSY + 
                                 (int)(txr.getVolume().getTop() * spriteHeight)/* +     // смещаем до полезного объема сверху
                                 (int)(spriteHeight * pos.z)*/;
                //int dSY = drawStartY;
                //int drawStartY = -spriteHeight / 2 + HEIGHT / 2 + depth;
                //int drawStartY = -halfSpriteHeight + HALF_HEIGHT + depth;
                int drawEndY = dSY /*+ spriteHeight*/ + 
                               (int)Math.ceil(txr.getVolume().getBottom() * spriteHeight) /* смещаем до полезного объема снизу*/;            
                if (drawStartY < 1) {
                    drawStartY = 1;
                }
                if (drawEndY >= HEIGHT) {
                    drawEndY = HEIGHT - 1;
                } 

                //calculate width of the sprite
                int spriteWidth = ((int)(HEIGHT * invTransformY * scale.x));//Math.abs((int)(HEIGHT / transform.y));
                int halfSpriteWidth = spriteWidth >> 1;
                int dSX = spriteScreenX - halfSpriteWidth;
                int drawStartX = dSX + (int)(txr.getVolume().getLeft() * spriteWidth);
                int drawEndX = dSX /*+ spriteWidth*/ + (int)Math.ceil(txr.getVolume().getRight() * spriteWidth);   
                if (drawStartX < 0) {
                    drawStartX = 0;
                }
                if (drawEndX >= WIDTH) {
                    drawEndX = WIDTH - 1;
                }

                // учитываем чересстрочность
                if (Config.interlacing) {
                    // берем четный/нечетный столбец
                    if ((drawStartX % 2 == 0 && evenFactor == 1) || 
                        (drawStartX % 2 != 0 && evenFactor == 0)) {
                        ++drawStartX;
                    }
                }           

                // loop through every vertical stripe of the sprite on screen
                
                double invSpriteWidth = 1.0 / spriteWidth;
                /*int prevTexX = -1;
                int[][] pixels = txr.getPixels();
                int[]   pixelsColumn = null;*/
                for (int x = drawStartX; x < drawEndX; x += xStep) {
                    int texX = (int)(((x - dSX) << Texture.WIDTH_POT) * invSpriteWidth);
                    if (texX < 0 || texX >= Texture.WIDTH) {
                        continue;
                    }
                    
                    // for every pixel of the current stripe
                    for (int y = drawStartY; y < drawEndY; ++y) {
                        //int arrayPos = y * WIDTH + x;
                        /*if (ZBUFFER[arrayPos] < transform.y) {
                            continue;
                        }*/
                        if (ZBUFFER[x][y] <= transform.y) {
                            continue;
                        }

                        int d = ((y + depth) << 8) - (HEIGHT << 7) + (spriteHeight << 7); //256 and 128 factors to avoid floats
                        int texY = (int)((d << Texture.HEIGHT_POT) * invSpriteHeight) >> 8;
                        //int texY = (int)((y - dSY) * Texture.HEIGHT / (double)spriteHeight);
                        if (texY < 0 || texY >= Texture.HEIGHT) {
                            continue;
                        }

                        int color = txr.getPixel(texX, texY);                        
                        int a = (color/* >> 24*/) & 0xFF;
                        
                        if (a < MIN_VISIBLE_TRANSPARENT) {
                            continue;
                        } else {
                            if (Config.fog != Fog.NOTHING) {
                                double brightness = transform.y - sprite.getBrightness();
                                color = applyFog(color, brightness);
                            }
                        
                            // paint pixel if it visible
                            /*if (a == 255) { 
                                TEMP_BUFFER[y * WIDTH + x] = color;
                                //TEMP_BUFFER.setElem(arrayPos, color);
                                //ZBUFFER[arrayPos] = transform.y;
                                ZBUFFER[x][y] = transform.y;                                

                                if (Config.STEP_BY_STEP_RENDERING) {
                                    try {
                                        Thread.sleep(0, Config.STEP_BY_STEP_DELAY);
                                    } catch (InterruptedException ex) {
                                        LOG.log(Level.SEVERE, null, ex);
                                    }
                                    SCREEN_RASTER.setDataElements(0, 0, WIDTH, HEIGHT, TEMP_BUFFER);
                                    repaint();
                                }
                            } else {
                                int arrayPos = y * WIDTH + x;
                                int pixel = TEMP_BUFFER[arrayPos];
                                int newPixel = makeAdditive(pixel, color);
                                TEMP_BUFFER[arrayPos] = newPixel;                            
                            }*/
                            drawPixel(x, y, color);
                            //TEMP_BUFFER.put(y * WIDTH + x, color);
                            if (a >= MAX_ADDITIVE_TRANSPARENT) {
                                ZBUFFER[x][y] = transform.y;                                   
                            }
                        }
                    }                    
                }
            }
        }
        
        /*
        // MODEL CASTING
        MODELS_FOR_DRAW.clear();
        for (Model model : Model.LIB) {
            //sprite.update();
            int x = (int)model.getPos().x;
            int y = (int)model.getPos().y;
            
            if (model.isVisible() &&
                x >= 0 && x < Map.WIDTH  &&
                y >= 0 && y < Map.HEIGHT &&
                Map.VISIBLE_CELLS[x][y]) {
                MODELS_FOR_DRAW.add(model);
            } 
        }
        
        // сортируем в порядке от меньшего расстояния к большему
        Collections.sort(MODELS_FOR_DRAW, Collections.reverseOrder());
        
        Matrix4 projMtx = activeCamera.getProjectionMtx();
        Matrix4 viewMtx = activeCamera.getViewMtx();
        Matrix4 pv = projMtx.mul(viewMtx);
        for (Model model : Model.LIB) {   
            Matrix4 mdlMtx = model.getModelMtx();
            Matrix4 wldMtx = pv.mul(mdlMtx);
            
            Mesh mesh = model.getMesh();
            for (Polygon poly : mesh.getPolygons()) {
                ArrayList<Vector2> points = new ArrayList<>();
                for (Vector3 vec3 : poly.getVerticiesV()) {
                    Vector4 vec4 = new Vector4(vec3);
                    Vector4 mul = wldMtx.mul(vec4);
                    Vector3 vec4_to_3 = new Vector3(mul);
                    Vector2 point = new Vector2(vec4_to_3);
                    points.add(point);                    
                }
                
                for (Vector2 point : points) {
                    point.x = (point.x + 1) * HALF_WIDTH;
                    point.y = (point.y + 1) * HALF_HEIGHT;
                }
                
                Vector2 start;
                Vector2 point0 = points.get(0);
                Vector2 point1 = points.get(1);   
                Vector2 point2 = points.get(2);  
                
                double distance = point0.distance(point1);
                Vector2 pDir = point1.sub(point0).normalize();
                start = new Vector2(point0);
                while(distance > 0) {                    
                    int x = (int) start.x;
                    int y = (int) start.y;
                    
                    start = start.add(pDir);
                    --distance;   
                    if (x >= 0 && x < WIDTH &&
                        y >= 0 && y < HEIGHT) {
                        TEMP_BUFFER.put(y * WIDTH + x, 0xFF00FFFF);
                    }                 
                }
                
                distance = point0.distance(point2);
                pDir = point2.sub(point0).normalize();
                start = new Vector2(point0);
                while(distance > 0) {                    
                    int x = (int) start.x;
                    int y = (int) start.y;
                    
                    start = start.add(pDir);
                    --distance;   
                    if (x >= 0 && x < WIDTH &&
                        y >= 0 && y < HEIGHT) {
                        TEMP_BUFFER.put(y * WIDTH + x, 0xFF00FFFF);
                    }                 
                }
                
                distance = point1.distance(point2);
                pDir = point2.sub(point1).normalize();
                start = new Vector2(point1);
                while(distance > 0) {                    
                    int x = (int) start.x;
                    int y = (int) start.y;
                    
                    start = start.add(pDir);
                    --distance;   
                    if (x >= 0 && x < WIDTH &&
                        y >= 0 && y < HEIGHT) {
                        TEMP_BUFFER.put(y * WIDTH + x, 0xFF00FFFF);
                    }                 
                }
            }
        }*/
        
        Player player = Player.getInstance();
        WeaponInHand weapon = player.getWeaponInHand();
        if (weapon != null) {
            Image image = weapon.getFrame();
            if (image != null) {
                int volumeLeft   = (int)(image.getVolume().getLeft()   * image.getWidth());
                int volumeRight  = (int)(image.getVolume().getRight()  * image.getWidth());
                int volumeTop    = (int)(image.getVolume().getTop()    * image.getHeight());
                int volumeBottom = (int)(image.getVolume().getBottom() * image.getHeight());
                
                int startX = (int)weapon.getPos().x - (int)(image.getWidth() * image.getCenter().x);
                int startY = (int)weapon.getPos().y - image.getHeight();
                
                for (int y = volumeTop; y < volumeBottom; ++y) {  
                    if (startY + y >= HEIGHT) {
                        break;
                    }
                    
                    int col = (startY + y) * WIDTH + startX;
                    for (int x = volumeLeft; x < volumeRight; ++x) { 
                        if (x < 0 || x >= WIDTH) {
                            continue;
                        }
                        
                        int color = image.getPixel(x, y);
                        if (((color/* >> 24*/) & 0xFF) != 0) {
                            TEMP_BUFFER.put(col + x, color);
                        }
                    }
                }
            }
        }
        
        if (Config.antialiasing) {
            if (Config.multithreading) {
                CountDownLatch cdl = new CountDownLatch(ANTIALIASING_TASKS.length);
                for (AntialiasingTask task : ANTIALIASING_TASKS) {
                    task.setLatch(cdl);
                    EXECUTOR.execute(task);
                }
                //try {
                //    cdl.await();
                //} catch(InterruptedException ignored) {} 
                while (cdl.getCount() > 0) {
                    Thread.yield();
                }
            } else {
                antialiasing(WIDTH, TEMP_BUFFER.limit() - 1);
            }
        }
                                
        //SCREEN_BUFFER.setRGB(0, 0, WIDTH, HEIGHT, buffer, 0, WIDTH);   
        //SCREEN.getRaster().setDataElements(0, 0, WIDTH, HEIGHT, SCREEN_BUFFER);
        //raster.setDataElements(0, 0, WIDTH, HEIGHT, SCREEN_BUFFER);
        //raster.setPixels(0, 0, WIDTH >> 2, HEIGHT >> 2, SCREEN_BUFFER);

        //SCREEN_RASTER.setDataElements(0, 0, WIDTH, HEIGHT, SCREEN_BUFFER);
        
        //g.drawImage(SCREEN_SMALL, 0, 0, Window.WIDTH, Window.HEIGHT, null);
        
        //paintImmediately(0, 0, Window.WIDTH, Window.HEIGHT);
        //REPAINT_MANAGER.setDoubleBufferingEnabled(false);
        //repaint();
        //REPAINT_MANAGER.setDoubleBufferingEnabled(true);
        while (!canRender) {
            Thread.yield();
        }
        
        /*if (canRender)*/ {
            //SCREEN.draw(SCREEN_RASTER, 0, 0);
            for (int i = 0; i < TEMP_BUFFER.limit(); ++i) {
                SCREEN_BUFFER.put(i, TEMP_BUFFER.get(i));
            }
            //SCREEN_BUFFER.put(TEMP_BUFFER);            
            alreadyRendered = true;
        }
        //IntBuffer buffer = SCREEN_RASTER.getPixels().asIntBuffer();
        // timing for input and FPS counter
/*        oldTime = time;
        time = System.currentTimeMillis();
        deltaTime = (time - oldTime) / 1000.0; //frameTime is the time this frame has taken, in seconds
        
        // calc FPS
        ++fpsCounter;
        fpsTimer += deltaTime;
        if (fpsTimer >= 1.0) {
            fpsTimer -= 1.0;
            fps = fpsCounter;
            fpsCounter = 0;
        }        
*/        
        if (Config.STEP_BY_STEP_RENDERING) {
            try { 
                Thread.sleep(5000); 
            } catch (InterruptedException ex) {}
        }
        
        //alreadyRendered = true;
    }
    
    public com.badlogic.gdx.graphics.Texture getFrame() {
        while (!alreadyRendered) {
            Thread.yield();
        }
                
        canRender = alreadyRendered = false;
        SCREEN.draw(SCREEN_RASTER, 0, 0);
        canRender = true;
        
        return SCREEN;
    }
    
    public static void init() {
        WIDTH  = Config.WIDTH  >> Config.quality;
        HEIGHT = Config.HEIGHT >> Config.quality;
        HALF_WIDTH  = WIDTH  >> 1;
        HALF_HEIGHT = HEIGHT >> 1;
        QUARTER_WIDTH = HALF_WIDTH >> 1;

        RAY_STEP = 1.0 / WIDTH;
    }

    public void start() {
        if (!started) {
            started = true;
            Thread renderThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ignored) {}
                    

                    final Renderer renderer = Renderer.getInstance();
                    while (started) {
                        renderer.render();
                    }
                }
            }, "Renderer thread");
            renderThread.setPriority(Thread.MAX_PRIORITY);
            renderThread.start();
        }
    }
    
    public void setActiveCamera(Camera camera) {
        if (nextActiveCamera == null) {
            nextActiveCamera = new Camera();
        }
        nextActiveCamera.duplicate(camera);
    }
    
    public final Camera getActiveCamera() {
        return activeCamera;
    }
    
    public static void deinit() {  
        started = false;
    }
    
    private RenderTask newRenderTask(final String name, final int fromX, final int toX) {
        return new RenderTask(name, fromX, toX);
    }
    
    private AntialiasingTask newAntialiasingTask(final String name, final int fromX, final int toX) {
        return new AntialiasingTask(name, fromX, toX);
    }
    
    private Renderer() {
        init();
        //ANG_STEP = Player.FOV / WIDTH;

        //SCREEN = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        //SCREEN_RASTER = SCREEN.getRaster();            
        SCREEN_RASTER = new Pixmap(WIDTH, HEIGHT, Format.RGBA8888);
        SCREEN_RASTER.setFilter(Filter.NearestNeighbour);
        SCREEN_RASTER.setBlending(Pixmap.Blending.None);
        
        TEMP_RASTER = new Pixmap(WIDTH, HEIGHT, Format.RGBA8888);
        TEMP_RASTER.setFilter(Filter.NearestNeighbour);
        TEMP_RASTER.setBlending(Pixmap.Blending.None);
        
        //SCREEN_RASTER = pxmap.getPixels().asIntBuffer();
        //SCREEN_SMALL = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        //SCREEN_BUFFER = new int[WIDTH * HEIGHT];
        SCREEN_BUFFER = SCREEN_RASTER.getPixels().asIntBuffer();
        TEMP_BUFFER = TEMP_RASTER.getPixels().asIntBuffer();
        SCREEN = new com.badlogic.gdx.graphics.Texture(SCREEN_RASTER);
        SCREEN.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        //SCREEN_BUFFER = SCREEN.getRaster().getDataBuffer();
        ZBUFFER = new double[WIDTH][HEIGHT];
                        
        // инициализируем задачи для потоков
        /*
        RENDER_TASKS[0] = new RenderTask("task1", 0,                     QUARTER_WIDTH);
        RENDER_TASKS[1] = new RenderTask("task2", QUARTER_WIDTH,         HALF_WIDTH);
        RENDER_TASKS[2] = new RenderTask("task3", HALF_WIDTH,            WIDTH - QUARTER_WIDTH);
        RENDER_TASKS[3] = new RenderTask("task4", WIDTH - QUARTER_WIDTH, WIDTH);
        */
        int widthStep = WIDTH / Config.THREADS_COUNT;
        for (int i = 0; i < Config.THREADS_COUNT; ++i) {
            int fromX = i * widthStep;
            int toX = fromX + widthStep;
            if (toX >= WIDTH) {
                toX = WIDTH - 1;
            }
            
            RENDER_TASKS[i] = newRenderTask("render_task " + i, fromX, toX);
        }
        
        int lengthStep = SCREEN_BUFFER.limit() / Config.THREADS_COUNT;
        for (int i = 0; i < Config.THREADS_COUNT; ++i) {
            int fromX = i * lengthStep;
            if (i == 0) {
                fromX += WIDTH;
            }
            int toX = fromX + lengthStep;
            if (toX >= SCREEN_BUFFER.limit() - 1) {
                toX = SCREEN_BUFFER.limit() - 1;
            }
            
            ANTIALIASING_TASKS[i] = newAntialiasingTask("antialiasing_task " + i, fromX, toX);
        }        
    }
}
