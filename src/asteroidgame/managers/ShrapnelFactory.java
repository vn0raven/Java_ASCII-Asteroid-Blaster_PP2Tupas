package asteroidgame.managers;

import asteroidgame.core.GameConfig;
import asteroidgame.objects.Asteroid;
import asteroidgame.objects.GameSymbols;
import asteroidgame.objects.MeteorShard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Creates meteor-shard bursts when a larger asteroid is destroyed.
 * This gives asteroid destruction a short risk-reward effect instead of
 * simply removing the object from the board.
 */
public class ShrapnelFactory {
    private static final Random random = new Random();

    private static final int[][] BURST_DIRECTIONS = {
            {-1, -1}, {0, -1}, {1, -1},
            {-1, 0},           {1, 0},
            {-1, 1},  {0, 1},  {1, 1}
    };

    public static List<MeteorShard> createBurst(Asteroid asteroid, int boardWidth, int boardHeight,
                                                int existingShardCount) {
        List<MeteorShard> burst = new ArrayList<MeteorShard>();
        int remainingSlots = GameConfig.MAX_METEOR_SHARDS_ON_SCREEN - existingShardCount;

        if (remainingSlots <= 0) {
            return burst;
        }

        int count = Math.min(asteroid.getShrapnelCount(), remainingSlots);
        int centerX = asteroid.getX();
        int centerY = asteroid.getCenterY();

        for (int i = 0; i < count; i++) {
            int[] direction = BURST_DIRECTIONS[(i + random.nextInt(BURST_DIRECTIONS.length)) % BURST_DIRECTIONS.length];
            int offsetX = random.nextInt(3) - 1;
            int offsetY = random.nextInt(3) - 1;
            char symbol = chooseShardSymbol(i);

            burst.add(new MeteorShard(
                    centerX + offsetX,
                    centerY + offsetY,
                    boardWidth,
                    boardHeight,
                    GameConfig.SHRAPNEL_SPEED + random.nextInt(2),
                    direction[0],
                    direction[1],
                    symbol,
                    GameConfig.SHRAPNEL_ACTIVATION_DELAY
            ));
        }

        return burst;
    }

    private static char chooseShardSymbol(int index) {
        if (index % 3 == 0) return GameSymbols.METEOR_SHARD_SOLID;
        if (index % 3 == 1) return GameSymbols.METEOR_SHARD_LIGHT;
        return GameSymbols.METEOR_SHARD_CHIP;
    }

    private ShrapnelFactory() {
        // Utility class. No object needed.
    }
}
