package com.example.catanclock.model

data class CatanClockState(
    val players: List<Player> = Player.DEFAULT_PLAYERS.take(4),
    val activePlayerIndex: Int = 0,
    val remainingTurnTimeMs: Long = 60_000L,
    val isUsingTimeBank: Boolean = false,
    val isPaused: Boolean = true,
    val isWarning10s: Boolean = false,
    val roundNumber: Int = 1,
    val turnCount: Int = 1,
    val settings: GameSettings = GameSettings(),
    val lastDiceRoll: Pair<Int, Int>? = null
) {
    val activePlayer: Player get() = players[activePlayerIndex.coerceIn(0, players.lastIndex)]
}
