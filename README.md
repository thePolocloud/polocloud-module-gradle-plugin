# PoloCloud Gradle Module Plugin

A custom Gradle plugin for building **PoloCloud** modules with automatic metadata generation and packaging.

This plugin simplifies module creation by automatically:
- Validating required metadata fields.
- Generating a `module.json` file based on your Gradle configuration.
- Packaging the metadata file into your module’s JAR.
- Providing a dedicated Gradle task for module building.

---

## 💡 Features

- **Automatic module metadata generation** (`module.json`).
- **Validation** for required fields like ID, name, author, version, and main class.
- **Custom Gradle tasks** for simplified building.
- **Flexible configuration** via Gradle Kotlin DSL.
- **Seamless integration** with the Java plugin.

---

## ⚙️ Installation

To use the PoloCloud Gradle plugin in your project, apply it to your `build.gradle.kts` file:

```kotlin
plugins {
    id("dev.httpmarco.polocloud.module.gradle") version "3.0.0-pre.8-SNAPSHOT"
}
```
(Replace `3.0.0-pre.8-SNAPSHOT` with the current version of the plugin.)

---

## 🧩 Configuration

You can configure your module metadata using the `polocloudModule` block in `build.gradle.kts`:

```kotlin
polocloudModule {
    id = "my-module"
    moduleName = "My Module"
    description = "A module that does cool things"
    author = "YourName"
    version = "1.0.0"
    mainClass = "com.example.MyModule"

    // Optional
    loadOrder = LoadOrder.STARTUP
    apiVersion = ">=3.0.0-pre.7-SNAPSHOT" //TODO link version checker
}
```

### Required fields

| Field      | Description                                             |
| ---------- |---------------------------------------------------------|
| `id`         | Unique, lowercase module identifier (e.g. `my-module`).   |
| `moduleName` | Name of your module.                                    |
| `author`     | Author or maintainer’s name.                            |
| `version`    | Version of your module.                                 |
| `mainClass`  | Fully-qualified name of the module’s entry point class. |

### Optional fields

| Field       | Description                                                        |
| ----------- | ------------------------------------------------------------------ |
| `description` | Description of your module.                                        |
| `loadOrder`   | Defines when the module should load (`STARTUP`, `POST_STARTUP`, `LATE`). |
| `apiVersion`  | Target PoloCloud API version.                                      |

---

## 🏗️ Building your module

Run the following task to build your module:

```bash
polocloud > buildModule
```

This will:
1. Validate your configuration.
2. Generate a `module.json` file in the build directory.
3. Include the file in your JAR.
4. Output build details to the console.

Example output:
```text
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
✓ PoloCloud Module built successfully!
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Module ID:   my-module
Name:        My Module
Version:     1.0.0
Output:      /path/to/project/build/libs/my-module-1.0.0.jar
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

---

## 📦 Output Structure

The resulting JAR file will contain your compiled module code along with an automatically generated `module.json`, for example:

```json
{
  "id": "my-module",
  "name": "My Module",
  "version": "1.0.0",
  "description": "A module that does cool things",
  "author": "YourName",
  "main": "com.example.MyModule",
  "loadOrder": "STARTUP",
  "apiVersion": ">=3.0.0-pre.7-SNAPSHOT"
}
```

---

## 🔍 Validation
If required properties are missing (such as `id` or `mainClass`), the build will fail with an informative error message like:
```text
PoloCloud module configuration is incomplete:
- Module ID is required
- Main class is required
```

---

## 🧠 Load Orders

You can control when your module loads within PoloCloud:

| Value        | Description                                           |
| ------------ | ----------------------------------------------------- |
| `STARTUP`      | Load during initial startup (default).                |
| `POST_STARTUP` | Load after all STARTUP modules have been initialized. |
| `LATE`         | Load last, after the entire system is ready.          |

---

## 🪪 License

This project is licensed under the Apache License 2.0.
See at [LICENSE](LICENSE).