package asteroidgame.systems.collision;

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
 * Simple result object for bullet and asteroid collisions.
 */
public class CollisionResult {
    private int earnedPoints;
    private int destroyedCount;
    private int damagedCount;

    public CollisionResult(int earnedPoints, int destroyedCount, int damagedCount) {
        this.earnedPoints = earnedPoints;
        this.destroyedCount = destroyedCount;
        this.damagedCount = damagedCount;
    }

    public int getEarnedPoints() {
        return earnedPoints;
    }

    public int getDestroyedCount() {
        return destroyedCount;
    }

    public int getDamagedCount() {
        return damagedCount;
    }
}
