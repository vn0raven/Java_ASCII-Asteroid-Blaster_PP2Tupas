# Shooting Alignment Fix Notes

This update adjusts the directional shooting system based on playtest feedback.

## Changes

- Removed fan-shot behavior that made multi-shot feel scattered.
- All bullets now travel in the same direction the ship is facing.
- Single shot fires one centered forward bullet.
- Double shot fires two side-lane bullets beside the ship nose.
- Triple shot fires side + center + side, all moving forward.
- Player bullets now move 2 cells per update for snappier left/right shooting.
- Bullet collision now checks the movement path so faster bullets do not skip through asteroids or shards.
- Diagonal shots spawn from the ship corner and use the correct diagonal bullet symbol.

## Design Reason

The shooting system now behaves more like a mobile arcade shooter: upgrades widen the forward attack instead of scattering shots into different directions.
