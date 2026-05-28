# Final Gameplay Notes

## Main Direction

The final version is a level-based retro arcade shooter. The project still follows the ASCII-inspired concept, but it uses Unicode retro symbols and a fixed-cell Swing renderer to make the game look cleaner and more stable.

## Why Big Asteroids Only?

Earlier versions used small asteroid symbols, but they were annoying to hit on a keyboard-controlled grid. The final version removes visually tiny asteroids. Instead, every asteroid stage remains 3 cells wide.

Damage stages:

```text
◉ armored asteroid  -> 3 hits total
● cracked asteroid  -> 2 hits total
◎ weak asteroid     -> 1 hit remaining
```

This keeps the satisfaction of shooting asteroids smaller without forcing the player to hit tiny targets.

## Why Slower Gameplay?

The gameplay was intentionally slowed down because the game is grid-based. If the asteroids move too fast, the challenge becomes unfair instead of fun. The game now has a speed cap so higher levels become harder through spawn pressure and asteroid durability, not uncontrollable speed.

## Campaign Length

The game is capped at 6 levels. This is better than making the game endless because it gives the project a clear start, middle, and finish.

## Upgrade Philosophy

Upgrades make the player feel stronger between levels, but every upgrade has a cap so the game remains balanced.

Final upgrade caps:

- Fire Speed: 3
- Shot Width: 2
- Max Lives: 6 HP
- Shield: 2 charges

## Danger Meter

Missed asteroids increase the danger meter. At 5 missed asteroids, the player loses 1 HP. This prevents the player from only dodging and gives the player a reason to actively shoot asteroids.

## Final Arcade Loop

```text
Start level
↓
Shoot and dodge big asteroids
↓
Asteroids crack through damage stages
↓
Reach target score
↓
Choose one upgrade
↓
Launch the next level
↓
Clear Level 6 or lose all HP
```
