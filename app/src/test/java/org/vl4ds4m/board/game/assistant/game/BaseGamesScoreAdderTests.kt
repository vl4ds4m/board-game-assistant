package org.vl4ds4m.board.game.assistant.game

import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.vl4ds4m.board.game.assistant.game.simple.FreeGame
import org.vl4ds4m.board.game.assistant.game.simple.FreeGameEnv
import org.vl4ds4m.board.game.assistant.game.simple.SimpleScoreAdder

class BaseGamesScoreAdderTests {
    private lateinit var game: FreeGame
    private lateinit var adder: SimpleScoreAdder
    private var playerId: PID = 0

    @Before
    fun initGame() {
        game = FreeGameEnv()
        playerId = game.addPlayer(null, "ASFa")
        adder = SimpleScoreAdder(game)
    }

    @Test
    fun addPoints() {
        adder.addPoints(playerId, 234)
        assertEquals(234, game.playerScore(playerId))
    }

    @Test
    fun addTooManyPoints() {
        adder.addPoints(playerId, 900_000_000)
        adder.addPoints(playerId, 900_000_000)
        assertEquals(999_999_999, game.playerScore(playerId))
    }

    @Test
    fun minusTooManyPoints() {
        adder.addPoints(playerId, 100)
        adder.addPoints(playerId, -150)
        assertEquals(0, game.playerScore(playerId))
    }

    @Test
    fun addPointsNonExistedPlayer() {
        adder.addPoints(-1, 50)
        assertEquals(null, game.players.value[-1]?.state?.score)
    }
}

fun Game.playerScore(id: PID): Int? = players.value[id]?.state?.score
