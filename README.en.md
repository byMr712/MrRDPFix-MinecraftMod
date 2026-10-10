> **Language:** [Русский](README.md) · English

# MrTrueRdpFix (Fabric 1.21.x)

![Java 21](https://img.shields.io/badge/Java-21-blue.svg)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.x-blue.svg)
![Fabric](https://img.shields.io/badge/Loader-Fabric-blue.svg)
[![ModMenu](https://img.shields.io/badge/ModMenu-Supported-blue.svg)](https://www.curseforge.com/minecraft/mc-mods/modmenu)
![License](https://img.shields.io/badge/License-MIT-blue.svg)

Fixes mouse look and erratic camera behavior when playing **Minecraft 1.21.x (Fabric)** over **Windows Remote Desktop (RDP)**.

This mod is inspired by the work of: [**RDPMouse by KesslerCascade**](https://www.curseforge.com/minecraft/mc-mods/rdpmouse)

## The Problem & The Solution

Minecraft 1.13 through 1.21.x bundles LWJGL 3.3.3 with an older GLFW 3.3 build, which fails to handle Windows virtual desktop input over RDP ([MC-126875](https://bugs.mojang.com/browse/MC-126875), GLFW issue [#1276](https://github.com/glfw/glfw/issues/1276)). Under RDP, Windows sets the `MOUSE_VIRTUAL_DESKTOP` flag, causing the camera to spin uncontrollably or get stuck at the edge of the monitor.

Mojang officially updated LWJGL/GLFW only in the 26.X snapshots.

**MrTrueRdpFix** provides a clean **native backport of GLFW 3.4+** to Minecraft 1.21.x:
1. On startup (before the game window is created), the mod transparently unpacks and directs LWJGL to a bundled **GLFW 3.4+** native library (incorporating [PR #1279](https://github.com/glfw/glfw/pull/1279) and [PR #2431](https://github.com/glfw/glfw/pull/2431)).
2. Mouse input is processed natively and hardware-accelerated by GLFW — no fragile Java-level hacks, input latency, or cursor stutter.
3. Features an optional mouse sensitivity boost (from 0% to +100%, default 0%). This is an add-on applied on top of normal in-game Minecraft sensitivity settings: if camera movement feels too sluggish over RDP, the multiplier can be customized in the mod settings via [Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu).
4. Provides additional quality-of-life features: smooth keyboard camera panning (arrow keys) and a hold-to-free cursor key (Alt).

## Controls

- **Mouse:** Works smoothly with 360-degree rotation over RDP, identical to vanilla local gameplay.
- **Sensitivity Boost:** Optional add-on (0% to +100%, default 0%) on top of normal Minecraft sensitivity settings (configured in [Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu)).
- **F8:** Toggle RDP mode on or off.
- **Alt (hold):** Temporarily release cursor grab (Free Mouse).
- **Arrow keys (Left / Right / Up / Down):** Smooth camera rotation from the keyboard.

All key bindings are configurable under *Options -> Controls -> Key Binds -> [MR] True RDP Fix*.

## Mod Menu Support

The mod includes full [**Mod Menu**](https://www.curseforge.com/minecraft/mc-mods/modmenu) configuration:
- Toggle RDP mode.
- Extra mouse sensitivity boost slider (applied on top of vanilla sensitivity from 0% to 100%, default 0%).

## Building

1. Requires Java 21 and Fabric Loader.
2. Run:
   ```cmd
   build.bat
   ```
   or via Gradle:
   ```bash
   ./gradlew build
   ```
3. The built JAR file will be in `build/libs/`.

## Authors & Licenses

- **MrTrueRdpFix:** Licensed under the [MIT License](LICENSE).  
  Authors: [KesslerCascade](https://www.curseforge.com/minecraft/mc-mods/rdpmouse), [Mr712](https://github.com/byMr712).
- **Bundled Third-Party Components:**
  - **GLFW:** [zlib/libpng License](https://www.glfw.org/license.html) (Copyright © Camilla Löwy, Marcus Geelnard & GLFW Contributors).
  - **LWJGL:** [BSD 3-Clause License](https://www.lwjgl.org/license) (Copyright © Lightweight Java Game Library).
  - Full third-party license texts are documented in [NOTICE.md](NOTICE.md).
