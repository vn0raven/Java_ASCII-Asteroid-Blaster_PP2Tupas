# UFO Enemy Update

This update adds regular UFO enemies as the next major combat layer after the arena and asteroid systems.

## Gameplay Changes

- UFO enemies begin appearing at Level 4.
- Level 4 allows one active UFO at a time.
- Level 5 and above can have up to two active UFOs.
- UFOs move across the upper arena and slightly bob up and down.
- UFOs shoot enemy bullets toward the player's current position.
- Enemy bullets travel straight after being fired, so they are dodgeable and not unfairly homing.
- Player bullets can damage and destroy UFOs.
- Destroyed UFOs give a larger score reward than normal asteroid hits.
- The game now tracks UFOs destroyed for the victory and game-over summaries.

## Balance Notes

UFOs were added only from Level 4 onward so the player first learns movement, asteroid shooting, meteor clusters, and all-direction meteor pressure. The UFO adds projectile pressure without replacing the asteroid system.

## OOP Notes

The update adds new object classes:

- `UfoEnemy`
- `EnemyBullet`

and a new manager class:

- `UfoSpawner`

This strengthens the OOP design because UFOs and enemy bullets are separate objects with their own movement, drawing, and collision behavior.
