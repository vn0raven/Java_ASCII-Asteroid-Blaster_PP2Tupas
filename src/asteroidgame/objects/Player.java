package asteroidgame.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Player spaceship controlled by the user.
 * The player can move in eight directions, visually face that direction,
 * shoot, lose lives, and receive upgrades.
 */
public class Player extends GameObject {
    public static final int MAX_LIFE_CAP = 6;
    public static final int FIRE_RATE_CAP = 3;
    public static final int SHOT_CAP = 2;
    public static final int SHIELD_CAP = 2;

    private int lives;
    private int maxLives;
    private int boardWidth;
    private int boardHeight;
    private int lastShotFrame;
    private int shotCooldown;
    private int fireRateLevel;
    private int shotLevel;
    private int shieldCharges;
    private int shotSideToggle;
    private int lastMoveDx;
    private int lastMoveDy;
    private char facingSymbol;

    public Player(int x, int y, int boardWidth, int boardHeight) {
        super(x, y, GameSymbols.PLAYER_UP);
        this.lives = 3;
        this.maxLives = 3;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.lastShotFrame = -100;
        this.shotCooldown = 8; // slower starting fire rate for better arcade balance
        this.fireRateLevel = 0;
        this.shotLevel = 0;
        this.shieldCharges = 0;
        this.shotSideToggle = 0;
        this.lastMoveDx = 0;
        this.lastMoveDy = -1;
        this.facingSymbol = GameSymbols.PLAYER_UP;
    }

    /**
     * Backward-compatible constructor for older tests or simple object creation.
     */
    public Player(int x, int y, int boardWidth) {
        this(x, y, boardWidth, Integer.MAX_VALUE);
    }

    @Override
    public void update() {
        // Movement is handled by move(dx, dy) from the game engine.
    }

    /**
     * Moves the player by one grid cell in any of the eight directions.
     * dx and dy are clamped to -1, 0, or 1 so diagonal movement remains controlled.
     */
    public void move(int dx, int dy) {
        dx = clampDirection(dx);
        dy = clampDirection(dy);

        if (dx == 0 && dy == 0) {
            return;
        }

        int nextX = x + dx;
        int nextY = y + dy;

        if (nextX >= 0 && nextX < boardWidth) {
            x = nextX;
        }

        if (nextY >= 0 && nextY < boardHeight) {
            y = nextY;
        }

        lastMoveDx = dx;
        lastMoveDy = dy;
        facingSymbol = determineFacingSymbol(dx, dy);
        symbol = facingSymbol;
    }

    private char determineFacingSymbol(int dx, int dy) {
        if (dx < 0 && dy < 0) return GameSymbols.PLAYER_UP_LEFT;
        if (dx > 0 && dy < 0) return GameSymbols.PLAYER_UP_RIGHT;
        if (dx < 0 && dy > 0) return GameSymbols.PLAYER_DOWN_LEFT;
        if (dx > 0 && dy > 0) return GameSymbols.PLAYER_DOWN_RIGHT;
        if (dx < 0) return GameSymbols.PLAYER_LEFT;
        if (dx > 0) return GameSymbols.PLAYER_RIGHT;
        if (dy > 0) return GameSymbols.PLAYER_DOWN;
        return GameSymbols.PLAYER_UP;
    }

    public void moveLeft() {
        move(-1, 0);
    }

    public void moveRight() {
        move(1, 0);
    }

    public void moveUp() {
        move(0, -1);
    }

    public void moveDown() {
        move(0, 1);
    }

    public List<Bullet> shoot(int currentFrame) {
        List<Bullet> firedBullets = new ArrayList<Bullet>();

        if (currentFrame - lastShotFrame < shotCooldown) {
            return firedBullets;
        }

        lastShotFrame = currentFrame;

        int shootDx = lastMoveDx;
        int shootDy = lastMoveDy;

        if (shootDx == 0 && shootDy == 0) {
            shootDy = -1;
        }

        shootDx = clampDirection(shootDx);
        shootDy = clampDirection(shootDy);

        // All shot upgrades now fire straight forward in the ship's facing direction.
        // The upgrade only changes the starting lanes, not the bullet travel direction.
        // Level 0: centered forward shot.
        // Level 1: two side shots.
        // Level 2: side + center + side.
        if (shotLevel <= 0) {
            addForwardBullet(firedBullets, shootDx, shootDy, 0);
        } else if (shotLevel == 1) {
            addForwardBullet(firedBullets, shootDx, shootDy, -1);
            addForwardBullet(firedBullets, shootDx, shootDy, 1);
        } else {
            addForwardBullet(firedBullets, shootDx, shootDy, -1);
            addForwardBullet(firedBullets, shootDx, shootDy, 0);
            addForwardBullet(firedBullets, shootDx, shootDy, 1);
        }

        return firedBullets;
    }

