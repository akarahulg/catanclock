package com.example.catanclock

import com.example.catanclock.model.CatanClockState
import com.example.catanclock.model.ClockMode
import com.example.catanclock.model.GameSettings
import com.example.catanclock.model.Player
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CatanClockStateTest {

    @Test
    fun testDefaultStateValues() {
        val state = CatanClockState()
        assertTrue(state.isOpeningMenuVisible)
        assertEquals(4, state.players.size)
        assertEquals(0, state.activePlayerIndex)
        assertEquals("Red", state.activePlayer.name)
        assertEquals(60_000L, state.remainingTurnTimeMs)
        assertFalse(state.isPaused)
        assertFalse(state.isWarning10s)
        assertEquals(1, state.roundNumber)
        assertEquals(1, state.turnCount)
    }

    @Test
    fun testZenModeDefault() {
        val settings = GameSettings()
        assertEquals(ClockMode.ZEN, settings.mode)
        assertEquals(60, settings.turnDurationSeconds)
    }

    @Test
    fun testPlayerDefaultColors() {
        val players = Player.DEFAULT_PLAYERS
        assertEquals(6, players.size)
        assertEquals("Red", players[0].name)
        assertEquals("Blue", players[1].name)
        assertEquals("White", players[2].name)
        assertEquals("Orange", players[3].name)
        assertEquals("Green", players[4].name)
        assertEquals("Brown", players[5].name)
    }
}
