package asteroidgame.objects;

/**
 * Heavy asteroid. It is a large target and needs three hits.
 */
public class HeavyAsteroid extends Asteroid {
    public HeavyAsteroid(int x, int y, int boardHeight, int speed) {
        super(x, y, boardHeight, speed, Asteroid.ARMORED_STAGE);
    }
}
