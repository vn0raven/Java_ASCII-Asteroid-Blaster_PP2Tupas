package asteroidgame.objects.powerups;

import asteroidgame.audio.*;
import asteroidgame.config.*;
import asteroidgame.core.*;
import asteroidgame.state.*;
import asteroidgame.objects.assets.*;
import asteroidgame.objects.base.*;
import asteroidgame.objects.asteroids.*;
import asteroidgame.objects.effects.*;
import asteroidgame.objects.enemies.*;
import asteroidgame.objects.player.*;
import asteroidgame.objects.powerups.*;
import asteroidgame.objects.projectiles.*;
import asteroidgame.systems.collision.*;
import asteroidgame.systems.scoring.*;
import asteroidgame.systems.spawning.*;
import asteroidgame.systems.upgrades.*;
import asteroidgame.ui.input.*;
import asteroidgame.ui.render.*;
import asteroidgame.ui.swing.*;
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
