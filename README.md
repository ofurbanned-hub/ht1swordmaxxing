# HT1 Client

A singleplayer combat-assist Fabric mod: smooth human-like auto-aim, autonomous
movement, and perfectly-timed auto-attack — built for content creation, not
public multiplayer use.

## Requirements
- Java 17+
- Minecraft 1.20.1
- Fabric Loader 0.15.11+
- Fabric API (matching version is pulled automatically by Gradle)

## Build
```bash
cd ht1-client
./gradlew build
```
The compiled mod jar will show up in `build/libs/ht1-client-1.0.0.jar`.

## Install
1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 1.20.1.
2. Download/build the matching **Fabric API** jar and drop it in `.minecraft/mods/`.
3. Drop `ht1-client-1.0.0.jar` into `.minecraft/mods/`.
4. Launch the Fabric profile.

## Usage
- Press **Right Shift** in-game to open the toggle panel (doesn't pause the world).
- Flip **AI Mode** on to activate everything; flip individual modules
  (Auto Aim / Auto Move / Auto Attack) on or off independently.
- Press **Right Shift** again to close the panel.

## Tuning
Open `HT1Config.java` to adjust:
- `reachDistance` — attack range
- `turnSmoothing` — lower = laggier, more human camera movement
- `maxDegreesPerTick` — caps how fast the head can snap toward a target

## Notes
This is built around client-side input simulation (movement fields, real
attack calls) rather than teleportation or packet spoofing, so it behaves
like normal player input for recording/demo purposes. It's intended for
singleplayer worlds — using automated combat like this on servers you don't
own/control will violate almost every server's rules and can get accounts banned.
