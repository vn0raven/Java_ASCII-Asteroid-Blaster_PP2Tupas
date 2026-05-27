package asteroidgame.objects;

import asteroidgame.core.GameBoard;

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