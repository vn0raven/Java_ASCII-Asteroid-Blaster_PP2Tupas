package asteroidgame.objects;

import asteroidgame.core.GameBoard;

/**
 * Abstract parent class for all visible game objects.
 * It demonstrates abstraction, inheritance, and polymorphism.
 */
public abstract class GameObject {
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

    public abstract void update();

    public void draw(GameBoard board) {
        if (active) {
            board.placeSymbol(x, y, symbol);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getSymbol() {
        return symbol;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
