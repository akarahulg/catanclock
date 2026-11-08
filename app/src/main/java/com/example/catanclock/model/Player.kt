package com.example.catanclock.model

import androidx.compose.ui.graphics.Color

data class Player(
    val id: Int,
    val name: String,
    val colorHex: Long,
    val bankTimeRemainingMs: Long,
    val turnsCompleted: Int = 0,
    val totalTimeSpentMs: Long = 0L
) {
    val color: Color get() = Color(colorHex)

    companion object {
        val COLOR_PALETTE = listOf(
            0xFFE53935L, // Red
            0xFF1E88E5L, // Blue
            0xFFF5F5F5L, // White
            0xFFFB8C00L, // Orange
            0xFF4CAF50L, // Green
            0xFF795548L, // Brown
            0xFFFFD600L, // Yellow
            0xFFAB47BCL, // Purple
            0xFF00E5FFL  // Cyan
        )

        val DEFAULT_PLAYERS = listOf(
            Player(id = 1, name = "Red", colorHex = 0xFFE53935L, bankTimeRemainingMs = 60_000L),
            Player(id = 2, name = "Blue", colorHex = 0xFF1E88E5L, bankTimeRemainingMs = 60_000L),
            Player(id = 3, name = "White", colorHex = 0xFFF5F5F5L, bankTimeRemainingMs = 60_000L),
            Player(id = 4, name = "Orange", colorHex = 0xFFFB8C00L, bankTimeRemainingMs = 60_000L),
            Player(id = 5, name = "Green", colorHex = 0xFF4CAF50L, bankTimeRemainingMs = 60_000L),
            Player(id = 6, name = "Brown", colorHex = 0xFF795548L, bankTimeRemainingMs = 60_000L)
        )
    }
}
