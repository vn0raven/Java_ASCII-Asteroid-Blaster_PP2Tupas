package asteroidgame.objects;

import asteroidgame.core.GameBoard;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A physics-based particle explosion
 */

public class Explosion implements Updatable, Drawable {
    
    private class Particle {
        float x, y;
        float velocityX, velocityY;
        char symbol;

        public Particle(float x, float y, float vx, float vy, char symbol) {
            this.x = x;
            this.y = y;
            this.velocityX = vx;
            this.velocityY = vy;
            this.symbol = symbol;
        }
    }

    private List<Particle> particles;
    private int lifeTimer;
    private boolean active;
    private static final Random random = new Random();

    // --- FLOATING TEXT VARIABLES ---
    private float textY;
    private int textX;
    private String popupText;

    public Explosion(int startX, int startY) {
        this.particles = new ArrayList<>();
        
        this.lifeTimer = 15; 
        this.active = true;

        // --- TEXT SETUP ---
        this.textX = startX;
        this.textY = startY;
        this.popupText = "+22"; 

        // --- DEBRIS SETUP ---
        char[] debrisSymbols = {'*', '°', ',', '\'', '`'};
        int numParticles = 6 + random.nextInt(5);
        
        for (int i = 0; i < numParticles; i++) {
            float vx = (random.nextFloat() * 3.0f) - 1.5f;
            float vy = (random.nextFloat() * 3.0f) - 1.5f;
            char sym = debrisSymbols[random.nextInt(debrisSymbols.length)];
            
            float randomStartX = startX + (random.nextFloat() - 0.5f);
            float randomStartY = startY + (random.nextFloat() - 0.5f);
            
            particles.add(new Particle(randomStartX, randomStartY, vx, vy, sym));
        }
    }

    @Override
    public void update() {
        lifeTimer--;
        
        if (lifeTimer <= 0) {
            active = false;
            return;
        }

        textY -= 0.3f; 

        for (Particle p : particles) {
            p.x += p.velocityX;
            p.y += p.velocityY;
            
            p.velocityX *= 0.85f; 
            p.velocityY *= 0.85f;
        }
    }

    @Override
    public void draw(GameBoard board) {
        if (!active) return;
        
        for (Particle p : particles) {
            int drawX = Math.round(p.x);
            int drawY = Math.round(p.y);
            board.placeSymbol(drawX, drawY, p.symbol);
        }

        int tY = Math.round(textY);
        for (int i = 0; i < popupText.length(); i++) {
            board.placeSymbol(textX + i - 1, tY, popupText.charAt(i));
        }
    }

    @Override
    public boolean isActive() {
        return active;
    }
}