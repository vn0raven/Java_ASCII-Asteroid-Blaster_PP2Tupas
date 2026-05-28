package asteroidgame.managers;

import asteroidgame.objects.Asteroid;
import asteroidgame.objects.Bullet;
import asteroidgame.objects.Explosion;
import asteroidgame.objects.Player;
import asteroidgame.objects.PowerUp;
import asteroidgame.objects.UFO;

import java.util.List;

/**
 * Handles object collision checking for Asteroids, Power-Ups, and UFOs.
 */
public class CollisionManager {

    // --- ASTEROID COLLISIONS ---
    public CollisionResult checkBulletAsteroidCollisions(List<Bullet> bullets, List<Asteroid> asteroids, List<Explosion> explosions) {
        int earnedPoints = 0;
        int destroyedCount = 0;
        int damagedCount = 0;

        for (Bullet bullet : bullets) {
            if (!bullet.isActive()) continue;

            for (Asteroid asteroid : asteroids) {
                if (!asteroid.isActive()) continue;

                if (asteroid.isHitBy(bullet.getX(), bullet.getY())) {
                    bullet.setActive(false);

                    int hitScore = asteroid.getHitScoreValue();
                    boolean destroyed = asteroid.takeHit();

                    if (destroyed) {
                        earnedPoints += asteroid.getDestroyScoreValue();
                        destroyedCount++;
                        explosions.add(new Explosion(asteroid.getX(), asteroid.getY())); 
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
            if (!asteroid.isActive()) continue;

            if (asteroid.overlapsPoint(player.getX(), player.getY())) {
                asteroid.setActive(false);
                return true;
            }
        }
        return false;
    }

    // --- POWER-UP COLLISIONS ---
    public int checkPlayerPowerUpCollision(Player player, List<PowerUp> powerUps) {
        int collected = 0;

        for (PowerUp powerUp : powerUps) {
            if (!powerUp.isActive()) continue;

            if (powerUp.getX() == player.getX() && powerUp.getY() == player.getY()) {
                powerUp.applyTo(player);
                powerUp.setActive(false);
                collected++;
            }
        }

        return collected;
    }

    // --- NEW: UFO COLLISIONS ---
    public CollisionResult checkBulletUFOCollisions(List<Bullet> bullets, List<UFO> ufos, List<Explosion> explosions) {
        int earnedPoints = 0;
        int destroyedCount = 0;

        for (Bullet bullet : bullets) {
            if (!bullet.isActive()) continue;

            for (UFO ufo : ufos) {
                // Cannot shoot a UFO while it is in its flashing warning phase!
                if (!ufo.isActive() || ufo.isSpawning()) continue;

                if (ufo.getX() == bullet.getX() && ufo.getY() == bullet.getY()) {
                    bullet.setActive(false);
                    
                    boolean destroyed = ufo.takeHit();

                    if (destroyed) {
                        earnedPoints += 50; // UFOs are worth massive points!
                        destroyedCount++;
                        explosions.add(new Explosion(ufo.getX(), ufo.getY())); 
                    } else {
                        earnedPoints += 10; // Small points for damaging a heavy UFO
                    }

                    break;
                }
            }
        }

        return new CollisionResult(earnedPoints, destroyedCount, 0);
    }

    public boolean checkPlayerUFOCollision(Player player, List<UFO> ufos) {
        for (UFO ufo : ufos) {
            // Cannot crash into a UFO during its warning phase
            if (!ufo.isActive() || ufo.isSpawning()) continue;

            if (ufo.getX() == player.getX() && ufo.getY() == player.getY()) {
                ufo.setActive(false); // The UFO is destroyed in the crash
                return true; // The player takes a hit
            }
        }
        return false;
    }
}