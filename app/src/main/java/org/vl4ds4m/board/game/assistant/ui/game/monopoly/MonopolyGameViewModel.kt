package org.vl4ds4m.board.game.assistant.ui.game.monopoly

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.data.MonopolyPlayerState
import org.vl4ds4m.board.game.assistant.game.monopoly.MonopolyGame
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelProducer

class MonopolyGameViewModel(
    private val gameEnv: MonopolyGame = MonopolyGame(),
    sessionId: String? = null,
    app: BoardGameAssistantApp
) : OrderedGameViewModel(gameEnv, sessionId, app) {
    val inPrison: StateFlow<Boolean> = gameEnv.currentPlayerId
        .combine(gameEnv.players) { id, p -> p[id] }
        .filterNotNull()
        .map { it.state as MonopolyPlayerState }
        .map { it.inPrison }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    fun addMoney(money: Int) {
        gameEnv.addMoney(money)
    }

    fun spendMoney(money: Int) {
        gameEnv.spendMoney(money)
    }

    fun movePlayer(steps: Int) {
        gameEnv.movePlayer(steps)
    }

    fun moveToPrison() {
        gameEnv.moveToPrison()
    }

    fun leavePrison(rescued: Boolean) {
        gameEnv.leavePrison(rescued)
    }

    fun transferMoney(senderId: Long, receiverId: Long, money: Int) {
        gameEnv.transferMoney(senderId, receiverId, money)
    }

    companion object : GameViewModelProducer<MonopolyGameViewModel> {
        override fun createViewModel(game: Game, app: BoardGameAssistantApp) =
            MonopolyGameViewModel(
                gameEnv = game as MonopolyGame,
                app = app
            )

        override fun createViewModel(sessionId: String, app: BoardGameAssistantApp) =
            MonopolyGameViewModel(
                sessionId = sessionId,
                app = app
            )
    }
}
