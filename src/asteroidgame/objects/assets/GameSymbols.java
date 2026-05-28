package asteroidgame.objects.assets;

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
/**
 * Centralized symbol set for the Unicode retro arcade art style.
 */
public class GameSymbols {
    public static final char PLAYER = '▲';
    public static final char PLAYER_UP = '▲';
    public static final char PLAYER_DOWN = '▼';
    public static final char PLAYER_LEFT = '◀';
    public static final char PLAYER_RIGHT = '▶';
    public static final char PLAYER_UP_LEFT = '◤';
    public static final char PLAYER_UP_RIGHT = '◥';
    public static final char PLAYER_DOWN_LEFT = '◣';
    public static final char PLAYER_DOWN_RIGHT = '◢';

    public static final char PLAYER_HIT = '◆';
    public static final char PLAYER_SHIELDED = '△';
    public static final char BULLET = '┃';
    public static final char BULLET_VERTICAL = '┃';
    public static final char BULLET_HORIZONTAL = '━';
    public static final char BULLET_DIAGONAL_FORWARD = '╱';
    public static final char BULLET_DIAGONAL_BACK = '╲';

    public static final char ASTEROID_WEAK = '◎';
    public static final char ASTEROID_NORMAL = '●';
    public static final char ASTEROID_HEAVY = '◉';

    public static final char METEOR_SHARD_SOLID = '◆';
    public static final char METEOR_SHARD_LIGHT = '◇';
    public static final char METEOR_SHARD_CHIP = '▪';

    public static final char UFO_CORE = '◉';
    public static final char ENEMY_BULLET_DOWN = '▼';
    public static final char ENEMY_BULLET_UP = '▲';
    public static final char ENEMY_BULLET_LEFT = '◀';
    public static final char ENEMY_BULLET_RIGHT = '▶';

    public static final char POWER_UP_LIFE = '✦';
    public static final char EXPLOSION = '✹';
    public static final char LIFE = '♥';
    public static final char EMPTY_LIFE = '·';
    public static final char SHIELD = '◇';

    private GameSymbols() {
        // Utility class. No object needed.
    }
}
