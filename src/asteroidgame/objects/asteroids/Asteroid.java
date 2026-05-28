package asteroidgame.objects.asteroids;

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
 * Parent asteroid class.
 *
 * Asteroids are multi-cell faceted sprites instead of simple circles.
 * Each asteroid keeps its own family, damage stage, visual variant, and
 * movement vector. Spawners can now send asteroids diagonally from the
 * top-left, top-center, or top-right area toward the player.
 */
public abstract class Asteroid extends GameObject {
    public static final int ARMORED_STAGE = 3;
    public static final int CRACKED_STAGE = 2;
    public static final int WEAK_STAGE = 1;

    private int speed;
    private int moveCounter;
    private int boardHeight;
    private int damageStage;
    private int spriteFamily;
    private int spriteVariant;
    private boolean escaped;
    private int moveDx;
    private int moveDy;

    public Asteroid(int x, int y, int boardHeight, int speed, int damageStage, int spriteFamily) {
        this(x, y, boardHeight, speed, damageStage, spriteFamily, 0, 1);
    }

    public Asteroid(int x, int y, int boardHeight, int speed, int damageStage, int spriteFamily, int moveDx, int moveDy) {
        super(x, y, GameSymbols.ASTEROID_NORMAL);
        this.boardHeight = boardHeight;
        this.speed = Math.max(GameConfig.MIN_ASTEROID_SPEED, speed);
        this.damageStage = damageStage;
        this.spriteFamily = spriteFamily;
        this.spriteVariant = AsteroidSprites.randomVariant(spriteFamily);
        this.moveCounter = 0;
        this.escaped = false;
        this.moveDx = clampDirection(moveDx);
        this.moveDy = moveDy == 0 ? 1 : clampDirection(moveDy);
        refreshSymbol();
    }

    @Override
    public void update() {
        moveCounter++;

        if (moveCounter >= speed) {
            x += moveDx;
            y += moveDy;
            moveCounter = 0;
        }

        if (y >= boardHeight || getRightX() < 0 || getLeftX() >= GameConfig.BOARD_WIDTH) {
            active = false;
            escaped = true;
        }
    }

    @Override
    public void draw(GameBoard board) {
        if (!active) return;

        char[][] sprite = getCurrentSprite();
        int left = getLeftX();

        for (int row = 0; row < sprite.length; row++) {
            for (int col = 0; col < sprite[row].length; col++) {
                char symbolToDraw = sprite[row][col];
                if (AsteroidSprites.isSolid(symbolToDraw)) {
                    board.placeSymbol(left + col, y + row, symbolToDraw);
                }
            }
        }
    }

    /**
     * Returns true if the asteroid was destroyed.
     * Returns false if it only changed to the next visual damage stage.
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
     * Bullet hitbox checks the whole sprite instead of only a center point.
     * It is slightly forgiving so cracks or empty detail cells do not feel unfair.
     */
    public boolean isHitBy(int targetX, int targetY) {
        if (!active) return false;

        char[][] sprite = getCurrentSprite();
        int left = getLeftX();
        int right = left + getSpriteWidth() - 1;
        int bottom = y + sprite.length - 1;

        if (targetX < left - 1 || targetX > right + 1) return false;
        if (targetY < y - 1 || targetY > bottom) return false;

        for (int checkY = targetY - 1; checkY <= targetY + 1; checkY++) {
            int localY = checkY - y;
            if (localY < 0 || localY >= sprite.length) continue;

            for (int checkX = targetX - 1; checkX <= targetX + 1; checkX++) {
                int localX = checkX - left;
                if (localX < 0 || localX >= sprite[localY].length) continue;

                if (AsteroidSprites.isSolid(sprite[localY][localX])) {
                    return true;
                }
            }
        }

        return true;
    }

    /**
     * Player collision uses the sprite footprint, not just the asteroid center.
     */
    public boolean overlapsPoint(int targetX, int targetY) {
        if (!active) return false;

        char[][] sprite = getCurrentSprite();
        int left = getLeftX();
        int localX = targetX - left;
        int localY = targetY - y;

        if (localY < 0 || localY >= sprite.length) return false;
        if (localX < 0 || localX >= sprite[localY].length) return false;

        return AsteroidSprites.isSolid(sprite[localY][localX]);
    }

    public boolean hasEscaped() {
        return escaped;
    }

    public int getDamageStage() {
        return damageStage;
    }

    public int getSpriteWidth() {
        return getCurrentSprite()[0].length;
    }

    public int getSpriteHeight() {
        return getCurrentSprite().length;
    }

    public int getLeftX() {
        return x - getSpriteWidth() / 2;
    }

    public int getRightX() {
        return getLeftX() + getSpriteWidth() - 1;
    }


    public int getCenterY() {
        return y + getSpriteHeight() / 2;
    }

    public int getShrapnelCount() {
        if (spriteFamily == AsteroidSprites.FAMILY_HEAVY) return GameConfig.HEAVY_SHRAPNEL_COUNT;
        if (spriteFamily == AsteroidSprites.FAMILY_STANDARD) return GameConfig.NORMAL_SHRAPNEL_COUNT;
        return GameConfig.LIGHT_SHRAPNEL_COUNT;
    }

    public int getMoveDx() {
        return moveDx;
    }

    public int getMoveDy() {
        return moveDy;
    }

    public int getHitScoreValue() {
        if (spriteFamily == AsteroidSprites.FAMILY_HEAVY) {
            if (damageStage >= ARMORED_STAGE) return 10;
            if (damageStage >= CRACKED_STAGE) return 8;
            return 6;
        }

        if (spriteFamily == AsteroidSprites.FAMILY_STANDARD) {
            if (damageStage >= CRACKED_STAGE) return 8;
            return 6;
        }

        return 5;
    }

    public int getDestroyScoreValue() {
        if (spriteFamily == AsteroidSprites.FAMILY_HEAVY) return 36;
        if (spriteFamily == AsteroidSprites.FAMILY_STANDARD) return 26;
        return 18;
    }

    private int clampDirection(int value) {
        if (value < 0) return -1;
        if (value > 0) return 1;
        return 0;
    }

    private char[][] getCurrentSprite() {
        return AsteroidSprites.getSprite(spriteFamily, damageStage, spriteVariant);
    }

    private void refreshSymbol() {
        if (damageStage >= ARMORED_STAGE) {
            symbol = GameSymbols.ASTEROID_HEAVY;
        } else if (damageStage == CRACKED_STAGE) {
            symbol = GameSymbols.ASTEROID_NORMAL;
        } else {
            symbol = GameSymbols.ASTEROID_WEAK;
        }
    }
}
