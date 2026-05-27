package asteroidgame.ui;

import asteroidgame.core.InputState;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Swing keyboard listener.
 * It converts key presses into a plain InputState for the game engine.
 */
public class KeyboardInput extends KeyAdapter {
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean upRequested;
    private boolean downRequested;
    private boolean shootRequested;
    private boolean startRequested;
    private boolean pauseRequested;
    private boolean restartRequested;
    private boolean quitRequested;
    private int upgradeChoice;

    @Override
    public void keyPressed(KeyEvent event) {
        int key = event.getKeyCode();

        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) leftPressed = true;
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) rightPressed = true;
        
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) upRequested = true;
        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) downRequested = true;

        if (key == KeyEvent.VK_SPACE) shootRequested = true;
        if (key == KeyEvent.VK_ENTER) startRequested = true;
        if (key == KeyEvent.VK_P) pauseRequested = true;
        if (key == KeyEvent.VK_R) restartRequested = true;
        if (key == KeyEvent.VK_Q) quitRequested = true;

        if (key == KeyEvent.VK_1) upgradeChoice = 1;
        if (key == KeyEvent.VK_2) upgradeChoice = 2;
        if (key == KeyEvent.VK_3) upgradeChoice = 3;
        if (key == KeyEvent.VK_4) upgradeChoice = 4;
    }

    @Override
    public void keyReleased(KeyEvent event) {
        int key = event.getKeyCode();
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) leftPressed = false;
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) rightPressed = false;
    }

    public InputState collectInputState() {
        return new InputState(
                leftPressed,
                rightPressed,
                consumeUpRequest(),
                consumeDownRequest(),
                consumeShootRequest(),
                consumeStartRequest(),
                consumePauseRequest(),
                consumeRestartRequest(),
                quitRequested,
                consumeUpgradeChoice()
        );
    }

    private boolean consumeUpRequest() {
        if (upRequested) { upRequested = false; return true; } return false;
    }

    private boolean consumeDownRequest() {
        if (downRequested) { downRequested = false; return true; } return false;
    }

    private boolean consumeShootRequest() {
        if (shootRequested) { shootRequested = false; return true; } return false;
    }

    private boolean consumeStartRequest() {
        if (startRequested) { startRequested = false; return true; } return false;
    }

    private boolean consumePauseRequest() {
        if (pauseRequested) { pauseRequested = false; return true; } return false;
    }

    private boolean consumeRestartRequest() {
        if (restartRequested) { restartRequested = false; return true; } return false;
    }

    private int consumeUpgradeChoice() {
        int choice = upgradeChoice;
        upgradeChoice = 0;
        return choice;
    }
}