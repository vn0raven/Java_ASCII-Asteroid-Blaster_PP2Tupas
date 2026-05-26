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
        this.cellWidth = Math.max(14, metrics.charWidth('W') + 2);
        this.cellHeight = Math.max(22, metrics.getHeight() + 2);
        this.baselineOffset = metrics.getAscent() + 1;

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
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g2 = (Graphics2D) graphics;
        g2.setFont(gameFont);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        for (int row = 0; row < rows; row++) {
            String line = lines[row];

            for (int col = 0; col < columns; col++) {
                char symbol = line.charAt(col);
                g2.setColor(getColorForSymbol(symbol));
                int x = col * cellWidth;
                int y = row * cellHeight + baselineOffset;
                g2.drawString(String.valueOf(symbol), x, y);
            }
        }
    }

    private Color getColorForSymbol(char symbol) {
        if (symbol == GameSymbols.PLAYER || symbol == GameSymbols.PLAYER_SHIELDED) {
            return new Color(120, 255, 160);
        }

        if (symbol == GameSymbols.PLAYER_HIT) {
            return new Color(255, 100, 100);
        }

        if (symbol == GameSymbols.BULLET) {
            return new Color(120, 220, 255);
        }

        if (symbol == GameSymbols.ASTEROID_WEAK || symbol == GameSymbols.ASTEROID_NORMAL || symbol == GameSymbols.ASTEROID_HEAVY) {
            return new Color(255, 190, 90);
        }

        if (symbol == GameSymbols.POWER_UP_LIFE || symbol == GameSymbols.LIFE || symbol == GameSymbols.SHIELD) {
            return new Color(255, 235, 100);
        }

        if (symbol == '╔' || symbol == '╗' || symbol == '╚' || symbol == '╝' || symbol == '║' || symbol == '═' || symbol == '╠' || symbol == '╣' || symbol == '─') {
            return new Color(170, 210, 255);
        }

        if (symbol == '·') {
            return new Color(70, 70, 90);
        }

        return Color.WHITE;
    }

    private String repeat(char character, int count) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < count; i++) {
            result.append(character);
        }

        return result.toString();
    }
}
