> **Language:** Русский · [English](README.en.md)

# MrRdpFix (Fabric 1.21.x)

![Java 21](https://img.shields.io/badge/Java-21-blue.svg)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.x-blue.svg)
![Fabric](https://img.shields.io/badge/Loader-Fabric-blue.svg)
![ModMenu](https://img.shields.io/badge/ModMenu-Supported-blue.svg)
![License](https://img.shields.io/badge/License-MIT-blue.svg)

Исправление управления мышью при игре в **Minecraft 1.21.x (Fabric)** через **Windows Remote Desktop (RDP)**.

Оригинальный разработчик: [KesslerCascade/RDPMouse](https://github.com/KesslerCascade/RDPMouse).  
Обновление, бэкпорт GLFW и поддержка 1.21.x: [byMr712](https://github.com/byMr712).

---

## О проблеме и решении

В Minecraft версий 1.13–1.21.x используется LWJGL 3.3.3 со старой версией GLFW 3.3, в которой присутствует баг обработки виртуального рабочего стола Windows ([MC-126875](https://bugs.mojang.com/browse/MC-126875), GLFW issue [#1276](https://github.com/glfw/glfw/issues/1276)). При подключении по RDP мышь шлёт флаг `MOUSE_VIRTUAL_DESKTOP`, из-за чего камера бешено крутится в небеса, либо блокируется у края экрана.

Mojang исправили эту проблему только в снапшотах 26.X, обновив GLFW до версии 3.4+.

**MrRdpFix** выполняет **нативный бэкпорт GLFW 3.4+** в Minecraft 1.21.x:
1. При запуске игры мод автоматически распаковывает и подключает нативную библиотеку **GLFW 3.4+** (с официальными исправлениями PR #1279 / PR #2431) до загрузки графического окна.
2. Игра считывает ввод мыши аппаратно и нативно через пропатченный GLFW — без кривых Java-перехватов, задержек или срывов курсора.
3. Дополнительно предоставляется возможность плавного вращения камеры стрелками с клавиатуры и быстрого освобождения курсора (Alt).

---

## Использование и управление

- **Мышь:** работает на 100% ванильно, плавно и на 360 градусов при подключении по RDP.
- **F8:** переключить режим RDP / клавиатурного вращения (вкл/выкл).
- **Alt (удержание):** временно освободить захват курсора (Free Mouse).
- **Стрелки (Left / Right / Up / Down):** плавное вращение камеры с клавиатуры.

Все клавиши настраиваются в: *Настройки -> Управление -> Назначение клавиш -> [MR] RDP Fix*.

---

## Настройки (Mod Menu)

Мод полностью поддерживает **Mod Menu**:
- Включение / отключение режима RDP.
- Настройка чувствительности клавиатурного вращения.

---

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

---

## Авторы и лицензии

- **MrRdpFix:** распространяется под лицензией [MIT License](LICENSE).  
  Авторы: [KesslerCascade](https://github.com/KesslerCascade), [Mr712](https://github.com/byMr712).
- **Встроенные сторонние компоненты:**
  - **GLFW:** [zlib/libpng License](https://www.glfw.org/license.html) (Copyright © Camilla Löwy, Marcus Geelnard & GLFW Contributors).
  - **LWJGL:** [BSD 3-Clause License](https://www.lwjgl.org/license) (Copyright © Lightweight Java Game Library).
  - Подробные тексты лицензий третьих сторон приведены в файле [NOTICE.md](NOTICE.md).
