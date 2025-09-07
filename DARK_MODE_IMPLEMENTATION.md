# Dark Mode Implementation

## Overview
This document describes the implementation of dark mode functionality in the Water Quality App.

## Components

### 1. ThemeManager (`app/src/main/java/com/example/myapplication/utils/ThemeManager.kt`)
- Manages dark mode state using SharedPreferences
- Provides methods to toggle and set dark mode
- Persists user preference across app sessions

### 2. Updated Theme (`app/src/main/java/com/example/myapplication/ui/theme/`)
- Enhanced color schemes for both light and dark themes
- Improved color definitions with better contrast
- Dynamic status bar color adaptation

### 3. Settings Screen Integration
- Dark mode toggle in the Appearance section
- Real-time theme switching
- Persistent state management

## How It Works

1. **ThemeManager Initialization**: Created in MainActivity and passed down through the component hierarchy
2. **State Management**: Uses SharedPreferences to persist dark mode preference
3. **Theme Application**: MainActivity observes the dark mode state and applies the appropriate theme
4. **UI Updates**: All screens automatically adapt to the selected theme

## Usage

### For Users:
1. Navigate to Profile → Settings
2. Find "Dark Mode" in the Appearance section
3. Toggle the switch to enable/disable dark mode
4. The entire app will immediately switch themes

### For Developers:
```kotlin
// Access ThemeManager in any screen
val isDarkMode by themeManager.isDarkMode

// Toggle dark mode
themeManager.toggleDarkMode()

// Set specific state
themeManager.setDarkMode(true) // or false
```

## Features

- ✅ Persistent dark mode preference
- ✅ Real-time theme switching
- ✅ Enhanced color schemes
- ✅ Status bar color adaptation
- ✅ Material Design 3 compliance
- ✅ Dynamic color support (Android 12+)

## Technical Details

- **Storage**: SharedPreferences with key "dark_mode_enabled"
- **State Management**: Compose State with mutableStateOf
- **Theme Application**: MaterialTheme with dynamic color schemes
- **Compatibility**: Android API 24+ (minSdk)
