package asteroidgame.managers;

import asteroidgame.objects.Asteroid;
import asteroidgame.objects.Bullet;
import asteroidgame.objects.Player;
import asteroidgame.objects.PowerUp;

import java.util.List;

/**
 * Handles object collision checking.
 */
public class CollisionManager {

    public CollisionResult checkBulletAsteroidCollisions(List<Bullet> bullets, List<Asteroid> asteroids) {
        int earnedPoints = 0;
        int destroyedCount = 0;
        int damagedCount = 0;

        for (Bullet bullet : bullets) {
            if (!bullet.isActive()) {
                continue;
            }

            for (Asteroid asteroid : asteroids) {
                if (!asteroid.isActive()) {
                    continue;
                }

                if (asteroid.isHitBy(bullet.getX(), bullet.getY())) {
                    bullet.setActive(false);

                    int hitScore = asteroid.getHitScoreValue();
                    boolean destroyed = asteroid.takeHit();

                    if (destroyed) {
                        earnedPoints += asteroid.getDestroyScoreValue();
                        destroyedCount++;
                    } else {
                        earnedPoints += hitScore;
                        damagedCount++;
                    }

                    break;
                }
            }
        }

        return new CollisionResult(earnedPoints, destroyedCount, damagedCount);
    }

    public boolean checkPlayerAsteroidCollision(Player player, List<Asteroid> asteroids) {
        for (Asteroid asteroid : asteroids) {
            if (!asteroid.isActive()) {
                continue;
            }

            if (asteroid.overlapsPoint(player.getX(), player.getY())) {
                asteroid.setActive(false);
                return true;
            }
        }

        return false;
    }

    public int checkPlayerPowerUpCollision(Player player, List<PowerUp> powerUps) {
        int collected = 0;

        for (PowerUp powerUp : powerUps) {
            if (!powerUp.isActive()) {
                continue;
            }

            if (powerUp.getX() == player.getX() && powerUp.getY() == player.getY()) {
                powerUp.applyTo(player);
                powerUp.setActive(false);
                collected++;
            }
        }

        return collected;
    }
}
