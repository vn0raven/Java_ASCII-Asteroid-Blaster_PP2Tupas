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
