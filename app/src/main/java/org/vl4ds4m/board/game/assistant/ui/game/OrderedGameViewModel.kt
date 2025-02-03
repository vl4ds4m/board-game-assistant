package org.vl4ds4m.board.game.assistant.ui.game

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.vl4ds4m.board.game.assistant.domain.game.OrderedGame
import org.vl4ds4m.board.game.assistant.domain.player.Player

class OrderedGameViewModel(
    private val name: String,
    players: List<String>
) : ViewModel() {
    private val game: OrderedGame = OrderedGame()

    init {
        players.forEach {
            val newPlayer = Player(0).apply { name = it }
            game.addPlayer(newPlayer)
        }
    }

    val players: List<Player> = mutableStateListOf<Player>().apply {
        game.getPlayers().forEach { this += it }
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
