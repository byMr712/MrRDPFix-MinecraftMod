# MrTrueRdpFix ([MR] True RDP Fix) — Agent & Developer Documentation

## 1. Project Overview & Identity
- **Mod Name:** MrTrueRdpFix
- **Display Name in Mod Menu / Loaders:** `[MR] True RDP Fix`
- **Mod ID:** `mrtruerdpfix`
- **Version:** `1.0`
- **Supported Minecraft Versions:** 1.21 through 1.21.11 (Branches: `1.21`, `1.21.1`, ..., `1.21.11`)
- **Default Branch:** `1.21.11`
- **Loaders:** Pure Fabric Loader (>=0.19.5) and NeoForge (>=21.0.0) — Zero extra dependencies
- **Java Requirement:** Java 21 LTS
- **Build Output Patterns:** `MrTrueRdpFix-Fabric-XXXX-byMr712-v1.0.jar` and `MrTrueRdpFix-NeoForge-XXXX-byMr712-v1.0.jar`
- **Repository:** https://github.com/byMr712/MrTrueRdpFix-MinecraftMod
- **Original Mod:** [RDPMouse by KesslerCascade](https://github.com/KesslerCascade/RDPMouse)
- **Authors:** Mr712 (Lead), KesslerCascade

## 2. Problem Statement: Why This Mod Exists
When playing Minecraft (versions 1.13 through 1.21.x) over Windows Remote Desktop (RDP):
- LWJGL 3.3.3 bundles an older build of GLFW (3.3), which fails to handle Windows virtual desktop mouse input over RDP.
- Windows RDP marks mouse packets with `MOUSE_VIRTUAL_DESKTOP` with absolute coordinates in `0..65535`.
- Additionally, calling `SetCursor(NULL)` in GLFW 3.3 breaks cursor movement updates across the virtual boundary, trapping the mouse cursor at the physical monitor edge or causing runaway camera spinning into the sky.
- This root cause is documented in Mojang bug **MC-126875** and GLFW issue **#1276**.
- Mojang officially resolved this issue only starting in snapshot 26.X by updating GLFW to 3.4+.

## 3. How the Mod Works: Core Architecture
MrTrueRdpFix performs a clean, native backport of **GLFW 3.4+** directly into Minecraft 1.21.x.

### 3.1 PreLaunch Injection (`RDPMousePreLaunch.java`)
- Implements `net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint`.
- Before the Minecraft window, graphics context, or LWJGL classes are initialized:
  1. Detects OS architecture (`x64`, `arm64`, or `x86`).
  2. Unpacks the bundled, precompiled native `glfw-3.4.2-rdpfix.dll` from `natives/windows/<arch>/glfw.dll` into the system temporary directory.
  3. Directs LWJGL to use this modern GLFW build via `Configuration.GLFW_LIBRARY_NAME.set(...)` and `System.setProperty("org.lwjgl.glfw.libname", ...)`.
- GLFW 3.4+ natively fixes Windows RDP mouse handling (via official patches PR #1279 and PR #2431). In addition, our native binaries are patched by Mr712 to unconditionally create the transparent blank cursor at startup, enabling seamless "hot" RDP connections to already-running Minecraft sessions without game restarts.
- Mouse look runs 100% natively in hardware at the operating system level, with zero input lag, no cursor snaps, and full 360-degree rotation identical to local play.

### 3.2 Zero-Config & Transparent ("Install & Forget")
- The mod requires no configuration files, no config menus, and no custom keybindings.
- No Mixins, no Access Wideners, and no background tick listeners are needed.
- Operates completely silently in the background with zero CPU overhead during gameplay.
- **Zero Dependencies:** Does NOT require Fabric API, Mod Menu, or any other library. Works on bare Fabric Loader as well as any modpack.

## 4. Key Classes & File Structure
- `src/main/java/kesslercascade/rdpmouse/`
  - `RDPMousePreLaunch.java`: PreLaunch entrypoint that unpacks and registers the GLFW 3.4+ DLL.
  - `RDPMouse.java`: Mod metadata and logger holder (`MOD_ID = "mrtruerdpfix"`).
- `src/main/resources/`
  - `fabric.mod.json`: Mod metadata (`id`: `mrtruerdpfix`, `version`: `1.0`, `name`: `[MR] True RDP Fix`).
  - `assets/mrtruerdpfix/icon.png`: Mod icon.
  - `natives/windows/`: Bundled GLFW 3.4.1 DLLs (`arm64`, `x64`, `x86`).
- `NOTICE.md`: Third-party licenses for GLFW (zlib) and LWJGL (BSD 3-Clause).
- `LICENSE`: MIT License (Copyright Mr712, KesslerCascade).

## 5. Build & Development Instructions
- Prerequisites: Java 21 LTS installed.
- To compile and build:
  ```bash
  ./gradlew build
  ```
- The built jar is copied to `build/libs/` and `C:/Users/Mr712/Documents/RDPMouse_all_builds/`.
