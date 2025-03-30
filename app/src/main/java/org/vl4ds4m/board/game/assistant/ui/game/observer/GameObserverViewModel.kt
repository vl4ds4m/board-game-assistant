package org.vl4ds4m.board.game.assistant.ui.game.observer

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.network.GameObserver

class GameObserverViewModel : ViewModel() {
    private val observer = GameObserver()

    val state: StateFlow<GameSession> = observer.sessionState
}
