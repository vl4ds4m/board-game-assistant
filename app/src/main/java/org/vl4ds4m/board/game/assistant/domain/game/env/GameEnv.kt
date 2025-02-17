package org.vl4ds4m.board.game.assistant.domain.game.env

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.vl4ds4m.board.game.assistant.data.GameSession
import org.vl4ds4m.board.game.assistant.domain.Initializable
import org.vl4ds4m.board.game.assistant.domain.Player

interface GameEnv {
    fun loadFrom(session: GameSession)

    fun saveIn(session: GameSession)

    val players: StateFlow<Map<Long, Player>>

    val name: MutableStateFlow<String>

    val timeout: MutableStateFlow<Boolean>

    val secondsToEnd: StateFlow<Int>

    fun setSecondsToEnd(time: Int)

    val completed: StateFlow<Boolean>

    fun addPlayer(name: String): Long

    fun removePlayer(id: Long)

    fun renamePlayer(id: Long, name: String)

    fun freezePlayer(id: Long)

    fun unfreezePlayer(id: Long)

    fun changePlayerScore(id: Long, score: Int)

    val initializables: Array<Initializable>

    fun start()

    fun stop()

    fun complete()
}
