package asteroidgame.objects;

/**
 * Weak asteroid stage. It is not visually tiny; it is still drawn as a 3-cell-wide target.
 */
public class SmallAsteroid extends Asteroid {
    public SmallAsteroid(int x, int y, int boardHeight, int speed) {
        super(x, y, boardHeight, speed, Asteroid.WEAK_STAGE);
    }
}
