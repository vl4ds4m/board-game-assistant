package org.vl4ds4m.board.game.assistant.game

import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.vl4ds4m.board.game.assistant.game.carcassonne.CarcassonneGame
import org.vl4ds4m.board.game.assistant.game.carcassonne.CarcassonneGameEnv
import org.vl4ds4m.board.game.assistant.game.carcassonne.CarcassonneProperty
import org.vl4ds4m.board.game.assistant.game.data.PlayerState

class CarcassonneGameTests {
    private lateinit var game: CarcassonneGame
    private var playerId: Int = 0

    @Before
    fun initGame() {
        game = CarcassonneGameEnv()
        playerId = game.addPlayer(null, "Baflsad")
        game.changePlayerState(playerId, PlayerState(startScore, mapOf()))
    }

    @Test
    fun addPointsForRoad() {
        game.addPoints(CarcassonneProperty.ROAD, 23, false)
        assertEquals(23 + startScore, game.playerScore(playerId))
    }

    @Test
    fun addPointsForCity() {
        game.addPoints(CarcassonneProperty.CITY, 7, false)
        val d = 7 * 2
        assertEquals(d + startScore, game.playerScore(playerId))
        game.addPoints(CarcassonneProperty.CITY, 3, true)
        assertEquals(3 + d + startScore, game.playerScore(playerId))
    }

    @Test
    fun addPointsForCloister() {
        game.addPoints(CarcassonneProperty.CLOISTER, 2, false)
        assertEquals(2 + startScore, game.playerScore(playerId))
        game.addPoints(CarcassonneProperty.CLOISTER, 30, false)
        assertEquals(2 + startScore, game.playerScore(playerId))
    }

    @Test
    fun addPointsForField() {
        game.addPoints(CarcassonneProperty.FIELD, 5, true)
        val d = 5 * 3
        assertEquals(d + startScore, game.playerScore(playerId))
        game.addPoints(CarcassonneProperty.FIELD, 5, false)
        assertEquals(d + startScore, game.playerScore(playerId))
    }
}

private const val startScore = 27
