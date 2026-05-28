package asteroidgame.managers;

import asteroidgame.core.GameConfig;
import asteroidgame.objects.Asteroid;
import asteroidgame.objects.Player;
import asteroidgame.objects.UfoEnemy;

import java.util.List;
import java.util.Random;

/**
 * Spawns regular UFO enemies from Level 4 onward.
 *
 * UFOs now use a compact symmetrical sprite and avoid spawning directly on top
 * of active asteroids. This keeps the UFO readable without removing the arcade clutter.
 */
public class UfoSpawner {
    private int boardWidth;
    private int boardHeight;
    private Random random;

    public UfoSpawner(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.random = new Random();
    }

    public UfoEnemy attemptSpawn(int frameCount, int level,
                                 List<UfoEnemy> activeUfos,
                                 List<Asteroid> activeAsteroids,
                                 Player player) {
        if (level < GameConfig.UFO_START_LEVEL) {
            return null;
        }

        int maxActive = getMaxActiveUfos(level);
        if (activeUfos.size() >= maxActive) {
            return null;
        }

        int interval = getSpawnInterval(level);
        if (frameCount % interval != 0) {
            return null;
        }

        for (int attempt = 0; attempt < 10; attempt++) {
            UfoEnemy candidate = buildCandidate(level);

            if (isTooCloseToPlayer(candidate, player)) {
                continue;
            }

            if (overlapsAnyUfo(candidate, activeUfos)) {
                continue;
            }

            if (overlapsAnyAsteroid(candidate, activeAsteroids)) {
                continue;
            }

            return candidate;
        }

        return null;
    }

    /**
     * Backward-compatible helper for older tests/tools.
     */
    public UfoEnemy attemptSpawn(int frameCount, int level, List<UfoEnemy> activeUfos, Player player) {
        return attemptSpawn(frameCount, level, activeUfos, java.util.Collections.<Asteroid>emptyList(), player);
    }

    private UfoEnemy buildCandidate(int level) {
        int x;
        int y;
        int driftDx;
        int side = random.nextInt(3);

        int upperLaneMax = Math.max(3, boardHeight / 4);

        if (side == 0) {
            x = 4 + random.nextInt(Math.max(1, boardWidth / 4));
            y = 1 + random.nextInt(Math.max(1, upperLaneMax - 1));
            driftDx = 1;
        } else if (side == 1) {
            x = boardWidth - 5 - random.nextInt(Math.max(1, boardWidth / 4));
            y = 1 + random.nextInt(Math.max(1, upperLaneMax - 1));
            driftDx = -1;
        } else {
            x = boardWidth / 2 + random.nextInt(13) - 6;
            y = 1 + random.nextInt(Math.max(1, upperLaneMax - 1));
            driftDx = random.nextBoolean() ? 1 : -1;
        }

        return new UfoEnemy(x, y, boardWidth, boardHeight, level, driftDx);
    }

    private boolean isTooCloseToPlayer(UfoEnemy candidate, Player player) {
        return Math.abs(candidate.getX() - player.getX()) < GameConfig.UFO_PLAYER_SAFETY_RADIUS
                && Math.abs(candidate.getY() - player.getY()) < GameConfig.UFO_PLAYER_SAFETY_RADIUS;
    }

    private boolean overlapsAnyUfo(UfoEnemy candidate, List<UfoEnemy> activeUfos) {
        for (UfoEnemy ufo : activeUfos) {
            if (ufo.isActive() && rectanglesOverlap(candidate.getLeftX(), candidate.getY(),
                    candidate.getSpriteWidth(), candidate.getSpriteHeight(),
                    ufo.getLeftX(), ufo.getY(), ufo.getSpriteWidth(), ufo.getSpriteHeight(), 3)) {
                return true;
            }
        }
        return false;
    }

    private boolean overlapsAnyAsteroid(UfoEnemy candidate, List<Asteroid> activeAsteroids) {
        for (Asteroid asteroid : activeAsteroids) {
            if (asteroid.isActive() && rectanglesOverlap(candidate.getLeftX(), candidate.getY(),
                    candidate.getSpriteWidth(), candidate.getSpriteHeight(),
                    asteroid.getLeftX(), asteroid.getY(), asteroid.getSpriteWidth(), asteroid.getSpriteHeight(), 2)) {
                return true;
            }
        }
        return false;
    }

    private boolean rectanglesOverlap(int ax, int ay, int aw, int ah,
                                      int bx, int by, int bw, int bh,
                                      int padding) {
        int aLeft = ax - padding;
        int aRight = ax + aw - 1 + padding;
        int aTop = ay - padding;
        int aBottom = ay + ah - 1 + padding;

        int bLeft = bx;
        int bRight = bx + bw - 1;
        int bTop = by;
        int bBottom = by + bh - 1;

        return aLeft <= bRight && aRight >= bLeft && aTop <= bBottom && aBottom >= bTop;
    }

    private int getMaxActiveUfos(int level) {
        if (level >= 5) return 2;
        return 1;
    }

    private int getSpawnInterval(int level) {
        if (level >= 5) return GameConfig.UFO_LEVEL_5_SPAWN_INTERVAL;
        return GameConfig.UFO_LEVEL_4_SPAWN_INTERVAL;
    }
}
