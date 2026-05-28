package asteroidgame.objects;

/**
 * Hostile laser fired by UFOs. Moves downwards.
 */
public class EnemyBullet extends GameObject {
    private int boardHeight;

    public EnemyBullet(int x, int y, int boardHeight) {
        super(x, y, GameSymbols.ENEMY_BULLET);
        this.boardHeight = boardHeight;
    }

    @Override
    public void update() {
        y++; // Moves down the screen

        if (y >= boardHeight) {
            active = false;
        }
    }
}   