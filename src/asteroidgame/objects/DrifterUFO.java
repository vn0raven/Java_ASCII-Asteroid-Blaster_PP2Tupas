package asteroidgame.objects;

/**
 * Enters from the side of the screen and drifts diagonally downward.
 */
public class DrifterUFO extends UFO {
    private int speed;
    private int moveCounter;
    private int dirX;

    public DrifterUFO(int startY, boolean entersFromLeft, int boardWidth, int boardHeight) {
        super(startY, GameSymbols.UFO_DRIFTER, 1, entersFromLeft, boardWidth, boardHeight);
        this.speed = 3; // Fast movement
        this.dirX = entersFromLeft ? 1 : -1;
    }

    @Override
    protected void moveLogic() {
        moveCounter++;
        
        if (moveCounter >= speed) {
            x += dirX;
            y++;       // Move down diagonally
            moveCounter = 0;
        }

        // Deactivate if it floats off the bottom or sides of the screen
        if (y >= boardHeight || x < 0 || x >= boardWidth) {
            active = false;
        }
    }
}