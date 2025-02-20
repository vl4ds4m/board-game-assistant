package org.vl4ds4m.board.game.assistant.ui.game.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.vl4ds4m.board.game.assistant.data.GameSession
import org.vl4ds4m.board.game.assistant.data.Store
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.env.GameEnv

abstract class GameViewModel(
    private val gameEnv: GameEnv,
    private val sessionId: Long? = null
) : ViewModel(), Game by gameEnv {
    init {
        Log.d(TAG, "Initiate ${this::class.simpleName}")
        sessionId?.let { id ->
            Store.load(id)?.let { gameEnv.loadFrom(it) }
        }
        gameEnv.initializables.forEach { it.init(viewModelScope) }
    }

    override fun onCleared() {
        Log.d(TAG, "Clear ${this::class.simpleName}")
        gameEnv.initializables.forEach { it.close() }
        GameSession().let {
            gameEnv.saveIn(it)
            Store.save(it, sessionId)
        }
        super.onCleared()
    }
}

private const val TAG = "GameViewModel"
