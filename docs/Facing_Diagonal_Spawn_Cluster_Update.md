# Facing, Diagonal Spawns, and Meteor Cluster Update

This update focuses on improving the arcade feel without adding directional shooting yet.

## Added Player Facing Direction

The player now visually faces the direction of movement:

- `▲` up
- `▼` down
- `◀` left
- `▶` right
- `◤` up-left
- `◥` up-right
- `◣` down-left
- `◢` down-right

This makes 8-direction movement feel more intentional even before directional shooting is added.

## Added Diagonal Asteroid Spawning

Main faceted asteroids now spawn from three top-side zones:

- top-left area, drifting down-right
- top-center area, falling straight or slightly diagonal
- top-right area, drifting down-left

The asteroid chooses its approach direction when it spawns. It does not perfectly home toward the player every frame, which keeps the movement fair.

## Added Meteor Shard Clusters

Meteor shard clusters begin at Level 3. They use visible symbols such as `◆`, `◇`, and `▪` and move as loose diagonal formations.

Their role is different from the main asteroid sprites:

- Main asteroids are shooting targets with damage stages.
- Meteor shard clusters are dodge-or-shoot hazards.

The player can either dodge through the cluster gaps or shoot shards to clear a path.

## Balance Rules

- Shards do not spawn before Level 3.
- Shards are visible, not tiny dot targets.
- Shard speed is capped so they are quick but not unfair.
- The number of active shards is capped to avoid visual clutter.
- Missed shards do not increase the danger meter; main asteroids still control the danger system.

## Files Changed

- `GameConfig.java`
- `GameSymbols.java`
- `Player.java`
- `Asteroid.java`
- `HeavyAsteroid.java`
- `NormalAsteroid.java`
- `SmallAsteroid.java`
- `AsteroidSpawner.java`
- `MeteorShard.java`
- `MeteorClusterSpawner.java`
- `CollisionManager.java`
- `GameEngine.java`
- `RetroTextRenderer.java`


## Player Symbol Refinement

The left and right player-facing symbols were adjusted from `◄` and `►` to `◀` and `▶` so the A/D facing directions better match the visual weight of `▲` and `▼`.
