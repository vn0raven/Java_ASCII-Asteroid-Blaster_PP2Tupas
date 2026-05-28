package asteroidgame.state;

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
 * Plain input data object used by the game engine.
 *
 * Pressed inputs are continuous and are used for gameplay movement.
 * Requested inputs are one-shot and are used for menus, pause, restart,
 * upgrade choices, and other single actions.
 */
public class InputState {
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean upPressed;
    private boolean downPressed;

    private boolean upRequested;
    private boolean downRequested;
    private boolean shootRequested;
    private boolean startRequested;
    private boolean pauseRequested;
    private boolean restartRequested;
    private boolean quitRequested;
    private boolean menuRequested;
    private int upgradeChoice;

    public InputState(boolean leftPressed, boolean rightPressed,
                      boolean upPressed, boolean downPressed,
                      boolean upRequested, boolean downRequested,
                      boolean shootRequested, boolean startRequested,
                      boolean pauseRequested, boolean restartRequested,
                      boolean quitRequested, boolean menuRequested,
                      int upgradeChoice) {
        this.leftPressed = leftPressed;
        this.rightPressed = rightPressed;
        this.upPressed = upPressed;
        this.downPressed = downPressed;
        this.upRequested = upRequested;
        this.downRequested = downRequested;
        this.shootRequested = shootRequested;
        this.startRequested = startRequested;
        this.pauseRequested = pauseRequested;
        this.restartRequested = restartRequested;
        this.quitRequested = quitRequested;
        this.menuRequested = menuRequested;
        this.upgradeChoice = upgradeChoice;
    }

    public boolean isLeftPressed() { return leftPressed; }
    public boolean isRightPressed() { return rightPressed; }
    public boolean isUpPressed() { return upPressed; }
    public boolean isDownPressed() { return downPressed; }

    public boolean isUpRequested() { return upRequested; }
    public boolean isDownRequested() { return downRequested; }
    public boolean isShootRequested() { return shootRequested; }
    public boolean isStartRequested() { return startRequested; }
    public boolean isPauseRequested() { return pauseRequested; }
    public boolean isRestartRequested() { return restartRequested; }
    public boolean isQuitRequested() { return quitRequested; }
    public boolean isMenuRequested() { return menuRequested; }
    public int getUpgradeChoice() { return upgradeChoice; }
}
