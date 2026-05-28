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
 * Bullet fired by UFO enemies.
 * Enemy bullets are slower than player bullets so they remain dodgeable.
 */
public class EnemyBullet extends GameObject {
    private int boardWidth;
    private int boardHeight;
    private int moveDx;
    private int moveDy;
    private int moveCounter;
    private int moveDelay;

    public EnemyBullet(int x, int y, int boardWidth, int boardHeight, int moveDx, int moveDy) {
        super(x, y, determineBulletSymbol(moveDx, moveDy));
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.moveDx = clampDirection(moveDx);
        this.moveDy = clampDirection(moveDy);
        this.moveCounter = 0;
        this.moveDelay = GameConfig.ENEMY_BULLET_MOVE_DELAY;

        if (this.moveDx == 0 && this.moveDy == 0) {
            this.moveDy = 1;
            this.symbol = determineBulletSymbol(this.moveDx, this.moveDy);
        }

        if (x < 0 || x >= boardWidth || y < 0 || y >= boardHeight) {
            active = false;
        }
    }

    @Override
    public void update() {
        moveCounter++;

        if (moveCounter >= moveDelay) {
            x += moveDx;
            y += moveDy;
            moveCounter = 0;
        }

        if (x < 0 || x >= boardWidth || y < 0 || y >= boardHeight) {
            active = false;
        }
    }

    public boolean overlapsPoint(int targetX, int targetY) {
        return active && x == targetX && y == targetY;
    }

    private static int clampDirection(int value) {
        if (value < 0) return -1;
        if (value > 0) return 1;
        return 0;
    }

    private static char determineBulletSymbol(int dx, int dy) {
        dx = clampDirection(dx);
        dy = clampDirection(dy);

        if (dx == 0 && dy > 0) return GameSymbols.ENEMY_BULLET_DOWN;
        if (dx == 0 && dy < 0) return GameSymbols.ENEMY_BULLET_UP;
        if (dx < 0 && dy == 0) return GameSymbols.ENEMY_BULLET_LEFT;
        if (dx > 0 && dy == 0) return GameSymbols.ENEMY_BULLET_RIGHT;
        if (dx == dy) return GameSymbols.BULLET_DIAGONAL_BACK;
        return GameSymbols.BULLET_DIAGONAL_FORWARD;
    }
}
