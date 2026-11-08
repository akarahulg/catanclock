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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import com.example.catanclock.model.Player
import com.example.catanclock.ui.theme.CatanPrimary

@Composable
fun SettingsDialog(
    state: CatanClockState,
    onDismiss: () -> Unit,
    onSaveSettings: (GameSettings, List<String>, List<Long>) -> Unit,
    onResetGame: () -> Unit
) {
    var mode by remember { mutableStateOf(state.settings.mode) }
    var zenColorHex by remember { mutableLongStateOf(state.settings.zenLedColorHex) }
    var turnSeconds by remember { mutableIntStateOf(state.settings.turnDurationSeconds) }
    var customSecondsText by remember { mutableStateOf(state.settings.turnDurationSeconds.toString()) }
    var isTimeBankEnabled by remember { mutableStateOf(state.settings.isTimeBankEnabled) }
    var bankSeconds by remember { mutableIntStateOf(state.settings.bankDurationSeconds) }
    var numPlayers by remember { mutableIntStateOf(state.settings.numPlayers) }
    var playerDropdownExpanded by remember { mutableStateOf(false) }
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

    val playerColors = remember {
        mutableStateListOf<Long>().apply {
            addAll(state.players.map { it.colorHex })
            while (size < 6) {
                add(Player.DEFAULT_PLAYERS[size % Player.DEFAULT_PLAYERS.size].colorHex)
            }
        }
    }

    val turnPresetsRow1 = listOf(30, 45, 60)
    val turnPresetsRow2 = listOf(90, 120, 180)
    val bankPresets = listOf(30, 60, 90, 120)

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
                    onSaveSettings(newSettings, playerNames.take(numPlayers), playerColors.take(numPlayers))
                    onDismiss()
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save & Apply", fontWeight = FontWeight.Bold)
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
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = CatanPrimary
                )
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Segmented Clock Mode Selector
                Column {
                    Text(
                        text = "CLOCK MODE",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.LightGray,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        color = Color(0xFF222222),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFF383838), RoundedCornerShape(14.dp))
                    ) {
                        Row(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                            Surface(
                                color = if (mode == ClockMode.ZEN) CatanPrimary else Color.Transparent,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { mode = ClockMode.ZEN }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                ) {
                                    Text(
                                        text = "🧘 Zen Mode",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (mode == ClockMode.ZEN) Color.Black else Color.White
                                    )
                                }
                            }

                            Surface(
                                color = if (mode == ClockMode.MATCH) CatanPrimary else Color.Transparent,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { mode = ClockMode.MATCH }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                ) {
                                    Text(
                                        text = "⚔ Match Mode",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (mode == ClockMode.MATCH) Color.Black else Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                // 2. Zen LED Color Palette (Zen Mode only)
                if (mode == ClockMode.ZEN) {
                    Column {
                        Text(
                            text = "ZEN DISPLAY COLOR",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.LightGray,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            GameSettings.ZEN_COLOR_PRESETS.forEach { colorHex ->
                                val isSelected = zenColorHex == colorHex
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color(colorHex))
                                        .border(
                                            width = if (isSelected) 3.5.dp else 1.dp,
                                            color = if (isSelected) Color.White else Color.DarkGray,
                                            shape = CircleShape
                                        )
                                        .clickable { zenColorHex = colorHex }
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(color = Color(0xFF333333))

                // 3. Timer Seconds Configuration
                Column {
                    Text(
                        text = "TIMER DURATION (SECONDS)",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.LightGray,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        turnPresetsRow1.forEach { secs ->
                            Surface(
                                color = if (turnSeconds == secs) CatanPrimary.copy(alpha = 0.25f) else Color(0xFF222222),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .border(
                                        width = if (turnSeconds == secs) 2.dp else 1.dp,
                                        color = if (turnSeconds == secs) CatanPrimary else Color(0xFF383838),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        turnSeconds = secs
                                        customSecondsText = secs.toString()
                                    }
                            ) {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 8.dp)) {
                                    Text("${secs}s", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        turnPresetsRow2.forEach { secs ->
                            Surface(
                                color = if (turnSeconds == secs) CatanPrimary.copy(alpha = 0.25f) else Color(0xFF222222),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .border(
                                        width = if (turnSeconds == secs) 2.dp else 1.dp,
                                        color = if (turnSeconds == secs) CatanPrimary else Color(0xFF383838),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        turnSeconds = secs
                                        customSecondsText = secs.toString()
                                    }
                            ) {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 8.dp)) {
                                    Text("${secs}s", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

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
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CatanPrimary,
                            unfocusedBorderColor = Color(0xFF444444)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // 4. Match Mode Specific Player Setup
                if (mode == ClockMode.MATCH) {
                    HorizontalDivider(color = Color(0xFF333333))

                    // Time Bank Row
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "TIME BANK PER PLAYER",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.LightGray,
                                fontWeight = FontWeight.Bold
                            )
                            Switch(
                                checked = isTimeBankEnabled,
                                onCheckedChange = { isTimeBankEnabled = it }
                            )
                        }

                        if (isTimeBankEnabled) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                bankPresets.forEach { secs ->
                                    FilterChip(
                                        selected = bankSeconds == secs,
                                        onClick = { bankSeconds = secs },
                                        label = { Text("${secs}s", fontSize = 11.sp) }
                                    )
                                }
                            }
                        }
                    }

                    // Number of Players Dropdown Selector
                    Column {
                        Text(
                            text = "NUMBER OF PLAYERS",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.LightGray,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Box(modifier = Modifier.fillMaxWidth()) {
                            Surface(
                                color = Color(0xFF222222),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Color(0xFF383838), RoundedCornerShape(12.dp))
                                    .clickable { playerDropdownExpanded = true }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "$numPlayers Players",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = null,
                                        tint = CatanPrimary
                                    )
                                }
                            }

                            DropdownMenu(
                                expanded = playerDropdownExpanded,
                                onDismissRequest = { playerDropdownExpanded = false },
                                modifier = Modifier.background(Color(0xFF2B2B2B))
                            ) {
                                (3..6).forEach { n ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = "$n Players",
                                                fontWeight = if (numPlayers == n) FontWeight.Bold else FontWeight.Normal,
                                                color = if (numPlayers == n) CatanPrimary else Color.White
                                            )
                                        },
                                        onClick = {
                                            numPlayers = n
                                            playerDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Player Names & Color Customization Cards
                    Column {
                        Text(
                            text = "PLAYER SETUP (NAME & COLOR)",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.LightGray,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        (0 until numPlayers).forEach { idx ->
                            Surface(
                                color = Color(0xFF1E1E1E),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .border(1.dp, Color(0xFF333333), RoundedCornerShape(12.dp))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    OutlinedTextField(
                                        value = playerNames.getOrElse(idx) { "" },
                                        onValueChange = { playerNames[idx] = it },
                                        label = { Text("Player ${idx + 1} Name") },
                                        singleLine = true,
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Player.COLOR_PALETTE.forEach { colorHex ->
                                            val isSelected = playerColors.getOrNull(idx) == colorHex
                                            Box(
                                                modifier = Modifier
                                                    .size(26.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(colorHex))
                                                    .border(
                                                        width = if (isSelected) 3.dp else 1.dp,
                                                        color = if (isSelected) Color.White else Color.DarkGray,
                                                        shape = CircleShape
                                                    )
                                                    .clickable { playerColors[idx] = colorHex }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                HorizontalDivider(color = Color(0xFF333333))

                // 5. Audio & Vibration Alert Toggles
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Sound Warning (10s beeps)", fontSize = 13.sp)
                        Switch(checked = soundEnabled, onCheckedChange = { soundEnabled = it })
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Vibration Alert", fontSize = 13.sp)
                        Switch(checked = vibrationEnabled, onCheckedChange = { vibrationEnabled = it })
                    }
                }

                // 6. Reset Button
                Button(
                    onClick = {
                        onResetGame()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Reset Full Timer", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    )
}
