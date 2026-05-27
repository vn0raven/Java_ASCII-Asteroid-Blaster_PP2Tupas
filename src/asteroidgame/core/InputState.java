package asteroidgame.core;

/**
 * Plain input data object used by the game engine.
 * This keeps Swing keyboard code away from the game logic.
 */
public class InputState {
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

    public InputState(boolean leftPressed, boolean rightPressed, 
                      boolean upRequested, boolean downRequested,
                      boolean shootRequested, boolean startRequested, 
                      boolean pauseRequested, boolean restartRequested, 
                      boolean quitRequested, int upgradeChoice) {
        this.leftPressed = leftPressed;
        this.rightPressed = rightPressed;
        this.upRequested = upRequested;
        this.downRequested = downRequested;
        this.shootRequested = shootRequested;
        this.startRequested = startRequested;
        this.pauseRequested = pauseRequested;
        this.restartRequested = restartRequested;
        this.quitRequested = quitRequested;
        this.upgradeChoice = upgradeChoice;
    }

    public boolean isLeftPressed() { return leftPressed; }
    public boolean isRightPressed() { return rightPressed; }
    public boolean isUpRequested() { return upRequested; }
    public boolean isDownRequested() { return downRequested; }
    public boolean isShootRequested() { return shootRequested; }
    public boolean isStartRequested() { return startRequested; }
    public boolean isPauseRequested() { return pauseRequested; }
    public boolean isRestartRequested() { return restartRequested; }
    public boolean isQuitRequested() { return quitRequested; }
    public int getUpgradeChoice() { return upgradeChoice; }
    
}