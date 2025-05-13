package org.vl4ds4m.board.game.assistant.game.env

import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.vl4ds4m.board.game.assistant.States
import org.vl4ds4m.board.game.assistant.data.User
import org.vl4ds4m.board.game.assistant.game.PID
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.SimpleOrdered
import org.vl4ds4m.board.game.assistant.game.currentPlayerChangedAction
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.game.data.PlayerState
import org.vl4ds4m.board.game.assistant.game.playerStateChangedAction

class OrderedGameEnvTests {
    private lateinit var gameEnv: OrderedGameEnv

    @Before
    fun initGameEnv() {
        gameEnv = OrderedGameEnv(SimpleOrdered)
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

    @Test
    fun dataState() {
        val startGameSession = GameSession(
            completed = true,
            type = SimpleOrdered,
            name = "Alin",
            players = listOf(
                8 to Player("Uii", PlayerState.Empty),
                98 to Player("Osjd", Player.Presence.FROZEN, PlayerState(54, mapOf()))
            ),
            users = mapOf(
                8 to User("OInds", false, "Onoon")
            ),
            currentPid = 98,
            nextNewPid = 1231,
            startTime = 812L,
            stopTime = 65345L,
            duration = 10043L,
            timeout = false,
            secondsUntilEnd = 34,
            actions = listOf(
                currentPlayerChangedAction(States(8, 98)),
                playerStateChangedAction(98, States(PlayerState.Empty, PlayerState(54, mapOf())))
            ),
            currentActionPosition = 2
        )
        gameEnv.load(startGameSession)
        val endGameSession = gameEnv.save()
        assertEquals(startGameSession, endGameSession)
    }
}
