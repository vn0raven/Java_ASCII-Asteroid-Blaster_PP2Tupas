package asteroidgame.config;

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
 * Central place for balance values and screen dimensions.
 * Keeping these constants here makes gameplay tuning easier.
 */
public final class GameConfig {
    public static final int BOARD_WIDTH = 60;
    public static final int BOARD_HEIGHT = 40;

    public static final int MAX_LEVEL = 7;
    public static final int BOSS_LEVEL = 7;
    public static final int BOSS_MAX_HP = 60;
    public static final int BOSS_MOVE_DELAY = 1;
    public static final int BOSS_SHOT_COOLDOWN = 30;
    public static final int BOSS_ASTEROID_SPAWN_INTERVAL = 52;
    public static final int MAX_BOSS_ASTEROIDS_ON_SCREEN = 5;
    // Cumulative score targets are defined in ScoreManager. This value is kept only for older references.
    public static final int SCORE_PER_LEVEL = 300;

    public static final int TIMER_DELAY = 125;
    public static final int MAX_ASTEROIDS_ON_SCREEN = 12;
    public static final int MIN_ASTEROID_SPEED = 4;
    public static final int MIN_HEAVY_ASTEROID_SPEED = 5;
    public static final int PLAYER_BULLET_SPEED = 2; // kept for backward reference
    public static final int PLAYER_BULLET_HORIZONTAL_SPEED = 2;
    public static final int PLAYER_BULLET_VERTICAL_SPEED = 1;
    public static final int PLAYER_BULLET_DIAGONAL_SPEED = 1;

    public static final int MAX_METEOR_SHARDS_ON_SCREEN = 84;
    public static final int METEOR_CLUSTER_START_LEVEL = 3;
    public static final int ALL_DIRECTION_CLUSTER_START_LEVEL = 4;
    public static final int METEOR_CLUSTER_PLAYER_SAFETY_RADIUS = 8;
    public static final int MIN_METEOR_SHARD_SPEED = 2;
    public static final int MAX_METEOR_CLUSTER_SIZE = 15;

    public static final int UFO_START_LEVEL = 4;
    public static final int UFO_LEVEL_4_SPAWN_INTERVAL = 135;
    public static final int UFO_LEVEL_5_SPAWN_INTERVAL = 105;
    public static final int UFO_MOVE_DELAY = 3;
    public static final int UFO_BOB_DELAY = 18;
    public static final int UFO_BASE_SHOT_COOLDOWN = 58;
    public static final int UFO_PLAYER_SAFETY_RADIUS = 10;
    public static final int ENEMY_BULLET_MOVE_DELAY = 2;

    public static final int SHRAPNEL_ACTIVATION_DELAY = 7;
    public static final int SHRAPNEL_SPEED = 3;
    public static final int HEAVY_SHRAPNEL_COUNT = 9;
    public static final int NORMAL_SHRAPNEL_COUNT = 6;
    public static final int LIGHT_SHRAPNEL_COUNT = 4;

    public static final int DANGER_LIMIT = 4;
    public static final int HIT_FLASH_DURATION = 8;
    public static final int MESSAGE_DURATION = 24;
    public static final int SPAWN_SEPARATION = 8;

    private GameConfig() {
        // Utility class. No object needed.
    }
}
