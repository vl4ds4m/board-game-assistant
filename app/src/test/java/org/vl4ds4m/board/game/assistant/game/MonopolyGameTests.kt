package org.vl4ds4m.board.game.assistant.game

import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.vl4ds4m.board.game.assistant.game.data.PlayerState
import org.vl4ds4m.board.game.assistant.game.monopoly.MonopolyGame
import org.vl4ds4m.board.game.assistant.game.monopoly.MonopolyGameEnv
import org.vl4ds4m.board.game.assistant.game.monopoly.Prison
import org.vl4ds4m.board.game.assistant.game.monopoly.position

class MonopolyGameTests {
    private lateinit var game: MonopolyGame
    private var playerId: PID = 0
    private lateinit var startState: PlayerState

    @Before
    fun initGame() {
        game = MonopolyGameEnv()
        playerId = game.addPlayer(null, "SDFsdf")
        startState = game.playerState(playerId)!!
    }

    @Test
    fun addMoney() {
        game.addMoney(5634)
        assertEquals(startState.score + 5634, game.playerScore(playerId))
    }

    @Test
    fun spendMoney() {
        game.spendMoney(8_345)
        assertEquals(startState.score - 8_345, game.playerScore(playerId))
    }

    @Test
    fun transferMoney() {
        val anotherId = game.addPlayer(null, "SDfs")
        val anotherScore = game.playerScore(anotherId)!!
        game.transferMoney(senderId = playerId, receiverId = anotherId, 6_823)
        assertEquals(startState.score - 6_823, game.playerScore(playerId))
        assertEquals(anotherScore + 6_823, game.playerScore(anotherId))
    }

    @Test
    fun movePlayer() {
        game.movePlayer(11)
        assertEquals(startState.position!! + 11, game.playerState(playerId)?.position)
        game.movePlayer(10)
        game.movePlayer(11)
        game.movePlayer(12)
        assertEquals(startState.position!! + 4, game.playerState(playerId)?.position)
        assertEquals(startState.score + 2_000, game.playerScore(playerId))
    }

    @Test
    fun prisonScenario() {
        game.moveToPrison()
        assertEquals(Prison.POSITION, game.playerState(playerId)?.position)
        game.movePlayer(3)
        assertEquals(Prison.POSITION, game.playerState(playerId)?.position)
        game.leavePrison(false)
        assertEquals(startState.score - 500, game.playerScore(playerId))
        game.movePlayer(5)
        assertEquals(Prison.POSITION + 5, game.playerState(playerId)?.position)
    }
}

private fun Game.playerState(id: PID): PlayerState? = players.value[id]?.state
