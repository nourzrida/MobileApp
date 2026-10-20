# MobileApp

**GestionDeSpectacle** is an Android application project for spectacle/event management, built with Java and Gradle Kotlin DSL.

## Overview

This repository contains the Android app module for the project (`teskertievents`). Based on the project name and dependencies, the app is intended for event-oriented workflows (for example, listing or managing spectacle-related data) and includes networking, JSON parsing, navigation, and QR/barcode support.

> Note: This README describes only what is supported by the repository contents and build configuration.

## Technology Stack

- **Language:** Java (source/target compatibility: Java 8)
- **Build system:** Gradle with Kotlin DSL
- **Android SDK:**
  - `compileSdk`: 34
  - `targetSdk`: 34
  - `minSdk`: 24
- **App identifiers:**
  - Root project name: `teskertievents`
  - Namespace / Application ID: `com.example.teskertievents`
- **Version:** `versionCode 1`, `versionName "1.0"`
- **UI:** Android Views with **View Binding** enabled
- **Main libraries/dependencies:**
  - Retrofit `2.9.0`
  - Gson `2.8.9`
  - OkHttp logging interceptor `4.9.1`
  - ZXing Android Embedded `4.3.0`
  - Material Components
  - RecyclerView
  - CardView
  - AppCompat
  - ConstraintLayout
  - Android Navigation (Fragment/UI)

## Requirements

- **Android Studio** (recommended)
- **JDK 8+** (project compiles with Java 8 compatibility)
- **Android SDK 34** installed
- A physical Android device or emulator running **API 24+**

## Project Structure

```text
MobileApp/
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/
│   │   └── res/
│   │       ├── drawable/
│   │       ├── layout/
│   │       ├── navigation/
│   │       └── values/
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
└── gradlew.bat
```

## Setup & Installation

1. Clone the repository.
2. Open the project in Android Studio.
3. Let Gradle sync dependencies.
4. If environment-specific values are required (for example API endpoints or keys), configure them according to the project source before running.

## Build & Run

### macOS / Linux

```bash
./gradlew assembleDebug
```

### Windows

```bat
gradlew.bat assembleDebug
```

After building, run the app from Android Studio on an emulator/device (API 24+), or install the generated debug APK from `app/build/outputs/apk/debug/`.

## Testing

### Unit tests (JVM)

- macOS / Linux:

  ```bash
  ./gradlew test
  ```

- Windows:

  ```bat
  gradlew.bat test
  ```

### Instrumented Android tests

- macOS / Linux:

  ```bash
  ./gradlew connectedAndroidTest
  ```

- Windows:

  ```bat
  gradlew.bat connectedAndroidTest
  ```

## Contributing

Contributions are welcome. Please keep changes focused, verify builds/tests locally, and open a pull request with a clear description of what changed.

## License

No license file is currently present in this repository. If you plan to reuse or distribute this project, add or confirm the intended license with the repository maintainers.
