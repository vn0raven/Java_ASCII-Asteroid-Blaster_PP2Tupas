package asteroidgame.objects;

/**
 * Light faceted asteroid. It is smaller than the others but still drawn as a
 * readable multi-cell sprite, not as a tiny annoying dot. It now needs two hits for better balance.
 */
public class SmallAsteroid extends Asteroid {
    public SmallAsteroid(int x, int y, int boardHeight, int speed) {
        super(x, y, boardHeight, speed, 2, AsteroidSprites.FAMILY_LIGHT);
    }

    public SmallAsteroid(int x, int y, int boardHeight, int speed, int moveDx, int moveDy) {
        super(x, y, boardHeight, speed, 2, AsteroidSprites.FAMILY_LIGHT, moveDx, moveDy);
    }
}
