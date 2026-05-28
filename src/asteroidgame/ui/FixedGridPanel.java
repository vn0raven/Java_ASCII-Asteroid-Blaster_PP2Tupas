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
import java.awt.geom.AffineTransform;
import java.util.Random;

/**
 * Draws the retro text screen on a fixed cell grid.
 * Fixes text spacing and solidifies borders without changing the grid dimensions.
 */
public class FixedGridPanel extends JPanel {
    private String[] lines;
    private int rows;
    private int columns;
    private int cellWidth;
    private int cellHeight;
    private int baselineOffset;
    private Font gameFont;
    
    // KEEPING YOUR EXACT GRID SIZE
    private static final int FIXED_CELL_WIDTH = 12;
    private static final int FIXED_CELL_HEIGHT = 13;

    // --- STARFIELD VARIABLES ---
    private Star[] stars;
    private Random random = new Random();

    // --- LEVEL PALETTE VARIABLE ---
    private int currentLevel = 1;
    private int trackingY = 0;

    private class Star {
        float x, y, speed;
        char symbol;
        Color color;

        public void reset(int maxX, int maxY, boolean randomizeY) {
            this.x = random.nextInt(Math.max(1, maxX));
            this.y = randomizeY ? random.nextInt(Math.max(1, maxY)) : -10; 
            
            int layer = random.nextInt(100);
            if (layer < 60) {
                speed = 0.3f; symbol = '.'; color = new Color(70, 70, 90); 
            } else if (layer < 90) {
                speed = 1.0f; symbol = '·'; color = new Color(120, 120, 140);
            } else {
                speed = 2.5f; symbol = '|'; color = new Color(180, 180, 210);
            }
        }
    }

    public FixedGridPanel(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.lines = new String[rows];
        
        // Base font setup - Bold helps visibility
        Font baseFont = new Font(Font.MONOSPACED, Font.BOLD, 14);
        FontMetrics fm = getFontMetrics(baseFont);
        
        // DYNAMIC FONT STRETCH:
        // We stretch the font horizontally so its characters are exactly 12px wide.
        // This removes the gaps between letters while respecting your fixed grid.
        int charW = fm.charWidth('W');
        if (charW > 0 && charW != FIXED_CELL_WIDTH) {
            float scaleX = (float) FIXED_CELL_WIDTH / charW;
            this.gameFont = baseFont.deriveFont(AffineTransform.getScaleInstance(scaleX, 1.0));
        } else {
            this.gameFont = baseFont;
        }

        setBackground(Color.BLACK);
        setFont(gameFont);
        setFocusable(true);

        this.cellWidth = FIXED_CELL_WIDTH;
        this.cellHeight = FIXED_CELL_HEIGHT;
        
        // Recalculate baseline with the new stretched font
        FontMetrics metrics = getFontMetrics(this.gameFont);
        this.baselineOffset = metrics.getAscent() + Math.max(0, (cellHeight - metrics.getHeight()) / 2);

        int panelWidth = columns * cellWidth;
        int panelHeight = rows * cellHeight;
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        clearLines();

        stars = new Star[130];
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

        g.setColor(new Color(10, 10, 15)); 
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setFont(gameFont);

        // Starfield
        for (Star star : stars) {
            star.y += star.speed;
            if (star.y > getHeight()) star.reset(getWidth(), getHeight(), false);
            g.setColor(star.color);
            g.drawString(String.valueOf(star.symbol), Math.round(star.x), Math.round(star.y));
        }
        
        trackingY += 3; 
        if (trackingY > getHeight() + 50) trackingY = -50; 
        
        for (int row = 0; row < rows; row++) {
            if (lines[row] == null) continue;
            String currentLine = lines[row];

            for (int col = 0; col < currentLine.length(); col++) {
                char c = currentLine.charAt(col);
                
                if (c != ' ') {
                    Color baseColor = getColorForSymbol(c);
                    
                    if (isBorderSymbol(c)) {
                        // Use the custom renderer to draw 100% solid connected borders
                        drawSolidBorder(g, c, col, row, baseColor);
                    } else {
                        // Standard text and symbols (now perfectly sized to 12px wide)
                        int symbolWidth = g.getFontMetrics().charWidth(c);
                        int drawX = col * cellWidth + Math.max(0, (cellWidth - symbolWidth) / 2);
                        int drawY = row * cellHeight + baselineOffset;

                        // Neon Halo Glow
                        Color glowColor = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 60); 
                        g.setColor(glowColor);
                        g.drawString(String.valueOf(c), drawX - 1, drawY); 
                        g.drawString(String.valueOf(c), drawX + 1, drawY); 
                        
                        // Solid Core
                        g.setColor(baseColor); 
                        g.drawString(String.valueOf(c), drawX, drawY);
                    }
                }
            }
        }

