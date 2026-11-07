package com.example.catanclock.model

data class GameSettings(
    val mode: ClockMode = ClockMode.ZEN,
    val turnDurationSeconds: Int = 60,
    val bankDurationSeconds: Int = 60,
    val isTimeBankEnabled: Boolean = true,
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val numPlayers: Int = 4,
    val zenLedColorHex: Long = 0xFF00E5FFL
) {
    companion object {
        val ZEN_COLOR_PRESETS = listOf(
            0xFF00E5FFL, // Electric Cyan
            0xFF00E676L, // Neon Green
            0xFFFF3D00L, // Classic Red
            0xFFFFB74DL, // Warm Amber
            0xFFFFEA00L, // Electric Yellow
            0xFFE040FBL  // Vivid Purple
        )
    }
}
