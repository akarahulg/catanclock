# Catan Clock - Tabletop Turn Timer 🎲⏱

A fast, high-visibility Android turn timer and 3D split-flap flip clock tailored for **Catan** and tabletop board games.

![Platform](https://img.shields.io/badge/Platform-Android-green) ![Release](https://img.shields.io/badge/Release-v1.0.0-blue) ![License](https://img.shields.io/badge/License-MIT-orange)

---

## 🌟 Key Features

- 📟 **3D Split-Flap Flip Clock**: Retro mechanical flip board cards with smooth 3D rotation physics. Supports 3 digits (`120`, `060`, `009`) with automatic screen-fitting layout.
- 🧘 **Zen Mode**: Clean, distraction-free standalone timer. Single-tap screen to restart, double-tap to pause/resume. Customizable LED card display colors (Electric Cyan, Neon Green, Classic Red, Warm Amber, Electric Yellow, Vivid Purple).
- ⚔ **Match Mode**: Full 3–6 player turn timer with custom player names and color selection (Red, Blue, White, Orange, Green, Brown, Yellow, Purple, Cyan), turn/round counters, and per-player **Time Banks**.
- ⏱ **Custom Seconds Input (1 - 999s)**: Configure preset or manual turn durations up to **999 seconds**.
- 🎲 **Split-Screen Catan Dice Roller**:
  - **Red Die** & **Yellow Die** with bold, enlarged pips.
  - **🛡 Cities & Knights Event Die**: Toggleable 3rd C&K Event Die with color-coded Shield faces (Black Barbarians, Blue Trade, Green Politics, Yellow Science).
  - **3D Physics Spin**: Tapping dice triggers a smooth 3D rotation animation. Pips lock in place before landing to eliminate flickering.
- 🔔 **Loud 10-Second Warning & Alarm**: Audio beeps during the final 10 seconds, and a long end alarm chime (`STREAM_ALARM`) + dual-pulse vibration alert when time expires.
- 📱 **Always-On Screen**: Keeps your device's display awake during game sessions.

---

## 📥 Direct APK Download

1. Go to the [Releases](https://github.com/akarahulg/catanclock/releases/tag/v1.0.0) section on GitHub.
2. Download **`CatanClock-v1.0.0.apk`**.
3. Tap the downloaded `.apk` file on your Android phone to install! *(Enable "Install from Unknown Sources" in your browser if prompted).*

---

## 🛠 Building from Source

```bash
# Clone the repository
git clone https://github.com/akarahulg/catanclock.git
cd catanclock

# Build Debug and Release APKs
./gradlew app:assembleDebug app:assembleRelease
```

The compiled APKs will be generated at:
- `CatanClock-v1.0.0.apk` (Project Root)
- `app/build/outputs/apk/debug/app-debug.apk`

---

## 📄 License
Licensed under the [MIT License](LICENSE).
