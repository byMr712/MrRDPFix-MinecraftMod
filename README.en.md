> **Language:** [Русский](README.md) · English

# MrRdpFix (Minecraft 1.21.4 Fabric Port)

![Java 21](https://img.shields.io/badge/Java-21-blue.svg)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.4-blue.svg)
![Fabric](https://img.shields.io/badge/Loader-Fabric-blue.svg)
![ModMenu](https://img.shields.io/badge/ModMenu-Supported-blue.svg)
![License](https://img.shields.io/badge/License-MIT-blue.svg)

Port and update of the **MrRdpFix** ([MR] RDP Mouse) mod for **Minecraft 1.21.4 (Fabric)**.

Original Developer: [KesslerCascade/RDPMouse](https://github.com/KesslerCascade/RDPMouse).

---

## Overview

Minecraft uses raw relative mouse input by default, which Windows blocks over Remote Desktop (RDP). As a result, the in-game camera spins uncontrollably the moment you move the mouse (known bugs [MC-107122](https://bugs.mojang.com/browse/MC-107122) and [MC-126875](https://bugs.mojang.com/browse/MC-126875)).

**RDPMouse** replaces raw relative input with absolute cursor position tracking supported by the RDP protocol, restoring full, smooth camera control.

---

## Usage & Controls

- Press **F8** to toggle RDP Mode on or off.
- When RDP Mode is active, moving the mouse controls the camera normally within window boundaries.
- If the camera stops turning, the cursor has reached the edge of the window: hold **Alt** to release the cursor, recenter it in the middle of the screen, and release **Alt** to resume.
- **Arrow keys** can also be used to smoothly pan the camera from the keyboard.

### Key Bindings

| Key | Action |
|---|---|
| F8 | Toggle RDP Mode |
| Alt (hold) | Release cursor to recenter |
| Arrow keys (Left / Right / Up / Down) | Pan camera from keyboard |

All key bindings can be configured in: *Options -> Controls -> Key Binds -> RDP Mouse*.

---

## Changes in 1.21.4 Port (byMr712)

- **Ported to Minecraft 1.21.4 (Fabric Loader)**:
  - Built on modern stack (Java 21 LTS, Fabric Loom 1.10.1, Yarn `1.21.4+build.7`).
  - Adapted key mapping registration and GLFW Window / Mouse hooks for 1.21.4 mappings and architecture.
  - Optimized flat Fabric project layout with zero unnecessary dependencies.
- **Full Localization**:
  - Added Russian (`ru_ru.json`) and English (`en_us.json`) translations.
- **Build Convenience**:
  - Added `build.bat` helper script for quick builds.

---

## Installation

1. Download the latest release from [GitHub Releases](https://github.com/byMr712/MrRDPFix-MinecraftMod/releases).
2. Requires:
   - [Fabric API](https://modrinth.com/mod/fabric-api)
3. Place the `.jar` file into your `mods` folder.
4. Launch the game.

---

## Building

1. Requires Java 21 and Fabric Loader for Minecraft 1.21.4.
2. To build the project, run:
   ```cmd
   build.bat
   ```
   or via Gradle:
   ```bash
   ./gradlew build
   ```
3. The built jar file will be located at `build/libs/MrRdpFix-Fabric-1.21.10-byMr712-v1.0.jar`.

---

## Credits & License

- Original Author: [KesslerCascade](https://github.com/KesslerCascade) ([RDPMouse](https://github.com/KesslerCascade/RDPMouse)).
- Ported and adapted for 1.21.4 by: [Mr712](https://github.com/byMr712).
- Distributed under the [MIT License](LICENSE).
