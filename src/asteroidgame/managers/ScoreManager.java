package asteroidgame.managers;

import asteroidgame.core.GameConfig;

/**
 * Tracks score, current level, max level, and the next level target.
 */
public class ScoreManager {
    private static final int[] LEVEL_TARGETS = { 0, 300, 650, 1050, 1500, 2000, 2600 };

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
        return score >= getNextLevelScore();
    }

    public boolean isFinalLevelCleared() {
        return level >= GameConfig.MAX_LEVEL && isReadyForLevelUp();
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
        return "FINAL WAVE";
    }

    public void reset() {
        score = 0;
        level = 1;
    }
}
