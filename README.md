> **Language:** Русский · [English](README.en.md)

# MrTrueRdpFix (Fabric 1.21.x)

![Java 21](https://img.shields.io/badge/Java-21-blue.svg)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.x-blue.svg)
![Fabric](https://img.shields.io/badge/Loader-Fabric-blue.svg)
![Fabric API](https://img.shields.io/badge/Fabric%20API-Not%20Required-success.svg)
![Zero Config](https://img.shields.io/badge/Config-Zero%20Config-brightgreen.svg)
![License](https://img.shields.io/badge/License-MIT-blue.svg)

Исправление управления мышью при игре в **Minecraft 1.21.x (Fabric)** через **Remote Desktop Protocol (RDP)**.

Данный мод вдохновлён работой: [**RDPMouse от KesslerCascade**](https://www.curseforge.com/minecraft/mc-mods/rdpmouse)

## Галерея
| Ванильная игра | Игра с моим модом |
| :---: | :---: |
| ![Full_vanilla](images/Full_vanilla.webp) | ![This_mod](images/This_mod.webp) |

## О моде
В Minecraft версий 1.13–1.21.x используется библиотека LWJGL 3.3.3 со старой версией GLFW 3.3, в которой присутствует баг некорректной обработки виртуального рабочего стола Windows ([MC-126875](https://bugs.mojang.com/browse/MC-126875), GLFW issue [#1276](https://github.com/glfw/glfw/issues/1276)). При подключении по RDP мышь отправляет флаг `MOUSE_VIRTUAL_DESKTOP`, из-за чего камера бесконтрольно улетает в небо либо намертво блокируется у границы экрана.

Mojang решили эту проблему только в снапшотах 26.X, обновив GLFW до версии 3.4+.


## Что умеет мод?
**MrTrueRdpFix** выполняет чистый **нативный бэкпорт GLFW 3.4+** в Minecraft 1.21.x:
1. При запуске игры мод автоматически подключает нативную библиотеку **GLFW 3.4+** (содержащую официальные исправления [PR #1279](https://github.com/glfw/glfw/pull/1279) и [PR #2431](https://github.com/glfw/glfw/pull/2431)) до инициализации окна игры.
2. Ввод мыши обрабатывается аппаратно и нативно на уровне самой системы — без задержек, срывов курсора и сторонних Java-перехватов.

## Установка

1. Скачайте последнюю версию мода со страницы на [CurseForge](https://www.curseforge.com/minecraft/mc-mods/mr-true-rdp-fix) или [GitHub Releases](https://github.com/byMr712/MrTrueRdpFix-MinecraftMod/releases).
2. Требуются:
   - Только ядро ​​Fabric - другие моды, такие как Fabric API, не требуются.
3. Поместите `.jar` файл в папку `mods`.
4. Запустите игру.

## Сборка

1. Требуется Java 21 и Fabric Loader.
2. Для сборки выполните:
   ```cmd
   build.bat
   ```
   или через Gradle:
   ```bash
   ./gradlew build
   ```
3. Собранный файл мода находится в `build/libs/`.

## Авторы и лицензии

- **MrTrueRdpFix:** распространяется под лицензией [MIT License](LICENSE).  
  Авторы: [Mr712](https://github.com/byMr712), [KesslerCascade](https://github.com/KesslerCascade/RDPMouse).
- **Встроенные сторонние компоненты:**
  - **GLFW:** [zlib/libpng License](https://www.glfw.org/license.html) (Copyright © Camilla Löwy, Marcus Geelnard & GLFW Contributors).
  - **LWJGL:** [BSD 3-Clause License](https://www.lwjgl.org/license) (Copyright © Lightweight Java Game Library).
  - Подробные тексты лицензий третьих сторон приведены в файле [NOTICE.md](NOTICE.md).
