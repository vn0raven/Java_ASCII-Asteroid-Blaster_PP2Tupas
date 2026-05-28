# Asteroid Sprite Design Notes

This version replaces simple circle asteroids with faceted multi-cell asteroid sprites. The design is inspired by an angular arcade asteroid reference, but each asteroid family uses different silhouettes and inner crack details so the visuals do not look repetitive.

## Sprite Families

### Heavy Asteroids
- Large armored sprites
- 3 HP
- Slow movement
- Uses dense characters such as `█`, `▓`, and `▒`
- Changes into cracked and weak forms when shot

### Standard Asteroids
- Mid-sized faceted rock sprites
- 2 HP
- Common wave enemy
- Uses varied inner crack patterns

### Light Asteroids
- Smaller but still readable multi-cell sprites
- 1 HP
- Faster than standard asteroids
- Not represented by tiny dots

## Gameplay Reason

The sprites are larger than one character, so asteroid collision now checks the sprite footprint instead of only the center point. This makes the asteroid feel like an actual object and makes shooting fairer.

## OOP Reason

The `Asteroid` class now stores its sprite family, variant, and damage stage. The `AsteroidSprites` utility class provides the visual patterns. Heavy, standard, and light asteroids still inherit from the same `Asteroid` parent class, strengthening the inheritance and polymorphism explanation.
