package asteroidgame.systems.scoring;

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
 * Tracks score, current level, max level, and the next level target.
 */
public class ScoreManager {
    private static final int[] LEVEL_TARGETS = { 0, 300, 650, 1050, 1500, 2000, 2600, Integer.MAX_VALUE };

    private int score;
    private int level;

    public ScoreManager() {
        reset();
    }

    public void addPoints(int points) {
        if (points > 0) {
            score += points;
        }
    }

    public int getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxLevel() {
        return GameConfig.MAX_LEVEL;
    }

    public int getNextLevelScore() {
        if (level >= 1 && level < LEVEL_TARGETS.length) {
            return LEVEL_TARGETS[level];
        }
        return level * GameConfig.SCORE_PER_LEVEL;
    }

    public boolean isReadyForLevelUp() {
        if (level >= GameConfig.BOSS_LEVEL) {
            return false;
        }
        return score >= getNextLevelScore();
    }

    public boolean isFinalLevelCleared() {
        return false; // Level 7 victory is handled by defeating the AlienBoss.
    }

    public void advanceLevel() {
        if (level < GameConfig.MAX_LEVEL) {
            level++;
        }
    }

    public String getLevelName() {
        if (level == 1) return "TRAINING FIELD";
        if (level == 2) return "LIGHT ROCKS";
        if (level == 3) return "SPLIT ZONE";
        if (level == 4) return "METEOR FIELD";
        if (level == 5) return "METEOR STORM";
        if (level == 6) return "FINAL WAVE";
        return "ALIEN MOTHERSHIP";
    }

    public void reset() {
        score = 0;
        level = 1;
    }
}
