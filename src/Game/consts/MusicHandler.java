package Game.consts;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Armaghan on 7/10/2017.
 */
public class MusicHandler {
    public static Clip clip;

    public static Clip music(String name) {
        name = "music.wav";
        try {
            clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(name));
            clip.open(inputStream);
        }catch(FileNotFoundException e){
            System.out.println("wave file not found.");
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clip;
    }
}