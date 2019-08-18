/**
    Side enum (Nuke3D Editor)
    Copyright (C) 2019 Anton "Vuvk" Shcherbatykh <vuvk69@gmail.com>

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
package swingraycaster.graphic;

/**
 * Енумератор стороны чего-либо в редакторе
 * @author Anton "Vuvk" Shcherbatykh
 */
public enum Side {        
    FRONT(0), BACK  (1),
    LEFT (2), RIGHT (3);

    // номер стороны в массиве сторон
    private int num;
    public int getNum() {
        return num;
    }

    Side(int num) {
        this.num = num;
    }        

    /** 
     * Получить сторону по номеру
     * @param num номер стороны в промежутке [0-3]
     * @return Константа енумератора: 0 - FRONT, 1 - BACK, 2 - LEFT, 3 - RIGHT
     */
    public static Side getByNum(int num) {
        if (num < 0) {
            num = 0;
        } else if (num >= 4) {
            num = 3;
        }

        switch (num) {
            case 0  : return FRONT;
            case 1  : return BACK;
            case 2  : return LEFT;
            default : return RIGHT;
        }
    }
}