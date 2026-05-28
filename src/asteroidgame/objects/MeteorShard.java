package asteroidgame.objects;

import asteroidgame.core.GameConfig;

/**
 * Small debris hazard used in meteor shard clusters and asteroid shrapnel bursts.
 *
 * Unlike the main faceted asteroids, meteor shards are not tank targets.
 * They are quick, visible debris pieces that create dodge-or-shoot pressure.
 */
public class MeteorShard extends GameObject {
    private int boardWidth;
    private int boardHeight;
    private int speed;
    private int moveCounter;
    private int moveDx;
    private int moveDy;
    private boolean escaped;
    private int scoreValue;
    private int age;
    private int activationDelay;

    public MeteorShard(int x, int y, int boardWidth, int boardHeight,
                       int speed, int moveDx, int moveDy, char symbol) {
        this(x, y, boardWidth, boardHeight, speed, moveDx, moveDy, symbol, 0);
    }

    public MeteorShard(int x, int y, int boardWidth, int boardHeight,
                       int speed, int moveDx, int moveDy, char symbol, int activationDelay) {
        super(x, y, symbol);
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.speed = Math.max(GameConfig.MIN_METEOR_SHARD_SPEED, speed);
        this.moveDx = clampDirection(moveDx);
        this.moveDy = clampDirection(moveDy);

        if (this.moveDx == 0 && this.moveDy == 0) {
            this.moveDy = 1;
        }
        this.moveCounter = 0;
        this.escaped = false;
        this.scoreValue = 6;
        this.age = 0;
        this.activationDelay = Math.max(0, activationDelay);
    }

    @Override
    public void update() {
        age++;
        moveCounter++;

        if (moveCounter >= speed) {
            x += moveDx;
            y += moveDy;
            moveCounter = 0;
        }

        if (x < 0 || x >= boardWidth || y < 0 || y >= boardHeight) {
            active = false;
            escaped = true;
        }
    }

    public boolean overlapsPoint(int targetX, int targetY) {
        return active && x == targetX && y == targetY;
    }

    public boolean isDangerous() {
        return age >= activationDelay;
    }

    public boolean hasEscaped() {
        return escaped;
    }

    public int getScoreValue() {
        return scoreValue;
    }

    private int clampDirection(int value) {
        if (value < 0) return -1;
        if (value > 0) return 1;
        return 0;
    }
}
