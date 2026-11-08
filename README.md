# Custom UI Scale (Fabric)

Client-side Fabric mod that provides separate interface and inventory scale controls.

## Supported Minecraft versions
- 1.21.4
- 1.21.11

## Controls
- Video Settings: custom UI scale slider from 1 to 10. Value 10 targets the size of vanilla GUI Scale 3.
- Inventory screen: independent scale slider from 1 to 5. Value 5 targets the size of vanilla GUI Scale 3.
- Both values are saved independently in `config/customuiscale.json`.
- The mod does not edit Sodium's configuration or call Sodium internals.

## Build artifacts
Each successful GitHub Actions run publishes two downloadable artifacts:
- `customuiscale-mc-1.21.4`
- `customuiscale-mc-1.21.11`

Open the Actions tab, select a successful **Build Fabric mod** run, then download the matching artifact.

## Compatibility
The project compiles successfully for both target versions. The mod is client-side and does not depend on Sodium. Full in-game testing with Sodium on both versions is still required before claiming verified compatibility.
