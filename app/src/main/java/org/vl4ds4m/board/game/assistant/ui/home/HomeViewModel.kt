package org.vl4ds4m.board.game.assistant.ui.home

import android.net.nsd.NsdManager
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

class HomeViewModel private constructor(
    app: BoardGameAssistantApp
) : SessionViewModel(app) {
    override fun isMatched(session: GameSessionInfo) = !session.completed

    private val sessionObserver: SessionObserver =
        app.applicationContext.getSystemService(NsdManager::class.java)
        .let { SessionObserver(it) }
        .also { it.startDiscovery() }

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
        val Factory: ViewModelProvider.Factory = createFactory { HomeViewModel(it) }
    }
}
