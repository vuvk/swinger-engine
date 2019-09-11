/**
    Copyright 2019 Anton "Vuvk" Shcherbatykh

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.vuvk.retard_sound_system;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.Control;

/**
 *
 * @author vuvk
 */
public final class Music extends SoundBasis {
    private static final Logger LOG = Logger.getLogger(Music.class.getName());    
    private static final int BUFFER_SIZE = 10240;
    
    private SourceDataLine line;
    
    public Music(String path) {
        this(new File(path));
    }
    
    public Music(File file) {
        prepareStream(file);
        SoundSystem.MUSICS.add(this);
    }   
    
    public Music(URL url) {
        prepareStream(url);
        SoundSystem.MUSICS.add(this);
    }
    
    public Music(InputStream in) {
        prepareStream(in);
        SoundSystem.MUSICS.add(this);
    }
    
    @Override
    public Music play(final boolean looping) {        
        super.play(looping);
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                final byte[] buffer = new byte[BUFFER_SIZE];

                while (inputAudioStream != null && 
                       line             != null && 
                       isPlaying()) {
                    
                    int cnt = 0;                    
                    Arrays.fill(buffer, (byte)0);
                    
                    if ((cnt = read(buffer)) != -1) {
                        // apply volume
                        for (int i = 0; i < buffer.length; i += 2) {                                
                            byte low  = buffer[i];
                            byte high = buffer[i + 1];

                            int value = (short)(((high & 0xFF) << 8) | (low & 0xFF));
                            value *= getVolume();                                

                            buffer[i]     = (byte) value;
                            buffer[i + 1] = (byte) (value >> 8);
                        }

                        line.write(buffer, 0, cnt); 
                    } else {
                        break;
                    }
                }

                if (isLooping()) {
                    play();
                } else {
                    stop();
                }
            }
        }).start();
        
        return this;
    }
    
    private void prepareLine() { 
        if (inputAudioStream != null) {
            AudioFormat format = inputAudioStream.getFormat();

            final DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            try {
                line = (SourceDataLine) AudioSystem.getLine(info);
                Control[] controls = line.getControls();
                for (Control control : controls) {
                    System.out.println(control);
                }
                line.open(format);
                line.start();
            } catch (LineUnavailableException ex) {
                Logger.getLogger(Music.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
         
    @Override
    protected void prepareStream(File file) {
        super.prepareStream(file);
        prepareLine();
    }
    
    @Override 
    protected void prepareStream(URL url) {
        super.prepareStream(url);
        prepareLine();
    }
    
    @Override 
    protected void prepareStream(InputStream in) {
        super.prepareStream(in);
        prepareLine();
    }
    
    @Override 
    protected void prepareStream(AudioInputStream in) {
        super.prepareStream(in);
        prepareLine();
    }
}
