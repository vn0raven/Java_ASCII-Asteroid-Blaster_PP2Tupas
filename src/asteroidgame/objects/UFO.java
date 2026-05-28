package asteroidgame.objects;

import asteroidgame.core.GameBoard;

/**
 * Parent class for all Alien enemies.
 * Features a built-in "Warning Spawning Phase" before it enters the board.
 */
public abstract class UFO extends GameObject {
    protected int hp;
    protected int spawnTimer;
    protected boolean entersFromLeft;
    protected int boardWidth;
    protected int boardHeight;

    public UFO(int y, char symbol, int hp, boolean entersFromLeft, int boardWidth, int boardHeight) {
        // Start precisely on the left edge (0) or right edge
        super(entersFromLeft ? 0 : boardWidth - 1, y, symbol);
        this.hp = hp;
        this.entersFromLeft = entersFromLeft;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.spawnTimer = 12; // 1.5 seconds of warning before it appears
    }

    @Override
    public void update() {
        if (spawnTimer > 0) {
            spawnTimer--;
            return; // Do not move or shoot while spawning
        }
        
        moveLogic();
    }

    protected abstract void moveLogic();

    @Override
    public void draw(GameBoard board) {
        if (!active) return;

        // --- THE WARNING PHASE ---
        if (spawnTimer > 0) {
            // Flash on and off every 3 frames
            if (spawnTimer % 6 > 2) {
                if (entersFromLeft) {
                    board.placeSymbol(0, y, GameSymbols.WARNING_LEFT);
                    board.placeSymbol(1, y, GameSymbols.WARNING_LEFT);
                    board.placeSymbol(2, y, GameSymbols.WARNING_LEFT);
                } else {
                    board.placeSymbol(boardWidth - 1, y, GameSymbols.WARNING_RIGHT);
                    board.placeSymbol(boardWidth - 2, y, GameSymbols.WARNING_RIGHT);
                    board.placeSymbol(boardWidth - 3, y, GameSymbols.WARNING_RIGHT);
                }
            }
            return;
        }

        // --- MATERIALIZED PHASE ---
        board.placeSymbol(x, y, symbol);
    }

    public boolean isSpawning() {
        return spawnTimer > 0;
    }

    public boolean takeHit() {
        hp--;
        if (hp <= 0) {
            active = false;
            return true; // Destroyed
        }
        return false; // Still alive
    }
}