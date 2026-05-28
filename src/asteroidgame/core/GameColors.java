package asteroidgame.core;

import java.awt.Color;

/**
 * The retro neon arcade color palette.
 */
public class GameColors {
    public static final Color BACKGROUND = new Color(10, 10, 15); // Deep space black/blue
    public static final Color DEFAULT_TEXT = new Color(200, 200, 200); // Off-white
    
    public static final Color PLAYER_CYAN = new Color(0, 255, 255);
    public static final Color BULLET_YELLOW = new Color(255, 255, 0);
    public static final Color ASTEROID_BROWN = new Color(139, 90, 43);
    public static final Color ASTEROID_RED = new Color(255, 80, 80); // For cracked/damaged states
    public static final Color EXPLOSION_ORANGE = new Color(255, 140, 0);
    public static final Color POWERUP_GREEN = new Color(50, 255, 50);
}