    private void addForwardBullet(List<Bullet> firedBullets, int shootDx, int shootDy, int sideLane) {
        int[] offset = getForwardShotOffset(shootDx, shootDy, sideLane);
        int bulletX = x + offset[0];
        int bulletY = y + offset[1];
        firedBullets.add(new Bullet(bulletX, bulletY, boardWidth, boardHeight, shootDx, shootDy));
    }

    private int[] getForwardShotOffset(int shootDx, int shootDy, int sideLane) {
        shootDx = clampDirection(shootDx);
        shootDy = clampDirection(shootDy);

        // Diagonal shots visually felt one row too high/low because the old
        // spawn point used both the x and y corner immediately. The center lane
        // now starts beside the ship on the same row, then travels diagonally.
        // This keeps the shot aligned with the visible diagonal ship symbol.
        if (shootDx != 0 && shootDy != 0) {
            if (sideLane == 0) {
                return new int[] { shootDx, 0 };
            }

            // Keep diagonal side lanes tight and horizontal to the ship body.
            // The bullets still move diagonally; only the spawn lane changes.
            if (sideLane < 0) {
                return new int[] { 0, 0 };
            }
            return new int[] { shootDx * 2, 0 };
        }

        // Center lane starts at the pointed/nose direction of the ship.
        if (sideLane == 0) {
            return new int[] { shootDx, shootDy };
        }

        // Straight vertical shots are widened slightly so their perceived
        // spacing matches the horizontal side lanes on a rectangular text grid.
        if (shootDx == 0 && shootDy != 0) {
            return new int[] { sideLane * 2, shootDy };
        }

        // Straight horizontal shots stay close to the ship: one row above/below.
        if (shootDx != 0 && shootDy == 0) {
            return new int[] { shootDx, sideLane };
        }

        return new int[] { shootDx, shootDy };
    }

    private int clampDirection(int value) {
        if (value < 0) return -1;
        if (value > 0) return 1;
        return 0;
    }

    public void loseLife() {
        if (lives > 0) {
            lives--;
        }
    }

    public void addLife() {
        if (lives < maxLives) {
            lives++;
        }
    }

    public void increaseMaxLives() {
        if (maxLives < MAX_LIFE_CAP) {
            maxLives++;
            lives = maxLives;
        }
    }

    public void upgradeFireRate() {
        if (fireRateLevel < FIRE_RATE_CAP) {
            fireRateLevel++;

            if (fireRateLevel == 1) {
                shotCooldown = 6;
            } else if (fireRateLevel == 2) {
                shotCooldown = 5;
            } else {
                shotCooldown = 4; // cap: fastest allowed fire cooldown
            }
        }
    }

    public void upgradeShotLevel() {
        if (shotLevel < SHOT_CAP) {
            shotLevel++;
        }
    }

    public void addShield() {
        if (shieldCharges < SHIELD_CAP) {
            shieldCharges++;
        }
    }

    public boolean hasShield() {
        return shieldCharges > 0;
    }

    public void useShield() {
        if (shieldCharges > 0) {
            shieldCharges--;
        }
    }

    public int getLives() { return lives; }
    public int getMaxLives() { return maxLives; }
    public int getShotCooldown() { return shotCooldown; }
    public int getFireRateLevel() { return fireRateLevel; }
    public int getShotLevel() { return shotLevel; }
    public int getShieldCharges() { return shieldCharges; }
    public int getLastMoveDx() { return lastMoveDx; }
    public int getLastMoveDy() { return lastMoveDy; }
    public char getFacingSymbol() { return facingSymbol; }

    public boolean isAlive() {
        return lives > 0;
    }

    public void setHitSymbol(boolean hitFlash) {
        if (hitFlash) {
            symbol = GameSymbols.PLAYER_HIT;
        } else {
            symbol = facingSymbol;
        }
    }
}
