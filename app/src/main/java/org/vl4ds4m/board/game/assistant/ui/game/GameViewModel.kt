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
import org.vl4ds4m.board.game.assistant.data.GameSession
import org.vl4ds4m.board.game.assistant.data.Store
import org.vl4ds4m.board.game.assistant.domain.game.Game
import org.vl4ds4m.board.game.assistant.domain.game.GameType
import org.vl4ds4m.board.game.assistant.domain.player.Player
import org.vl4ds4m.board.game.assistant.ui.game.free.FreeGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameViewModel

abstract class GameViewModel(
    private val game: Game<*>,
    private val sessionId: Long? = null
) : ViewModel(*game.initializables) {
    val name: String

    private val mPlayers: MutableStateFlow<List<Player>> = MutableStateFlow(listOf())
    val players: StateFlow<List<Player>> = mPlayers.asStateFlow()

    init {
        sessionId?.let { id ->
            Store.load(id)?.let { game.loadFrom(it) }
        }
        name = game.name.value ?: "no name"
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

    override fun onCleared() {
        super.onCleared()
        val session = GameSession()
        game.saveIn(session)
        Store.save(session, this.sessionId)
    }

    companion object {
        fun getFactory(type: GameType, sessionId: Long?): ViewModelProvider.Factory =
            viewModelFactory {
                when (type) {
                    GameType.FREE -> {
                        initializer { FreeGameViewModel.create(sessionId) }
                    }
                    GameType.ORDERED -> {
                        initializer { OrderedGameViewModel.create(sessionId) }
                    }
                    else -> TODO()
                }
            }
    }
}
