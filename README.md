> **Language:** Русский · [English](README.en.md)

# MrRdpFix (Minecraft 1.21.4 Fabric Port)

![Java 21](https://img.shields.io/badge/Java-21-blue.svg)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.4-blue.svg)
![Fabric](https://img.shields.io/badge/Loader-Fabric-blue.svg)
![ModMenu](https://img.shields.io/badge/ModMenu-Supported-blue.svg)
![License](https://img.shields.io/badge/License-MIT-blue.svg)

Порт и обновление мода **MrRdpFix** ([MR] RDP Fix) для **Minecraft 1.21.4 (Fabric)**.

Оригинальный разработчик: [KesslerCascade/RDPMouse](https://github.com/KesslerCascade/RDPMouse).

---

## О моде

Minecraft по умолчанию использует прямое относительное считывание мыши (raw relative mouse input), которое блокируется операционной системой Windows при подключении через Remote Desktop (RDP). В результате камера начинает бесконтрольно вращаться при малейшем движении мыши (известные баги [MC-107122](https://bugs.mojang.com/browse/MC-107122) и [MC-126875](https://bugs.mojang.com/browse/MC-126875)).

**MrRdpFix** заменяет прямое относительное считывание на абсолютное отслеживание позиции курсора, поддерживаемое протоколом RDP, возвращая полный и плавный контроль над камерой в игре.

---

## Использование и управление

- Нажмите **F8** для включения или выключения режима RDP.
- При активном режиме RDP перемещение мыши управляет камерой внутри границ окна игры.
- Если камера перестала поворачиваться, значит курсор достиг границы окна: зажмите **Alt**, чтобы освободить курсор, верните его в центр экрана и отпустите **Alt** для продолжения игры.
- Клавиши со **стрелками** позволяют плавно вращать камеру с клавиатуры.

### Назначение клавиш

| Клавиша | Действие |
|---|---|
| F8 | Переключить режим RDP |
| Alt (удержание) | Освободить курсор для центрирования |
| Стрелки (Left / Right / Up / Down) | Вращение камеры с клавиатуры |

Все привязки клавиш можно настроить в меню: *Настройки -> Управление -> Назначение клавиш -> RDP Мышь*.

---

## Что изменено в порте для 1.21.4 (byMr712)

- **Портирование на Minecraft 1.21.4 (Fabric Loader)**:
  - Сборка на актуальном стеке (Java 21 LTS, Fabric Loom 1.10.1, Yarn `1.21.4+build.7`).
  - Адаптация регистрации горячих клавиш и хуков GLFW Window / Mouse под маппинги и архитектуру 1.21.4.
  - Оптимизированная плоская структура Fabric-проекта без лишних зависимостей.
- **Поддержка Mod Menu и настройка**:
  - Экран конфигурации мода в Mod Menu.
  - Настраиваемый ползунок увеличения чувствительности мыши (0–100%, по умолчанию 75%).
- **Полная локализация**:
  - Добавлены русская (`ru_ru.json`) и английская (`en_us.json`) локализации.
- **Удобство сборки**:
  - Добавлен скрипт `build.bat` для быстрой компиляции.

---

## Установка

1. Скачайте последнюю версию мода со страницы [GitHub Releases](https://github.com/byMr712/MrRDPFix-MinecraftMod/releases).
2. Требуются:
   - [Fabric API](https://modrinth.com/mod/fabric-api)
3. Поместите `.jar` файл в папку `mods`.
4. Запустите игру.

---

## Сборка

1. Требуется Java 21 и Fabric Loader для Minecraft 1.21.4.
2. Для сборки выполните скрипт:
   ```cmd
   build.bat
   ```
   или через Gradle:
   ```bash
   ./gradlew build
   ```
3. Собранный файл находится в `build/libs/MrRdpFix-Fabric-1.21.2-byMr712-v1.0.jar`.

---

## Авторы и лицензия

- Оригинальный автор: [KesslerCascade](https://github.com/KesslerCascade) ([RDPMouse](https://github.com/KesslerCascade/RDPMouse)).
- Порт и адаптация для 1.21.4: [Mr712](https://github.com/byMr712).
- Распространяется под лицензией [MIT License](LICENSE).
