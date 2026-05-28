package asteroidgame.objects;

/**
 * Heavy faceted asteroid. It is large, slower, and needs five hits.
 */
public class HeavyAsteroid extends Asteroid {
    public HeavyAsteroid(int x, int y, int boardHeight, int speed) {
        super(x, y, boardHeight, speed, 5, AsteroidSprites.FAMILY_HEAVY);
    }

    public HeavyAsteroid(int x, int y, int boardHeight, int speed, int moveDx, int moveDy) {
        super(x, y, boardHeight, speed, 5, AsteroidSprites.FAMILY_HEAVY, moveDx, moveDy);
    }
}
