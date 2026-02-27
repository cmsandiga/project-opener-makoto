# Project Opener Makoto

A lightweight IntelliJ IDEA plugin to quickly find and open projects by name. Configure your project root directories once, then navigate to any project with a single keyboard shortcut.

## Inspiration

This plugin was inspired by [Project Opener](https://plugins.jetbrains.com/plugin/11384-project-opener) by Santiago Perez. The original stopped working on newer versions of IntelliJ IDEA (2025.1+), so this is a fresh implementation built from scratch with Kotlin and modern IntelliJ Platform APIs.

## Features

- **Speed search**: Type to filter projects instantly by name
- **Configurable roots**: Scan multiple directories (e.g. `~/code`, `~/dev`, `~/IdeaProjects`)
- **Project detection**: Recognizes Gradle, Maven, and IntelliJ projects automatically
- **Force new window**: Option to always open projects in a new IDE window
- **Centered popup**: The search dialog opens centered in the IDE window
- **Cross-platform shortcut**: Works on Windows, Linux, and macOS

## Installation

### From JetBrains Marketplace

1. Open **Settings** (Ctrl+Alt+S / Cmd+,) → **Plugins** → **Marketplace**
2. Search for **Project Opener Makoto**
3. Click **Install** and restart the IDE

### From disk

1. Download the `.zip` from [Releases](https://github.com/cmsandiga/project-opener-makoto/releases)
2. Go to **Settings** → **Plugins** → ⚙️ → **Install Plugin from Disk...**
3. Select the downloaded `.zip` and restart

## Getting Started

### 1. Configure your project directories

Go to **Settings** (Ctrl+Alt+S / Cmd+,) → **Tools** → **Project Opener Makoto**

- Click **+** to add directories where your projects live (e.g. `/home/you/code`)
- Click **−** to remove a directory
- Enable **Force to open project in new window** if you prefer separate windows

### 2. Open a project

Press **Ctrl+Alt+Shift+O** (Windows/Linux) or **Ctrl+Option+Shift+O** (macOS)

- The "Enter Project Name" popup appears centered in the IDE
- Start typing to filter projects by name
- Use **↑/↓** arrow keys to navigate the list
- Press **Enter** to open the selected project
- Press **Escape** to cancel

## Detected Project Types

The plugin scans up to 3 levels deep and recognizes projects containing any of:

| Marker | Project Type |
|--------|-------------|
| `.idea/` | IntelliJ project |
| `build.gradle` | Gradle (Groovy) |
| `build.gradle.kts` | Gradle (Kotlin) |
| `settings.gradle` | Gradle multi-module |
| `settings.gradle.kts` | Gradle multi-module (Kotlin) |
| `pom.xml` | Maven |

## Compatibility

- IntelliJ IDEA 2025.1+
- All JetBrains IDEs based on the IntelliJ Platform (WebStorm, PyCharm, PhpStorm, GoLand, etc.)
- JVM 21+

## Building from Source

```bash
# Run the plugin in a sandbox IDE
./gradlew runIde

# Build the plugin distribution
./gradlew buildPlugin
```

The distributable `.zip` is generated in `build/distributions/`.

## License

[MIT License](LICENSE) — free to use, modify, and distribute.

---

*Powered by [Cursor](https://cursor.com) vibe coding* <img src="https://www.cursor.com/favicon.ico" alt="Cursor" width="20" height="20">

