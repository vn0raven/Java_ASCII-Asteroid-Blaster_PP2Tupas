package asteroidgame.core;

/**
 * Central place for balance values and screen dimensions.
 * Keeping these constants here makes gameplay tuning easier.
 */
public final class GameConfig {
    public static final int BOARD_WIDTH = 39;
    public static final int BOARD_HEIGHT = 18;

    public static final int MAX_LEVEL = 6;
    public static final int SCORE_PER_LEVEL = 160;

    public static final int TIMER_DELAY = 125;
    public static final int MAX_ASTEROIDS_ON_SCREEN = 9;
    public static final int MIN_ASTEROID_SPEED = 4;
    public static final int MIN_HEAVY_ASTEROID_SPEED = 5;

    public static final int DANGER_LIMIT = 5;
    public static final int HIT_FLASH_DURATION = 8;
    public static final int MESSAGE_DURATION = 24;
    public static final int SPAWN_SEPARATION = 4;

    private GameConfig() {
        // Utility class. No object needed.
    }
}
