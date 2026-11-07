package com.example.catanclock.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.catanclock.model.CatanClockState
import com.example.catanclock.model.ClockMode
import com.example.catanclock.ui.theme.CatanWarningAmber
import com.example.catanclock.ui.theme.CatanWarningRed
import java.util.Locale

@Composable
fun BigTimerDisplay(
    state: CatanClockState,
    onTapTimer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isZenMode = state.settings.mode == ClockMode.ZEN
    val activePlayer = state.activePlayer
    val displayColor = if (isZenMode) Color(state.settings.zenLedColorHex) else activePlayer.color

    // Pulsing animation for 10s warning
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (state.isWarning10s && !state.isPaused) 1.05f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val warningColor = if (state.isUsingTimeBank) CatanWarningRed else CatanWarningAmber
    val borderColor by animateColorAsState(
        targetValue = if (state.isWarning10s) warningColor else displayColor.copy(alpha = 0.6f),
        animationSpec = tween(300),
        label = "borderColor"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (state.isWarning10s) {
            if (state.isUsingTimeBank) CatanWarningRed.copy(alpha = 0.18f) else CatanWarningAmber.copy(alpha = 0.15f)
        } else {
            Color(0xFF141414) // Dark Matte Background for Flip Board
        },
        animationSpec = tween(300),
        label = "backgroundColor"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(width = if (state.isWarning10s) 4.dp else 2.dp, color = borderColor, shape = RoundedCornerShape(24.dp))
            .clickable { onTapTimer() },
        color = backgroundColor,
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                // Header (Match Mode only)
                if (!isZenMode) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(displayColor)
                                .border(1.dp, Color.White, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "${activePlayer.name}'s Turn",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = displayColor
                            )
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Classy Retro Split-Flap Flip Board Display
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(pulseScale)
                ) {
                    val totalSeconds = (state.remainingTurnTimeMs / 1000L).coerceAtLeast(0L).toInt()

                    val timerColor = when {
                        state.isUsingTimeBank -> CatanWarningRed
                        state.isWarning10s -> CatanWarningAmber
                        else -> displayColor
                    }

                    FlipClockNumber(
                        number = totalSeconds,
                        textColor = timerColor,
                        maxDigitCount = 3
                    )
                }

                // Active Player Time Bank Progress Bar (Match Mode only)
                if (!isZenMode && state.settings.isTimeBankEnabled) {
                    val bankMaxMs = (state.settings.bankDurationSeconds * 1000L).coerceAtLeast(1L)
                    val bankRemainingMs = activePlayer.bankTimeRemainingMs
                    val bankProgress = (bankRemainingMs.toFloat() / bankMaxMs.toFloat()).coerceIn(0f, 1f)
                    val bankSecs = (bankRemainingMs / 1000L).coerceAtLeast(0L)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Time Bank",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.LightGray
                            )
                            Text(
                                text = String.format(Locale.getDefault(), "%ds left", bankSecs),
                                style = MaterialTheme.typography.labelMedium,
                                color = if (bankRemainingMs == 0L) CatanWarningRed else Color.LightGray,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { bankProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = if (bankRemainingMs < 15_000L) CatanWarningRed else displayColor,
                            trackColor = Color.DarkGray
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Pause Overlay Icon
            if (state.isPaused) {
                Surface(
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.75f),
                    modifier = Modifier.size(76.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Paused",
                            tint = Color.White,
                            modifier = Modifier.size(52.dp)
                        )
                    }
                }
            }
        }
    }
}
