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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

/**
 *
 * @author vuvk
 */
public abstract class SoundBasis implements AutoCloseable {
    private static final Logger LOG = Logger.getLogger(SoundBasis.class.getName());   
    
    protected URL inputURL = null;
    protected File inputFile = null;
    protected InputStream inputStream;
    protected AudioInputStream inputAudioStream;
    private boolean playing = false;
    private boolean looping = false;
    private double volume = 1.0;
    
    public double getVolume() {
        return volume;
    }
	
    public boolean isPlaying() {
        return playing;
    }

    public boolean isLooping() {
        return looping;
    }   
    
    public AudioFormat getFormat() {
        if (inputAudioStream != null) {
            return inputAudioStream.getFormat();
        }
        return null;
    } 
    
    public SoundBasis setVolume(double value) {
        if (value < 0.0) {
            value = 0.0;
        } else if (value > 1.0) {
            value = 1.0;
        }
        volume = value;
        
        return this;
    }

    public SoundBasis setLooping(boolean looping) {
        this.looping = looping;
        return this;
    }
    
    protected SoundBasis setPlaying(boolean playing) {
        this.playing = playing;
        return this;
    }
    
    int read(byte[] buffer) {
        if (inputAudioStream != null) {
            try {
                return inputAudioStream.read(buffer);            
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        
        return -1;
    }
    
    public SoundBasis play() {
        return play(false);
    }
    
    public SoundBasis play(final boolean looping) {
        rewind();
        
        setLooping(looping);
        setPlaying(true);
        
        return this;        
    }    
    
    public SoundBasis loop() {
        return play(true);
    }
    
    
    public SoundBasis rewind() {    
        //setPlaying(false);
        
        if (inputFile != null) {
            prepareStream(inputFile);
        } else if (inputURL != null) {
            prepareStream(inputURL);
        } else if (inputAudioStream != null && inputAudioStream.markSupported()) {
            try {
                inputAudioStream.reset();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        
        return this;
    }
    
    public SoundBasis stop() {
        setLooping(false);
        setPlaying(false);
        return this;
    }
    
    protected void markStream() {    
        if (inputAudioStream != null && inputAudioStream.markSupported()) {
            inputAudioStream.mark(-1);
        }
    }
    
    /*
    protected void prepareStream() {
        rewind();
    }*/
    
    protected void prepareStream(InputStream stream) {
        inputStream = stream;
        inputAudioStream = SoundSystem.getEncodedAudioInputStream(stream);
        markStream();   
    }
    
    protected void prepareStream(File file) {
        inputFile = file;
        inputURL = null;
        try {
            prepareStream(new BufferedInputStream(new FileInputStream(file)));
        } catch (FileNotFoundException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    
    protected void prepareStream(URL url) {
        inputFile = null;
        inputURL = url;
        try {
            prepareStream(new BufferedInputStream(url.openStream()));
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }        
    }
	
    @Override
    public void close() {
        stop();
        
        inputFile = null;
        inputURL  = null;
        
        if (inputAudioStream != null) {
            try {
                inputAudioStream.close();
                inputAudioStream = null;
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }         
        
        if (inputStream != null) {
            try {
                inputStream.close();
                inputStream = null;
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }
}
