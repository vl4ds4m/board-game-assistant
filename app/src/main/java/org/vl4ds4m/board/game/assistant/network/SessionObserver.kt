package org.vl4ds4m.board.game.assistant.network

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SessionObserver {
    val sessions: StateFlow<List<RemoteSession>> = MutableStateFlow(listOf())

    suspend fun observe() {}
}
