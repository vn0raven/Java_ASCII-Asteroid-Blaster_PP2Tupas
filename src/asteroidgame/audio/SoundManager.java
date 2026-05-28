package asteroidgame.audio;

import asteroidgame.audio.*;
import asteroidgame.config.*;
import asteroidgame.core.*;
import asteroidgame.state.*;
import asteroidgame.objects.assets.*;
import asteroidgame.objects.base.*;
import asteroidgame.objects.asteroids.*;
import asteroidgame.objects.effects.*;
import asteroidgame.objects.enemies.*;
import asteroidgame.objects.player.*;
import asteroidgame.objects.powerups.*;
import asteroidgame.objects.projectiles.*;
import asteroidgame.systems.collision.*;
import asteroidgame.systems.scoring.*;
import asteroidgame.systems.spawning.*;
import asteroidgame.systems.upgrades.*;
import asteroidgame.ui.input.*;
import asteroidgame.ui.render.*;
import asteroidgame.ui.swing.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import java.io.File;

/**
 * Handles playing retro .wav sound effects and looping background music.
 */
public class SoundManager {
    
    private static final String SOUND_FOLDER = "sounds/";
    private static Clip currentMusicClip;

    // 0.0f is max volume. -10.0f is quiet. -20.0f is very quiet. -80.0f is muted.
    public static float musicVolume = -9.0f; 
    public static float sfxVolume = -5.0f; 

    /**
     * Plays a looping background track at the specified musicVolume.
     */
    public static void playMusic(String fileName) {
        new Thread(() -> {
            try {
                if (currentMusicClip != null && currentMusicClip.isRunning()) {
                    currentMusicClip.stop();
                    currentMusicClip.close();
                }

                File musicFile = new File(SOUND_FOLDER + fileName);
                
                if (musicFile.exists()) {
                    AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicFile);
                    currentMusicClip = AudioSystem.getClip();
                    currentMusicClip.open(audioInput);
                    
                    // 1. Lower the Music Volume!
                    FloatControl gainControl = (FloatControl) currentMusicClip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(musicVolume);
                    
                    currentMusicClip.loop(Clip.LOOP_CONTINUOUSLY); 
                    currentMusicClip.start();
                }
            } catch (Exception e) {
                // Sound is optional. If audio fails, gameplay should continue silently.
            }
        }).start();
    }

    public static void stopMusic() {
        if (currentMusicClip != null && currentMusicClip.isRunning()) {
            currentMusicClip.stop();
        }
    }

    /**
     * Plays a sound effect once at the specified sfxVolume.
     */
    public static void playSound(String fileName) {
        new Thread(() -> {
            try {
                File soundFile = new File(SOUND_FOLDER + fileName);
                
                if (soundFile.exists()) {
                    AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundFile);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInput);
                    
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(sfxVolume);
                    
                    clip.start(); 
                    
                    clip.addLineListener(event -> {
                        if (event.getType() == LineEvent.Type.STOP) {
                            clip.close();
                        }
                    });
                }
            } catch (Exception e) {
                // Sound is optional. If audio fails, gameplay should continue silently.
            }
        }).start();
    }
}
