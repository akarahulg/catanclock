package com.example.catanclock.ui.components

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
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.catanclock.model.ClockMode
import com.example.catanclock.ui.theme.CatanPrimary

@Composable
fun OpeningMenuScreen(
    onSelectMode: (ClockMode) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Header / App Title
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(CatanPrimary.copy(alpha = 0.15f))
                        .border(2.dp, CatanPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = CatanPrimary,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "CATAN CLOCK",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = CatanPrimary,
                    letterSpacing = 2.sp
                )

                Text(
                    text = "Select game mode to begin",
                    fontSize = 14.sp,
                    color = Color.LightGray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Mode Option Cards
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Zen Mode Option Card
                ModeCard(
                    title = "🧘 Zen Mode",
                    subtitle = "Simple 3D flip timer. Single tap to restart, double tap to pause.",
                    color = CatanPrimary,
                    onClick = { onSelectMode(ClockMode.ZEN) }
                )

                // Match Mode Option Card
                ModeCard(
                    title = "⚔ Match Mode",
                    subtitle = "Catan turn timer with player colors, turn counters, & Time Banks.",
                    color = Color(0xFFE53935),
                    onClick = { onSelectMode(ClockMode.MATCH) }
                )
            }

            Text(
                text = "Tap any card to start timer",
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ModeCard(
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(2.dp, color.copy(alpha = 0.8f), RoundedCornerShape(20.dp))
            .clickable { onClick() },
        color = Color(0xFF1E1E1E),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = Color.LightGray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
