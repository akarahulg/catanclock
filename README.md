# Catan Clock - Tabletop Turn Timer 🎲⏱

A fast, high-visibility Android turn timer and 3D split-flap flip clock tailored for **Catan** and tabletop board games.

![Catan Clock Banner](https://img.shields.io/badge/Platform-Android-green) ![Release](https://img.shields.io/badge/Release-v1.0.0-blue) ![License](https://img.shields.io/badge/License-MIT-orange)

## 🌟 Key Features

- 🧘 **Zen Mode**: Pure standalone 3D flip clock display with giant seconds, 10-second visual/audio/vibration warnings, and single-tap screen restart.
- ⚔ **Match Mode**: Full 3–6 player turn timer with standard Catan player colors (Red, Blue, White, Orange, Green, Brown), turn/round counters, and per-player **Time Banks**.
- 📟 **3D Split-Flap Flip Clock**: Retro mechanical flip board display with smooth 3D card rotation physics.
- ⏱ **Custom Seconds Input**: Configure any turn duration from 1 up to **999 seconds**.
- 🔔 **10-Second Warning & Alarm**: Audio beeps and long alarm chime + vibration alert when time expires.
- 🎲 **Integrated 2d6 Dice Roller**: Built-in Catan dice utility with Robber (7) alert.
- 📱 **Always-On Display**: Keeps the screen awake during game sessions.

---

## 📥 Download & Installation

1. Go to the [Releases](https://github.com/akarahulg/catanclock/releases) tab.
2. Download `CatanClock-v1.0.0.apk`.
3. Open the file on your Android phone to install! (Allow "Install from Unknown Sources" if prompted by Android).

---

## 🛠 Building from Source

```bash
# Clone the repository
git clone https://github.com/akarahulg/catanclock.git
cd catanclock

# Build APK
./gradlew app:assembleDebug
```

The compiled APK will be located at `app/build/outputs/apk/debug/app-debug.apk`.
