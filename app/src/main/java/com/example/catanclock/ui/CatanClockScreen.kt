package com.example.catanclock.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.catanclock.ui.components.BigTimerDisplay
import com.example.catanclock.ui.components.ControlBar
import com.example.catanclock.ui.components.DiceRollDialog
import com.example.catanclock.ui.components.PlayerBar
import com.example.catanclock.ui.components.SettingsDialog
import com.example.catanclock.ui.theme.CatanClockTheme
import com.example.catanclock.viewmodel.CatanClockViewModel

@Composable
fun CatanClockScreen(
    viewModel: CatanClockViewModel
) {
    val state by viewModel.uiState.collectAsState()

    var showSettingsDialog by remember { mutableStateOf(false) }
    var showDiceDialog by remember { mutableStateOf(false) }

    CatanClockTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top: Player Info Bar (shown in Match mode only)
                PlayerBar(
                    state = state,
                    onSelectPlayer = { idx ->
                        viewModel.nextTurn()
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Center: 7-Segment Digital Clock Display (Tap screen to restart / next turn)
                BigTimerDisplay(
                    state = state,
                    onTapTimer = {
                        if (state.isPaused) {
                            viewModel.togglePauseResume()
                        } else {
                            viewModel.nextTurn()
                        }
                    },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Bottom: Action Control Bar
                ControlBar(
                    state = state,
                    onTogglePause = { viewModel.togglePauseResume() },
                    onNextTurn = { viewModel.nextTurn() },
                    onRestartTurn = { viewModel.restartCurrentTurn() },
                    onOpenDiceRoll = {
                        if (state.lastDiceRoll == null) {
                            viewModel.rollDice()
                        }
                        showDiceDialog = true
                    },
                    onOpenSettings = {
                        if (!state.isPaused) {
                            viewModel.togglePauseResume()
                        }
                        showSettingsDialog = true
                    }
                )
            }
        }

        // Dialogs
        if (showSettingsDialog) {
            SettingsDialog(
                state = state,
                onDismiss = { showSettingsDialog = false },
                onSaveSettings = { newSettings, playerNames ->
                    viewModel.updateSettings(newSettings, playerNames)
                },
                onResetGame = {
                    viewModel.resetGame()
                }
            )
        }

        if (showDiceDialog) {
            DiceRollDialog(
                lastRoll = state.lastDiceRoll,
                onRollDice = { viewModel.rollDice() },
                onDismiss = { showDiceDialog = false }
            )
        }
    }
}
