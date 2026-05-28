package asteroidgame.managers;

import asteroidgame.core.GameConfig;
import asteroidgame.objects.Asteroid;
import asteroidgame.objects.HeavyAsteroid;
import asteroidgame.objects.NormalAsteroid;

import java.util.Random;

/**
 * Handles the logic for when and where asteroids should appear.
 */
public class AsteroidSpawner {
    private int boardWidth;
    private int boardHeight;
    private Random random;

    public AsteroidSpawner(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.random = new Random();
    }

    /**
     * Checks if it's time to spawn an asteroid. 
     * Returns a new Asteroid object if successful, or null if it's not time yet.
     */
    public Asteroid attemptSpawn(int frameCount, int level, int currentAsteroidCount) {
        if (currentAsteroidCount >= GameConfig.MAX_ASTEROIDS_ON_SCREEN) {
            return null;
        }

        int spawnInterval = Math.max(20, 44 - level * 3);

        // Standard spawn
        if (frameCount % spawnInterval == 0) {
            return createRandomAsteroid(level);
        }

        // Bonus spawn for high levels
        if (level >= 5 && frameCount % (spawnInterval * 2) == 0 && currentAsteroidCount < GameConfig.MAX_ASTEROIDS_ON_SCREEN - 1) {
            return createRandomAsteroid(level);
        }

        return null;
    }

    private Asteroid createRandomAsteroid(int level) {
        int x = 1 + random.nextInt(boardWidth - 2);
        int roll = random.nextInt(100);

        int normalSpeed = Math.max(GameConfig.MIN_ASTEROID_SPEED, 9 - level / 2);
        int heavySpeed = Math.max(GameConfig.MIN_HEAVY_ASTEROID_SPEED, 10 - level / 2);

        if (level >= 3 && roll < 35 + level * 5) {
            return new HeavyAsteroid(x, 0, boardHeight, heavySpeed);
        }

        return new NormalAsteroid(x, 0, boardHeight, normalSpeed);
    }
}