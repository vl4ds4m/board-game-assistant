package org.vl4ds4m.board.game.assistant.game.env

import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.vl4ds4m.board.game.assistant.game.PID
import org.vl4ds4m.board.game.assistant.game.SimpleOrdered

class OrderedGameEnvTests {
    private lateinit var gameEnv: OrderedGameEnv

    @Before
    fun initGameEnv() {
        gameEnv = OrderedGameEnv(SimpleOrdered)
    }

    @Test
    fun changeCurrentPlayer() {
        val idState = gameEnv.currentPid
        val idA = addNewPlayer()
        val idB = addNewPlayer()
        val idC = addNewPlayer()
        assertEquals(idA, idState.value)
        gameEnv.changeCurrentPid()
        assertEquals(idB, idState.value)
        gameEnv.changePlayerOrder(idB, 0)
        gameEnv.changeCurrentPid()
        assertEquals(idA, idState.value)
        gameEnv.changeCurrentPid()
        assertEquals(idC, idState.value)
    }

    private fun addNewPlayer(): PID = gameEnv.addPlayer(null, "A")
}
