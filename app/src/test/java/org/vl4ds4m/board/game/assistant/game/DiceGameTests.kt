package org.vl4ds4m.board.game.assistant.game

import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.vl4ds4m.board.game.assistant.game.data.PlayerState

class DiceGameTests {
    private lateinit var game: DiceGame
    private var playerId: Int = 0

    @Before
    fun initGame() {
        game = DiceGameEnv()
        playerId = game.addPlayer(null, "Oinods")
        game.changePlayerState(playerId, PlayerState(startScore, mapOf()))
    }

    @Test
    fun addValidPoints() {
        game.addPoints(20)
        assertEquals(startScore + 20, game.playerScore(playerId))
    }

    @Test
    fun addInvalidPoints() {
        game.addPoints(4)
        assertEquals(startScore, game.playerScore(playerId))
    }

    @Test
    fun minusPointsOnOverflow() {
        game.addPoints(900)
        assertEquals(startScore - 100, game.playerScore(playerId))
    }

    @Test
    fun getMaxScore() {
        game.addPoints(1000 - startScore)
        assertEquals(1000, game.playerScore(playerId))
        game.addPoints(5)
        assertEquals(1000, game.playerScore(playerId))
    }
}

private const val startScore = 145
