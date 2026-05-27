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
import java.util.Random;

/**
 * Draws the retro text screen on a fixed cell grid.
 * Now features a CRT Monitor Filter and a 3D Parallax Starfield!
 */
public class FixedGridPanel extends JPanel {
    private String[] lines;
    private int rows;
    private int columns;
    private int cellWidth;
    private int cellHeight;
    private int baselineOffset;
    private Font gameFont;

    // --- STARFIELD VARIABLES ---
    private Star[] stars;
    private Random random = new Random();

    // Inner class to handle individual background stars
    private class Star {
        float x, y, speed;
        char symbol;
        Color color;

        public void reset(int maxX, int maxY, boolean randomizeY) {
            this.x = random.nextInt(Math.max(1, maxX));
            this.y = randomizeY ? random.nextInt(Math.max(1, maxY)) : -10; // Start at top if recycling
            
            // Assign 1 of 3 depth layers
            int layer = random.nextInt(100);
            if (layer < 60) {
                // Layer 1: Deep Background (Lots of them, slow and dark)
                speed = 0.3f; symbol = '.'; color = new Color(70, 70, 90); 
            } else if (layer < 90) {
                // Layer 2: Midground (Medium speed and brightness)
                speed = 1.0f; symbol = '·'; color = new Color(120, 120, 140);
            } else {
                // Layer 3: Foreground (Few of them, fast and bright)
                speed = 2.5f; symbol = '|'; color = new Color(180, 180, 210);
            }
        }
    }

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

        int panelWidth = columns * cellWidth;
        int panelHeight = rows * cellHeight;
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        clearLines();

        // Initialize 75 stars and scatter them randomly across the screen
        stars = new Star[75];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star();
            stars[i].reset(panelWidth, panelHeight, true); 
        }
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

        // 1. Draw the deep space background color
        g.setColor(new Color(10, 10, 15)); 
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setFont(gameFont);

        // 2. DRAW AND UPDATE THE STARFIELD (Behind the game grid!)
        for (Star star : stars) {
            // Move the star down
            star.y += star.speed;
            
            // If it goes off the bottom of the screen, recycle it at the top
            if (star.y > getHeight()) {
                star.reset(getWidth(), getHeight(), false);
            }

            g.setColor(star.color);
            g.drawString(String.valueOf(star.symbol), Math.round(star.x), Math.round(star.y));
        }

        // 3. DRAW THE GAME GRID (Asteroids, Player, Text)
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

        // 4. APPLY THE CRT MONITOR POST-PROCESSING
        // A. Scanlines
        g.setColor(new Color(0, 0, 0, 85)); 
        for (int y = 0; y < getHeight(); y += 3) {
            g.drawLine(0, y, getWidth(), y);
        }

        // B. CRT Vignette
        java.awt.geom.Point2D center = new java.awt.geom.Point2D.Float(getWidth() / 2.0f, getHeight() / 2.0f);
        float radius = Math.max(getWidth(), getHeight()) / 1.2f;
        float[] dist = {0.0f, 1.0f};
        java.awt.Color[] colors = {new java.awt.Color(0, 0, 0, 0), new java.awt.Color(0, 0, 0, 210)};
        
        java.awt.RadialGradientPaint vignette = new java.awt.RadialGradientPaint(center, radius, dist, colors);
        g2d.setPaint(vignette);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
    
    private Color getColorForSymbol(char symbol) {
        // FLOATING COMBAT TEXT & HUD SCORES (Neon Green!)
        if (symbol == '+' || Character.isDigit(symbol)) {
            return new Color(50, 255, 50); 
        }

        // DEBRIS / EXPLOSION PARTICLES (Fiery Orange!)
        if (symbol == '*' || symbol == '°' || symbol == ',' || symbol == '\'' || symbol == '`') {
            return new Color(255, 140, 0); 
        }

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