package com.example.catanclock.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.catanclock.ui.theme.CatanPrimary
import com.example.catanclock.ui.theme.CatanWarningRed

@Composable
fun DiceRollDialog(
    lastRoll: Pair<Int, Int>?,
    onRollDice: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onRollDice,
                colors = ButtonDefaults.buttonColors(containerColor = CatanPrimary)
            ) {
                Text("🎲 Roll Dice!", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
        title = {
            Text("Catan Dice Roller", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (lastRoll != null) {
                    val (d1, d2) = lastRoll
                    val sum = d1 + d2

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        DieFace(value = d1)
                        Text("+", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        DieFace(value = d2)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "TOTAL: $sum",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black,
                        color = if (sum == 7) CatanWarningRed else MaterialTheme.colorScheme.primary
                    )

                    if (sum == 7) {
                        Text(
                            text = "🏴‍☠️ ROBBER ATTACK! (8+ cards discard)",
                            color = CatanWarningRed,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                } else {
                    Text(
                        text = "Tap button to roll 2d6!",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }
            }
        }
    )
}

@Composable
fun DieFace(value: Int) {
    Surface(
        modifier = Modifier
            .size(64.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(2.dp, Color.Black, RoundedCornerShape(12.dp)),
        color = Color(0xFFFFF9C4), // Warm die color
        shadowElevation = 4.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = value.toString(),
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = Color.Black
            )
        }
    }
}
