# Custom UI Scale (Fabric)

Fabric client-side mod for independent interface scaling.

## Target versions
- Minecraft 1.21.4
- Minecraft 1.21.11

## Planned controls
- Graphics settings: UI scale slider from 1 to 10 (10 maps to vanilla GUI scale 3).
- Inventory screen: independent slider from 1 to 5 (5 maps to vanilla GUI scale 3).
- Persist both values independently.
- Do not modify Sodium configuration or depend on Sodium internals.

## Build
GitHub Actions builds each Minecraft target separately. Successful JARs will be attached to the workflow run as artifacts.

## Compatibility note
Sodium compatibility is a goal; it is not claimed as verified until tested in-game on both target versions.

## Development status
Repository initialized. Rendering/input transforms and in-game controls are not implemented yet. This initial commit is a buildable project scaffold, not a finished playable mod.
