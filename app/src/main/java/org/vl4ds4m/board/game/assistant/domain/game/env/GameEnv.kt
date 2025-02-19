package org.vl4ds4m.board.game.assistant.domain.game.env

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.vl4ds4m.board.game.assistant.data.GameSession
import org.vl4ds4m.board.game.assistant.domain.Initializable
import org.vl4ds4m.board.game.assistant.domain.Player

interface GameEnv {
    val name: MutableStateFlow<String>

    val players: StateFlow<Map<Long, Player>>

    val currentPlayerId: StateFlow<Long?>

    fun changeCurrentPlayerId(id: Long)

    val currentPlayer: Player?
        get() = currentPlayerId.value?.let { players.value[it] }

    fun addPlayer(name: String): Long

    fun removePlayer(id: Long)

    fun renamePlayer(id: Long, name: String)

    fun freezePlayer(id: Long)

    fun unfreezePlayer(id: Long)

    fun changePlayerScore(id: Long, score: Int)

    val initializables: Array<Initializable>

    val timeout: MutableStateFlow<Boolean>

    val secondsToEnd: StateFlow<Int>

    fun changeSecondsToEnd(seconds: Int)

    val completed: StateFlow<Boolean>

    fun start()

    fun stop()

    fun complete()

    fun returnGame()

    fun loadFrom(session: GameSession)

    fun saveIn(session: GameSession)
}
