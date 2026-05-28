# ASCII Asteroid Blaster

ASCII Asteroid Blaster is a Java Swing retro arcade arena shooter created for an Object-Oriented Programming project. The game uses a fixed-grid text renderer, Unicode arcade visuals, multi-directional movement, directional shooting, asteroid waves, meteor shard clusters, UFO enemies, upgrades, score tracking, lives, audio, and level progression.

## Controls

| Key | Action |
|---|---|
| W / Up Arrow | Move up |
| A / Left Arrow | Move left |
| S / Down Arrow | Move down |
| D / Right Arrow | Move right |
| W+A / W+D / S+A / S+D | Diagonal movement |
| Space | Shoot in the facing direction |
| P | Pause / resume |
| Enter | Start / launch level |
| 1, 2, 3, 4 | Choose upgrade |
| R | Restart after game over or victory |
| Q | Quit |

## Gameplay Features

- Java Swing game window
- Fixed-cell renderer for stable Unicode display
- 60x40 playable arena
- 8-direction player movement
- Directional shooting based on the ship facing direction
- Multi-shot upgrade with forward-lane bullets
- Faceted multi-cell asteroid sprites
- Heavy, standard, and light asteroid classes
- All-direction asteroid spawning
- Meteor shard clusters and asteroid shrapnel
- UFO enemies and enemy bullets
- Score, lives, danger meter, upgrades, high score, game over, and victory states
- Sound effects and looping background music

## Project Structure

```text
src/asteroidgame/
├── app/                  # Program entry point
├── audio/                # Sound manager
├── config/               # Game constants and color palettes
├── core/                 # Main engine, board, and frame snapshot
├── state/                # Game state and input state
├── objects/
│   ├── assets/           # Shared symbols
│   ├── asteroids/        # Asteroids, meteor shards, asteroid sprites
│   ├── base/             # GameObject, Drawable, Updatable
│   ├── effects/          # Explosion effect
│   ├── enemies/          # UFO enemy
│   ├── player/           # Player ship
│   ├── powerups/         # Power-up classes
│   └── projectiles/      # Player and enemy bullets
├── systems/
│   ├── collision/        # Collision checking and collision results
│   ├── scoring/          # Score and high-score managers
│   ├── spawning/         # Asteroid, cluster, UFO, power-up, shrapnel spawning
│   └── upgrades/         # Upgrade manager
└── ui/
    ├── input/            # Keyboard input
    ├── render/           # Fixed-grid panel and renderer
    └── swing/            # Swing launcher and window
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

## OOP Concepts Demonstrated

### Encapsulation
Player lives, score, input state, upgrade levels, high score, UFO behavior, asteroid damage, and movement values are stored inside classes and accessed through methods.

### Inheritance
`Player`, `Bullet`, `EnemyBullet`, `Asteroid`, `MeteorShard`, `UfoEnemy`, and `PowerUp` are built around the shared object structure. Asteroid and power-up variations extend their parent classes.

### Polymorphism
The game engine updates and draws different object types through shared behaviors such as `update()` and `draw()`, while each object still implements its own movement, drawing, and collision behavior.

### Abstraction
`GameObject`, `Drawable`, and `Updatable` provide shared contracts and base behavior so concrete objects can focus on their own specific roles.

## Final Status

This package is organized into clearer subpackages for readability and maintainability. It was compiled successfully with UTF-8 using `javac` after the package restructuring.
