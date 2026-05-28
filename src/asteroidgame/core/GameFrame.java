package asteroidgame.core;

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
 * Read-only snapshot of the current game screen and status.
 * The UI receives this object and renders it as retro arcade text.
 */
public class GameFrame {
    private int boardWidth;
    private int boardHeight;
    private char[][] grid;
    private int score;
    private int lives;
    private int maxLives;
    private int level;
    private int maxLevel;
    private String levelName;
    private int nextLevelScore;
    private int asteroidsDestroyed;
    private int powerUpsCollected;
    private int ufosDestroyed;
    private int bossHp;
    private int bossMaxHp;
    private int frameCount;
    private GameState state;
    private boolean playerHitFlash;
    private int fireRateLevel;
    private int fireRateCap;
    private int shotLevel;
    private int shotCap;
    private int shieldCharges;
    private int shieldCap;
    private int dangerLevel;
    private int dangerLimit;
    private String statusMessage;
    private String[] upgradeMenuLines;
    private int menuSelectedIndex;
    private int highScore;

    public GameFrame(int boardWidth, int boardHeight, char[][] grid,
                     int score, int lives, int maxLives, int level, int maxLevel,
                     String levelName, int nextLevelScore,
                     int asteroidsDestroyed, int powerUpsCollected, int ufosDestroyed,
                     int bossHp, int bossMaxHp,
                     int frameCount, GameState state, boolean playerHitFlash,
                     int fireRateLevel, int fireRateCap,
                     int shotLevel, int shotCap,
                     int shieldCharges, int shieldCap,
                     int dangerLevel, int dangerLimit,
                     String statusMessage,
                     String[] upgradeMenuLines,
                     int menuSelectedIndex,
                    int highScore) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.grid = grid;
        this.score = score;
        this.lives = lives;
        this.maxLives = maxLives;
        this.level = level;
        this.maxLevel = maxLevel;
        this.levelName = levelName;
        this.nextLevelScore = nextLevelScore;
        this.asteroidsDestroyed = asteroidsDestroyed;
        this.powerUpsCollected = powerUpsCollected;
        this.ufosDestroyed = ufosDestroyed;
        this.bossHp = bossHp;
        this.bossMaxHp = bossMaxHp;
        this.frameCount = frameCount;
        this.state = state;
        this.playerHitFlash = playerHitFlash;
        this.fireRateLevel = fireRateLevel;
        this.fireRateCap = fireRateCap;
        this.shotLevel = shotLevel;
        this.shotCap = shotCap;
        this.shieldCharges = shieldCharges;
        this.shieldCap = shieldCap;
        this.dangerLevel = dangerLevel;
        this.dangerLimit = dangerLimit;
        this.statusMessage = statusMessage;
        this.upgradeMenuLines = upgradeMenuLines;
        this.menuSelectedIndex = menuSelectedIndex;
        this.highScore = highScore;
    }

    public int getBoardWidth() { return boardWidth; }
    public int getBoardHeight() { return boardHeight; }
    public char[][] getGrid() { return grid; }
    public int getScore() { return score; }
    public int getLives() { return lives; }
    public int getMaxLives() { return maxLives; }
    public int getLevel() { return level; }
    public int getMaxLevel() { return maxLevel; }
    public String getLevelName() { return levelName; }
    public int getNextLevelScore() { return nextLevelScore; }
    public int getAsteroidsDestroyed() { return asteroidsDestroyed; }
    public int getPowerUpsCollected() { return powerUpsCollected; }
    public int getUfosDestroyed() { return ufosDestroyed; }
    public int getBossHp() { return bossHp; }
    public int getBossMaxHp() { return bossMaxHp; }
    public boolean hasBoss() { return bossMaxHp > 0; }
    public int getFrameCount() { return frameCount; }
    public GameState getState() { return state; }
    public boolean isPlayerHitFlash() { return playerHitFlash; }
    public int getFireRateLevel() { return fireRateLevel; }
    public int getFireRateCap() { return fireRateCap; }
    public int getShotLevel() { return shotLevel; }
    public int getShotCap() { return shotCap; }
    public int getShieldCharges() { return shieldCharges; }
    public int getShieldCap() { return shieldCap; }
    public int getDangerLevel() { return dangerLevel; }
    public int getDangerLimit() { return dangerLimit; }
    public String getStatusMessage() { return statusMessage; }
    public String[] getUpgradeMenuLines() { return upgradeMenuLines; }
    public int getMenuSelectedIndex() { return menuSelectedIndex; }
    public int getHighScore() { return highScore; } 

    public boolean isStartScreen() { return state == GameState.START_SCREEN; }
    public boolean isLevelIntro() { return state == GameState.LEVEL_INTRO; }
    public boolean isPlaying() { return state == GameState.PLAYING; }
    public boolean isPaused() { return state == GameState.PAUSED; }
    public boolean isLevelUp() { return state == GameState.LEVEL_UP; }
    public boolean isVictory() { return state == GameState.VICTORY; }
    public boolean isGameOver() { return state == GameState.GAME_OVER; }
    public boolean isHighScoreScreen() { return state == GameState.HIGH_SCORE_SCREEN; } 
}
