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
 * Parent class for falling power-ups.
 */
public abstract class PowerUp extends GameObject {
    private int speed;
    private int moveCounter;
    private int boardHeight;

    public PowerUp(int x, int y, int boardHeight, int speed, char symbol) {
        super(x, y, symbol);
        this.boardHeight = boardHeight;
        this.speed = speed;
        this.moveCounter = 0;
    }

    @Override
    public void update() {
        moveCounter++;

        if (moveCounter >= speed) {
            y++;
            moveCounter = 0;
        }

        if (y >= boardHeight) {
            active = false;
        }
    }

    public abstract void applyTo(Player player);
}
