package org.vl4ds4m.board.game.assistant.ui.game

import androidx.lifecycle.ViewModel
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
import org.vl4ds4m.board.game.assistant.domain.player.Player
import org.vl4ds4m.board.game.assistant.domain.player.state.Score

class OrderedGameViewModel(
    val name: String,
    players: List<String>
) : ViewModel() {
    private val game: OrderedGame = OrderedGame(viewModelScope)

    private val mPlayers: MutableStateFlow<List<Player>> = MutableStateFlow(listOf())
    val players: StateFlow<List<Player>> = mPlayers.asStateFlow()

    private val mCurrentPlayerId: MutableStateFlow<Long?> = MutableStateFlow(null)
    val currentPlayerId: StateFlow<Long?> = mCurrentPlayerId.asStateFlow()

    init {
        players.forEach {
            game.addPlayer(it)
        }
        launchPlayersUpdate()
        launchCurrentPlayerIdUpdate()
    }

    private fun launchPlayersUpdate() {
        game.playerScores.combine(game.players) { map, players -> map to players }
            .onEach { (map, players) ->
                mPlayers.update {
                    buildList {
                        map.toList()
                            .sortedByDescending { (_, score) -> score.value }
                            .forEach { (id, _) ->
                                players.find { it.id == id }
                                    ?.let { add(it) }
                            }
                    }
                }
            }
            .launchIn(viewModelScope)
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
