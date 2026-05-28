package asteroidgame.objects;

/**
 * Standard faceted asteroid. It is a common mid-sized asteroid and needs three hits.
 */
public class NormalAsteroid extends Asteroid {
    public NormalAsteroid(int x, int y, int boardHeight, int speed) {
        super(x, y, boardHeight, speed, 3, AsteroidSprites.FAMILY_STANDARD);
    }

    public NormalAsteroid(int x, int y, int boardHeight, int speed, int moveDx, int moveDy) {
        super(x, y, boardHeight, speed, 3, AsteroidSprites.FAMILY_STANDARD, moveDx, moveDy);
    }
}
