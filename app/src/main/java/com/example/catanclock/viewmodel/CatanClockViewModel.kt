package com.example.catanclock.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.catanclock.audio.SoundAndHapticManager
import com.example.catanclock.model.CatanClockState
import com.example.catanclock.model.ClockMode
import com.example.catanclock.model.GameSettings
import com.example.catanclock.model.Player
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class CatanClockViewModel(application: Application) : AndroidViewModel(application) {

    private val soundAndHapticManager = SoundAndHapticManager(application.applicationContext)

    private val _uiState = MutableStateFlow(CatanClockState())
    val uiState: StateFlow<CatanClockState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var lastTickTimestamp: Long = 0L
    private var lastWarningSecondPlayed: Int = -1

    init {
        startTimerLoop()
    }

    private fun startTimerLoop() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            lastTickTimestamp = System.currentTimeMillis()
            while (true) {
                delay(50)
                val now = System.currentTimeMillis()
                val delta = now - lastTickTimestamp
                lastTickTimestamp = now

                val currentState = _uiState.value
                if (!currentState.isPaused && !currentState.isOpeningMenuVisible) {
                    onTimerTick(delta)
                }
            }
        }
    }

    private fun onTimerTick(deltaMs: Long) {
        val state = _uiState.value
        val turnMs = state.remainingTurnTimeMs
        val settings = state.settings

        if (turnMs > 0) {
            val newTurnMs = (turnMs - deltaMs).coerceAtLeast(0L)
            val currentSecond = (newTurnMs / 1000L).toInt()
            val isWarning = newTurnMs in 1L..10_000L

            // Trigger warning tick on second boundary transition
            if (isWarning && currentSecond != lastWarningSecondPlayed && currentSecond <= 10) {
                lastWarningSecondPlayed = currentSecond
                soundAndHapticManager.playWarningTick(settings.soundEnabled, settings.vibrationEnabled)
            }

            // Trigger end alarm sound the instant remaining turn time hits 0
            if (newTurnMs == 0L) {
                soundAndHapticManager.playTimeExpiredAlarm(settings.soundEnabled, settings.vibrationEnabled)
            }

            _uiState.update {
                it.copy(
                    remainingTurnTimeMs = newTurnMs,
                    isWarning10s = isWarning,
                    isUsingTimeBank = false
                )
            }
        } else if (settings.mode == ClockMode.MATCH && settings.isTimeBankEnabled) {
            // Main turn time spent, depleting active player's time bank (Match Mode only)
            val activePlayer = state.activePlayer
            val currentBankMs = activePlayer.bankTimeRemainingMs

            if (currentBankMs > 0) {
                val newBankMs = (currentBankMs - deltaMs).coerceAtLeast(0L)

                val updatedPlayers = state.players.mapIndexed { idx, player ->
                    if (idx == state.activePlayerIndex) {
                        player.copy(bankTimeRemainingMs = newBankMs)
                    } else player
                }

                _uiState.update {
                    it.copy(
                        players = updatedPlayers,
                        isUsingTimeBank = true,
                        isWarning10s = true
                    )
                }

                if (newBankMs == 0L) {
                    soundAndHapticManager.playTimeExpiredAlarm(settings.soundEnabled, settings.vibrationEnabled)
                }
            } else {
                if (!state.isPaused) {
                    soundAndHapticManager.playTimeExpiredAlarm(settings.soundEnabled, settings.vibrationEnabled)
                }
            }
        }
    }

    fun toggleDicePanel() {
        _uiState.update { it.copy(isDicePanelVisible = !it.isDicePanelVisible) }
    }

    fun closeDicePanel() {
        _uiState.update { it.copy(isDicePanelVisible = false) }
    }

    fun toggleCitiesAndKnights(enabled: Boolean) {
        _uiState.update {
            val newSettings = it.settings.copy(isCitiesAndKnightsEnabled = enabled)
            it.copy(settings = newSettings)
        }
    }

    fun selectModeFromOpeningMenu(mode: ClockMode) {
        _uiState.update { current ->
            val newSettings = current.settings.copy(mode = mode)
            current.copy(
                isOpeningMenuVisible = false,
                settings = newSettings,
                remainingTurnTimeMs = newSettings.turnDurationSeconds * 1000L,
                isUsingTimeBank = false,
                isWarning10s = false,
                isPaused = false
            )
        }
        lastTickTimestamp = System.currentTimeMillis()
    }

    fun openOpeningMenu() {
        _uiState.update { it.copy(isOpeningMenuVisible = true, isPaused = true) }
    }

    fun togglePauseResume() {
        _uiState.update {
            val nextPaused = !it.isPaused
            lastTickTimestamp = System.currentTimeMillis()
            it.copy(isPaused = nextPaused)
        }
    }

    fun toggleClockMode() {
        _uiState.update { current ->
            val newMode = if (current.settings.mode == ClockMode.ZEN) ClockMode.MATCH else ClockMode.ZEN
            val newSettings = current.settings.copy(mode = newMode)
            current.copy(
                settings = newSettings,
                remainingTurnTimeMs = newSettings.turnDurationSeconds * 1000L,
                isUsingTimeBank = false,
                isWarning10s = false,
                isPaused = false
            )
        }
        lastTickTimestamp = System.currentTimeMillis()
    }

    fun nextTurn() {
        val state = _uiState.value
        val settings = state.settings

        if (settings.mode == ClockMode.ZEN) {
            // In Zen mode, next turn is simply restarting the clock
            restartCurrentTurn()
            _uiState.update { it.copy(isPaused = false) }
            return
        }

        val nextIdx = (state.activePlayerIndex + 1) % state.players.size
        val newTurnCount = state.turnCount + 1
        val newRound = if (nextIdx == 0) state.roundNumber + 1 else state.roundNumber

        lastWarningSecondPlayed = -1
        soundAndHapticManager.playTurnStartBeep(settings.soundEnabled, settings.vibrationEnabled)

        val turnDurationMs = settings.turnDurationSeconds * 1000L

        val updatedPlayers = state.players.mapIndexed { idx, player ->
            if (idx == state.activePlayerIndex) {
                val timeSpent = turnDurationMs - state.remainingTurnTimeMs
                player.copy(
                    turnsCompleted = player.turnsCompleted + 1,
                    totalTimeSpentMs = player.totalTimeSpentMs + timeSpent.coerceAtLeast(0L)
                )
            } else player
        }

        _uiState.update {
            it.copy(
                players = updatedPlayers,
                activePlayerIndex = nextIdx,
                remainingTurnTimeMs = turnDurationMs,
                isUsingTimeBank = false,
                isWarning10s = false,
                isPaused = false,
                turnCount = newTurnCount,
                roundNumber = newRound
            )
        }
        lastTickTimestamp = System.currentTimeMillis()
    }

    fun restartCurrentTurn() {
        val state = _uiState.value
        lastWarningSecondPlayed = -1
        soundAndHapticManager.playTurnStartBeep(state.settings.soundEnabled, state.settings.vibrationEnabled)
        _uiState.update {
            it.copy(
                remainingTurnTimeMs = it.settings.turnDurationSeconds * 1000L,
                isUsingTimeBank = false,
                isWarning10s = false,
                isPaused = false
            )
        }
        lastTickTimestamp = System.currentTimeMillis()
    }

    fun resetGame() {
        val state = _uiState.value
        val settings = state.settings
        val bankMs = settings.bankDurationSeconds * 1000L
        val resetPlayers = state.players.map {
            it.copy(
                bankTimeRemainingMs = bankMs,
                turnsCompleted = 0,
                totalTimeSpentMs = 0L
            )
        }
        lastWarningSecondPlayed = -1
        _uiState.update {
            it.copy(
                players = resetPlayers,
                activePlayerIndex = 0,
                remainingTurnTimeMs = settings.turnDurationSeconds * 1000L,
                isUsingTimeBank = false,
                isPaused = false,
                isWarning10s = false,
                roundNumber = 1,
                turnCount = 1,
                lastDiceRoll = null,
                lastEventDieRoll = null
            )
        }
        lastTickTimestamp = System.currentTimeMillis()
    }

    fun updateSettings(
        newSettings: GameSettings,
        newPlayerNames: List<String>,
        newPlayerColors: List<Long> = emptyList()
    ) {
        val currentPlayers = _uiState.value.players
        val bankMs = newSettings.bankDurationSeconds * 1000L

        val updatedPlayers = List(newSettings.numPlayers) { idx ->
            val name = newPlayerNames.getOrNull(idx) ?: Player.DEFAULT_PLAYERS[idx % Player.DEFAULT_PLAYERS.size].name
            val defaultColor = Player.DEFAULT_PLAYERS[idx % Player.DEFAULT_PLAYERS.size].colorHex
            val colorHex = newPlayerColors.getOrNull(idx) ?: defaultColor
            val existingPlayer = currentPlayers.getOrNull(idx)

            Player(
                id = idx + 1,
                name = name,
                colorHex = colorHex,
                bankTimeRemainingMs = existingPlayer?.bankTimeRemainingMs ?: bankMs,
                turnsCompleted = existingPlayer?.turnsCompleted ?: 0,
                totalTimeSpentMs = existingPlayer?.totalTimeSpentMs ?: 0L
            )
        }

        val activeIdx = _uiState.value.activePlayerIndex.coerceAtMost(updatedPlayers.lastIndex)
        lastWarningSecondPlayed = -1

        _uiState.update {
            it.copy(
                settings = newSettings,
                players = updatedPlayers,
                activePlayerIndex = activeIdx,
                remainingTurnTimeMs = newSettings.turnDurationSeconds * 1000L,
                isUsingTimeBank = false,
                isWarning10s = false,
                isPaused = false
            )
        }
        lastTickTimestamp = System.currentTimeMillis()
    }

    fun rollDice() {
        val die1 = Random.nextInt(1, 7)
        val die2 = Random.nextInt(1, 7)
        val eventDie = Random.nextInt(1, 7)
        _uiState.update {
            it.copy(
                lastDiceRoll = Pair(die1, die2),
                lastEventDieRoll = eventDie
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        soundAndHapticManager.release()
    }
}
