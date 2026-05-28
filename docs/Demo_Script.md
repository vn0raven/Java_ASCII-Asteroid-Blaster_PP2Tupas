# Demo Script / Presentation Guide

## Opening

Good day everyone. Our project is called ASCII Asteroid Blaster. It is a Java Swing retro arcade game that demonstrates object-oriented programming through a playable asteroid shooting system.

## Game Overview

The player controls a spaceship inside the playfield using 8-direction movement. The goal is to shoot falling asteroids, avoid getting hit, collect power-ups, and clear all 6 levels. After each cleared level, the player chooses an upgrade to improve the ship.

## Visual Design

The game started as an ASCII-style project, but we improved the art style into a Unicode retro arcade style. The game uses symbols like ▲ for the player, ┃ for bullets, and ◉, ●, and ◎ for asteroid damage stages. The display is drawn on a fixed grid so the text does not move around or resize during gameplay.

## Gameplay Mechanics

The asteroids are intentionally large targets. Instead of tiny asteroids that are annoying to hit, every asteroid is three cells wide. Heavy asteroids take three hits and change appearance as they weaken: ◉ becomes ●, then ◎, then it is destroyed.

The game also includes a danger meter. If too many asteroids pass through the screen, the danger meter fills up and the player loses a life. This encourages the player to shoot asteroids instead of only dodging.

## Upgrade System

After clearing a level, the player can choose from four upgrades: fire speed, max lives, shot width, or shield. These upgrades have caps to keep the game balanced. For example, fire speed can only be upgraded three times, and max lives are capped at six.

## OOP Explanation

The project uses encapsulation by keeping object data like lives, score, asteroid damage, and upgrades inside their own classes.

It uses inheritance because Player, Bullet, Asteroid, and PowerUp inherit from the abstract GameObject class.

It uses polymorphism because the game engine can update and draw different objects through shared methods, even though each object behaves differently.

It uses abstraction because GameObject defines the common structure for visible game objects while subclasses provide the specific behavior.

## Closing

Overall, the final version is a complete 6-level arcade game with 8-direction player movement, shooting, collisions, scoring, lives, power-ups, upgrades, game over, and victory conditions.
