package asteroidgame.core;

import asteroidgame.managers.AsteroidSpawner;
import asteroidgame.managers.CollisionManager;
import asteroidgame.managers.CollisionResult;
import asteroidgame.managers.HighScoreManager;
import asteroidgame.managers.PowerUpSpawner;
import asteroidgame.managers.ScoreManager;
import asteroidgame.managers.UpgradeManager;
import asteroidgame.objects.Asteroid;
import asteroidgame.objects.Bullet;
import asteroidgame.objects.Drawable;
import asteroidgame.objects.Explosion;
import asteroidgame.objects.Player;
import asteroidgame.objects.PowerUp;
import asteroidgame.objects.Updatable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Pure game logic class.
 * It has no Swing imports, so gameplay stays separate from the UI.
 */
public class GameEngine {
    public static final int BOARD_WIDTH = GameConfig.BOARD_WIDTH;
    public static final int BOARD_HEIGHT = GameConfig.BOARD_HEIGHT;

    private GameBoard board;
    private Player player;
    private List<Bullet> bullets;
    private List<Asteroid> asteroids;
    private List<PowerUp> powerUps;
    private List<Explosion> explosions;
    private CollisionManager collisionManager;
    private ScoreManager scoreManager;
    private UpgradeManager upgradeManager;
    private Random random;
    private HighScoreManager highScoreManager;
    private AsteroidSpawner asteroidSpawner; 
    private PowerUpSpawner powerUpSpawner;

    private int frameCount;
    private int asteroidsDestroyed;
    private int powerUpsCollected;
    private int hitFlashCounter;
    private int dangerLevel;
    private int messageCounter;
    private String statusMessage;
    private GameState state;
    private boolean quitRequested;
    private int menuSelectedIndex;

    private String targetStatusMessage = ""; 
    private int typewriterIndex = 0;
    private int typewriterTimer = 0;
    

    public GameEngine() {
        board = new GameBoard(BOARD_WIDTH, BOARD_HEIGHT);
        bullets = new ArrayList<Bullet>();
        asteroids = new ArrayList<Asteroid>();
        powerUps = new ArrayList<PowerUp>();  
        explosions = new ArrayList<Explosion>();   
        collisionManager = new CollisionManager();
        scoreManager = new ScoreManager();
        upgradeManager = new UpgradeManager();
        highScoreManager = new HighScoreManager();
        asteroidSpawner = new AsteroidSpawner(BOARD_WIDTH, BOARD_HEIGHT); 
        powerUpSpawner = new PowerUpSpawner(BOARD_WIDTH, BOARD_HEIGHT); 
        random = new Random();   
        resetToStartScreen(); 
    }

    public void update(InputState input) {
        updateStatusTimer();

        if (input.isQuitRequested()) {
            quitRequested = true;
            return;
        }

        if (state == GameState.START_SCREEN) {
            // Move cursor up
            if (input.isUpRequested()) {
                SoundManager.playSound("scrollup.wav");
                menuSelectedIndex--;
                if (menuSelectedIndex < 0) menuSelectedIndex = 2; // Loop to bottom
            }
            
            // Move cursor down
            if (input.isDownRequested()) {
                SoundManager.playSound("scrolldown.wav");
                menuSelectedIndex++;
                if (menuSelectedIndex > 2) menuSelectedIndex = 0; // Loop to top
            }

            // Execute selection
            if (input.isStartRequested() || input.isShootRequested()) {
                SoundManager.playSound("click.wav");
                if (menuSelectedIndex == 0) {
                    resetAndShowLevelIntro();
                } else if (menuSelectedIndex == 1) {
                    state = GameState.HIGH_SCORE_SCREEN; 
                    setStatusMessage("LOCAL LEADERBOARD");
                } else if (menuSelectedIndex == 2) {
                    quitRequested = true;
                }
            }
            return;
        }

        if (state == GameState.HIGH_SCORE_SCREEN) {
            if (input.isMenuRequested() || input.isStartRequested()) {
                resetToStartScreen();
            }
            return;
        }

        if (state == GameState.LEVEL_INTRO) {
            if (input.isMenuRequested()) { 
                resetToStartScreen();
                return;
            }
            if (input.isStartRequested() || input.isShootRequested()) {
                SoundManager.playSound("click.wav");
                clearDangerousObjects();
                setStatusMessage("LEVEL " + scoreManager.getLevel() + " START!");
                state = GameState.PLAYING;
            }
            return;
        }

        if (state == GameState.VICTORY) {
            if (input.isMenuRequested()) { 
                resetToStartScreen();
                return;
            }
            if (input.isRestartRequested() || input.isStartRequested()) {
                resetAndShowLevelIntro();
            }
            return;
        }

        if (state == GameState.GAME_OVER) {
            if (input.isMenuRequested()) { 
                resetToStartScreen();
                return;
            }
            if (input.isRestartRequested() || input.isStartRequested()) {
                resetAndShowLevelIntro();
            }
            return;
        }

        if (state == GameState.LEVEL_UP) {
            handleUpgradeChoice(input);
            return;
        }

        if (state == GameState.PAUSED) {
            if (input.isMenuRequested()) { 
                resetToStartScreen();
                return;
            }
            if (input.isPauseRequested()) {
                state = GameState.PLAYING;
                setStatusMessage("RESUMED");
            }
            return;
        }

        if (input.isPauseRequested()) {
            state = GameState.PAUSED;
            setStatusMessage("PAUSED");
            return;
        }

        updatePlayingState(input);
    }

