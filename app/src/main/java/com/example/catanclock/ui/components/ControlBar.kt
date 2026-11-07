package com.example.catanclock.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    modifier: Modifier = Modifier
) {
    val isZenMode = state.settings.mode == ClockMode.ZEN
    val mainButtonColor = if (isZenMode) CatanPrimary else state.activePlayer.color

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Pause / Resume Button
        IconButton(onClick = onTogglePause) {
            Icon(
                imageVector = if (state.isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                contentDescription = if (state.isPaused) "Resume" else "Pause",
                tint = CatanPrimary
            )
        }

        // Restart Current Turn
        IconButton(onClick = onRestartTurn) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Restart Turn",
                tint = Color.LightGray
            )
        }

        // Main Giant Action Button
        Button(
            onClick = onNextTurn,
            colors = ButtonDefaults.buttonColors(
                containerColor = mainButtonColor
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .height(48.dp)
                .weight(1f)
        ) {
            Icon(
                imageVector = if (isZenMode) Icons.Default.Refresh else Icons.Default.SkipNext,
                contentDescription = null,
                tint = if (isZenMode) Color.Black else Color.White
            )
            Text(
                text = if (isZenMode) " RESTART TIMER" else " NEXT TURN",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = if (isZenMode) Color.Black else Color.White
            )
        }

        // Roll Dice Dialog Button
        IconButton(onClick = onOpenDiceRoll) {
            Icon(
                imageVector = Icons.Default.Casino,
                contentDescription = "Roll Dice",
                tint = CatanPrimary
            )
        }

        // Settings Button
        IconButton(onClick = onOpenSettings) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = Color.LightGray
            )
        }
    }
}
