package asteroidgame.managers;

import asteroidgame.core.GameConfig;
import asteroidgame.objects.GameSymbols;
import asteroidgame.objects.MeteorShard;
import asteroidgame.objects.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Creates jagged meteor shard clusters.
 *
 * Early levels keep clusters coming mostly from the top so players can learn
 * the hazard. From Level 4 onward, clusters can enter from all board edges,
 * creating the meteor-field feeling of classic arcade Asteroids.
 */
public class MeteorClusterSpawner {
    private static final int TOP_LEFT = 0;
    private static final int TOP_CENTER = 1;
    private static final int TOP_RIGHT = 2;
    private static final int LEFT_EDGE = 3;
    private static final int RIGHT_EDGE = 4;
    private static final int BOTTOM_LEFT = 5;
    private static final int BOTTOM_CENTER = 6;
    private static final int BOTTOM_RIGHT = 7;

    private int boardWidth;
    private int boardHeight;
    private Random random;

    private static final int[][][] CLUSTER_PATTERNS = {
            { {0, 0}, {3, 1}, {1, 3} },
            { {0, 0}, {2, 1}, {5, 1}, {3, 3} },
            { {1, 0}, {4, 0}, {0, 2}, {3, 3}, {6, 2} },
            { {0, 1}, {2, 0}, {5, 1}, {1, 3}, {4, 4}, {7, 3} },
            { {1, 0}, {4, 0}, {7, 1}, {0, 2}, {3, 3}, {6, 3}, {2, 5} },
            { {0, 0}, {3, 1}, {6, 0}, {1, 2}, {5, 3}, {8, 2}, {2, 5}, {7, 5} },
            { {1, 0}, {5, 0}, {9, 1}, {0, 2}, {3, 3}, {7, 3}, {10, 4}, {2, 5}, {6, 6} },
            { {0, 1}, {2, 0}, {5, 1}, {8, 0}, {11, 2}, {1, 3}, {4, 4}, {7, 5}, {10, 4}, {3, 7} },
            { {0, 0}, {2, 2}, {4, 1}, {6, 3}, {8, 0}, {10, 2}, {1, 5}, {5, 5}, {9, 6}, {12, 4}, {3, 7} },
            { {1, 1}, {4, 0}, {7, 2}, {10, 1}, {0, 4}, {3, 5}, {6, 4}, {9, 6}, {12, 5}, {2, 8}, {8, 8}, {11, 7} }
    };

    public MeteorClusterSpawner(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.random = new Random();
    }

    public List<MeteorShard> attemptSpawn(int frameCount, int level,
                                          List<MeteorShard> activeShards,
                                          Player player) {
        List<MeteorShard> created = new ArrayList<MeteorShard>();

        if (level < GameConfig.METEOR_CLUSTER_START_LEVEL) {
            return created;
        }

        if (activeShards.size() >= GameConfig.MAX_METEOR_SHARDS_ON_SCREEN) {
            return created;
        }

        // More common cluster pressure. The max active shard cap prevents
        // unreadable full-screen spam.
        int interval = Math.max(16, 58 - level * 9);
        if (frameCount % interval != 0) {
            return created;
        }

        int remainingSlots = GameConfig.MAX_METEOR_SHARDS_ON_SCREEN - activeShards.size();
        int clusterSize = Math.min(remainingSlots, Math.min(GameConfig.MAX_METEOR_CLUSTER_SIZE, 6 + level));
        int[][] pattern = choosePattern(clusterSize);
        int spawnZone = chooseSpawnZone(level);

        int baseSpeed = Math.max(GameConfig.MIN_METEOR_SHARD_SPEED, 4 - level / 3);

        for (int i = 0; i < pattern.length && created.size() < clusterSize; i++) {
            int[] spawn = calculateSpawnPoint(spawnZone, pattern[i], player);
            int[] direction = calculateDirection(spawnZone, spawn[0], spawn[1], player);

            // Small per-shard variation keeps the cluster jagged without making
            // every shard unfairly homing.
            direction = applyJaggedVariation(spawnZone, direction[0], direction[1]);

            int shardSpeed = baseSpeed + random.nextInt(3);
            char symbol = chooseShardSymbol(i);
            created.add(new MeteorShard(spawn[0], spawn[1], boardWidth, boardHeight,
                    shardSpeed, direction[0], direction[1], symbol));
        }

        return created;
    }

    private int chooseSpawnZone(int level) {
        if (level < GameConfig.ALL_DIRECTION_CLUSTER_START_LEVEL) {
            return random.nextInt(3); // top-left, top-center, top-right only
        }

        // Higher levels open all edges. Top zones still appear often so the
        // game keeps its shooter readability, while side/bottom waves add chaos.
        int roll = random.nextInt(100);
        if (roll < 18) return TOP_LEFT;
        if (roll < 34) return TOP_CENTER;
        if (roll < 52) return TOP_RIGHT;
        if (roll < 65) return LEFT_EDGE;
        if (roll < 78) return RIGHT_EDGE;
        if (roll < 86) return BOTTOM_LEFT;
        if (roll < 94) return BOTTOM_RIGHT;
        return BOTTOM_CENTER;
    }

