package org.vl4ds4m.board.game.assistant.game

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.vl4ds4m.board.game.assistant.game.state.PlayerState

interface Game {
    val type: GameType

    val name: MutableStateFlow<String>

    val players: StateFlow<Map<Long, Player>>

    val currentPlayerId: StateFlow<Long?>

    fun changeCurrentPlayerId(id: Long)

    val currentPlayer: Player?
        get() = currentPlayerId.value?.let { players.value[it] }

    fun addPlayer(name: String, state: PlayerState): Long

    fun addPlayer(name: String)

    fun removePlayer(id: Long)

    fun renamePlayer(id: Long, name: String)

    fun freezePlayer(id: Long)

    fun unfreezePlayer(id: Long)

    fun changePlayerState(id: Long, state: PlayerState)

    val timeout: MutableStateFlow<Boolean>

    val secondsToEnd: StateFlow<Int>

    fun changeSecondsToEnd(seconds: Int)

    val completed: StateFlow<Boolean>

    fun start()

    fun stop()

    fun complete()

    fun returnGame()
}
