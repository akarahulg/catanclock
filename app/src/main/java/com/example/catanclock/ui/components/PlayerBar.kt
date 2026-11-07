package com.example.catanclock.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.catanclock.model.CatanClockState
import com.example.catanclock.model.ClockMode
import java.util.Locale

@Composable
fun PlayerBar(
    state: CatanClockState,
    onSelectPlayer: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val isZenMode = state.settings.mode == ClockMode.ZEN
    if (isZenMode) return

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ROUND ${state.roundNumber}  •  TURN ${state.turnCount}",
                style = MaterialTheme.typography.labelMedium,
                color = Color.LightGray,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.players.forEachIndexed { index, player ->
                val isActive = index == state.activePlayerIndex
                val playerColor = player.color

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = if (isActive) 3.dp else 1.dp,
                            color = if (isActive) playerColor else Color.Gray.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onSelectPlayer(index) },
                    color = if (isActive) playerColor.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(playerColor)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = player.name,
                                fontSize = 13.sp,
                                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                                color = if (isActive) Color.White else Color.LightGray,
                                maxLines = 1
                            )
                        }

                        if (state.settings.isTimeBankEnabled) {
                            val bankSecs = (player.bankTimeRemainingMs / 1000L).coerceAtLeast(0L)
                            Text(
                                text = String.format(Locale.getDefault(), "Bank %ds", bankSecs),
                                fontSize = 10.sp,
                                color = if (player.bankTimeRemainingMs == 0L) Color.Red else Color.Gray,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
