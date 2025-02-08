package org.vl4ds4m.board.game.assistant.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.vl4ds4m.board.game.assistant.domain.game.Game
import org.vl4ds4m.board.game.assistant.domain.player.Player

abstract class GameViewModel(
    val name: String,
    playerNames: List<String>,
    game: Game<*>
) : ViewModel(*game.initializables) {
    private val mPlayers: MutableStateFlow<List<Player>> = MutableStateFlow(listOf())
    val players: StateFlow<List<Player>> = mPlayers.asStateFlow()

    init {
        playerNames.forEach {
            game.addPlayer(it)
        }
        game.playerStates.combine(game.players) { map, players -> map to players }
            .onEach { (map, players) ->
                mPlayers.update {
                    buildList {
                        map.toList()
                            .sortedByDescending { (_, state) -> state }
                            .forEach { (id, _) ->
                                players.find { it.id == id }
                                    ?.let { add(it) }
                            }
                    }
                }
            }
            .launchIn(viewModelScope)
        game.initializables.forEach {
            it.init(viewModelScope)
        }
    }
}
