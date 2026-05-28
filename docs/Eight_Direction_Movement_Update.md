# Eight-Direction Movement Update

This update changes the player from left/right movement to controlled 8-direction movement. The ship can now move up, down, left, right, and diagonally using WASD or the arrow keys.

## Controls

| Key Combination | Movement |
|---|---|
| W / Up Arrow | Up |
| S / Down Arrow | Down |
| A / Left Arrow | Left |
| D / Right Arrow | Right |
| W + A | Up-left |
| W + D | Up-right |
| S + A | Down-left |
| S + D | Down-right |

## Code Changes

- `KeyboardInput` now tracks continuous up/down key states in addition to one-shot menu requests.
- `InputState` separates pressed movement inputs from requested menu/action inputs.
- `Player` now has a `move(dx, dy)` method that supports all eight directions while keeping the ship inside the board.
- `GameEngine` converts held keys into `dx` and `dy` values before moving the player.

## Design Note

Shooting is intentionally still upward in this phase. Directional shooting should be implemented as the next separate upgrade so the movement change can be tested safely first.
