> **Language:** [Русский](README.md) · English

# MrTrueRdpFix (Fabric 1.21.x)

![Java 21](https://img.shields.io/badge/Java-21-blue.svg)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.x-blue.svg)
![Fabric](https://img.shields.io/badge/Loader-Fabric-blue.svg)
![Fabric API](https://img.shields.io/badge/Fabric%20API-Not%20Required-success.svg)
![Zero Config](https://img.shields.io/badge/Config-Zero%20Config-brightgreen.svg)
![License](https://img.shields.io/badge/License-MIT-blue.svg)

Fixes mouse look when playing **Minecraft 1.21.x (Fabric)** over **Remote Desktop Protocol (RDP)**.

This mod is inspired by the work of: [**RDPMouse by KesslerCascade**](https://www.curseforge.com/minecraft/mc-mods/rdpmouse)

## Gallery
| Vanilla Game | With This Mod |
| :---: | :---: |
| ![Full_vanilla](images/Full_vanilla.webp) | ![This_mod](images/This_mod.webp) |

## About the Mod
Minecraft versions 1.13 through 1.21.x bundle LWJGL 3.3.3 with an older GLFW 3.3 build, which fails to handle Windows virtual desktop mouse input over RDP ([MC-126875](https://bugs.mojang.com/browse/MC-126875), GLFW issue [#1276](https://github.com/glfw/glfw/issues/1276)). Under RDP, Windows sets the `MOUSE_VIRTUAL_DESKTOP` flag, causing the camera to spin uncontrollably into the sky or get stuck at the edge of the monitor.

Mojang resolved this issue in the 26.X snapshots by upgrading GLFW to 3.4+.

## What Does the Mod Do?
**MrTrueRdpFix** provides a clean **native backport of GLFW 3.4+** to Minecraft 1.21.x:
1. On startup (before the game window is created), the mod transparently unpacks and directs LWJGL to a bundled **GLFW 3.4+** native library (incorporating official fixes from [PR #1279](https://github.com/glfw/glfw/pull/1279) and [PR #2431](https://github.com/glfw/glfw/pull/2431)).
2. Mouse input is processed natively and hardware-accelerated by GLFW — no input latency, cursor stutter, or fragile Java-level hacks.

## "Install & Forget"

The mod operates completely silently in the background:
- **No extra menus or config files:** Mouse look behaves 100% like local vanilla gameplay right after installation.
- **No unnecessary keybindings:** Does not clutter the game's controls menu with extra keys.
- **Zero Dependencies:** Fabric API is **NOT required** — the mod runs directly on pure Fabric Loader (while remaining 100% compatible with any modpacks).

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
