package asteroidgame.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Player spaceship controlled by the user.
 * The player can move, shoot, lose lives, and receive upgrades.
 */
public class Player extends GameObject {
    public static final int MAX_LIFE_CAP = 6;
    public static final int FIRE_RATE_CAP = 3;
    public static final int SHOT_CAP = 2;
    public static final int SHIELD_CAP = 2;

    private int lives;
    private int maxLives;
    private int boardWidth;
    private int lastShotFrame;
    private int shotCooldown;
    private int fireRateLevel;
    private int shotLevel;
    private int shieldCharges;

    public Player(int x, int y, int boardWidth) {
        super(x, y, GameSymbols.PLAYER);
        this.lives = 3;
        this.maxLives = 3;
        this.boardWidth = boardWidth;
        this.lastShotFrame = -100;
        this.shotCooldown = 7; // slower starting fire rate for better arcade balance
        this.fireRateLevel = 0;
        this.shotLevel = 0;
        this.shieldCharges = 0;
    }

    @Override
    public void update() {
        // Movement is handled through moveLeft() and moveRight().
    }

    public void moveLeft() {
        if (x > 0) {
            x--;
        }
    }

    public void moveRight() {
        if (x < boardWidth - 1) {
            x++;
        }
    }

    public List<Bullet> shoot(int currentFrame) {
        List<Bullet> firedBullets = new ArrayList<Bullet>();

        if (currentFrame - lastShotFrame < shotCooldown) {
            return firedBullets;
        }

        lastShotFrame = currentFrame;

        // By spawning at 'y', the engine's immediate update shifts them to 'y - 1',
        // perfectly connecting the base of the laser to the nose of the ship!
        if (shotLevel == 0) {
            firedBullets.add(new Bullet(x, y, boardWidth));
        } else if (shotLevel == 1) {
            firedBullets.add(new Bullet(x - 1, y, boardWidth));
            firedBullets.add(new Bullet(x + 1, y, boardWidth));
        } else {
            firedBullets.add(new Bullet(x - 1, y, boardWidth));
            firedBullets.add(new Bullet(x, y, boardWidth));
            firedBullets.add(new Bullet(x + 1, y, boardWidth));
        }

        return firedBullets;
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
                shotCooldown = 5;
            } else if (fireRateLevel == 2) {
                shotCooldown = 4;
            } else {
                shotCooldown = 3; // cap: fastest allowed fire cooldown
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

    public boolean isAlive() {
        return lives > 0;
    }

    public void setHitSymbol(boolean hitFlash) {
        if (hitFlash) {
            symbol = GameSymbols.PLAYER_HIT;
        } else if (shieldCharges > 0) {
            symbol = GameSymbols.PLAYER_SHIELDED;
        } else {
            symbol = GameSymbols.PLAYER;
        }
    }
}
