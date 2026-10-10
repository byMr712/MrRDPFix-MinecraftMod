> **Language:** [Русский](README.md) · English

# MrRdpFix (Fabric 1.21.x)

![Java 21](https://img.shields.io/badge/Java-21-blue.svg)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.x-blue.svg)
![Fabric](https://img.shields.io/badge/Loader-Fabric-blue.svg)
![ModMenu](https://img.shields.io/badge/ModMenu-Supported-blue.svg)
![License](https://img.shields.io/badge/License-MIT-blue.svg)

Fixes mouse look and erratic camera behavior when playing **Minecraft 1.21.x (Fabric)** over **Windows Remote Desktop (RDP)**.

Original Author: [KesslerCascade/RDPMouse](https://github.com/KesslerCascade/RDPMouse).  
Modern update, GLFW backport & 1.21.x support: [byMr712](https://github.com/byMr712).

---

## The Problem & The Solution

Minecraft 1.13 through 1.21.x bundles LWJGL 3.3.3 with an older GLFW 3.3 build, which fails to handle Windows virtual desktop input over RDP ([MC-126875](https://bugs.mojang.com/browse/MC-126875), GLFW issue [#1276](https://github.com/glfw/glfw/issues/1276)). Under RDP, Windows sets the `MOUSE_VIRTUAL_DESKTOP` flag, causing the camera to spin uncontrollably or get stuck at the edge of the monitor.

Mojang officially updated LWJGL/GLFW only in the 26.X snapshots.

**MrRdpFix** provides a clean **native backport of GLFW 3.4+** to Minecraft 1.21.x:
1. On startup (before the game window is created), the mod transparently unpacks and directs LWJGL to a bundled **GLFW 3.4+** native library (incorporating PR #1279 and PR #2431).
2. Mouse input is processed natively and hardware-accelerated by GLFW — no fragile Java-level hacks, input latency, or cursor stutter.
3. Provides additional quality-of-life features: smooth keyboard camera panning (arrow keys) and a hold-to-free cursor key (Alt).

---

## Controls

- **Mouse:** Works smoothly with 360-degree rotation over RDP, identical to vanilla local gameplay.
- **F8:** Toggle RDP / keyboard rotation mode on or off.
- **Alt (hold):** Temporarily release cursor grab (Free Mouse).
- **Arrow keys (Left / Right / Up / Down):** Smooth camera rotation from the keyboard.

All key bindings are configurable under *Options -> Controls -> Key Binds -> [MR] RDP Fix*.

---

## Mod Menu Support

The mod includes full **Mod Menu** configuration:
- Toggle RDP / keyboard rotation mode.
- Customize keyboard rotation sensitivity.

---

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

---

## Authors & Licenses

- **MrRdpFix:** Licensed under the [MIT License](LICENSE).  
  Authors: [KesslerCascade](https://github.com/KesslerCascade), [Mr712](https://github.com/byMr712).
- **Bundled Third-Party Components:**
  - **GLFW:** [zlib/libpng License](https://www.glfw.org/license.html) (Copyright © Camilla Löwy, Marcus Geelnard & GLFW Contributors).
  - **LWJGL:** [BSD 3-Clause License](https://www.lwjgl.org/license) (Copyright © Lightweight Java Game Library).
  - Full third-party license texts are documented in [NOTICE.md](NOTICE.md).
