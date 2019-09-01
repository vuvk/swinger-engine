/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.audio;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

/**
 *
 * @author tai-prg3
 */
public class SoundBuffer {
    private AudioFormat format;
    private byte[] buffer;
    
    public SoundBuffer(File file) {      
        AudioInputStream stream = Sound.getEncodedAudioInputStream(file);
        if (stream != null) {
            try {
                format = stream.getFormat();
                System.out.println(format.toString());
                //System.out.println("start read = " + file.getName() + " | size = " + file.length() + " | available = " + stream.available());
                
                //buffer = stream.readAllBytes();
                List<Byte> buf = new ArrayList<>((int)file.length());
                int n;
                //byte[] readed = new byte[(int)file.length()];
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
                
                /*
                int pos = 0;
                int n;
                byte[] readed = new byte[(int)file.length()];
                buffer = new byte[(int)file.length()];
                while ((n = stream.read(readed)) != -1) {
                    for (int i = 0; i < n; ++i) {
                        buffer[pos] = (byte)readed[i];
                        pos++;
                    }
                }
                */
                
                //System.out.println("readed = " + file.getName());
                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public AudioInputStream getAudioInputStream() {
        if (buffer == null || buffer.length <= 0 || format == null) {
            return null;
        }
        
        return new AudioInputStream(new ByteArrayInputStream(buffer), format, buffer.length);
    }
}
