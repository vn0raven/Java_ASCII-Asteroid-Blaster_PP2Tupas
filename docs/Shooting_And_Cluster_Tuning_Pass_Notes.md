# Shooting and Cluster Tuning Pass

This update responds to the playtest notes after the shooting-alignment build.

## Shooting Changes

- Left and right bullets remain fast because horizontal shooting felt responsive.
- Up, down, and diagonal bullets were slowed for better readability.
- Bullet speed is now direction-based:
  - Left/right: 2 cells per update
  - Up/down: 1 cell per update
  - Diagonal: 1 cell per update
- Multi-shot remains forward-only.
- Double shot remains two side-lane bullets.
- Triple shot remains side + center + side.
- Diagonal side-lane offsets were compacted so the bullets do not appear one cell too high or too low from the ship.

## Meteor Cluster Changes

- Meteor shard clusters spawn more frequently beginning at Level 3.
- Maximum active meteor shards increased to make the board feel less empty.
- Maximum cluster size increased.
- Additional jagged cluster patterns were added.
- Shards inside the same cluster now have slight movement variation, making clusters feel less uniform and more chaotic.
- Cluster movement is still controlled so it remains dodgeable.

## Design Goal

The goal of this pass is to keep shooting readable while making the asteroid field feel more alive. Horizontal shooting remains snappy, while vertical and diagonal shots are slower and easier to track. Meteor clusters now create more frequent dodge-or-shoot moments without becoming unfair.
