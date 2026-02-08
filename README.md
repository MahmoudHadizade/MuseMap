# MuseMap

**MuseMap** is a modern Android travel diary application designed to capture embodied experiences through location-based multimedia notes. It allows users to log their journey using **Video**, **Audio**, and **GPS**, visualizing everything on an interactive world map.

> **Tech Stack:** Kotlin, Jetpack Compose, Clean Architecture, MVVM, Hilt, Coroutines, Room, ExoPlayer (Media3), OSMDroid, Scoped Storage.

## Features
* **Location Intelligence:** Automatically tags notes with GPS coordinates using FusedLocationProvider.
* **Video Logs:** Attach videos to notes using **Scoped Storage** (preserving privacy and persistence) and play them back with **ExoPlayer**.
* **Voice Memos:** Integrated Audio Recorder with permission handling and native playback.
* **Interactive Map:** View all notes on a global map interface powered by OpenStreetMap (OSMDroid).
* **Dark Mode Support:** Fully adaptive UI with Material You (Material 3) dynamic theming.
* **Offline First:** Fully functional offline using a local Room Database as the single source of truth.

## Architecture
This project follows strict **Clean Architecture** principles to ensure scalability and testability.

### Layers
1. **Domain Layer (Pure Kotlin):**
    * Contains `Entities` (Business Models), `UseCases` (Business Logic), and `Repository Interfaces`.
    * *Highlight:* This layer has zero dependencies on the Android Framework.
2. **Data Layer:**
    * Implements the Repository interfaces.
    * Manages data sources: **Room** (Local DB), **Media Files** (Scoped Storage), and **Sensors** (Location/Mic).
3. **Presentation Layer (MVVM):**
    * **UI:** Built 100% with **Jetpack Compose**.
    * **State Management:** Uses `ViewModel` exposing `StateFlow` to the UI.
    * **Navigation:** Jetpack Compose Navigation with argument passing.

## Key Technical Implementations

### 1. Scoped Storage & Media
To respect user privacy and modern Android standards (Android 10+), `MuseMap` copies video/audio files from the cache/gallery to the app's internal private storage. This ensures data persistence even if the original file is deleted.

### 2. Dependency Injection (Hilt)
Hilt is used to manage the object graph, providing Singletons for the Database and generic implementations for Repositories and Media Helpers.

### 3. Concurrency
* **Coroutines** are used for all background tasks (Database I/O, File Copying).
* **Flow** is used for reactive data updates. When a note is added, the UI updates instantly without manual refreshing.

### 4. Custom UI Components
* **Vector Drawables:** Custom XML icons for media controls (Mic, Video, Location).
* **Dynamic Theming:** Supports System Dark/Light mode switching instantly.

## 📸 Screenshots
| Home List | Media Recording | Map View |
|:---:|:---:|:---:|
| <img src="https://github.com/user-attachments/assets/e934a548-6273-4bb1-958a-f0a22f23b900" width="200" /> | <img src="https://github.com/user-attachments/assets/c08dcdb0-c9fa-42f5-9e8b-00e6eaf8ddcb" width="200" /> | <img src="https://github.com/user-attachments/assets/be7c6e42-15f9-4753-a357-e5f5e28c9f7b" width="200" /> |
