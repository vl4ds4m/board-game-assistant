package org.vl4ds4m.board.game.assistant.game

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.vl4ds4m.board.game.assistant.data.User
import org.vl4ds4m.board.game.assistant.game.log.GameAction
import org.vl4ds4m.board.game.assistant.game.data.PlayerState

typealias Players = Map<Long, Player>
typealias OrderedPlayers = List<Pair<Long, Player>>
typealias Actions = List<GameAction>

interface Game {
    val type: GameType

    val name: MutableStateFlow<String>

    val players: StateFlow<Players>

    val orderedPlayers: StateFlow<OrderedPlayers>

    val currentPlayerId: StateFlow<Long?>

    fun changeCurrentPlayerId(id: Long)

    val currentPlayer: Pair<Long, Player>?
        get() = currentPlayerId.value?.let { id ->
            players.value[id]?.let { player -> id to player }
        }

    fun addPlayer(user: User?, name: String): Long

    fun removePlayer(id: Long)

    fun bindPlayer(id: Long, user: User)

    fun unbindPlayer(id: Long)

    fun renamePlayer(id: Long, name: String)

    fun freezePlayer(id: Long)

    fun unfreezePlayer(id: Long)

    fun changePlayerState(id: Long, state: PlayerState)

    val timeout: MutableStateFlow<Boolean>

    val secondsToEnd: StateFlow<Int>

    fun changeSecondsToEnd(seconds: Int)

    val initialized: StateFlow<Boolean>

    val completed: StateFlow<Boolean>

    fun initialize()

    fun start()

    fun stop()

    fun complete()

    fun returnGame()

    val reverted: StateFlow<Boolean>

    val repeatable: StateFlow<Boolean>

    val actions: StateFlow<Actions>

    fun revert()

    fun repeat()

    companion object {
        fun getOrderedPlayers(ids: List<Long>, players: Players): OrderedPlayers =
            if (ids.isEmpty()) players.toList()
            else ids.mapNotNull { id ->
                players[id]?.let { p -> id to p }
            }
    }
}
