package asteroidgame.managers;

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