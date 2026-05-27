package asteroidgame.core;

import asteroidgame.managers.CollisionManager;
import asteroidgame.managers.CollisionResult;
import asteroidgame.managers.ScoreManager;
import asteroidgame.managers.UpgradeManager;
import asteroidgame.objects.Asteroid;
import asteroidgame.objects.Bullet;
import asteroidgame.objects.ExtraLifePowerUp;
import asteroidgame.objects.HeavyAsteroid;
import asteroidgame.objects.NormalAsteroid;
import asteroidgame.objects.Player;
import asteroidgame.objects.PowerUp;

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
    private CollisionManager collisionManager;
    private ScoreManager scoreManager;
    private UpgradeManager upgradeManager;
    private Random random;

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

    public GameEngine() {
        board = new GameBoard(BOARD_WIDTH, BOARD_HEIGHT);
        bullets = new ArrayList<Bullet>();
        asteroids = new ArrayList<Asteroid>();
        powerUps = new ArrayList<PowerUp>();
        collisionManager = new CollisionManager();
        scoreManager = new ScoreManager();
        upgradeManager = new UpgradeManager();
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
                menuSelectedIndex--;
                if (menuSelectedIndex < 0) menuSelectedIndex = 2; // Loop to bottom
            }
            
            // Move cursor down
            if (input.isDownRequested()) {
                menuSelectedIndex++;
                if (menuSelectedIndex > 2) menuSelectedIndex = 0; // Loop to top
            }

            // Execute selection
            if (input.isStartRequested() || input.isShootRequested()) {
                if (menuSelectedIndex == 0) {
                    resetAndShowLevelIntro();
                } else if (menuSelectedIndex == 1) {
                    setStatusMessage("HIGH SCORES COMING SOON!");
                } else if (menuSelectedIndex == 2) {
                    quitRequested = true;
                }
            }
            return;
        }

        if (state == GameState.LEVEL_INTRO) {
            if (input.isStartRequested() || input.isShootRequested()) {
                clearDangerousObjects();
                setStatusMessage("LEVEL " + scoreManager.getLevel() + " START!");
                state = GameState.PLAYING;
            }
            return;
        }

        if (state == GameState.VICTORY) {
            if (input.isRestartRequested() || input.isStartRequested()) {
                resetAndShowLevelIntro();
            }
            return;
        }

        if (state == GameState.GAME_OVER) {
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
        spawnAsteroids();
        spawnPowerUps();
        updateObjects();
        checkCollisions();
        removeInactiveObjects();

        if (!player.isAlive()) {
            state = GameState.GAME_OVER;
            setStatusMessage("GAME OVER");
            return;
        }

        if (scoreManager.isFinalLevelCleared()) {
            clearDangerousObjects();
            state = GameState.VICTORY;
            setStatusMessage("MISSION COMPLETE");
            return;
        }

        if (scoreManager.isReadyForLevelUp()) {
            state = GameState.LEVEL_UP;
            setStatusMessage("LEVEL CLEAR - CHOOSE UPGRADE");
        }
    }

    private void resetToStartScreen() {
        resetFieldData();
        state = GameState.START_SCREEN;
        quitRequested = false;
        menuSelectedIndex = 0; // Starts cursor on "PLAY"
        setStatusMessage("READY");
    }

    private void resetAndShowLevelIntro() {
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
            bullets.addAll(firedBullets);
        }
    }

    private void spawnAsteroids() {
        int level = scoreManager.getLevel();
        int spawnInterval = Math.max(20, 44 - level * 3);

        if (asteroids.size() >= GameConfig.MAX_ASTEROIDS_ON_SCREEN) {
            return;
        }

        if (frameCount % spawnInterval == 0) {
            asteroids.add(createRandomAsteroid(level));
        }

        if (level >= 5 && frameCount % (spawnInterval * 2) == 0
                && asteroids.size() < GameConfig.MAX_ASTEROIDS_ON_SCREEN) {
            asteroids.add(createRandomAsteroid(level));
        }
    }

    private Asteroid createRandomAsteroid(int level) {
        int x = chooseSpawnX();
        int roll = random.nextInt(100);

        int normalSpeed = Math.max(GameConfig.MIN_ASTEROID_SPEED, 9 - level / 2);
        int heavySpeed = Math.max(GameConfig.MIN_HEAVY_ASTEROID_SPEED, 10 - level / 2);

        // No tiny asteroid spawns. The game only creates big, readable targets.
        if (level >= 3 && roll < 35 + level * 5) {
            return new HeavyAsteroid(x, 0, BOARD_HEIGHT, heavySpeed);
        }

        return new NormalAsteroid(x, 0, BOARD_HEIGHT, normalSpeed);
    }

    private int chooseSpawnX() {
        for (int attempt = 0; attempt < 15; attempt++) {
            int x = 1 + random.nextInt(BOARD_WIDTH - 2);

            if (isSpawnPositionClear(x)) {
                return x;
            }
        }

        return 1 + random.nextInt(BOARD_WIDTH - 2);
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

    private void spawnPowerUps() {
        int powerUpInterval = Math.max(160, 260 - scoreManager.getLevel() * 10);

        if (frameCount > 0 && frameCount % powerUpInterval == 0) {
            int chance = random.nextInt(100);

            if (chance < 40) {
                int x = random.nextInt(BOARD_WIDTH);
                powerUps.add(new ExtraLifePowerUp(x, 0, BOARD_HEIGHT));
            }
        }
    }

    private void updateObjects() {
        player.update();

        for (Bullet bullet : bullets) {
            bullet.update();
        }

        for (Asteroid asteroid : asteroids) {
            asteroid.update();
        }

        for (PowerUp powerUp : powerUps) {
            powerUp.update();
        }
    }

    private void checkCollisions() {
        CollisionResult result = collisionManager.checkBulletAsteroidCollisions(bullets, asteroids);
        scoreManager.addPoints(result.getEarnedPoints());
        asteroidsDestroyed += result.getDestroyedCount();

        if (result.getDestroyedCount() > 0) {
            setStatusMessage("ASTEROID DESTROYED  +" + result.getEarnedPoints());
        } else if (result.getDamagedCount() > 0) {
            setStatusMessage("ASTEROID CRACKED  +" + result.getEarnedPoints());
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
        statusMessage = message;
        messageCounter = GameConfig.MESSAGE_DURATION;
    }

    private void updateStatusTimer() {
        if (messageCounter > 0) {
            messageCounter--;
        }

        if (messageCounter == 0) {
            statusMessage = "";
        }
    }

    public GameFrame getCurrentFrame() {
        board.clear();

        if (state == GameState.PLAYING || state == GameState.PAUSED
                || state == GameState.LEVEL_UP || state == GameState.GAME_OVER) {
            for (PowerUp powerUp : powerUps) {
                powerUp.draw(board);
            }

            for (Asteroid asteroid : asteroids) {
                asteroid.draw(board);
            }

            for (Bullet bullet : bullets) {
                bullet.draw(board);
            }

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
                menuSelectedIndex
        );
    }

    public boolean isQuitRequested() {
        return quitRequested;
    }
}
