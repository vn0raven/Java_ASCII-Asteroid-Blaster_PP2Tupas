package asteroidgame.objects;

/**
 * Bullet fired by the player ship.
 */
public class Bullet extends GameObject {
    private int boardWidth;

    public Bullet(int x, int y, int boardWidth) {
        super(x, y, GameSymbols.BULLET);
        this.boardWidth = boardWidth;

        if (x < 0 || x >= boardWidth) {
            active = false;
        }
    }

    @Override
    public void update() {
        y--;

        if (y < 0 || x < 0 || x >= boardWidth) {
            active = false;
        }
    }
}
