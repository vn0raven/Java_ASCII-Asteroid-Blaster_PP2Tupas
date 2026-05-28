package asteroidgame.managers;

import asteroidgame.objects.ExtraLifePowerUp;
import asteroidgame.objects.PowerUp;

import java.util.Random;

/**
 * Handles the drop logic for beneficial items.
 */
public class PowerUpSpawner {
    private int boardWidth;
    private int boardHeight;
    private Random random;

    public PowerUpSpawner(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.random = new Random();
    }

    /**
     * Returns a PowerUp if the timing and random chance align, otherwise null.
     */
    public PowerUp attemptSpawn(int frameCount, int level) {
        int powerUpInterval = Math.max(160, 260 - level * 10);

        if (frameCount > 0 && frameCount % powerUpInterval == 0) {
            if (random.nextInt(100) < 40) {
                int x = random.nextInt(boardWidth);
                return new ExtraLifePowerUp(x, 0, boardHeight);
            }
        }
        return null;
    }
}