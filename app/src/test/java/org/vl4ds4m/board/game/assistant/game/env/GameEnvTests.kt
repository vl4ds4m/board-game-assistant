package org.vl4ds4m.board.game.assistant.game.env

import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.vl4ds4m.board.game.assistant.data.User
import org.vl4ds4m.board.game.assistant.game.Free

class GameEnvTests {
    private lateinit var gameEnv: GameEnv

    @Before
    fun initGameEnv() {
        gameEnv = GameEnv(Free)
    }

    @Test
    fun removePlayer() {
        val id1 = gameEnv.addPlayer(null, "Abc")
        gameEnv.removePlayer(id1)
        assertTrue(gameEnv.players.value[id1]?.removed == true)
    }

    @Test
    fun playerBinding() {
        val id = gameEnv.addPlayer(null, "A")
        val user = User("lsjf", false, "Odsf")
        gameEnv.bindPlayer(id, user)
        assertTrue(gameEnv.users.value[id] == user)
        gameEnv.unbindPlayer(id)
        assertTrue(id !in gameEnv.users.value)
    }

    @Test
    fun playerFreezing() {
        val id1 = gameEnv.addPlayer(null, "Agds")
        val id2 = gameEnv.addPlayer(null, "dsfsdf")
        assertTrue(gameEnv.currentPid.value == id1)
        gameEnv.freezePlayer(id1)
        assertTrue(gameEnv.players.value[id1]?.active == false)
        assertTrue(gameEnv.currentPid.value == id2)
        gameEnv.unfreezePlayer(id1)
        assertTrue(gameEnv.players.value[id1]?.active == true)
    }

    @Test
    fun playerRenaming() {
        val oldName = "OInsdf"
        val id = gameEnv.addPlayer(null, oldName)
        assertEquals(oldName, gameEnv.players.value[id]?.name)
        val newName = "oNOInsdf"
        gameEnv.renamePlayer(id, newName)
        assertEquals(newName, gameEnv.players.value[id]?.name)
    }

    @Test
    fun playerStateChanged() {
        val id = gameEnv.addPlayer(null, "OBnsid")
        val oldState = gameEnv.players.value[id]!!.state
        val newState = oldState.copy(score = oldState.score + 1)
        gameEnv.changePlayerState(id, newState)
        assertEquals(newState, gameEnv.players.value[id]?.state)
    }
}
