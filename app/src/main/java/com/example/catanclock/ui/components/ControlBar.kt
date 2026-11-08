package com.example.catanclock.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.catanclock.model.CatanClockState
import com.example.catanclock.model.ClockMode
import com.example.catanclock.ui.theme.CatanPrimary

@Composable
fun ControlBar(
    state: CatanClockState,
    onTogglePause: () -> Unit,
    onNextTurn: () -> Unit,
    onRestartTurn: () -> Unit,
    onOpenDiceRoll: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenHomeMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isZenMode = state.settings.mode == ClockMode.ZEN
    val mainButtonColor = if (isZenMode) CatanPrimary else state.activePlayer.color

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Home Menu Symbol
        IconButton(onClick = onOpenHomeMenu) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Home Menu",
                tint = CatanPrimary,
                modifier = Modifier.size(28.dp)
            )
        }

        // Secondary Button: Pause/Resume in Zen Mode, Restart Player Turn in Match Mode
        IconButton(onClick = if (isZenMode) onTogglePause else onRestartTurn) {
            Icon(
                imageVector = if (isZenMode) {
                    if (state.isPaused) Icons.Default.PlayArrow else Icons.Default.Pause
                } else {
                    Icons.Default.Refresh
                },
                contentDescription = if (isZenMode) "Pause/Resume" else "Restart Player Turn",
                tint = Color.LightGray,
                modifier = Modifier.size(28.dp)
            )
        }

        // Main Center Action Button Symbol (Restart in Zen Mode, Next Turn in Match Mode)
        FilledIconButton(
            onClick = onNextTurn,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = mainButtonColor
            ),
            shape = CircleShape,
            modifier = Modifier.size(54.dp)
        ) {
            Icon(
                imageVector = if (isZenMode) Icons.Default.Refresh else Icons.Default.SkipNext,
                contentDescription = "Main Action",
                tint = if (isZenMode) Color.Black else Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

        // Roll Dice Panel Symbol
        IconButton(onClick = onOpenDiceRoll) {
            Icon(
                imageVector = Icons.Default.Casino,
                contentDescription = "Roll Dice",
                tint = CatanPrimary,
                modifier = Modifier.size(28.dp)
            )
        }

        // Settings Symbol
        IconButton(onClick = onOpenSettings) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = Color.LightGray,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
