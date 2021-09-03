package io.github.vuvk.audiosystem.test;



import io.github.vuvk.audiosystem.AudioSystem;
import io.github.vuvk.audiosystem.Music;
import io.github.vuvk.audiosystem.Sound;
import io.github.vuvk.audiosystem.SoundBuffer;
import com.jogamp.openal.AL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author vuvk
 */
public class SingleSource {
    static SoundBuffer buffer;
    static Sound source;

    static boolean loadALData() {
        buffer = AudioSystem.newSoundBuffer("mono.wav");
        if (buffer == null) {
            return false;
        }

        // variables to load into
        source = AudioSystem.newSound(buffer);
        if (source == null) {
            return false;
        }

        source.setPosition(0, 0, 0);
        source.setVelocity(0, 0, 0);
        source.setPitch(1f);
        source.setVolume(1f);
        source.setLooping(true);

        // Do another error check and return.
        return (AudioSystem.checkError() == AL.AL_NO_ERROR);
    }

    public static void main(String[] args) {
        AudioSystem.init();

        // Load the wav data.
        if (!loadALData()) {
            System.exit(-1);
        }

        Music music = AudioSystem.newMusic("music.ogg");
        music.setLooping(true)
             .setVolume(0.25f)
             .play();

        char[] c = new char[1];
        while (c[0] != 'q') {
            try {
                BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Press a key and hit ENTER:\n" +
                                   "Sound: \n\t'w' to play, 'e' to stop, 'r' to pause, 't' to buffer dispose \n" +
                                   "Music: \n\t's' to play, 'd' to stop, 'f' to pause\n" +
                                   "and 'q' to quit");
                buf.read(c);
                switch (c[0]) {
                    case 'w':
                        // Pressing 'p' will begin playing the sample.
                        source.play();
                        break;
                    case 'e':
                        // Pressing 's' will stop the sample from playing.
                        source.stop();
                        break;
                    case 'r':
                        // Pressing 'n' will pause (hold) the sample.
                        source.pause();
                        break;
                    case 't':
                        buffer.dispose();
                        break;

                    case 's':
                        music.play();
                        break;
                    case 'd':
                        music.stop();
                        break;
                    case 'f':
                        music.pause();
                        break;
                }
            } catch (IOException e) {
                System.exit(1);
            }
        }

        AudioSystem.deinit();
    }
}
