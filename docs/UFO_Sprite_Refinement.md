# UFO Sprite Refinement

This update refines the regular UFO enemy visual design.

## Reason for the change

The previous UFO sprite used literal `UFO` letters and a taller body. It was readable, but it looked too clunky and visually overlapped with asteroid sprites too strongly during crowded gameplay.

## New UFO style

The UFO now uses a compact symmetrical saucer sprite:

```text
 ╭─◉─╮ 
 ╰═╩═╯ 
```

## Gameplay effect

- The UFO is still readable as an alien ship.
- The sprite is smaller and cleaner.
- It reduces visual clutter when asteroids, shards, bullets, and UFOs are on screen together.
- It fits the retro arcade style better than a literal text-label design.

## Design rule

The UFO should look different from asteroids while staying compact. The game should feel chaotic, but important enemy types should remain visually distinct.
