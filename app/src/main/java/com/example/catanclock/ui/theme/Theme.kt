package com.example.catanclock.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val CatanDarkBackground = Color(0xFF19120E)
val CatanSurface = Color(0xFF261D17)
val CatanPrimary = Color(0xFFFFB74D) // Warm Amber / Grain
val CatanOnPrimary = Color(0xFF3E2723)
val CatanSecondary = Color(0xFF8D6E63)
val CatanWarningRed = Color(0xFFFF5252)
val CatanWarningAmber = Color(0xFFFFC107)

private val DarkColorScheme = darkColorScheme(
    primary = CatanPrimary,
    onPrimary = CatanOnPrimary,
    secondary = CatanSecondary,
    background = CatanDarkBackground,
    surface = CatanSurface,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun CatanClockTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
