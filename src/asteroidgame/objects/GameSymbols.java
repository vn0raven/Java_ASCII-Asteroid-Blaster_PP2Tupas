package asteroidgame.objects;

/**
 * Centralized symbol set for the Unicode retro arcade art style.
 */
public class GameSymbols {
    public static final char PLAYER = '▲';
    public static final char PLAYER_HIT = '◆';
    public static final char PLAYER_SHIELDED = '△';
    public static final char BULLET = '┃';

    public static final char ASTEROID_WEAK = '◎';
    public static final char ASTEROID_NORMAL = '●';
    public static final char ASTEROID_HEAVY = '◉';

    public static final char POWER_UP_LIFE = '✦';
    public static final char EXPLOSION = '✹';
    public static final char LIFE = '♥';
    public static final char EMPTY_LIFE = '·';
    public static final char SHIELD = '◇';

    public static final char UFO_DRIFTER = 'Θ'; 
    public static final char WARNING_LEFT = '>';
    public static final char WARNING_RIGHT = '<';

    public static final char UFO_SHOOTER = 'Ω'; 
    public static final char ENEMY_BULLET = '▼';

    private GameSymbols() {
        // Utility class. No object needed.
    }
}
