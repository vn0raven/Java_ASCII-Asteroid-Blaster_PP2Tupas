package asteroidgame.objects;

import asteroidgame.core.GameBoard;
import asteroidgame.core.GameColors;
import asteroidgame.core.GameConfig;

/**
 * Parent asteroid class. Asteroids use damage stages, but every stage stays large.
 * Stage 3: armored asteroid, Stage 2: cracked asteroid, Stage 1: weak asteroid.
 * The asteroid is drawn across three grid cells so it is not annoying to hit.
 */
public abstract class Asteroid extends GameObject {
    public static final int ARMORED_STAGE = 3;
    public static final int CRACKED_STAGE = 2;
    public static final int WEAK_STAGE = 1;

    private static final int HIT_RADIUS = 1;

    private int speed;
    private int moveCounter;
    private int boardHeight;
    private int damageStage;
    private boolean escaped;

    public Asteroid(int x, int y, int boardHeight, int speed, int damageStage) {
        super(x, y, symbolForStage(damageStage));
        this.boardHeight = boardHeight;
        this.speed = Math.max(GameConfig.MIN_ASTEROID_SPEED, speed);
        this.damageStage = damageStage;
        this.moveCounter = 0;
        this.escaped = false;
        refreshSymbol();
    }

    @Override
    public void update() {
        moveCounter++;

        if (moveCounter >= speed) {
            y++;
            moveCounter = 0;
        }

        if (y >= boardHeight) {
            active = false;
            escaped = true;
        }
    }

    @Override
    public void draw(GameBoard board) {
        if (!active) return;

        java.awt.Color asteroidColor = (getDamageStage() <= CRACKED_STAGE) 
            ? GameColors.ASTEROID_RED 
            : GameColors.ASTEROID_BROWN;

        board.placeSymbol(x - 1, y, symbol); // Left
        board.placeSymbol(x, y, symbol);     // Center
        board.placeSymbol(x + 1, y, symbol); // Right
    }

    /**
     * Returns true if the asteroid was destroyed.
     * Returns false if it only changed to the next damaged large stage.
     */
    public boolean takeHit() {
        damageStage--;

        if (damageStage <= 0) {
            active = false;
            escaped = false;
            return true;
        }

        refreshSymbol();
        return false;
    }

    /**
     * Bullet hitbox: the asteroid is wide and forgiving to shoot.
     */
    public boolean isHitBy(int targetX, int targetY) {
        boolean horizontallyInside = Math.abs(targetX - x) <= HIT_RADIUS;
        boolean verticallyClose = Math.abs(targetY - y) <= 1;
        return active && horizontallyInside && verticallyClose;
    }

    /**
     * Player collision hitbox: asteroid hits the ship when it reaches the same row.
     */
    public boolean overlapsPoint(int targetX, int targetY) {
        boolean horizontallyInside = Math.abs(targetX - x) <= HIT_RADIUS;
        boolean sameRow = targetY == y;
        return active && horizontallyInside && sameRow;
    }

    public boolean hasEscaped() {
        return escaped;
    }

    public int getDamageStage() {
        return damageStage;
    }

    public int getHitScoreValue() {
        if (damageStage == ARMORED_STAGE) return 10;
        if (damageStage == CRACKED_STAGE) return 8;
        return 6;
    }

    public int getDestroyScoreValue() {
        return 22;
    }

    private void refreshSymbol() {
        symbol = symbolForStage(damageStage);
    }

    private static char symbolForStage(int damageStage) {
        if (damageStage >= ARMORED_STAGE) return GameSymbols.ASTEROID_HEAVY;
        if (damageStage == CRACKED_STAGE) return GameSymbols.ASTEROID_NORMAL;
        return GameSymbols.ASTEROID_WEAK;
    }
}
