package asteroidgame.objects.enemies;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Final Level 7 alien mothership boss.
 * The boss is larger than a regular UFO, has its own HP, and changes
 * its firing pattern as its health drops.
 */
public class AlienBoss extends GameObject {
    private static final char[][] SPRITE = {
            {' ', ' ', '▄', '█', '█', '█', '█', '█', '▄', ' ', ' '},
            {' ', '▄', '█', '▓', '◉', '▓', '◉', '▓', '█', '▄', ' '},
            {'<', '█', '▓', '▓', '█', '█', '█', '▓', '▓', '█', '>'},
            {' ', '▀', '█', '█', '▓', '╦', '▓', '█', '█', '▀', ' '},
            {' ', ' ', ' ', '▀', '█', '▀', '█', '▀', ' ', ' ', ' '}
    };

    private int boardWidth;
    private int boardHeight;
    private int hp;
    private int maxHp;
    private int moveDx;
    private int moveCounter;
    private int lastShotFrame;
    private int shotCooldown;

    public AlienBoss(int boardWidth, int boardHeight) {
        super(boardWidth / 2, 2, GameSymbols.UFO_CORE);
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.maxHp = GameConfig.BOSS_MAX_HP;
        this.hp = maxHp;
        this.moveDx = 1;
        this.moveCounter = 0;
        this.lastShotFrame = -GameConfig.BOSS_SHOT_COOLDOWN;
        this.shotCooldown = GameConfig.BOSS_SHOT_COOLDOWN;
    }

    @Override
    public void update() {
        if (!active) return;

        moveCounter++;
        if (moveCounter >= GameConfig.BOSS_MOVE_DELAY) {
            x += moveDx;
            moveCounter = 0;
        }

        if (getLeftX() <= 1) {
            x = 1 + getSpriteWidth() / 2;
            moveDx = 1;
        }

        if (getRightX() >= boardWidth - 2) {
            x = boardWidth - 2 - getSpriteWidth() / 2;
            moveDx = -1;
        }
    }

    @Override
    public void draw(GameBoard board) {
        if (!active) return;

        int left = getLeftX();
        for (int row = 0; row < SPRITE.length; row++) {
            for (int col = 0; col < SPRITE[row].length; col++) {
                char c = SPRITE[row][col];
                if (c != ' ') {
                    board.placeSymbol(left + col, y + row, c);
                }
            }
        }
    }

    public List<EnemyBullet> tryShoot(int frameCount, Player player) {
        List<EnemyBullet> shots = new ArrayList<EnemyBullet>();
        if (!active) return shots;
        if (frameCount - lastShotFrame < shotCooldown) return shots;

        lastShotFrame = frameCount;

        int phase = getPhase();
        int originY = y + getSpriteHeight();
        int centerX = x;
        int leftPort = getLeftX() + 2;
        int rightPort = getRightX() - 2;
        int aimDx = Integer.compare(player.getX(), centerX);

        // Core aimed pressure. This prevents the player from safely sitting off-center.
        shots.add(new EnemyBullet(centerX, originY, boardWidth, boardHeight, aimDx, 1));
        shots.add(new EnemyBullet(leftPort, originY - 1, boardWidth, boardHeight, Integer.compare(player.getX(), leftPort), 1));
        shots.add(new EnemyBullet(rightPort, originY - 1, boardWidth, boardHeight, Integer.compare(player.getX(), rightPort), 1));

        // Wide downward lanes. These punish staying beside the boss instead of moving.
        shots.add(new EnemyBullet(leftPort, originY, boardWidth, boardHeight, 0, 1));
        shots.add(new EnemyBullet(rightPort, originY, boardWidth, boardHeight, 0, 1));

        if (phase >= 2) {
            // Crossfire pattern.
            shots.add(new EnemyBullet(centerX - 4, originY, boardWidth, boardHeight, -1, 1));
            shots.add(new EnemyBullet(centerX + 4, originY, boardWidth, boardHeight, 1, 1));
            shots.add(new EnemyBullet(1, Math.max(6, player.getY()), boardWidth, boardHeight, 1, 0));
            shots.add(new EnemyBullet(boardWidth - 2, Math.max(6, player.getY()), boardWidth, boardHeight, -1, 0));
        }

        if (phase >= 3) {
            // Final phase: denser center and anti-corner pressure.
            shots.add(new EnemyBullet(centerX, originY, boardWidth, boardHeight, 0, 1));
            shots.add(new EnemyBullet(3, boardHeight - 8, boardWidth, boardHeight, 1, -1));
            shots.add(new EnemyBullet(boardWidth - 4, boardHeight - 8, boardWidth, boardHeight, -1, -1));
        }

        return shots;
    }

    public boolean takeHit() {
        hp--;
        if (hp <= 0) {
            hp = 0;
            active = false;
            return true;
        }
        return false;
    }

    public boolean isHitBy(int targetX, int targetY) {
        if (!active) return false;

        int localX = targetX - getLeftX();
        int localY = targetY - y;

        if (localY < 0 || localY >= SPRITE.length) return false;
        if (localX < 0 || localX >= SPRITE[localY].length) return false;

        return SPRITE[localY][localX] != ' ';
    }

    public boolean overlapsPoint(int targetX, int targetY) {
        return isHitBy(targetX, targetY);
    }

    public int getPhase() {
        if (hp <= maxHp / 3) return 3;
        if (hp <= (maxHp * 2) / 3) return 2;
        return 1;
    }

    public int getHitScoreValue() { return 10; }
    public int getDestroyScoreValue() { return 500; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getSpriteWidth() { return SPRITE[0].length; }
    public int getSpriteHeight() { return SPRITE.length; }
    public int getLeftX() { return x - getSpriteWidth() / 2; }
    public int getRightX() { return getLeftX() + getSpriteWidth() - 1; }
    public int getCenterY() { return y + getSpriteHeight() / 2; }
}
