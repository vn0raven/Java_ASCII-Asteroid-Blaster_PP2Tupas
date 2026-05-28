package asteroidgame.managers;

import asteroidgame.objects.Asteroid;
import asteroidgame.objects.Bullet;
import asteroidgame.objects.Player;
import asteroidgame.objects.Explosion;
import asteroidgame.objects.PowerUp;
import asteroidgame.objects.MeteorShard;
import asteroidgame.objects.UfoEnemy;
import asteroidgame.objects.EnemyBullet;

import java.util.List;

/**
 * Handles object collision checking.
 */
public class CollisionManager {

    public CollisionResult checkBulletAsteroidCollisions(List<Bullet> bullets, List<Asteroid> asteroids, List<Explosion> explosions, List<MeteorShard> meteorShards, int boardWidth, int boardHeight) {
        int earnedPoints = 0;
        int destroyedCount = 0;
        int damagedCount = 0;

        for (Bullet bullet : bullets) {
            if (!bullet.isActive() && bullet.getPathStepCount() == 0) continue;

            for (Asteroid asteroid : asteroids) {
                if (!asteroid.isActive()) continue;

                if (bulletHitsAsteroid(bullet, asteroid)) {
                    bullet.setActive(false);

                    int hitScore = asteroid.getHitScoreValue();
                    boolean destroyed = asteroid.takeHit();

                    if (destroyed) {
                        earnedPoints += asteroid.getDestroyScoreValue();
                        destroyedCount++;
                        addDestructionExplosions(explosions, asteroid);
                        meteorShards.addAll(ShrapnelFactory.createBurst(asteroid, boardWidth, boardHeight, meteorShards.size()));
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



    private boolean bulletHitsAsteroid(Bullet bullet, Asteroid asteroid) {
        int steps = Math.max(1, bullet.getPathStepCount());

        for (int step = 1; step <= steps; step++) {
            if (asteroid.isHitBy(bullet.getPathX(step), bullet.getPathY(step))) {
                return true;
            }
        }

        return asteroid.isHitBy(bullet.getX(), bullet.getY());
    }

    private boolean bulletHitsShard(Bullet bullet, MeteorShard shard) {
        int steps = Math.max(1, bullet.getPathStepCount());

        for (int step = 1; step <= steps; step++) {
            if (shard.overlapsPoint(bullet.getPathX(step), bullet.getPathY(step))) {
                return true;
            }
        }

        return shard.overlapsPoint(bullet.getX(), bullet.getY());
    }

    private void addDestructionExplosions(List<Explosion> explosions, Asteroid asteroid) {
        int centerX = asteroid.getX();
        int centerY = asteroid.getCenterY();

        explosions.add(new Explosion(centerX, centerY));

        if (asteroid.getSpriteWidth() >= 6) {
            explosions.add(new Explosion(centerX - 2, centerY));
            explosions.add(new Explosion(centerX + 2, centerY));
        }

        if (asteroid.getSpriteHeight() >= 4) {
            explosions.add(new Explosion(centerX, centerY - 1));
        }
    }

    public CollisionResult checkBulletMeteorShardCollisions(List<Bullet> bullets, List<MeteorShard> shards, List<Explosion> explosions) {
        int earnedPoints = 0;
        int destroyedCount = 0;

        for (Bullet bullet : bullets) {
            if (!bullet.isActive() && bullet.getPathStepCount() == 0) continue;

            for (MeteorShard shard : shards) {
                if (!shard.isActive()) continue;

                if (bulletHitsShard(bullet, shard)) {
                    bullet.setActive(false);
                    shard.setActive(false);
                    earnedPoints += shard.getScoreValue();
                    destroyedCount++;
                    explosions.add(new Explosion(shard.getX(), shard.getY()));
                    break;
                }
            }
        }

        return new CollisionResult(earnedPoints, destroyedCount, 0);
    }



    public CollisionResult checkBulletUfoCollisions(List<Bullet> bullets, List<UfoEnemy> ufos, List<Explosion> explosions) {
        int earnedPoints = 0;
        int destroyedCount = 0;
        int damagedCount = 0;

        for (Bullet bullet : bullets) {
            if (!bullet.isActive() && bullet.getPathStepCount() == 0) continue;

            for (UfoEnemy ufo : ufos) {
                if (!ufo.isActive()) continue;

                if (bulletHitsUfo(bullet, ufo)) {
                    bullet.setActive(false);
                    boolean destroyed = ufo.takeHit();

                    if (destroyed) {
                        earnedPoints += ufo.getDestroyScoreValue();
                        destroyedCount++;
                        explosions.add(new Explosion(ufo.getX(), ufo.getCenterY()));
                        explosions.add(new Explosion(ufo.getX() - 2, ufo.getCenterY()));
                        explosions.add(new Explosion(ufo.getX() + 2, ufo.getCenterY()));
                    } else {
                        earnedPoints += ufo.getHitScoreValue();
                        damagedCount++;
                    }

                    break;
                }
            }
        }

        return new CollisionResult(earnedPoints, destroyedCount, damagedCount);
    }

    private boolean bulletHitsUfo(Bullet bullet, UfoEnemy ufo) {
        int steps = Math.max(1, bullet.getPathStepCount());

        for (int step = 1; step <= steps; step++) {
            if (ufo.isHitBy(bullet.getPathX(step), bullet.getPathY(step))) {
                return true;
            }
        }

        return ufo.isHitBy(bullet.getX(), bullet.getY());
    }

    public boolean checkPlayerEnemyBulletCollision(Player player, List<EnemyBullet> enemyBullets) {
        for (EnemyBullet bullet : enemyBullets) {
            if (!bullet.isActive()) continue;

            if (bullet.overlapsPoint(player.getX(), player.getY())) {
                bullet.setActive(false);
                return true;
            }
        }

        return false;
    }

    public boolean checkPlayerUfoCollision(Player player, List<UfoEnemy> ufos) {
        for (UfoEnemy ufo : ufos) {
            if (!ufo.isActive()) continue;

            if (ufo.overlapsPoint(player.getX(), player.getY())) {
                ufo.setActive(false);
                return true;
            }
        }

        return false;
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

    public boolean checkPlayerMeteorShardCollision(Player player, List<MeteorShard> shards) {
        for (MeteorShard shard : shards) {
            if (!shard.isActive()) {
                continue;
            }

            if (shard.isDangerous() && shard.overlapsPoint(player.getX(), player.getY())) {
                shard.setActive(false);
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
