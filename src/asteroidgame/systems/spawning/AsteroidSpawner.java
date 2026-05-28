package asteroidgame.systems.spawning;

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

import java.util.List;
import java.util.Random;

/**
 * Handles the logic for when and where asteroids should appear.
 *
 * Main faceted asteroids now spawn from the top-left, top-center, and top-right
 * areas. They choose an approach direction toward the player when they spawn,
 * then keep drifting in that direction. This creates diagonal pressure without
 * making the asteroid unfairly home every frame.
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
     * Backward-compatible spawn method. It uses center-bottom as the assumed target.
     */
    public Asteroid attemptSpawn(int frameCount, int level, List<Asteroid> activeAsteroids) {
        return attemptSpawn(frameCount, level, activeAsteroids, null);
    }

    /**
     * Checks if it is time to spawn an asteroid.
     * Returns a new Asteroid object if successful, or null if it is not time yet.
     */
    public Asteroid attemptSpawn(int frameCount, int level, List<Asteroid> activeAsteroids, Player player) {
        return attemptSpawn(frameCount, level, activeAsteroids, player, java.util.Collections.<UfoEnemy>emptyList());
    }

    public Asteroid attemptSpawn(int frameCount, int level, List<Asteroid> activeAsteroids, Player player, List<UfoEnemy> activeUfos) {
        if (activeAsteroids.size() >= GameConfig.MAX_ASTEROIDS_ON_SCREEN) {
            return null;
        }

        int spawnInterval = Math.max(13, 40 - level * 5);

        if (frameCount % spawnInterval == 0) {
            return createRandomAsteroid(level, activeAsteroids, player, activeUfos);
        }

        if (level >= 4 && frameCount % Math.max(16, spawnInterval + 8) == 0
                && activeAsteroids.size() < GameConfig.MAX_ASTEROIDS_ON_SCREEN - 1) {
            return createRandomAsteroid(level, activeAsteroids, player, activeUfos);
        }

        return null;
    }

    public Asteroid attemptBossSpawn(int frameCount, int level, List<Asteroid> activeAsteroids, Player player) {
        if (activeAsteroids.size() >= GameConfig.MAX_BOSS_ASTEROIDS_ON_SCREEN) {
            return null;
        }

        if (frameCount % GameConfig.BOSS_ASTEROID_SPAWN_INTERVAL != 0) {
            return null;
        }

        return createRandomAsteroid(level, activeAsteroids, player, java.util.Collections.<UfoEnemy>emptyList());
    }

    private Asteroid createRandomAsteroid(int level, List<Asteroid> activeAsteroids, Player player, List<UfoEnemy> activeUfos) {
        SpawnPlan spawnPlan = chooseSpawnPlan(activeAsteroids, activeUfos, player);
        if (spawnPlan == null) {
            return null;
        }

        int roll = random.nextInt(100);

        int lightSpeed = Math.max(GameConfig.MIN_ASTEROID_SPEED, 6 - level / 2);
        int normalSpeed = Math.max(GameConfig.MIN_ASTEROID_SPEED, 8 - level / 2);
        int heavySpeed = Math.max(GameConfig.MIN_HEAVY_ASTEROID_SPEED, 11 - level / 2);

        /*
         * Larger arena balance:
         * Heavy asteroids should feel special, not constant. Standard and light
         * asteroids now appear more often to give the field a varied Asteroids-arcade feel.
         */
        int heavyChance = level >= 3 ? Math.min(20, 8 + level * 2) : 0;
        int standardChance = level == 1 ? 65 : 45;

        if (roll < heavyChance) {
            return new HeavyAsteroid(spawnPlan.x, 0, boardHeight, heavySpeed, spawnPlan.dx, 1);
        }

        if (roll < heavyChance + standardChance) {
            return new NormalAsteroid(spawnPlan.x, 0, boardHeight, normalSpeed, spawnPlan.dx, 1);
        }

        return new SmallAsteroid(spawnPlan.x, 0, boardHeight, lightSpeed, spawnPlan.dx, 1);
    }

    private SpawnPlan chooseSpawnPlan(List<Asteroid> activeAsteroids, List<UfoEnemy> activeUfos, Player player) {
        int margin = 5;

        for (int attempt = 0; attempt < 12; attempt++) {
            int zone = random.nextInt(3); // 0 top-left, 1 top-center, 2 top-right
            int x;
            int dx;

            if (zone == 0) {
                x = margin + random.nextInt(Math.max(1, boardWidth / 4));
                dx = 1;
            } else if (zone == 2) {
                x = boardWidth - margin - random.nextInt(Math.max(1, boardWidth / 4));
                dx = -1;
            } else {
                x = margin + random.nextInt(boardWidth - margin * 2);
                int targetX = player != null ? player.getX() : boardWidth / 2;
                dx = Integer.compare(targetX, x);

                // Center spawns sometimes fall straight down so patterns remain readable.
                if (Math.abs(targetX - x) <= 4 || random.nextInt(100) < 45) {
                    dx = 0;
                }
            }

            if (isSpawnPositionClear(x, activeAsteroids, activeUfos)) {
                return new SpawnPlan(x, dx);
            }
        }

        return null;
    }

    private boolean isSpawnPositionClear(int x, List<Asteroid> activeAsteroids, List<UfoEnemy> activeUfos) {
        for (Asteroid asteroid : activeAsteroids) {
            if (asteroid.isActive() && asteroid.getY() <= 4
                    && Math.abs(asteroid.getX() - x) < GameConfig.SPAWN_SEPARATION) {
                return false;
            }
        }

        for (UfoEnemy ufo : activeUfos) {
            if (ufo.isActive() && ufo.getY() <= 8
                    && Math.abs(ufo.getX() - x) < GameConfig.SPAWN_SEPARATION + 3) {
                return false;
            }
        }

        return true;
    }

    private static class SpawnPlan {
        int x;
        int dx;

        SpawnPlan(int x, int dx) {
            this.x = x;
            this.dx = dx;
        }
    }
}
