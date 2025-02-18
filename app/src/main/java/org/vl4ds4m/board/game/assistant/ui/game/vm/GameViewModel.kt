package org.vl4ds4m.board.game.assistant.ui.game.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.vl4ds4m.board.game.assistant.data.GameSession
import org.vl4ds4m.board.game.assistant.data.Store
import org.vl4ds4m.board.game.assistant.domain.Player
import org.vl4ds4m.board.game.assistant.domain.game.env.GameEnv

abstract class GameViewModel(
    private val game: GameEnv,
    private val sessionId: Long? = null
) : ViewModel(
    closeables = game.initializables
) {
    val players: StateFlow<Map<Long, Player>> = game.players

    init {
        Log.d(TAG, "Initiate ${this::class.simpleName}")
        sessionId?.let { id ->
            Store.load(id)?.let { game.loadFrom(it) }
        }
        game.initializables.forEach {
            it.init(viewModelScope)
        }
    }

    val name: MutableStateFlow<String> = game.name

    val timeout: StateFlow<Boolean> = game.timeout

    fun changeTimeout(enabled: Boolean) {
        game.timeout.value = enabled
    }

    val secondsToEnd: StateFlow<Int> = game.secondsToEnd

    fun changeSecondsToEnd(seconds: Int) {
        game.changeSecondsToEnd(seconds)
    }

    val completed: StateFlow<Boolean> = game.completed

    fun start() {
        game.start()
    }

    fun stop() {
        game.stop()
    }

    fun complete() {
        game.complete()
    }

    fun returnGame() {
        game.returnGame()
    }

    override fun onCleared() {
        Log.d(TAG, "Clear ${this::class.simpleName}")
        super.onCleared()
        val session = GameSession()
        game.saveIn(session)
        Store.save(session, this.sessionId)
    }
}

private val TAG = GameViewModel::class.simpleName
