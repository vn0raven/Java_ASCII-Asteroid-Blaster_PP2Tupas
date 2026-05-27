package asteroidgame.objects;

import asteroidgame.core.GameBoard;

/**
 * A multi-stage particle explosion effect.
 * It creates an expanding shockwave across the grid before fading away.
 */

public class Explosion extends GameObject {
    private int lifespan;

    public Explosion(int x, int y) {
        super(x, y, GameSymbols.EXPLOSION);
        this.lifespan = 3; 
    }

    @Override
    public void update() {
        lifespan--;
        
        if (lifespan <= 0) {
            active = false;
        }
    }


    @Override
    public void draw(GameBoard board) {
        if (!active) {
            return;
        }

        if (lifespan == 3) {
            board.placeSymbol(x, y, GameSymbols.EXPLOSION); 
        } 

        else if (lifespan == 2) {
            board.placeSymbol(x - 1, y, GameSymbols.POWER_UP_LIFE); 
            board.placeSymbol(x + 1, y, GameSymbols.POWER_UP_LIFE);
            board.placeSymbol(x, y - 1, GameSymbols.POWER_UP_LIFE);
            board.placeSymbol(x, y + 1, GameSymbols.POWER_UP_LIFE);
        } 
        else if (lifespan == 1) {
            board.placeSymbol(x - 2, y, GameSymbols.EMPTY_LIFE); 
            board.placeSymbol(x + 2, y, GameSymbols.EMPTY_LIFE);
            board.placeSymbol(x, y - 2, GameSymbols.EMPTY_LIFE);
            board.placeSymbol(x, y + 2, GameSymbols.EMPTY_LIFE);
            
            board.placeSymbol(x - 1, y - 1, GameSymbols.EMPTY_LIFE);
            board.placeSymbol(x + 1, y - 1, GameSymbols.EMPTY_LIFE);
            board.placeSymbol(x - 1, y + 1, GameSymbols.EMPTY_LIFE);
            board.placeSymbol(x + 1, y + 1, GameSymbols.EMPTY_LIFE);
        }
    }
}