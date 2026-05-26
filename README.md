# ASCII Asteroid Blaster - Final Arcade Build

A Java Swing retro text arcade game made for an Object-Oriented Programming project. The game keeps the original ASCII-inspired idea but uses a cleaner Unicode retro style, fixed-grid rendering, level progression, upgrades, capped speed, and big readable asteroids.

## Final Gameplay Design

The player controls a ship at the bottom of the screen and must shoot falling asteroids before they hit the ship or pass through the danger zone. The game has a 6-level campaign. After clearing a level, the player chooses one upgrade before launching the next wave.

## Controls

| Key | Action |
|---|---|
| A / Left Arrow | Move left |
| D / Right Arrow | Move right |
| Space | Shoot |
| P | Pause / resume |
| Enter | Start / launch level |
| 1, 2, 3, 4 | Choose upgrade |
| R | Restart after victory or game over |
| Q | Quit |

## Arcade Features

- Fixed-size Java Swing game window
- Fixed-cell renderer so Unicode symbols do not shift around
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
| 1 | Training Field | Learn movement and shooting rhythm |
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

This build is intended as the final playable version for submission. It compiles successfully using `javac` with UTF-8 encoding.
