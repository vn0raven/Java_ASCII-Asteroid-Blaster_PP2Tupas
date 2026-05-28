# Shrapnel and Directional Shooting Update

## Purpose

This update improves the arcade feel of the current asteroid system before adding UFO enemies or a boss battle. It focuses on four changes:

1. Asteroids create shrapnel when destroyed.
2. Shrapnel speed, count, and damage delay are capped for fairness.
3. Player bullets now travel in the player's facing direction.
4. Shot-width upgrades now work in all eight directions.

## Asteroid Shrapnel

When a faceted asteroid is destroyed, it now releases meteor shards. This creates a short risk-reward moment: destroying an asteroid gives points, but the player must also dodge or shoot the debris that follows.

Shrapnel count is based on asteroid strength:

- Heavy asteroid: 7 shards
- Normal asteroid: 5 shards
- Light asteroid: 3 shards

The shard symbols are:

- ◆ solid shard
- ◇ light shard
- ▪ small chip

## Damage Delay

New shrapnel has a short activation delay before it can damage the player. This prevents unfair instant damage when the player destroys an asteroid at close range.

Current delay:

- 6 frames before shrapnel becomes dangerous

Bullets can still destroy shards immediately, so the player can clear a path through the burst.

## Directional Shooting

Bullets now follow the player's last facing direction:

- ▲ shoots upward
- ▼ shoots downward
- ◀ shoots left
- ▶ shoots right
- ◤ shoots up-left
- ◥ shoots up-right
- ◣ shoots down-left
- ◢ shoots down-right

If the player has not moved yet, the default shot direction is upward.

## Shot-Width Upgrade

The shot-width upgrade now works relative to the current facing direction.

- Level 0: one forward bullet
- Level 1: center bullet + one side bullet
- Level 2: left side + center + right side bullets

This keeps the center shot active at every upgrade level, so the upgrade never feels like a downgrade.

## Gameplay Effect

The updated combat loop is:

1. Move and face the direction of travel.
2. Shoot in that direction.
3. Destroy large asteroids.
4. Dodge or clear the shrapnel burst.
5. Use shot-width and fire-rate upgrades to control more space.

This makes the game closer to a retro arcade shooter with mobile-style upgrade progression.
