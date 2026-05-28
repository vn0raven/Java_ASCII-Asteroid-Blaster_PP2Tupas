package asteroidgame.objects.asteroids;

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
