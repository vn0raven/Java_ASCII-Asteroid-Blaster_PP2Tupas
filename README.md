# ASCII Asteroid Blaster - Final Arcade Build

## Latest Gameplay Update

This version adds three gameplay improvements:

- Player-facing direction symbols for 8-direction movement (`▲ ▼ ◀ ▶ ◤ ◥ ◣ ◢`)
- Diagonal asteroid spawning from top-left, top-center, and top-right zones
- Meteor shard clusters starting at Level 3 using visible debris symbols (`◆ ◇ ▪`)

Directional shooting is not added yet. The ship now faces the movement direction first, while shooting still fires upward for stability.


A Java Swing retro text arcade game made for an Object-Oriented Programming project. The game keeps the original ASCII-inspired idea but uses a cleaner Unicode retro style, fixed-grid rendering, level progression, upgrades, capped speed, and big readable asteroids.

## Final Gameplay Design

The player controls a ship inside the playfield using 8-direction movement and must shoot falling asteroids before they hit the ship or pass through the danger zone. The game has a 6-level campaign. After clearing a level, the player chooses one upgrade before launching the next wave.

## Controls

| Key | Action |
|---|---|
| W / Up Arrow | Move up |
| A / Left Arrow | Move left |
| S / Down Arrow | Move down |
| D / Right Arrow | Move right |
| W+A, W+D, S+A, S+D | Move diagonally |
| Space | Shoot |
| P | Pause / resume |
| Enter | Start / launch level |
| 1, 2, 3, 4 | Choose upgrade |
| R | Restart after victory or game over |
| Q | Quit |

## Arcade Features

- Fixed-size Java Swing game window
- Fixed-cell renderer so Unicode symbols do not shift around
- 8-direction player movement using WASD or arrow keys
- Retro Unicode arcade art style
- 6-level campaign structure
- Start screen, level intro screens, pause screen, upgrade screen, victory screen, and game over screen
- Big asteroids only; no tiny annoying targets
- Asteroids shrink through damage stages while staying large and easy to hit
- Slower gameplay pace with capped asteroid speed
- Score target per level
- Upgrade system with caps
- Danger meter for missed asteroids
- Immediate feedback messages for hits, damage, shields, power-ups, and maxed upgrades

## Symbols

| Symbol | Meaning |
|---|---|
| ▲ | Player ship |
| △ | Shielded player |
| ◆ | Hit/damaged player flash |
| ┃ | Bullet |
| ◉ | Armored asteroid, 3 hits total |
| ● | Cracked asteroid, 2 hits total |
| ◎ | Weak asteroid, 1 hit remaining |
| ✦ | Extra-life power-up |

Asteroid damage flow:

```text
◉ → ● → ◎ → destroyed
```

The asteroid remains 3 cells wide in every stage. This avoids the problem of tiny asteroids being annoying to hit.

## Level Structure

| Level | Name | Gameplay Feel |
|---|---|---|
| 1 | Training Field | Learn 8-direction movement and shooting rhythm |
| 2 | Light Rocks | More readable asteroid pressure |
| 3 | Split Zone | Heavy asteroids begin appearing |
| 4 | Heavy Belt | More heavy asteroid damage stages |
| 5 | Meteor Storm | More lanes and spawn pressure |
| 6 | Final Wave | Final survival wave before victory |

## Upgrade System

| Upgrade | Cap | Effect |
|---|---:|---|
| Fire Speed | 3 | Lowers shot cooldown |
| Max Lives | 6 HP | Raises life cap and heals the player |
| Shot Width | 2 | Adds double/triple shot while keeping the center bullet |
| Shield | 2 charges | Blocks asteroid hits |

If an upgrade is already maxed, the game shows a warning instead of feeling unresponsive.

## Danger Meter

Missed asteroids increase the danger meter. When danger reaches 5/5, the player loses 1 HP and the danger meter resets. This prevents the player from only dodging forever and encourages active shooting.

## Project Structure

