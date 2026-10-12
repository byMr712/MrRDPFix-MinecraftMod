# MrTrueRdpFix ([MR] True RDP Fix) — Agent & Developer Documentation

## 1. Project Overview & Identity
- **Mod Name:** MrTrueRdpFix
- **Display Name in Mod Loaders:** `[MR] True RDP Fix`
- **Mod ID:** `mrtruerdpfix`
- **Current Version:** `1.1`
- **Supported Minecraft Versions:** 1.21 through 1.21.11 (Branches: `1.21`, `1.21.1`, ..., `1.21.11`)
- **Default Branch:** `1.21.11`
- **Supported Loaders:** Pure Fabric Loader (>=0.19.5) and NeoForge (>=21.0.0) — **Zero extra dependencies required**.
- **Java Requirement:** Java 21 LTS
- **Build Output Artifacts:**
  - `MrTrueRdpFix-Fabric-<MC_VERSION>-byMr712-v1.1.jar`
  - `MrTrueRdpFix-NeoForge-<MC_VERSION>-byMr712-v1.1.jar`
- **Repository:** https://github.com/byMr712/MrTrueRdpFix-MinecraftMod
- **Original Upstream:** [RDPMouse by KesslerCascade](https://github.com/KesslerCascade/RDPMouse)
- **Authors:** Mr712 (Lead Maintainer), KesslerCascade

---

## 2. Problem Statement: Why This Mod Exists

When playing Minecraft (versions 1.13 through 1.21.x) over Windows Remote Desktop (RDP):
1. **Virtual Desktop & Normalization Issues:** Vanilla Minecraft bundles older LWJGL 3.3.x with GLFW 3.3. RDP sends mouse input marked with `MOUSE_VIRTUAL_DESKTOP` and absolute coordinates normalized to the range `0..65535`. Older GLFW versions fail to map these coordinates back to the client window correctly.
2. **Cursor Hiding & Centering Failures:** In older GLFW, calling `SetCursor(NULL)` over Remote Desktop prevents `SetCursorPos` from moving the cursor on the connected remote machine. This traps the cursor at the edge of the physical monitor or causes infinite camera rotation (spinning into the sky).
3. **Mojang Bug MC-126875 & GLFW #1276:** Mojang resolved this only in snapshot 26.X by upgrading to modern GLFW 3.4+. For all 1.21.x versions, vanilla Minecraft remains unplayable over RDP without this mod.
4. **Diagonal Drift on Hot RDP Connect (v1.0 issue resolved in v1.1):** When connecting via RDP to an already running game, slow diagonal movements drifted toward top-left (`-dx, -dy`). This was caused by integer truncation (`cvttss2si` in GLFW's `WM_INPUT` handler), which subtracted 1 pixel upon every cursor re-center. In v1.1, this was eliminated by switching to round-to-nearest (`cvtss2si` / `FCVTNS`).

---

## 3. Core Architecture: Native GLFW Backport & Binary Patches

MrTrueRdpFix injects a native backport of **GLFW 3.4+** directly into the Minecraft JVM process before window creation.

### 3.1 Binary Modifications in Bundled GLFW
The native GLFW libraries bundled in `src/main/resources/natives/windows/` (`x64`, `x86`, `arm64`) incorporate two critical patches by Mr712:
1. **Unconditional Transparent Cursor (`blankCursor`) for Hot RDP Connections:**
   - Standard GLFW 3.4+ only initializes `blankCursor` if `GetSystemMetrics(SM_REMOTESESSION)` is true at startup.
   - If Minecraft starts locally and an RDP session connects later ("hot connect"), `blankCursor` was `NULL`, breaking cursor movement.
   - The startup branch was patched so that `blankCursor` is unconditionally created, allowing seamless RDP connections at any time without game restarts.
2. **Round-to-Nearest in Raw Input (`cvtss2si` / `FCVTNS`):**
   - In GLFW's `win32_window.c`, `pos.x += (int)((data->data.mouse.lLastX / 65535.f) * width)` originally used truncation (`cvttss2si` on x86/x64, `FCVTZS` on ARM64).
   - This caused floor quantization where 99.6% of coordinates lost 1 pixel, creating a persistent phantom delta of `(-1, -1)` on recentering.
   - Replaced with round-to-nearest (`cvtss2si` on x86/x64, `FCVTNS` on ARM64). Coordinate error is now strictly 0, ensuring smooth and symmetrical diagonal mouse motion.

---

## 4. Multi-Loader Porting Architecture: Fabric & NeoForge

Supporting both Fabric and NeoForge without external dependencies required solving fundamental loader lifecycle differences.

### 4.1 The NeoForge Porting Challenges
1. **No PreLaunch Entrypoint:** Fabric provides `net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint`, which runs before any game window or LWJGL class is initialized. NeoForge has no direct equivalent; `@Mod` constructors run much later in the lifecycle.
2. **Early Window Locking (`earlyWindowControl`):**
   - NeoForge defaults to `earlyWindowControl = true` in `config/fml.toml`.
   - FML's early display system creates a splash screen window and initializes vanilla LWJGL/GLFW **before** mod constructors are even loaded.
   - If vanilla GLFW initializes first, setting `Configuration.GLFW_LIBRARY_NAME` later has zero effect.
3. **Classloader Separation & `IllegalClassLoadError`:**
   - NeoForge's modular classloading system strictly controls package loading. Mod classes loaded too early or from unexpected packages can cause `IllegalClassLoadError` or break early scanning.
4. **Independent Artifact Requirements:**
   - Fabric users expect a jar without NeoForge `@Mod` annotations.
   - NeoForge users expect a jar without Fabric `PreLaunchEntrypoint` dependencies.

### 4.2 How NeoForge Compatibility Was Solved
1. **Universal Early Initialization via Mixin Plugin (`RDPMouseMixinPlugin`):**
   - Both Fabric and NeoForge utilize SpongePowered Mixin.
   - Created `kesslercascade.rdpmouse.mixin.RDPMouseMixinPlugin` implementing `IMixinConfigPlugin`.
   - In `onLoad(String mixinPackage)`, it invokes `RDPMouseCore.init()`.
   - Mixin configuration discovery occurs **5+ seconds before** Minecraft window creation and before any graphics/GLFW classes are referenced, guaranteeing early execution on both loaders.
2. **Package Isolation:**
   - Placed `RDPMouseMixinPlugin` into its own isolated package (`kesslercascade.rdpmouse.mixin`) and mapped it via `mrtruerdpfix.mixins.json`. This completely avoids early classloader conflicts.
3. **Automatic `fml.toml` Configuration:**
   - `RDPMouseCore.init()` automatically checks for `config/fml.toml`.
   - If `earlyWindowControl = true` is found, it automatically rewrites it to `earlyWindowControl = false`. If the file does not exist, it creates it with `earlyWindowControl = false`.
   - This ensures FML leaves window creation to Minecraft and allows our custom GLFW native to load cleanly.
4. **Compile-Time Multi-Targeting in Gradle:**
   - `src/main/` contains shared code (`RDPMouseCore`, `RDPMouseMixinPlugin`, natives, icons).
   - `src/neoforge/` contains `RDPMouseNeoForge` with `@Mod("mrtruerdpfix")` and a compile-time `@Mod` stub to allow building via Fabric Loom without full NeoGradle overhead.
   - `jarNeoForge` task generates a pure NeoForge JAR with `META-INF/neoforge.mods.toml`, excluding Fabric entrypoints.
   - `remapJar` produces the pure Fabric JAR with `fabric.mod.json`, excluding NeoForge metadata.
5. **Version Format (`X.X` without `v` inside Mod Metadata):**
   - In `gradle.properties`, `mod_version` is formatted as `X.X` (e.g., `1.1`), and `build.gradle` explicitly strips any leading `v` (`cleanVersion`) when expanding `${version}` into `fabric.mod.json` and `META-INF/neoforge.mods.toml` (complying with Maven/SemVer `ArtifactVersion` requirements in NeoForge and keeping Fabric consistent).
   - Only the external `.jar` artifact filenames retain the `-vX.X.jar` suffix.

---

## 5. Key Classes & File Layout

```
MrTrueRdpFix-MinecraftMod/
├── src/main/java/kesslercascade/rdpmouse/
│   ├── RDPMouse.java                  # Mod constants and metadata (MOD_ID = "mrtruerdpfix")
│   ├── RDPMouseCore.java              # Universal native unpacker, LWJGL config, fml.toml manager
│   ├── RDPMousePreLaunch.java          # Fabric PreLaunch entrypoint (calls RDPMouseCore.init)
│   └── mixin/
│       └── RDPMouseMixinPlugin.java   # Early Mixin hook for NeoForge & Fabric (calls RDPMouseCore.init)
├── src/main/resources/
│   ├── fabric.mod.json                # Fabric mod manifest
│   ├── mrtruerdpfix.mixins.json       # Mixin config registering RDPMouseMixinPlugin
│   ├── assets/mrtruerdpfix/icon.png   # Mod icon
│   └── natives/windows/               # Patched GLFW 3.4+ native binaries
│       ├── x64/glfw.dll
│       ├── x86/glfw.dll
│       └── arm64/glfw.dll
├── src/neoforge/java/
│   ├── kesslercascade/rdpmouse/
│   │   └── RDPMouseNeoForge.java      # NeoForge entrypoint annotated with @Mod("mrtruerdpfix")
│   └── net/neoforged/fml/common/
│       └── Mod.java                   # Minimal compile-time stub for @Mod annotation
├── src/neoforge/resources/
│   └── META-INF/neoforge.mods.toml    # NeoForge mod manifest
├── NOTICE.md                          # Third-party licenses & detailed binary modification records
├── LICENSE                            # MIT License
└── build.gradle                       # Dual-target build script producing both Fabric and NeoForge JARs
```

---

## 6. Build & Propagation Workflow

- **Prerequisites:** JDK 21 LTS installed.
- **Single Branch Build:**
  ```cmd
  gradlew.bat clean build
  ```
  Produces both Fabric and NeoForge JARs in `build/libs/` and automatically copies them to `C:\Users\Mr712\Documents\MrTrueRdpFix_all_builds\` (`Fabric/` and `NeoForge/`).
- **Cross-Branch Synchronization:**
  All 12 branches (`1.21` to `1.21.11`) share the exact same native binaries, Mixin plugin architecture, and NeoForge multi-loader structure, varying only by Minecraft version dependencies in `gradle.properties`.
