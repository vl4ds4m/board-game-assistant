package org.vl4ds4m.board.game.assistant.ui.game.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.vl4ds4m.board.game.assistant.data.GameSession
import org.vl4ds4m.board.game.assistant.data.Store
import org.vl4ds4m.board.game.assistant.domain.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.domain.Player

abstract class GameViewModel(
    private val game: GameEnv,
    private val sessionId: Long? = null
) : ViewModel(
    closeables = game.initializables
) {
    open val name: String = game.name.value

    private val mPlayers: MutableStateFlow<List<Player>> = MutableStateFlow(listOf())
    val players: StateFlow<List<Player>> = mPlayers.asStateFlow()

    init {
        Log.d(TAG, "Initiate ${this::class.simpleName}")
        sessionId?.let { id ->
            Store.load(id)?.let { game.loadFrom(it) }
        }
        game.players.onEach { list ->
            mPlayers.update {
                list.sortedByDescending { it.score }
            }
        }.launchIn(viewModelScope)
        game.initializables.forEach {
            it.init(viewModelScope)
        }
    }

    protected val mCurrentPlayerId: MutableStateFlow<Long?> = MutableStateFlow(null)
    val currentPlayerId: StateFlow<Long?> = mCurrentPlayerId.asStateFlow()

    abstract fun addPoints(points: Int)

    override fun onCleared() {
        Log.d(TAG, "Clear ${this::class.simpleName}")
        super.onCleared()
        val session = GameSession()
        game.saveIn(session)
        Store.save(session, this.sessionId)
    }
}

private val TAG = GameViewModel::class.simpleName
