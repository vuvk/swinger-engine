/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingraycaster.audio;

// ПОПРОБОВАТЬ ЭТО http://tv-games.ru/forum/blog.php?b=2032
// https://www.ntu.edu.sg/home/ehchua/programming/java/J8c_PlayingSound.html

/**
 *
 * @author tai-prg3
 */
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.sound.sampled.AudioFormat;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound implements AutoCloseable {    
    
    private boolean released = false;
    private AudioInputStream stream = null;
    //private byte[] buffer;
    private Clip clip = null;
    private FloatControl volumeControl = null;
    private boolean playing = false;
    private boolean looping = false;
    
    public Sound(SoundBuffer buffer) {
        stream = buffer.getAudioInputStream();
        released = prepareAudioClip();
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
            stream = new SoundBuffer(file).getAudioInputStream();  
        } else {
            stream = getEncodedAudioInputStream(file);
        }
            
        released = prepareAudioClip();
    }
    
    private boolean prepareAudioClip() {  
        if (stream == null) {
            return false;
        }
        
        try {
            clip = AudioSystem.getClip();
            clip.open(stream);
        } catch (LineUnavailableException | IOException exc) {
            exc.printStackTrace();
            close();
            
            return false;
        }
        
        clip.addLineListener(new Listener());
        volumeControl  = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    
        return true;
    }

    // true если звук успешно загружен, false если произошла ошибка
    /*
    public boolean isReleased() {
        return released;
    }
    */
	
    // проигрывается ли звук в данный момент
    public boolean isPlaying() {
        return playing;
    }

    /** 
     * Запуск
     * @param breakOld определяет поведение, если звук уже играется
      Если breakOld==true, о звук будет прерван и запущен заново
      Иначе ничего не произойдёт
    */
    public Sound play(boolean breakOld) {
        if (released) {
            if (breakOld && isPlaying()) {
                clip.flush();
                //int count = 0;
                while (clip.isRunning()) {
                    clip.stop();
                //    ++count;
                }
                /*if (count > 1) {
                    System.out.println("Долго останавливал " + count);
                }*/
                playing = false;
            }
            
            //if (!isPlaying()) {
                clip.setFramePosition(0);
                //int count = 0;
                while (!clip.isRunning()) {
                    if (looping) {
                        clip.loop(Clip.LOOP_CONTINUOUSLY);
                    } else {
                        clip.start();
                    }
                //    ++count;
                }
                /*if (count > 1) {
                    System.out.println("Долго запускал " + count);
                }*/
                playing = true;
            //}
        }
        return this;
    }
	
    /** 
     * То же самое, что и play(true)
     */
    public Sound play() {
        return play(true);
    }
    
    public Sound loop() {
        setLooping(true);
        return play();
    }
	
    // Останавливает воспроизведение
    public Sound stop() {
        if (playing) {
            clip.stop();
        }
        
        return this;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    public boolean isLooping() {
        return looping;
    }
	
    @Override
    public void close() {
        if (clip != null) {
            clip.close();
        }

        if (stream != null) {
            try {
                stream.close();
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        }
    }

    // Установка громкости
    /*
      x долже быть в пределах от 0 до 1 (от самого тихого к самому громкому)
    */
    public Sound setVolume(float x) {
        if (x < 0f) x = 0f;
        if (x > 1f) x = 1f;
        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();
        float v = (max - min) * x + min;
        
        volumeControl.setValue(v);
                
        return this;
    }

    // Возвращает текущую громкость (число от 0 до 1)
    public float getVolume() {
        float v = volumeControl.getValue();
        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();
        return (v - min) / (max - min);
    }

    // Дожидается окончания проигрывания звука
    /*public void join() {
        if (!released) 
            return;
        
        synchronized(clip) {
            try {
                while (playing) {
                    clip.wait();
                }
            } catch (InterruptedException exc) {
                System.out.println(exc.getMessage());
            }
        }
    }*/

    // Статический метод, для удобства
    /*public static Sound playSound(String path) {
        return playSound(new File(path));
    }
    
    public static Sound playSound(File file) {
        Sound snd = new Sound(file);
        snd.play();
        return snd;
    }*/
    
    /**
     * Проиграть случайный звук из массива (даже если какой-то из них уже проигрывается)
     * @param sounds Набор звуков
     */
    public static void playRandom(Sound ... sounds) {
        playRandom(false, sounds);
    }
    
    /**
     * Проиграть случайный звук из массива
     * @param checkPlaying Проверка играется ли какой-то звук из массива, если да, то не играть
     * @param sounds Набор звуков
     */
    public static void playRandom(boolean checkPlaying, Sound ... sounds) {
        if (sounds == null || sounds.length == 0) {
            return;
        }
        
        if (checkPlaying) {
            for (Sound sound : sounds) {
                if (sound.isPlaying()) {
                    return;
                }
            }
        }
        
        int variant = Math.abs(new Random().nextInt()) % sounds.length;
        sounds[variant].play();
    }
    
    public static AudioInputStream getEncodedAudioInputStream(File file) {  
        AudioInputStream in = null;
        try {
            in = AudioSystem.getAudioInputStream(file);
        } catch (UnsupportedAudioFileException | IOException ex) {
            ex.printStackTrace();
        }
        
        if (in != null) {
            // получаем формат аудио
            AudioFormat baseFormat = in.getFormat();

            // получаем несжатый формат
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                                 baseFormat.getSampleRate(),
                                 //22050,
                                 16,
                                 baseFormat.getChannels(),
                                 baseFormat.getChannels() * 2,
                                 baseFormat.getSampleRate(),
                                 true);

            return AudioSystem.getAudioInputStream(format, in);
        }
        
        return null;
    }

    private class Listener implements LineListener {
        @Override
        public void update(LineEvent ev) {
            if (ev.getType() == LineEvent.Type.STOP) {
                playing = false;
                synchronized(clip) {
                    clip.notify();
                }
            }
        }
    }
}