    private int[] calculateSpawnPoint(int zone, int[] offset, Player player) {
        int safety = GameConfig.METEOR_CLUSTER_PLAYER_SAFETY_RADIUS;
        int ox = offset[0];
        int oy = offset[1];

        if (zone == TOP_LEFT) {
            int anchorX = 1 + random.nextInt(Math.max(1, boardWidth / 7));
            return new int[] { anchorX + ox, -7 + oy };
        }

        if (zone == TOP_RIGHT) {
            int anchorX = boardWidth - 2 - random.nextInt(Math.max(1, boardWidth / 7));
            return new int[] { anchorX - ox, -7 + oy };
        }

        if (zone == TOP_CENTER) {
            int anchorX = avoidPlayerColumn(boardWidth / 2 - 7 + random.nextInt(14), player.getX(), safety);
            return new int[] { anchorX + ox, -7 + oy };
        }

        if (zone == LEFT_EDGE) {
            int anchorY = avoidPlayerRow(2 + random.nextInt(Math.max(1, boardHeight - 5)), player.getY(), safety);
            return new int[] { -7 + oy, anchorY + ox };
        }

        if (zone == RIGHT_EDGE) {
            int anchorY = avoidPlayerRow(2 + random.nextInt(Math.max(1, boardHeight - 5)), player.getY(), safety);
            return new int[] { boardWidth + 6 - oy, anchorY + ox };
        }

        if (zone == BOTTOM_LEFT) {
            int anchorX = 1 + random.nextInt(Math.max(1, boardWidth / 6));
            return new int[] { anchorX + ox, boardHeight + 6 - oy };
        }

        if (zone == BOTTOM_RIGHT) {
            int anchorX = boardWidth - 2 - random.nextInt(Math.max(1, boardWidth / 6));
            return new int[] { anchorX - ox, boardHeight + 6 - oy };
        }

        // bottom-center
        int anchorX = avoidPlayerColumn(boardWidth / 2 - 7 + random.nextInt(14), player.getX(), safety);
        return new int[] { anchorX + ox, boardHeight + 6 - oy };
    }

    private int[] calculateDirection(int zone, int startX, int startY, Player player) {
        if (zone == TOP_LEFT) return new int[] { 1, 1 };
        if (zone == TOP_RIGHT) return new int[] { -1, 1 };
        if (zone == TOP_CENTER) return new int[] { Integer.compare(player.getX(), startX), 1 };
        if (zone == LEFT_EDGE) return new int[] { 1, Integer.compare(player.getY(), startY) };
        if (zone == RIGHT_EDGE) return new int[] { -1, Integer.compare(player.getY(), startY) };
        if (zone == BOTTOM_LEFT) return new int[] { 1, -1 };
        if (zone == BOTTOM_RIGHT) return new int[] { -1, -1 };
        return new int[] { Integer.compare(player.getX(), startX), -1 };
    }

    private int[] applyJaggedVariation(int zone, int dx, int dy) {
        int roll = random.nextInt(100);

        if (zone == TOP_LEFT || zone == TOP_CENTER || zone == TOP_RIGHT) {
            if (roll < 20) dx = 0;
            else if (roll > 82) dx = -dx;
            return new int[] { dx, 1 };
        }

        if (zone == BOTTOM_LEFT || zone == BOTTOM_CENTER || zone == BOTTOM_RIGHT) {
            if (roll < 20) dx = 0;
            else if (roll > 82) dx = -dx;
            return new int[] { dx, -1 };
        }

        // Side waves mainly move inward but may slant up/down.
        if (roll < 25) dy = 0;
        else if (roll > 78) dy = random.nextBoolean() ? -1 : 1;
        return new int[] { dx, dy };
    }

    private int avoidPlayerColumn(int proposedX, int playerX, int safety) {
        proposedX = Math.max(1, Math.min(boardWidth - 2, proposedX));
        if (Math.abs(proposedX - playerX) >= safety) {
            return proposedX;
        }

        if (playerX < boardWidth / 2) {
            return Math.min(boardWidth - 2, playerX + safety + random.nextInt(4));
        }
        return Math.max(1, playerX - safety - random.nextInt(4));
    }

    private int avoidPlayerRow(int proposedY, int playerY, int safety) {
        proposedY = Math.max(1, Math.min(boardHeight - 2, proposedY));
        if (Math.abs(proposedY - playerY) >= safety) {
            return proposedY;
        }

        if (playerY < boardHeight / 2) {
            return Math.min(boardHeight - 2, playerY + safety + random.nextInt(3));
        }
        return Math.max(1, playerY - safety - random.nextInt(3));
    }

    private int[][] choosePattern(int clusterSize) {
        if (clusterSize <= 3) return CLUSTER_PATTERNS[0];
        if (clusterSize == 4) return CLUSTER_PATTERNS[1];
        if (clusterSize == 5) return CLUSTER_PATTERNS[2];
        if (clusterSize == 6) return CLUSTER_PATTERNS[3];
        if (clusterSize == 7) return CLUSTER_PATTERNS[4];
        if (clusterSize == 8) return CLUSTER_PATTERNS[5];
        if (clusterSize == 9) return CLUSTER_PATTERNS[6];
        if (clusterSize == 10) return CLUSTER_PATTERNS[7];
        if (clusterSize == 11) return CLUSTER_PATTERNS[8];
        return CLUSTER_PATTERNS[9];
    }

    private char chooseShardSymbol(int index) {
        if (index % 3 == 0) return GameSymbols.METEOR_SHARD_SOLID;
        if (index % 3 == 1) return GameSymbols.METEOR_SHARD_LIGHT;
        return GameSymbols.METEOR_SHARD_CHIP;
    }
}
