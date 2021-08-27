/**
    Copyright (C) 2021 Anton "Vuvk" Shcherbatykh <vuvk69@gmail.com>
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

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vuvk.swinger.math.Vector2;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class MouseManager implements MouseListener, MouseWheelListener, MouseMotionListener {
    private static final Logger LOG = Logger.getLogger(MouseManager.class.getName());
    private static MouseManager instance = null;       

    private static Robot robot;
    private static Vector2 prevLoc  = new Vector2();
    private static Vector2 location = new Vector2();
    private static float scrollAmountX = 0,
                         scrollAmountY = 0;
    private static boolean leftClick  = false;
    private static boolean rightClick = false;
    

    private MouseManager() {
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    public static MouseManager getInstance() {
        if (instance == null) {
            instance = new MouseManager();
        }
        return instance;
    }

    @Override
    public void mouseDragged(final MouseEvent e){
        mouseMoved(e);
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
        prevLoc.set(location);
        Point locOnScreen = e.getLocationOnScreen();
        location.set(locOnScreen.x, locOnScreen.y);
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1 :
                leftClick = true;
                break;
            case MouseEvent.BUTTON2 :
                rightClick = true;
                break;
        }
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1 :
                leftClick = false;
                break;
            case MouseEvent.BUTTON2 :
                rightClick = false;
                break;
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        /*if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL)*/ {
            scrollAmountY = e.getScrollAmount();    
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public static double getDeltaX() {
        return prevLoc.x - location.x;
    }

    public static double getDeltaY() {
        return prevLoc.y - location.y;
    }

    public static Vector2 getDelta() {
        return location.sub(prevLoc);
    }

    public static void setLocation(int x, int y) {
        prevLoc.set(location);
        location.set(x, y);
        robot.mouseMove(x, y);
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
}
