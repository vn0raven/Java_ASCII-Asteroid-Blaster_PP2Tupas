# Big Asteroids Design Notes

The previous shrinking asteroid system was fun, but the final small asteroid stage was too hard and annoying to hit. This version keeps the idea of damage stages while removing tiny targets.

## New asteroid behavior

Asteroids are always large targets. Each asteroid is drawn across three grid cells.

```text
◉◉◉  armored stage, 3 hits total
●●●  cracked stage, 2 hits remaining
◎◎◎  weak stage, 1 hit remaining
```

## Why this is better

From a gameplay standpoint, the player should fight the asteroid, not the controls. A tiny one-cell target can feel unfair in a keyboard-controlled grid game. The larger hitbox makes the game more readable and satisfying while keeping the multi-hit mechanic.

## Balance changes

- Removed tiny asteroid spawns.
- Asteroids are 3 cells wide.
- Bullet collision checks the full asteroid width.
- Asteroid speed is capped to stay readable.
- Maximum asteroids on screen reduced to prevent clutter.
- Timer delay changed to 120 ms for a slightly slower arcade pace.
