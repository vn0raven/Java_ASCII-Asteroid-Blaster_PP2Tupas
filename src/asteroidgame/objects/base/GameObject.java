package asteroidgame.objects.base;

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
 * Abstract parent class for all visible game objects.
 * It demonstrates abstraction, inheritance, and now INTERFACES.
 */
public abstract class GameObject implements Updatable, Drawable { 
    protected int x;
    protected int y;
    protected char symbol;
    protected boolean active;

    public GameObject(int x, int y, char symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
        this.active = true;
    }

    @Override
    public abstract void update();

    @Override
    public void draw(GameBoard board) {
        if (active) {
            board.placeSymbol(x, y, symbol);
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public char getSymbol() { return symbol; }
    
    @Override
    public boolean isActive() { return active; }
    
    public void setActive(boolean active) { this.active = active; }
}
