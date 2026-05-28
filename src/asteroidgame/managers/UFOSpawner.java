package asteroidgame.managers;

import asteroidgame.objects.DrifterUFO;
import asteroidgame.objects.UFO;

import java.util.Random;

/**
 * Controls the automated timing and logic for spawning Alien enemies.
 */
public class UFOSpawner {
    private int boardWidth;
    private int boardHeight;
    private Random random;

    public UFOSpawner(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.random = new Random();
    }

    /**
     * Checks if the conditions are right to spawn a UFO this frame.
     */
    public UFO attemptSpawn(int frameCount, int level, int currentUFOCount) {
        // 1. SAFE ZONE: No aliens on Level 1!
        if (level < 2) {
            return null;
        }

        // 2. SCREEN CAP: Max 1 UFO early on, max 2 on Level 5 and 6
        int maxUFOs = (level >= 5) ? 2 : 1;
        if (currentUFOCount >= maxUFOs) {
            return null;
        }

        // 3. THE TIMER: Spawns faster on higher levels
        // Base is every 160 frames, dropping by 20 frames per level
        int spawnInterval = Math.max(80, 200 - (level * 20));

        if (frameCount > 0 && frameCount % spawnInterval == 0) {
            
            // Randomize if it comes from the Left or Right edge
            boolean entersFromLeft = random.nextBoolean();
            
            // Spawn randomly in the upper half of the screen
            int startY = 1 + random.nextInt((boardHeight / 2) + 2); 
            
            // Currently returns our Drifter! We will add the others here later.
            return new DrifterUFO(startY, entersFromLeft, boardWidth, boardHeight);
        }

        return null;
    }
}