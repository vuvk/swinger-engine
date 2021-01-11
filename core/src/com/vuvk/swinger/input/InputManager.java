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
package com.vuvk.swinger.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputAdapter;
import com.vuvk.swinger.Config;
import static com.vuvk.swinger.Game.screenMsg;
import com.vuvk.swinger.graphic.Fog;
import com.vuvk.swinger.graphic.gui.menu.Menu;
import com.vuvk.swinger.math.Vector2;
import com.vuvk.swinger.objects.mortals.Player;
import com.vuvk.swinger.res.Map;
import com.vuvk.swinger.utils.SavedGame;
import com.vuvk.swinger.utils.Utils;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public final class InputManager extends InputAdapter {
    private static final Logger LOG = Logger.getLogger(InputManager.class.getName());

    private static Vector2 prevLoc  = new Vector2();
    private static Vector2 location = new Vector2();
    private static float scrollAmountX = 0,
                         scrollAmountY = 0;
    private static boolean leftClick  = false;
    private static boolean rightClick = false;

    public InputManager () {}

    public static double getDeltaX() {
        //return Gdx.input.getDeltaX();
        return location.x - prevLoc.x;
    }

    public static double getDeltaY() {
        //return Gdx.input.getDeltaY();
        return location.y - prevLoc.y;
    }

    public static Vector2 getDelta() {
        //return new Vector2(getDeltaX(), getDeltaY());
        return location.sub(prevLoc);
    }

    public static void setLocation(int x, int y) {
        prevLoc.set(location);
        location.set(x, y);
        Gdx.input.setCursorPosition(x, y);
    }

    public static void setLocation(Vector2 point) {
        setLocation((int)point.x, (int)point.y);
    }

    public static Vector2 getLocation() {
        return location;
    }

    public static boolean isScrollDown() {
        return (scrollAmountY > 0);
    }

    public static boolean isScrollUp() {
        return (scrollAmountY < 0);
    }

    public static boolean isLeftClick() {
        return leftClick;
    }

    public static boolean isRightClick() {
        return rightClick;
    }

    public static void reset() {
        //leftClick = rightClick = false;
        //location.set(0, 0);
        scrollAmountX = scrollAmountY = 0;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        switch (button) {
            case Buttons.LEFT : leftClick  = true; break;
            case Buttons.RIGHT: rightClick = true; break;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        switch (button) {
            case Buttons.LEFT : leftClick  = false; break;
            case Buttons.RIGHT: rightClick = false; break;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return mouseMoved(screenX, screenY);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        prevLoc.set(location);
        location.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        scrollAmountX = amountX;
        scrollAmountY = amountY;
        return false;
    }

    @Override
    public boolean keyDown (int keycode) {
        if (!Config.console) {
            Player player = Player.getInstance();
            switch (keycode) {
                case Input.Keys.UP:
                case Input.Keys.W :
                    if (Map.isLoaded() &&
                        Map.active     &&
                        player.getHealth() > 0.0
                       ) {
                        player.setMoveF(true);
                    }
                    if (Menu.isActive()) {
                        Menu.CURRENT.prev();
                    }
                    break;

                case Input.Keys.DOWN:
                case Input.Keys.S :
                    if (Map.isLoaded() &&
                        Map.active     &&
                        player.getHealth() > 0.0
                       ) {
                        player.setMoveB(true);
                    }
                    if (Menu.isActive()) {
                        Menu.CURRENT.next();
                    }
                    break;

                case Input.Keys.A :
                    if (Map.isLoaded() &&
                        Map.active     &&
                        player.getHealth() > 0.0
                       ) {
                        player.setMoveL(true);
                    }
                    if (Menu.isActive()) {
                        Menu.CURRENT.getCurrentButton().left();
                    }
                    break;
                case Input.Keys.D :
                    if (Map.isLoaded() &&
                        Map.active     &&
                        player.getHealth() > 0.0
                       ) {
                        player.setMoveR(true);
                    }
                    if (Menu.isActive()) {
                        Menu.CURRENT.getCurrentButton().right();
                    }
                    break;

                case Input.Keys.Q : player.getPos().z -= Gdx.graphics.getDeltaTime() * 15; break;
                case Input.Keys.Z : player.getPos().z += Gdx.graphics.getDeltaTime() * 15; break;

                case Input.Keys.LEFT :
                    if (Map.isLoaded() &&
                        Map.active     &&
                        player.getHealth() > 0.0
                       ) {
                        player.setRotL(true);
                    }
                    if (Menu.isActive()) {
                        Menu.CURRENT.getCurrentButton().left();
                    }
                    break;
                case Input.Keys.RIGHT:
                    if (Map.isLoaded() &&
                        Map.active     &&
                        player.getHealth() > 0.0
                       ) {
                        player.setRotR(true);
                    }
                    if (Menu.isActive()) {
                        Menu.CURRENT.getCurrentButton().right();
                    }
                    break;
            }
        } else {
            switch (keycode) {
                case Input.Keys.BACKSPACE :
                    if (Config.consoleCommand.length() > 0) {
                        char[] cmd = Config.consoleCommand.toCharArray();
                        Config.consoleCommand = "";
                        StringBuilder sb = new StringBuilder(cmd.length - 1);
                        for (int i = 0; i < cmd.length - 1; ++i) {
                            sb.append(cmd[i]);
                        }
                        Config.consoleCommand = sb.toString();
                        //Config.consoleCommand = Config.consoleCommand.substring(0, Config.consoleCommand.length() - 1);
                        //System.out.println("backspace");
                    } else {
                        Config.consoleCommand = "";
                    }
                    break;
            }
        }

        switch (keycode) {
            case Input.Keys.ENTER :
                if (!Menu.isActive()) {
                    if (Config.console) {
                        String fullCommand = Config.consoleCommand.trim().toLowerCase();
                        Scanner scanCommand = new Scanner(fullCommand);

                        if (scanCommand.hasNext()) {
                            switch (scanCommand.next()) {
                                case "interlacing" :
                                    Config.interlacing = (scanCommand.hasNextInt() && scanCommand.nextInt() == 1);
                                    Config.init();
                                    break;

                                case "fog" :
                                    if (scanCommand.hasNextInt()) {
                                        switch (scanCommand.nextInt()) {
                                            case 0 : Config.fog = Fog.NOTHING; break;
                                            case 1 : Config.fog = Fog.OLD;     break;
                                            case 2 : Config.fog = Fog.SMOOTH;  break;
                                        }
                                    }
                                    break;

                                case "fog_start" :
                                    if (scanCommand.hasNextDouble()) {
                                        double start = scanCommand.nextDouble();
                                        if (start < 0.1f) {
                                            start = 0.1f;
                                        }
                                        if (start > Fog.END) {
                                            start = Fog.END;
                                        }
                                        Fog.START = start;
                                        Fog.init();
                                    }
                                    break;

                                case "fog_end" :
                                    if (scanCommand.hasNextDouble()) {
                                        double end = scanCommand.nextDouble();
                                        if (end < 0.1f) {
                                            end = 0.1f;
                                        }
                                        if (end < Fog.START) {
                                            end = Fog.START;
                                        }
                                        Fog.END = end;
                                        Fog.init();
                                    }
                                    break;

                                case "fog_color" :
                                    int red, green, blue;

                                    // получаем компоненты R G B
                                    if (scanCommand.hasNextInt()) {
                                        red = scanCommand.nextInt();
                                        red = Utils.limit(red, 0, 255);
                                    } else {
                                        break;
                                    }

                                    if (scanCommand.hasNextInt()) {
                                        green = scanCommand.nextInt();
                                        green = Utils.limit(green, 0, 255);
                                    } else {
                                        break;
                                    }

                                    if (scanCommand.hasNextInt()) {
                                        blue = scanCommand.nextInt();
                                        blue = Utils.limit(blue, 0, 255);
                                    } else {
                                        break;
                                    }

                                    // все компоненты введены - установить цвет тумана
                                    Fog.COLOR = 0xFF |
                                                ((red) << 24) |
                                                ((green) << 16) |
                                                ((blue) <<  8);

                                    Fog.init();
                                    break;

                                case "antialiasing" :
                                    Config.antialiasing = (scanCommand.hasNextInt() && scanCommand.nextInt() == 1);
                                    Config.init();
                                    break;

                                case "multithreading" :
                                    Config.multithreading = (scanCommand.hasNextInt() && scanCommand.nextInt() == 1);
                                    break;

                                case "mouselook" :
                                    Config.mouseLook = (scanCommand.hasNextInt() && scanCommand.nextInt() == 1);
                                    break;

                                case "sky" :
                                    Config.drawSky = (scanCommand.hasNextInt() && scanCommand.nextInt() == 1);
                                    break;
                            }
                        }
                        Config.consoleCommand = "";
                    }
                    Config.console = !Config.console;
                } else {
                    Menu.CURRENT.getCurrentButton().click();
                }
                break;

            case Input.Keys.ESCAPE :
                //Config.QUIT = true;

                if (Menu.isActive()) {
                    Menu.deactivate();
                    //Map.active = true;
                } else {
                    Menu.setMainSubMenu();
                    Menu.activate();
                    //Map.active = false;

                    Player player = Player.getInstance();
                    if (player.getHealth() > 0.0) {
                        player.setMoveB(false);
                        player.setMoveF(false);
                        player.setMoveL(false);
                        player.setMoveR(false);

                        player.setRotL(false);
                        player.setRotR(false);
                    }
                }

                break;
        }

        return false;
    }

    /** Called when a key goes up. When true is returned, the event is {@link Event#handle() handled}. */
    @Override
    public boolean keyUp (int keycode) {
        Player player = Player.getInstance();

        if (!Config.console) {
            switch (keycode) {
                case Input.Keys.UP:
                case Input.Keys.W :
                    if (Map.active && player.getHealth() > 0.0) {
                        player.setMoveF(false);
                    }
                    break;

                case Input.Keys.DOWN:
                case Input.Keys.S :
                    if (Map.active && player.getHealth() > 0.0) {
                        player.setMoveB(false);
                    }
                    break;

                case Input.Keys.A : player.setMoveL(false); break;
                case Input.Keys.D : player.setMoveR(false); break;

                case Input.Keys.LEFT : player.setRotL(false); break;
                case Input.Keys.RIGHT: player.setRotR(false); break;

                case Input.Keys.NUM_1 : player.setWeaponInHand(0); break;
                case Input.Keys.NUM_2 : player.setWeaponInHand(1); break;
                case Input.Keys.NUM_3 : player.setWeaponInHand(2); break;
                case Input.Keys.NUM_4 : player.setWeaponInHand(3); break;
                case Input.Keys.NUM_5 : player.setWeaponInHand(4); break;

                case Input.Keys.SPACE : player.openDoor(); break;

                case Input.Keys.R :
                    Map.load(1);
                    Config.draw = true;
                    screenMsg.setMessage("LEVEL RESTARTED");
                    break;

                case Input.Keys.M :
                    Config.mouseLook = !Config.mouseLook;
                    screenMsg.setMessage("MOUSELOOK " + ((Config.mouseLook) ? "ON" : "OFF"));
                    break;

                case Input.Keys.F5 :
                    SavedGame.save("quick.sav");
                    break;

                case Input.Keys.F9 :
                    SavedGame.load("quick.sav");
                    break;
            }
        }

        return false;
    }

    /** Called when a key is typed. When true is returned, the event is {@link Event#handle() handled}.
     * @param character May be 0 for key typed events that don't map to a character (ctrl, shift, etc). */
    @Override
    public boolean keyTyped (char character) {
        if (Config.console) {
            if (Character.isLetterOrDigit(character) ||
                Character.isWhitespace(character) ||
                Character.isSpaceChar(character) ||
                character == ',' ||
                character == '.' ||
                character == '_' ||
                character == '-'
               ) {
                Config.consoleCommand += character;
            }
        }
        return false;
    }
}
