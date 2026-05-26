package asteroidgame.objects;

/**
 * Normal asteroid. It is still drawn as a large target, but it needs two hits.
 */
public class NormalAsteroid extends Asteroid {
    public NormalAsteroid(int x, int y, int boardHeight, int speed) {
        super(x, y, boardHeight, speed, Asteroid.CRACKED_STAGE);
    }
}