    private void handleUpgradeChoice(InputState input) {
        int choice = input.getUpgradeChoice();

        if (choice >= 1 && choice <= 4) {
            boolean applied = upgradeManager.applyUpgrade(player, choice);

            if (applied) {
                scoreManager.advanceLevel();
                clearDangerousObjects();
                setStatusMessage(upgradeManager.describeChoice(choice) + " UPGRADED");
                state = GameState.LEVEL_INTRO;
            } else {
                setStatusMessage(upgradeManager.describeChoice(choice) + " IS ALREADY MAXED");
            }
        }
    }

    private void updatePlayingState(InputState input) {
        frameCount++;

        if (hitFlashCounter > 0) {
            hitFlashCounter--;
        }

        handlePlayerInput(input);

        Asteroid newAsteroid = asteroidSpawner.attemptSpawn(frameCount, scoreManager.getLevel(), asteroids.size());
        if (newAsteroid != null) {
            asteroids.add(newAsteroid);
        }

        PowerUp newPowerUp = powerUpSpawner.attemptSpawn(frameCount, scoreManager.getLevel());
        if (newPowerUp != null) {
            powerUps.add(newPowerUp);
        }

        updateObjects();
        checkCollisions();
        removeInactiveObjects();

        if (!player.isAlive()) {
            SoundManager.stopMusic(); 
            
            highScoreManager.saveHighScore(scoreManager.getScore()); 
            clearDangerousObjects();
            state = GameState.GAME_OVER;
            setStatusMessage("GAME OVER");
            return;
        }

        if (scoreManager.isFinalLevelCleared()) {
            highScoreManager.saveHighScore(scoreManager.getScore()); 
            clearDangerousObjects();
            state = GameState.VICTORY;
            setStatusMessage("MISSION COMPLETE");
            return;
        }

        if (scoreManager.isReadyForLevelUp()) {
            clearDangerousObjects(); 
            state = GameState.LEVEL_UP;
            setStatusMessage("LEVEL CLEAR - CHOOSE UPGRADE");
        }

    }

    private void resetToStartScreen() {
        SoundManager.playMusic("menu_music.wav"); 
        
        resetFieldData();
        state = GameState.START_SCREEN;
        quitRequested = false;
        menuSelectedIndex = 0; 
        setStatusMessage("READY");
    }

    private void resetAndShowLevelIntro() {
        SoundManager.playMusic("game_music.wav"); 
        
        resetFieldData();
        state = GameState.LEVEL_INTRO;
        quitRequested = false;
        setStatusMessage("NEW RUN READY");
    }

    private void resetFieldData() {
        bullets.clear();
        asteroids.clear();
        powerUps.clear();
        scoreManager.reset();
        explosions.clear();

        int startX = BOARD_WIDTH / 2;
        int startY = BOARD_HEIGHT - 1;
        player = new Player(startX, startY, BOARD_WIDTH);

        frameCount = 0;
        asteroidsDestroyed = 0;
        powerUpsCollected = 0;
        hitFlashCounter = 0;
        dangerLevel = 0;
        statusMessage = "";
        messageCounter = 0;
    }

    private void clearDangerousObjects() {
        bullets.clear();
        asteroids.clear();
        powerUps.clear();
        explosions.clear();
        dangerLevel = 0;
    }

    private void handlePlayerInput(InputState input) {
        if (input.isLeftPressed()) {
            player.moveLeft();
        }

        if (input.isRightPressed()) {
            player.moveRight();
        }

        if (input.isShootRequested()) {
            List<Bullet> firedBullets = player.shoot(frameCount);
            if(!firedBullets.isEmpty()){
                SoundManager.playSound("shoot.wav");
            }
            bullets.addAll(firedBullets);
        }
    }

    private boolean isSpawnPositionClear(int x) {
        for (Asteroid asteroid : asteroids) {
            if (asteroid.isActive() && asteroid.getY() <= 2
                    && Math.abs(asteroid.getX() - x) < GameConfig.SPAWN_SEPARATION) {
                return false;
            }
        }

        return true;
    }

    private void updateObjects() {
        player.update();   
        updateList(bullets);
        updateList(asteroids);
        updateList(powerUps);
        updateList(explosions);
    }

