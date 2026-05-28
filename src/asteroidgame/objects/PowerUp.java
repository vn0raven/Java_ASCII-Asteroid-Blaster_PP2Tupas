package asteroidgame.objects;

/**
 * Parent class for falling power-ups.
 */
public abstract class PowerUp extends GameObject {
    private int speed;
    private int moveCounter;
    private int boardHeight;

    public PowerUp(int x, int y, int boardHeight, int speed, char symbol) {
        super(x, y, symbol);
        this.boardHeight = boardHeight;
        this.speed = speed;
        this.moveCounter = 0;
    }

    @Override
    public void update() {
        moveCounter++;

        if (moveCounter >= speed) {
            y++;
            moveCounter = 0;
        }

        if (y >= boardHeight) {
            active = false;
        }
    }

    public abstract void applyTo(Player player);
}
