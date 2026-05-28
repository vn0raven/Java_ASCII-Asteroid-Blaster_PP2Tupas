package asteroidgame.objects;

import asteroidgame.core.GameBoard;

/**
 * Contract for any object that can be rendered to the game board.
 */
public interface Drawable {
    void draw(GameBoard board);
    boolean isActive(); 
}