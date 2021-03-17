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

import com.vuvk.utils.FastByteArrayInputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

/**
 * load sound to memory buffer
 * @author vuvk
 */
public final class SoundBuffer {
    private static final Logger LOG = Logger.getLogger(SoundBuffer.class.getName());    
    
    private AudioFormat format;
    private byte[] buffer;
    
    public SoundBuffer(String path) {
        try {
            load(new BufferedInputStream(new FileInputStream(new File(path))));
        } catch (FileNotFoundException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    
    public SoundBuffer(URL url) {
        try {
            load(new BufferedInputStream(url.openStream()));
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    
    public SoundBuffer(File file) {
        try {
            load(new BufferedInputStream(new FileInputStream(file)));
        } catch (FileNotFoundException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    
    public SoundBuffer(InputStream inputStream) {   
        load(inputStream);
    }
    
    private void load(InputStream inputStream) {
        AudioInputStream stream = SoundSystem.getEncodedAudioInputStream(inputStream);
        if (stream != null) {
            try {
                format = stream.getFormat();
                
                List<Byte> buf = new ArrayList<>(inputStream.available());
                int n;
                int bufferSize = 0;
                byte[] readed = new byte[65535];
                while ((n = stream.read(readed)) != -1) {
                    bufferSize += n;
                    for (int i = 0; i < n; ++i) {
                        buf.add(readed[i]);
                    }
                }
                
                buffer = new byte[bufferSize];
                for (n = 0; n < bufferSize; ++n) {
                    buffer[n] = buf.get(n);
                }
                buf.clear();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        } 
    }
    
    public AudioInputStream getAudioInputStream() {
        if (buffer == null || format == null) {
            return null;
        }
        
        return new AudioInputStream(new FastByteArrayInputStream(buffer), format, buffer.length);
    }
}
