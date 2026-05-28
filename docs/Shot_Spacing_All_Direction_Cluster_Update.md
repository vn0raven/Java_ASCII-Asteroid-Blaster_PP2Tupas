# Shot Spacing and All-Direction Cluster Update

This update applies another tuning pass based on playtest feedback.

## Shooting Changes

- Horizontal shots remain fast and responsive.
- Vertical and diagonal shots remain slower for readability.
- Vertical double/triple shot lanes were widened slightly so their spacing feels closer to the horizontal lanes on the rectangular text grid.
- Diagonal shots were adjusted to start closer to the ship body instead of appearing one row too high or too low.
- Multi-shot remains forward-only: bullet upgrades increase width, not bullet direction spread.

## Meteor Cluster Changes

- Meteor shard clusters now appear more often starting at Level 3.
- From Level 4 onward, clusters can spawn from all edges of the board:
  - top
  - bottom
  - left
  - right
  - corner directions
- Clusters move inward from the edge where they spawned.
- Individual shards inside a cluster still vary slightly in movement and speed to create a jagged, chaotic arcade feel.
- A safety offset helps avoid spawning edge clusters directly beside the player.

## Gameplay Goal

The goal of this pass is to make shooting more visually consistent while increasing meteor-field pressure. The game should feel more active and chaotic, but still dodgeable and readable.
