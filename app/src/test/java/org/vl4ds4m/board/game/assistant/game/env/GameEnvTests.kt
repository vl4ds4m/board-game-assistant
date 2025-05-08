package org.vl4ds4m.board.game.assistant.game.env

import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.vl4ds4m.board.game.assistant.data.User
import org.vl4ds4m.board.game.assistant.game.SimpleOrdered

class GameEnvTests {
    private lateinit var gameEnv: GameEnv

    @Before
    fun initGameEnv() {
        gameEnv = GameEnv(SimpleOrdered)
    }

    @Test
    fun addPlayers() {
        gameEnv.addPlayer(User(netDevId = "1", self = false, name = ""), "Hello")
        gameEnv.addPlayer(null, "Bar")
        gameEnv.addPlayer(User(netDevId = "4", self = false, name = ""), "Hello")
        gameEnv.addPlayer(User(netDevId = "1fgh", self = false, name = ""), "Foo")
        assertEquals(4, gameEnv.players.value.size)
    }

    @Test
    fun checkCurrentPlayerId() {
        val idState = gameEnv.currentPid
        assertEquals(null, idState.value)
        val idA = gameEnv.addPlayer(null, "A")
        assertEquals(idA, idState.value)
        val idB = gameEnv.addPlayer(null, "B")
        val idC = gameEnv.addPlayer(null, "C")
        assertEquals(idA, idState.value)
        gameEnv.freezePlayer(idA)
        gameEnv.freezePlayer(idB)
        assertEquals(idC, idState.value)
        gameEnv.unfreezePlayer(idB)
        assertEquals(idC, idState.value)
        gameEnv.freezePlayer(idB)
        gameEnv.freezePlayer(idC)
        assertEquals(null, idState.value)
        val idD = gameEnv.addPlayer(null, "D")
        assertEquals(idD, idState.value)
    }
}
