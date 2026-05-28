# Arena, UFO, and Asteroid Variety Update

This update adjusts the game toward a wider Asteroids-arcade arena while keeping the retro text-based identity.

## Main changes

- Playable arena increased from 40x22 to 60x40.
- Fixed-grid font/cell size reduced so the larger arena still fits the game window.
- UFO sprite refined into a compact 5x2 symmetrical saucer:

```text
╭─◉─╮
╰═╩═╯
```

- UFO spawns now check nearby asteroids and other UFOs before appearing.
- Asteroid spawns also avoid active UFO lanes near the top of the arena.
- Heavy asteroid frequency was reduced so heavy asteroids feel special instead of constant.
- Standard and light asteroid frequency was increased for better size variety.
- Standard and light asteroid sprite libraries gained more variants.
- Meteor clusters retain their chaotic behavior but now have more arena space and a higher active-shard limit.

## Gameplay purpose

The larger arena gives the player more room to dodge, aim diagonally, react to all-direction clusters, and deal with UFO fire. The asteroid field should now feel less cramped while still becoming chaotic in higher levels.

## Balance direction

- Heavy asteroids are rare tank threats.
- Standard asteroids are the main wave enemy.
- Light asteroids provide faster pressure.
- Meteor shards and clusters add dodge-or-shoot decisions.
- UFOs occupy cleaner upper lanes and avoid spawning directly on top of asteroid sprites.
