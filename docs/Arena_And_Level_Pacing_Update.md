# Arena and Level Pacing Update

This update prepares the game for UFO enemies by stabilizing the arena and making levels last longer.

## Changes

- Changed the playable board from 39x18 to 40x22 for a more balanced arena feel.
- Kept the fixed-grid renderer so the screen remains locked.
- Replaced single flat score-per-level pacing with cumulative score targets.
- Increased level length so waves have more time to build pressure.
- Slightly increased asteroid pressure in Levels 4 and 5.
- Slightly increased meteor cluster frequency and maximum active shards.

## New Score Targets

- Level 1 target: 300
- Level 2 target: 650
- Level 3 target: 1050
- Level 4 target: 1500
- Level 5 target: 2000
- Level 6 target: 2600

## Design Reason

The previous build was fun and fair, but Levels 3 to 5 cleared too quickly. This update gives the asteroid and meteor systems more room to breathe before adding UFO enemies and a final boss.