    private void checkCollisions() {
        CollisionResult result = collisionManager.checkBulletAsteroidCollisions(bullets, asteroids, explosions);
        scoreManager.addPoints(result.getEarnedPoints());
        asteroidsDestroyed += result.getDestroyedCount();

        if (result.getDestroyedCount() > 0) {
            setStatusMessage("ASTEROID DESTROYED  +" + result.getEarnedPoints());
            SoundManager.playSound("explosion.wav");
        } else if (result.getDamagedCount() > 0) {
            setStatusMessage("ASTEROID CRACKED  +" + result.getEarnedPoints());
            SoundManager.playSound("hit.wav");
        }

        int collected = collisionManager.checkPlayerPowerUpCollision(player, powerUps);
        if (collected > 0) {
            powerUpsCollected += collected;
            scoreManager.addPoints(collected * 15);
            setStatusMessage("POWER-UP COLLECTED  +15");
        }

        boolean playerWasHit = collisionManager.checkPlayerAsteroidCollision(player, asteroids);
        if (playerWasHit) {
            if (player.hasShield()) {
                player.useShield();
                setStatusMessage("SHIELD BLOCKED THE HIT");
            } else {
                player.loseLife();
                setStatusMessage("SHIP DAMAGED  HP " + player.getLives() + "/" + player.getMaxLives());
            }
            hitFlashCounter = GameConfig.HIT_FLASH_DURATION;
        }
    }

    private void removeInactiveObjects() {
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            if (!bullet.isActive()) {
                bulletIterator.remove();
            }
        }

        Iterator<Asteroid> asteroidIterator = asteroids.iterator();
        while (asteroidIterator.hasNext()) {
            Asteroid asteroid = asteroidIterator.next();
            if (!asteroid.isActive()) {
                if (asteroid.hasEscaped()) {
                    registerMissedAsteroid();
                }
                asteroidIterator.remove();
            }
        }

        Iterator<Explosion> explosionIterator = explosions.iterator();
        while (explosionIterator.hasNext()) {
            Explosion explosion = explosionIterator.next();
            if (!explosion.isActive()) {
                explosionIterator.remove();
            }
        }

        Iterator<PowerUp> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp powerUp = powerUpIterator.next();
            if (!powerUp.isActive()) {
                powerUpIterator.remove();
            }
        }
    }

    private void registerMissedAsteroid() {
        dangerLevel++;

        if (dangerLevel >= GameConfig.DANGER_LIMIT) {
            dangerLevel = 0;
            player.loseLife();
            hitFlashCounter = GameConfig.HIT_FLASH_DURATION;
            setStatusMessage("DANGER LIMIT REACHED  -1 HP");
        } else {
            setStatusMessage("ASTEROID MISSED  DANGER " + dangerLevel + "/" + GameConfig.DANGER_LIMIT);
        }
    }

    private void setStatusMessage(String message) {
        targetStatusMessage = message;
        statusMessage = ""; 
        
        typewriterIndex = 0;
        typewriterTimer = 0;
        messageCounter = GameConfig.MESSAGE_DURATION;
    }

    private void updateStatusTimer() {
        if (targetStatusMessage != null && !targetStatusMessage.isEmpty()) {
            
            if (typewriterIndex < targetStatusMessage.length()) {
                typewriterTimer++;
                
                if (typewriterTimer >= 2) { 
                    statusMessage += targetStatusMessage.charAt(typewriterIndex);
                    typewriterIndex++;
                    typewriterTimer = 0;
                    
                    if (statusMessage.charAt(statusMessage.length() - 1) != ' ') {

                    }
                }
            } 
            else {
                if (messageCounter > 0) {
                    messageCounter--;
                } else {
                    statusMessage = "";
                    targetStatusMessage = "";
                }
            }
        }
    }

    public GameFrame getCurrentFrame() {
        board.clear();

        if (state == GameState.PLAYING || state == GameState.PAUSED
                || state == GameState.LEVEL_UP || state == GameState.GAME_OVER) {
            
            drawList(explosions);
            drawList(powerUps);
            drawList(asteroids);
            drawList(bullets);

            if (player.isAlive()) {
                player.setHitSymbol(hitFlashCounter > 0);
                player.draw(board);
            }
        }

        return new GameFrame(
                BOARD_WIDTH,
                BOARD_HEIGHT,
                board.copyGrid(),
                scoreManager.getScore(),
                player.getLives(),
                player.getMaxLives(),
                scoreManager.getLevel(),
                scoreManager.getMaxLevel(),
                scoreManager.getLevelName(),
                scoreManager.getNextLevelScore(),
                asteroidsDestroyed,
                powerUpsCollected,
                frameCount,
                state,
                hitFlashCounter > 0,
                player.getFireRateLevel(),
                Player.FIRE_RATE_CAP,
                player.getShotLevel(),
                Player.SHOT_CAP,
                player.getShieldCharges(),
                Player.SHIELD_CAP,
                dangerLevel,
                GameConfig.DANGER_LIMIT,
                statusMessage,
                upgradeManager.buildUpgradeMenu(player),
                menuSelectedIndex,
                highScoreManager.getHighScore()
        );
    }

    public boolean isQuitRequested() {
        return quitRequested;
    }

    private void updateList(List<? extends Updatable> list) {
        for (Updatable item : list) {
            item.update();
        }
    }

    private void drawList(List<? extends Drawable> list) {
        for (Drawable item : list) {
            if (item.isActive()) {
                item.draw(board);
            }
        }

    }
}
