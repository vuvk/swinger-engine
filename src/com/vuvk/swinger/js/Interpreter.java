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
package com.vuvk.swinger.js;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author Anton "Vuvk" Shcherbatykh
 */
public class Interpreter {
    private static final Logger LOG = Logger.getLogger(Interpreter.class.getName()); 

    private Interpreter() {}
    
    private final static ScriptEngineManager MANAGER = new ScriptEngineManager();
    private final static ScriptEngine ENGINE = MANAGER.getEngineByName("nashorn");    
    private final static StringBuilder SCRIPT_ACCUM = new StringBuilder();
    
    /**
     * Добавить строку к единому скрипту
     * @param script 
     */
    public static void addListing(String script) {        
        SCRIPT_ACCUM.append(script);
        SCRIPT_ACCUM.append('\n');
    }
    
    /**
     * Добавить файл к единому скрипту
     * @param file 
     */
    public static void addListing(File file) {        
        try {
            addListing(new String(Files.readAllBytes(file.toPath())));
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }            
    }
    
    /**
     * Выполнить единый аккумулированный скрипт
     * @return результат работы скрипта
     */
    public static Object runListing() {
        if (SCRIPT_ACCUM.length() > 0) {            
            String scriptString = SCRIPT_ACCUM.toString();
                        
            try {
                new FileOutputStream(new File("script.js")).write(scriptString.getBytes());
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
            
            try {
                return ENGINE.eval(scriptString);
            } catch (ScriptException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }            
        }
        
        return null;
    }
    
    /**
     * Очистить единый скрипт
     */
    public static void clearListing() {
        SCRIPT_ACCUM.delete(0, SCRIPT_ACCUM.length());
    }
    
    /**
     * Выполнить отдельный независимый скрипт
     * @param script Строка со скриптом
     * @return результат работы скрипта
     */
    public static Object evalScript(String script) {
        try {
            return ENGINE.eval(script);
        } catch (ScriptException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
                
        return null;
    }
    
    /**
     * Выполнить отдельный независимый скрипт
     * @param script Файл со скриптом
     * @return результат работы скрипта
     */
    public static Object evalScript(File file) {
        try {
            return ENGINE.eval(Files.newBufferedReader(file.toPath(), Charset.defaultCharset()));
        } catch (IOException | ScriptException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
}
