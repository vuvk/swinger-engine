/*
 * Copyright 2019 .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 * @author tai-prg3
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
