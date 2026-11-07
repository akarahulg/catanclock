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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.catanclock.model.CatanClockState
import com.example.catanclock.model.ClockMode
import com.example.catanclock.model.GameSettings

@Composable
fun SettingsDialog(
    state: CatanClockState,
    onDismiss: () -> Unit,
    onSaveSettings: (GameSettings, List<String>) -> Unit,
    onResetGame: () -> Unit
) {
    var mode by remember { mutableStateOf(state.settings.mode) }
    var zenColorHex by remember { mutableLongStateOf(state.settings.zenLedColorHex) }
    var turnSeconds by remember { mutableIntStateOf(state.settings.turnDurationSeconds) }
    var customSecondsText by remember { mutableStateOf(state.settings.turnDurationSeconds.toString()) }
    var isTimeBankEnabled by remember { mutableStateOf(state.settings.isTimeBankEnabled) }
    var bankSeconds by remember { mutableIntStateOf(state.settings.bankDurationSeconds) }
    var numPlayers by remember { mutableIntStateOf(state.settings.numPlayers) }
    var soundEnabled by remember { mutableStateOf(state.settings.soundEnabled) }
    var vibrationEnabled by remember { mutableStateOf(state.settings.vibrationEnabled) }

    val playerNames = remember {
        mutableStateListOf<String>().apply {
            addAll(state.players.map { it.name })
            while (size < 6) {
                add("Player ${size + 1}")
            }
        }
    }

    val turnOptions = listOf(30, 45, 60, 90, 120, 180, 300)
    val bankOptions = listOf(30, 60, 90, 120, 180)

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val finalSeconds = customSecondsText.toIntOrNull()?.coerceIn(1, 999) ?: turnSeconds
                    val newSettings = GameSettings(
                        mode = mode,
                        turnDurationSeconds = finalSeconds,
                        bankDurationSeconds = bankSeconds,
                        isTimeBankEnabled = isTimeBankEnabled,
                        soundEnabled = soundEnabled,
                        vibrationEnabled = vibrationEnabled,
                        numPlayers = numPlayers,
                        zenLedColorHex = zenColorHex
                    )
                    onSaveSettings(newSettings, playerNames.take(numPlayers))
                    onDismiss()
                }
            ) {
                Text("Save & Apply")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = {
            Text(
                text = "Catan Clock Settings",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Clock Mode Switch
                Text("Clock Mode", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = mode == ClockMode.ZEN,
                        onClick = { mode = ClockMode.ZEN },
                        label = { Text("🧘 Zen Mode (Simple)", fontSize = 12.sp) }
                    )
                    FilterChip(
                        selected = mode == ClockMode.MATCH,
                        onClick = { mode = ClockMode.MATCH },
                        label = { Text("⚔ Match Mode", fontSize = 12.sp) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Zen LED Display Color Palette
                if (mode == ClockMode.ZEN) {
                    Text("Zen LED Display Color", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        GameSettings.ZEN_COLOR_PRESETS.forEach { colorHex ->
                            val isSelected = zenColorHex == colorHex
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(colorHex))
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) Color.White else Color.Gray,
                                        shape = CircleShape
                                    )
                                    .clickable { zenColorHex = colorHex }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Turn Duration Section with Presets + Manual Entry
                Text("Timer Duration (Seconds, max 999)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    turnOptions.take(4).forEach { secs ->
                        FilterChip(
                            selected = turnSeconds == secs,
                            onClick = {
                                turnSeconds = secs
                                customSecondsText = secs.toString()
                            },
                            label = { Text("${secs}s", fontSize = 12.sp) }
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    turnOptions.drop(4).forEach { secs ->
                        FilterChip(
                            selected = turnSeconds == secs,
                            onClick = {
                                turnSeconds = secs
                                customSecondsText = secs.toString()
                            },
                            label = { Text("${secs}s", fontSize = 12.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = customSecondsText,
                    onValueChange = { input ->
                        val digitsOnly = input.filter { it.isDigit() }.take(3)
                        customSecondsText = digitsOnly
                        val parsed = digitsOnly.toIntOrNull()
                        if (parsed != null && parsed in 1..999) {
                            turnSeconds = parsed
                        }
                    },
                    label = { Text("Manual Seconds (1 - 999)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (mode == ClockMode.MATCH) {
                    // Time Bank Section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Time Bank per Player", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Switch(
                            checked = isTimeBankEnabled,
                            onCheckedChange = { isTimeBankEnabled = it }
                        )
                    }

                    if (isTimeBankEnabled) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            bankOptions.forEach { secs ->
                                FilterChip(
                                    selected = bankSeconds == secs,
                                    onClick = { bankSeconds = secs },
                                    label = { Text("${secs}s", fontSize = 12.sp) }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Number of Players Section
                    Text("Number of Players", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        (3..6).forEach { n ->
                            FilterChip(
                                selected = numPlayers == n,
                                onClick = { numPlayers = n },
                                label = { Text("$n Players", fontSize = 12.sp) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Player Names Editing
                    Text("Player Names", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    (0 until numPlayers).forEach { idx ->
                        OutlinedTextField(
                            value = playerNames.getOrElse(idx) { "" },
                            onValueChange = { playerNames[idx] = it },
                            label = { Text("Player ${idx + 1}") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Audio & Vibration Toggles
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Sound Warning (10s beeps)")
                    Switch(checked = soundEnabled, onCheckedChange = { soundEnabled = it })
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Vibration Alert")
                    Switch(checked = vibrationEnabled, onCheckedChange = { vibrationEnabled = it })
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Reset Game Button
                Button(
                    onClick = {
                        onResetGame()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Reset Timer", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    )
}