        // CRT Effects
        g.setColor(new Color(255, 255, 255, 8)); 
        g.fillRect(0, trackingY - 20, getWidth(), 40);

        int flicker = random.nextInt(9) - 4; 
        int scanlineOpacity = Math.max(0, Math.min(255, 85 + flicker)); 
        g.setColor(new Color(0, 0, 0, scanlineOpacity)); 
        for (int y = 0; y < getHeight(); y += 3) {
            g.drawLine(0, y, getWidth(), y);
        }

        int vignetteOpacity = Math.max(0, Math.min(255, 210 + flicker)); 
        java.awt.geom.Point2D center = new java.awt.geom.Point2D.Float(getWidth() / 2.0f, getHeight() / 2.0f);
        float radius = Math.max(getWidth(), getHeight()) / 1.2f;
        float[] dist = {0.0f, 1.0f};
        java.awt.Color[] colors = {new java.awt.Color(0, 0, 0, 0), new java.awt.Color(0, 0, 0, vignetteOpacity)};
        
        java.awt.RadialGradientPaint vignette = new java.awt.RadialGradientPaint(center, radius, dist, colors);
        g2d.setPaint(vignette);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
    
    /**
     * Manually draws UI borders as pure rectangles to ensure there are NEVER any
     * dashed gaps between them, regardless of font rendering limitations.
     */
    private void drawSolidBorder(Graphics g, char c, int col, int row, Color color) {
        int x = col * cellWidth;
        int y = row * cellHeight;
        int cx = x + cellWidth / 2;
        int cy = y + cellHeight / 2;
        int thick = 2; // Pixel thickness of the UI lines

        // Add glow to the borders
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 60));
        if (c == '═' || c == '─') g.fillRect(x, cy - thick + 1, cellWidth, thick + 2);
        if (c == '║') g.fillRect(cx - thick + 1, y, thick + 2, cellHeight);
        
        // Draw crisp core lines
        g.setColor(color);
        
        // Draw Rightward connection
        if (c == '═' || c == '─' || c == '╔' || c == '╚' || c == '╠') {
            g.fillRect(cx, cy - thick/2, cellWidth/2 + 1, thick); 
        }
        // Draw Leftward connection
        if (c == '═' || c == '─' || c == '╗' || c == '╝' || c == '╣') {
            g.fillRect(x, cy - thick/2, cellWidth/2 + 1, thick); 
        }
        // Draw Downward connection
        if (c == '║' || c == '╠' || c == '╣' || c == '╔' || c == '╗') {
            g.fillRect(cx - thick/2, cy, thick, cellHeight/2 + 1); 
        }
        // Draw Upward connection
        if (c == '║' || c == '╠' || c == '╣' || c == '╚' || c == '╝') {
            g.fillRect(cx - thick/2, y, thick, cellHeight/2 + 1); 
        }
    }

    private boolean isBorderSymbol(char c) {
        return c == '╔' || c == '╗' || c == '╚' || c == '╝' || c == '║' || c == '═' || c == '╠' || c == '╣' || c == '─';
    }
    
    private Color getColorForSymbol(char symbol) {
        if (symbol == '+' || Character.isDigit(symbol)) return new Color(50, 255, 50); 
        if (symbol == GameSymbols.PLAYER_HIT) return new Color(255, 100, 100); 
        if (symbol == GameSymbols.POWER_UP_LIFE || symbol == GameSymbols.LIFE || symbol == GameSymbols.SHIELD) return new Color(255, 235, 100); 
        if (isAsteroidSpriteSymbol(symbol)) return new Color(255, 150, 45);
        if (isBorderSymbol(symbol)) return new Color(170, 210, 255); 

        int palette = (currentLevel - 1) % 4; 

        switch (palette) {
            case 0:
                if (symbol == '*' || symbol == '°' || symbol == ',' || symbol == '\'' || symbol == '`') return new Color(255, 80, 0); 
                if (symbol == GameSymbols.PLAYER || symbol == GameSymbols.PLAYER_SHIELDED) return new Color(0, 255, 255); 
                if (symbol == GameSymbols.BULLET) return new Color(0, 255, 255);
                if (symbol == GameSymbols.ASTEROID_WEAK || symbol == GameSymbols.ASTEROID_NORMAL || symbol == GameSymbols.ASTEROID_HEAVY) return new Color(255, 140, 0);
                return new Color(255, 255, 0); 
                
            case 1:
                if (symbol == '*' || symbol == '°' || symbol == ',' || symbol == '\'' || symbol == '`') return new Color(255, 0, 255); 
                if (symbol == GameSymbols.PLAYER || symbol == GameSymbols.PLAYER_SHIELDED) return new Color(255, 255, 0); 
                if (symbol == GameSymbols.BULLET) return new Color(255, 0, 255);
                if (symbol == GameSymbols.ASTEROID_WEAK || symbol == GameSymbols.ASTEROID_NORMAL || symbol == GameSymbols.ASTEROID_HEAVY) return new Color(200, 0, 255);
                return new Color(0, 150, 255); 

            case 2:
                if (symbol == '*' || symbol == '°' || symbol == ',' || symbol == '\'' || symbol == '`') return new Color(50, 255, 0); 
                if (symbol == GameSymbols.PLAYER || symbol == GameSymbols.PLAYER_SHIELDED) return new Color(255, 100, 0); 
                if (symbol == GameSymbols.BULLET) return new Color(50, 255, 0);
                if (symbol == GameSymbols.ASTEROID_WEAK || symbol == GameSymbols.ASTEROID_NORMAL || symbol == GameSymbols.ASTEROID_HEAVY) return new Color(100, 255, 0);
                return new Color(0, 255, 0); 

            case 3:
                if (symbol == '*' || symbol == '°' || symbol == ',' || symbol == '\'' || symbol == '`') return new Color(255, 0, 50); 
                if (symbol == GameSymbols.PLAYER || symbol == GameSymbols.PLAYER_SHIELDED) return new Color(255, 255, 255); 
                if (symbol == GameSymbols.BULLET) return new Color(255, 0, 0); 
                if (symbol == GameSymbols.ASTEROID_WEAK || symbol == GameSymbols.ASTEROID_NORMAL || symbol == GameSymbols.ASTEROID_HEAVY) return new Color(200, 0, 0);
                return new Color(255, 50, 50); 
        }
        return Color.WHITE; 
    }

    private boolean isAsteroidSpriteSymbol(char symbol) {
        return symbol == '/' || symbol == '\\' || symbol == '<' || symbol == '>'
                || symbol == '_' || symbol == '█' || symbol == '▓' || symbol == '▒';
    }

    private String repeat(char character, int count) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(character);
        }
        return result.toString();
    }

    public void setCurrentLevel(int level) {
        this.currentLevel = level;
    }
}