package asteroidgame.objects;

/**
 * Power-up that restores one life up to the player's maximum lives.
 */
public class ExtraLifePowerUp extends PowerUp {
    public ExtraLifePowerUp(int x, int y, int boardHeight) {
        super(x, y, boardHeight, 3, GameSymbols.POWER_UP_LIFE);
    }

    @Override
    public void applyTo(Player player) {
        player.addLife();
    }
}
