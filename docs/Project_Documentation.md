# ASCII Asteroid Blaster - Project Documentation

## Project Overview
ASCII Asteroid Blaster is a Java Swing retro arcade arena shooter developed as an Object-Oriented Programming application. The game demonstrates encapsulation, inheritance, polymorphism, and abstraction through a playable system with player movement, directional shooting, asteroid enemies, meteor shards, UFO enemies, scoring, upgrades, audio, and game states.

## Required Outputs Included
- Java source code in `src/`
- UML diagrams in `docs/UML/`
- Demo guide / project README
- Screenshot/proof folder in `screenshots/`
- Sounds folder in `sounds/`
- Windows and Mac/Linux run scripts

## OOP Pillars

### Encapsulation
Game data such as player lives, score, high score, input state, asteroid health, movement direction, and upgrade levels are kept inside their own classes and accessed through methods.

### Inheritance
Game objects follow shared parent structures. For example, `Asteroid` is extended by `HeavyAsteroid`, `NormalAsteroid`, and `SmallAsteroid`. `PowerUp` is extended by `ExtraLifePowerUp`.

### Polymorphism
Different game objects share update and draw behavior while implementing their own specific movement and display logic. The game engine can work with multiple object types through shared base behavior.

### Abstraction
`GameObject`, `Updatable`, and `Drawable` define common responsibilities for visible and updating game objects while hiding the details of each concrete object.

## Gameplay Summary
The player controls a ship in a fixed-grid arena using 8-direction movement. Asteroids, meteor shards, and UFO enemies create hazards. The player shoots, dodges, earns score, receives upgrades, and progresses through levels until victory or game over.
