package asteroidgame.objects.projectiles;

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
 * Bullet fired by the player ship.
 *
 * Bullets support eight-direction movement. Player bullets move faster than
 * other hazards, but keep a collision trail so they do not skip through
 * asteroids when moving two cells per update.
 */
public class Bullet extends GameObject {
    private int boardWidth;
    private int boardHeight;
    private int moveDx;
    private int moveDy;
    private int previousX;
    private int previousY;
    private int speed;

    /**
     * Backward-compatible upward bullet constructor.
     */
    public Bullet(int x, int y, int boardWidth) {
        this(x, y, boardWidth, Integer.MAX_VALUE, 0, -1);
    }

    public Bullet(int x, int y, int boardWidth, int boardHeight, int moveDx, int moveDy) {
        super(x, y, determineBulletSymbol(moveDx, moveDy));
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.moveDx = clampDirection(moveDx);
        this.moveDy = clampDirection(moveDy);
        this.previousX = x;
        this.previousY = y;
        this.speed = determineBulletSpeed(this.moveDx, this.moveDy);

        if (this.moveDx == 0 && this.moveDy == 0) {
            this.moveDy = -1;
            this.symbol = determineBulletSymbol(this.moveDx, this.moveDy);
            this.speed = determineBulletSpeed(this.moveDx, this.moveDy);
        }

        if (x < 0 || x >= boardWidth || y < 0 || y >= boardHeight) {
            active = false;
        }
    }

    @Override
    public void update() {
        previousX = x;
        previousY = y;

        for (int step = 0; step < speed; step++) {
            x += moveDx;
            y += moveDy;

            if (y < 0 || y >= boardHeight || x < 0 || x >= boardWidth) {
                active = false;
                break;
            }
        }
    }

    public int getMoveDx() {
        return moveDx;
    }

    public int getMoveDy() {
        return moveDy;
    }

    public int getPreviousX() {
        return previousX;
    }

    public int getPreviousY() {
        return previousY;
    }

    public int getPathStepCount() {
        return Math.max(Math.abs(x - previousX), Math.abs(y - previousY));
    }

    public int getPathX(int step) {
        return previousX + moveDx * step;
    }

    public int getPathY(int step) {
        return previousY + moveDy * step;
    }

    private static int clampDirection(int value) {
        if (value < 0) return -1;
        if (value > 0) return 1;
        return 0;
    }

    private static int determineBulletSpeed(int dx, int dy) {
        dx = clampDirection(dx);
        dy = clampDirection(dy);

        if (dx != 0 && dy == 0) {
            return GameConfig.PLAYER_BULLET_HORIZONTAL_SPEED;
        }

        if (dx == 0 && dy != 0) {
            return GameConfig.PLAYER_BULLET_VERTICAL_SPEED;
        }

        return GameConfig.PLAYER_BULLET_DIAGONAL_SPEED;
    }

    private static char determineBulletSymbol(int dx, int dy) {
        dx = clampDirection(dx);
        dy = clampDirection(dy);

        if (dx == 0) return GameSymbols.BULLET_VERTICAL;
        if (dy == 0) return GameSymbols.BULLET_HORIZONTAL;
        if (dx == dy) return GameSymbols.BULLET_DIAGONAL_BACK;
        return GameSymbols.BULLET_DIAGONAL_FORWARD;
    }
}
