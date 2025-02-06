package org.vl4ds4m.board.game.assistant.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.StateFlow
import org.vl4ds4m.board.game.assistant.domain.game.OrderedGame
import org.vl4ds4m.board.game.assistant.domain.player.Player
import org.vl4ds4m.board.game.assistant.domain.player.state.Score

class OrderedGameViewModel(
    val name: String,
    players: List<String>
) : ViewModel() {
    private val game: OrderedGame = OrderedGame()

    init {
        players.forEach {
            game.addPlayer(it)
        }
        game.start(viewModelScope)
    }

    val players: StateFlow<List<Player>> = game.players
    val playerScores: StateFlow<Map<Long, Score>> = game.playerScores

    fun addScore(changing: Int) {
        game.addScore(Score(changing))
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
