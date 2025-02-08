package org.vl4ds4m.board.game.assistant.ui.game.ordered

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.vl4ds4m.board.game.assistant.domain.game.OrderedGame
import org.vl4ds4m.board.game.assistant.domain.player.state.Score
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModel

class OrderedGameViewModel private constructor(
    name: String,
    playerNames: List<String>,
    private val game: OrderedGame
) : GameViewModel(
    name = name,
    playerNames = playerNames,
    game = game
) {
    constructor(name: String, playerNames: List<String>) : this(
        name = name,
        playerNames = playerNames,
        game = OrderedGame()
    )

    private val mCurrentPlayerId: MutableStateFlow<Long?> = MutableStateFlow(null)
    val currentPlayerId: StateFlow<Long?> = mCurrentPlayerId.asStateFlow()

    init {
        launchCurrentPlayerIdUpdate()
    }

    private fun launchCurrentPlayerIdUpdate() {
        game.players.combine(game.order) { players, order -> players to order }
            .onEach { (players, order) ->
                mCurrentPlayerId.update {
                    order?.let { players[it].id }
                }
            }
            .launchIn(viewModelScope)
    }

    val playerScores: StateFlow<Map<Long, Score>> = game.playerStates

    fun addScore(points: Int) {
        game.addPoints(points)
    }

    companion object {
        fun getFactory(gameName: String, players: List<String>): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    OrderedGameViewModel(gameName, players)
                }
            }
    }
}
