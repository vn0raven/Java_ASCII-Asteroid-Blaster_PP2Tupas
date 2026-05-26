package asteroidgame.managers;

/**
 * Simple result object for bullet and asteroid collisions.
 */
public class CollisionResult {
    private int earnedPoints;
    private int destroyedCount;
    private int damagedCount;

    public CollisionResult(int earnedPoints, int destroyedCount, int damagedCount) {
        this.earnedPoints = earnedPoints;
        this.destroyedCount = destroyedCount;
        this.damagedCount = damagedCount;
    }

    public int getEarnedPoints() {
        return earnedPoints;
    }

    public int getDestroyedCount() {
        return destroyedCount;
    }

    public int getDamagedCount() {
        return damagedCount;
    }
}
