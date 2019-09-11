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

/**
 *
 * @author vuvk
 */
import java.io.File;
import java.net.URL;
import java.util.logging.Logger;

public final class Sound extends SoundBasis {  
    private static final Logger LOG = Logger.getLogger(Sound.class.getName());
    
    public Sound(SoundBuffer buffer) {
        prepareStream(buffer);
    }
    
    public Sound(String path) {
        this(new File(path));
    }
    
    public Sound(File file) {
        this(file, false);
    }

    public Sound(String path, boolean precached) {
        this(new File(path), precached);
    }
    
    public Sound(File file, boolean precached) {                     
        if (precached) {
            prepareStream(new SoundBuffer(file));
        } else {
            prepareStream(file);
        }            
    }
    
    public Sound(URL url) {
        this(url, false);
    }
    
    public Sound(URL url, boolean precached) {             
        if (precached) {
            prepareStream(new SoundBuffer(url));
        } else {
            prepareStream(url);
        }  
    }
    
    private void prepareStream(SoundBuffer buffer) {
        super.prepareStream(buffer.getAudioInputStream());
    }    

    @Override
    public Sound play(boolean looping) {
        super.play(looping);            
        SoundSystem.playSound(this);           
        return this;
    }
	
    @Override
    public Sound stop() {
        super.stop();
        SoundSystem.stopSound(this);
        return this;
    }
}