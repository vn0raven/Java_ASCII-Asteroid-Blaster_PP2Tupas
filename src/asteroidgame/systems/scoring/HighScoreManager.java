package asteroidgame.systems.scoring;

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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Handles reading and writing the high score to a local text file.
 */
public class HighScoreManager {
    private static final String FILE_NAME = "highscore.txt";
    private int highScore;

    public HighScoreManager() {
        loadHighScore();
    }

    private void loadHighScore() {
        try {
            File file = new File(FILE_NAME);
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                if (scanner.hasNextInt()) {
                    highScore = scanner.nextInt();
                }
                scanner.close();
            } else {
                // If no file exists yet, set a default high score and create the file
                highScore = 1500; 
                saveHighScore(highScore);
            }
        } catch (IOException e) {
            highScore = 1500;
        }
    }

    public void saveHighScore(int currentScore) {
        if (currentScore > highScore) {
            highScore = currentScore;
        }
        
        try {
            FileWriter writer = new FileWriter(FILE_NAME);
            writer.write(String.valueOf(highScore));
            writer.close();
        } catch (IOException e) {
            System.out.println("Could not save high score.");
        }
    }

    public int getHighScore() {
        return highScore;
    }
}
