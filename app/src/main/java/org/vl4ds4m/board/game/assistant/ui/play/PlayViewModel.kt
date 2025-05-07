package org.vl4ds4m.board.game.assistant.ui.play

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.game.data.GameSessionInfo
import org.vl4ds4m.board.game.assistant.network.RemoteSessionInfo
import org.vl4ds4m.board.game.assistant.network.SessionObserver
import org.vl4ds4m.board.game.assistant.ui.SessionViewModel

class PlayViewModel private constructor(
    app: BoardGameAssistantApp
) : SessionViewModel(app) {
    override fun isMatched(session: GameSessionInfo) = !session.completed

    private val sessionObserver: SessionObserver = app.sessionObserver.apply {
        startDiscovery()
    }

    val remoteSessions: StateFlow<List<RemoteSessionInfo>> =
        sessionObserver.sessions.map { it.values.toList() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), listOf())

    val localSessions: StateFlow<List<GameSessionInfo>> =
        sessions.combine(remoteSessions) { local, remote ->
            val remoteIds = remote.map { it.id }
            local.filter { it.id !in remoteIds }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), listOf())

    override fun onCleared() {
        super.onCleared()
        sessionObserver.stopDiscovery()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = createFactory { PlayViewModel(it) }
    }
}
