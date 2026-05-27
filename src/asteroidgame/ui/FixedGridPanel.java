package asteroidgame.ui;

import asteroidgame.objects.GameSymbols;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Draws the retro text screen on a fixed cell grid.
 * This prevents Unicode symbols from shifting the rest of the line.
 */
public class FixedGridPanel extends JPanel {
    private String[] lines;
    private int rows;
    private int columns;
    private int cellWidth;
    private int cellHeight;
    private int baselineOffset;
    private Font gameFont;

    public FixedGridPanel(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.lines = new String[rows];
        this.gameFont = new Font(Font.MONOSPACED, Font.PLAIN, 18);

        setBackground(Color.BLACK);
        setFont(gameFont);
        setFocusable(true);

        FontMetrics metrics = getFontMetrics(gameFont);
        this.cellWidth = metrics.charWidth('W');
        this.cellHeight = metrics.getHeight();
        this.baselineOffset = metrics.getAscent();

        setPreferredSize(new Dimension(columns * cellWidth, rows * cellHeight));
        clearLines();
    }

    public void setScreenText(String screenText) {
        String[] sourceLines = screenText.split("\\n", -1);

        for (int row = 0; row < rows; row++) {
            if (row < sourceLines.length) {
                lines[row] = fitLine(sourceLines[row]);
            } else {
                lines[row] = repeat(' ', columns);
            }
        }

        repaint();
    }

    private void clearLines() {
        for (int row = 0; row < rows; row++) {
            lines[row] = repeat(' ', columns);
        }
    }

    private String fitLine(String line) {
        if (line.length() > columns) {
            return line.substring(0, columns);
        }

        return line + repeat(' ', columns - line.length());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(new Color(10, 10, 15)); 
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setFont(gameFont);

        for (int row = 0; row < rows; row++) {
            if (lines[row] == null) continue;
            
            String currentLine = lines[row];

            for (int col = 0; col < currentLine.length(); col++) {
                char c = currentLine.charAt(col);
                
                if (c != ' ') {
                    g.setColor(getColorForSymbol(c)); 
                    
                    int drawX = col * cellWidth;
                    int drawY = row * cellHeight + baselineOffset;
                    g.drawString(String.valueOf(c), drawX, drawY);
                }
            }
        }
    }
    
    private Color getColorForSymbol(char symbol) {
        if (symbol == GameSymbols.PLAYER || symbol == GameSymbols.PLAYER_SHIELDED) {
            return new Color(120, 255, 160); // Mint Green
        }

        if (symbol == GameSymbols.PLAYER_HIT) {
            return new Color(255, 100, 100); // Damage Red
        }

        if (symbol == GameSymbols.BULLET) {
            return new Color(120, 220, 255); // Neon Blue
        }

        if (symbol == GameSymbols.ASTEROID_WEAK || symbol == GameSymbols.ASTEROID_NORMAL || symbol == GameSymbols.ASTEROID_HEAVY) {
            return new Color(255, 190, 90); // Asteroid Orange/Brown
        }

        if (symbol == GameSymbols.POWER_UP_LIFE || symbol == GameSymbols.LIFE || symbol == GameSymbols.SHIELD) {
            return new Color(255, 235, 100); // Gold/Yellow
        }

        if (symbol == '╔' || symbol == '╗' || symbol == '╚' || symbol == '╝' || symbol == '║' || symbol == '═' || symbol == '╠' || symbol == '╣' || symbol == '─') {
            return new Color(170, 210, 255); // UI Border Blue
        }

        if (symbol == '·' || symbol == '.') {
            return new Color(70, 70, 90); // Faded Starfield Grey
        }

        if (symbol == '*' || symbol == '°' || symbol == ',' || symbol == '\'' || symbol == '`') {
            return new Color(255, 140, 0); 
        }

        if (symbol == '+' || Character.isDigit(symbol)) {
            return new Color(50, 255, 50); 
        }

        return Color.WHITE; // Default fallback color
    }

    private String repeat(char character, int count) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < count; i++) {
            result.append(character);
        }

        return result.toString();
    }
}