```text
src/asteroidgame/
├── app/
│   └── Main.java
├── core/
│   ├── GameBoard.java
│   ├── GameConfig.java
│   ├── GameEngine.java
│   ├── GameFrame.java
│   ├── GameState.java
│   └── InputState.java
├── managers/
│   ├── CollisionManager.java
│   ├── CollisionResult.java
│   ├── ScoreManager.java
│   └── UpgradeManager.java
├── objects/
│   ├── Asteroid.java
│   ├── Bullet.java
│   ├── ExtraLifePowerUp.java
│   ├── GameObject.java
│   ├── GameSymbols.java
│   ├── HeavyAsteroid.java
│   ├── NormalAsteroid.java
│   ├── Player.java
│   ├── PowerUp.java
│   └── SmallAsteroid.java
└── ui/
    ├── FixedGridPanel.java
    ├── KeyboardInput.java
    ├── RetroTextRenderer.java
    ├── SwingGameWindow.java
    └── SwingLauncher.java
```

## How to Run

### Windows

Double-click:

```text
run_windows.bat
```

### Mac/Linux

```bash
chmod +x run_mac_linux.sh
./run_mac_linux.sh
```

### Manual Compile and Run

```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -Dfile.encoding=UTF-8 -cp out asteroidgame.app.Main
```

## OOP Concepts Used

### Encapsulation

Player lives, upgrade levels, asteroid damage stages, score, and input states are stored inside classes and accessed through methods.

### Inheritance

`Player`, `Bullet`, `Asteroid`, and `PowerUp` inherit from `GameObject`. Asteroid and power-up variations extend their parent classes.

### Polymorphism

The game updates and draws objects through shared methods like `update()` and `draw()`, even though each object behaves differently.

### Abstraction

`GameObject` defines shared object behavior, while specific subclasses provide their own movement, collision, and visual behavior.

## Final Status

This build adds the 8-direction movement upgrade while keeping the current asteroid-shooter gameplay stable. It compiles successfully using `javac` with UTF-8 encoding.

## Shrapnel + Directional Shooting Update

This version adds asteroid destruction shrapnel and directional shooting. Destroyed asteroids now release meteor shards, creating a dodge-or-shoot moment after each asteroid kill. The player now shoots in the direction the ship is facing, and shot-width upgrades work across all eight directions.

Run with:

```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -Dfile.encoding=UTF-8 -cp out asteroidgame.app.Main
```


## Shooting Alignment Fix

The current build uses forward-lane shooting: single shot is centered, double shot uses two side lanes, and triple shot uses side + center + side. All bullets travel in the ship-facing direction. Player bullets move two cells per update, with path collision checks to prevent skipped hits.


## Latest Tuning Pass

This version includes a shooting and meteor-cluster tuning pass. Horizontal shots remain fast, while vertical and diagonal shots are slower for better readability. Diagonal multi-shot offsets were tightened to reduce the one-cell alignment issue. Meteor shard clusters now spawn more often, use more jagged patterns, and vary shard movement so the asteroid field feels more chaotic while remaining fair.


## Arena and Level Pacing Update

The current build uses a larger 60x40 playable arena with reduced grid-cell size so the game still fits the window. This gives diagonal movement, diagonal shooting, meteor clusters, UFOs, and shrapnel more room to breathe while keeping the arcade chaos.


## UFO Enemy Update

The game now includes compact symmetrical UFO enemies starting at Level 4. UFOs move across cleaner upper lanes, avoid spawning directly on top of active asteroids, fire dodgeable enemy bullets toward the player, and can be destroyed for bonus points. Level 5 increases UFO pressure by allowing more active UFOs.

New classes added in this update:

```text
src/asteroidgame/objects/UfoEnemy.java
src/asteroidgame/objects/EnemyBullet.java
src/asteroidgame/managers/UfoSpawner.java
```

UFOs support the OOP structure because they are independent game objects with their own drawing, movement, hit detection, health, and shooting behavior.
