package asteroidgame.core;

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
 * Represents the 2D grid of characters that make up the game screen.
 */
public class GameBoard {
    private int width;
    private int height;
    private char[][] grid;

    public GameBoard(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new char[height][width];
        clear();
    }

    /**
     * Wipes the board clean with empty spaces.
     */
    public void clear() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = ' ';
            }
        }
    }

    /**
     * Safely places a character on the grid.
     */
    public void placeSymbol(int x, int y, char symbol) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            grid[y][x] = symbol;
        }
    }

    /**
     * Retrieves a character from the grid.
     */
    public char getSymbol(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return grid[y][x];
        }
        return ' '; 
    }

    /**
     * Creates a safe copy of the current grid state for the renderer.
     */
    public char[][] copyGrid() {
        char[][] copy = new char[height][width];
        for (int y = 0; y < height; y++) {
            System.arraycopy(grid[y], 0, copy[y], 0, width);
        }
        return copy;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
