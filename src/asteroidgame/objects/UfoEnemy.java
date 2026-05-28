package asteroidgame.objects;

import asteroidgame.core.GameBoard;
import asteroidgame.core.GameConfig;

/**
 * Regular UFO enemy inspired by mobile UFO shooter games.
 * UFOs move across the upper arena, shoot at the player, and can be destroyed.
 */
public class UfoEnemy extends GameObject {
    /*
     * Compact symmetrical UFO sprite.
     * The old sprite used literal letters and a taller body, which made the UFO
     * look clunky and caused it to visually compete with asteroid sprites.
     * This version keeps the UFO readable while reducing clutter and overlap. It is 5x2, fully symmetrical, and compact enough for the larger arena.
     */
    private static final char[][] SPRITE = {
            {'╭', '─', '◉', '─', '╮'},
            {'╰', '═', '╩', '═', '╯'}
    };

    private int boardWidth;
    private int boardHeight;
    private int hp;
    private int moveDx;
    private int verticalDir;
    private int moveCounter;
    private int bobCounter;
    private int shotCooldown;
    private int lastShotFrame;

    public UfoEnemy(int x, int y, int boardWidth, int boardHeight, int level, int moveDx) {
        super(x, y, GameSymbols.UFO_CORE);
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.hp = level >= 5 ? 4 : 3;
        this.moveDx = moveDx < 0 ? -1 : 1;
        this.verticalDir = 1;
        this.moveCounter = 0;
        this.bobCounter = 0;
        this.shotCooldown = Math.max(34, GameConfig.UFO_BASE_SHOT_COOLDOWN - level * 4);
        this.lastShotFrame = -shotCooldown;
    }

    @Override
    public void update() {
        moveCounter++;
        bobCounter++;

        if (moveCounter >= GameConfig.UFO_MOVE_DELAY) {
            x += moveDx;
            moveCounter = 0;
        }

        if (getLeftX() <= 0) {
            x = getSpriteWidth() / 2;
            moveDx = 1;
        }

        if (getRightX() >= boardWidth - 1) {
            x = boardWidth - 1 - getSpriteWidth() / 2;
            moveDx = -1;
        }

        if (bobCounter >= GameConfig.UFO_BOB_DELAY) {
            y += verticalDir;
            bobCounter = 0;

            int minY = 1;
            int maxY = Math.max(3, boardHeight / 3);

            if (y <= minY) {
                y = minY;
                verticalDir = 1;
            }

            if (y >= maxY) {
                y = maxY;
                verticalDir = -1;
            }
        }
    }

    @Override
    public void draw(GameBoard board) {
        if (!active) return;

        int left = getLeftX();

        for (int row = 0; row < SPRITE.length; row++) {
            for (int col = 0; col < SPRITE[row].length; col++) {
                char symbolToDraw = SPRITE[row][col];
                if (symbolToDraw != ' ') {
                    board.placeSymbol(left + col, y + row, symbolToDraw);
                }
            }
        }
    }

    public EnemyBullet tryShoot(int frameCount, Player player) {
        if (!active) return null;
        if (frameCount - lastShotFrame < shotCooldown) return null;

        lastShotFrame = frameCount;

        int bulletX = x;
        int bulletY = getCenterY() + 1;
        int dx = Integer.compare(player.getX(), bulletX);
        int dy = Integer.compare(player.getY(), bulletY);

        if (dx == 0 && dy == 0) {
            dy = 1;
        }

        return new EnemyBullet(bulletX, bulletY, boardWidth, boardHeight, dx, dy);
    }

    public boolean takeHit() {
        hp--;

        if (hp <= 0) {
            active = false;
            return true;
        }

        return false;
    }

    public boolean isHitBy(int targetX, int targetY) {
        if (!active) return false;

        int left = getLeftX();
        int localX = targetX - left;
        int localY = targetY - y;

        if (localY < 0 || localY >= SPRITE.length) return false;
        if (localX < 0 || localX >= SPRITE[localY].length) return false;

        return SPRITE[localY][localX] != ' ';
    }

    public boolean overlapsPoint(int targetX, int targetY) {
        return isHitBy(targetX, targetY);
    }

    public int getHitScoreValue() {
        return 12;
    }

    public int getDestroyScoreValue() {
        return 80;
    }

    public int getSpriteWidth() {
        return SPRITE[0].length;
    }

    public int getSpriteHeight() {
        return SPRITE.length;
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
}
