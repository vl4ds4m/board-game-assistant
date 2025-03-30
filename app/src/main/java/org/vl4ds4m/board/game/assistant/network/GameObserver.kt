package org.vl4ds4m.board.game.assistant.network

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.vl4ds4m.board.game.assistant.game.SimpleOrdered
import org.vl4ds4m.board.game.assistant.game.data.GameSession

class GameObserver {
    val sessionState: StateFlow<GameSession> = fakeState
}

private val fakeState = MutableStateFlow(GameSession(
    completed = false,
    type = SimpleOrdered,
    name = "Foo Bar",
    players = mapOf(),
    currentPlayerId = null,
    nextNewPlayerId = 999L,
    startTime = null,
    stopTime = null,
    timeout = false,
    secondsUntilEnd = 0,
    actions = listOf(),
    currentActionPosition = 0,
    additional = null
)).asStateFlow()
