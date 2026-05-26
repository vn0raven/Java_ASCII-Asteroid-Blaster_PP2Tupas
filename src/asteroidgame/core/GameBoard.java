package asteroidgame.core;

/**
 * Represents the fixed playable retro text grid.
 * This class stores symbols only and does not use Swing.
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

    public void clear() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col] = ' ';
            }
        }
    }

    public void placeSymbol(int x, int y, char symbol) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            grid[y][x] = symbol;
        }
    }

    public char[][] copyGrid() {
        char[][] copy = new char[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                copy[row][col] = grid[row][col];
            }
        }

        return copy;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
