package org.vl4ds4m.board.game.assistant.game

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.vl4ds4m.board.game.assistant.data.User
import org.vl4ds4m.board.game.assistant.game.log.GameAction
import org.vl4ds4m.board.game.assistant.game.data.PlayerState

typealias PID = Int // Player identifier
typealias Players = Map<PID, Player>
typealias OrderedPlayers = List<Pair<PID, Player>>
typealias Users = Map<PID, User>
typealias Actions = List<GameAction>

interface Game {
    val type: GameType

    val name: MutableStateFlow<String>

    val players: StateFlow<Players>

    val orderedPlayers: StateFlow<OrderedPlayers>

    val users: StateFlow<Users>

    val currentPid: StateFlow<PID?>

    fun changeCurrentPid(id: PID)

    val currentPlayer: Pair<PID, Player>?
        get() = currentPid.value?.let { id ->
            players.value[id]?.let { player -> id to player }
        }

    fun addPlayer(user: User?, name: String): PID

    fun removePlayer(id: PID)

    fun bindPlayer(id: PID, user: User)

    fun unbindPlayer(id: PID)

    fun renamePlayer(id: PID, name: String)

    fun freezePlayer(id: PID)

    fun unfreezePlayer(id: PID)

    fun changePlayerState(id: PID, state: PlayerState)

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
        fun getOrderedPlayers(ids: List<PID>, players: Players): OrderedPlayers =
            if (ids.isEmpty()) players.toList()
            else ids.mapNotNull { id ->
                players[id]?.let { p -> id to p }
            }
    }
}
