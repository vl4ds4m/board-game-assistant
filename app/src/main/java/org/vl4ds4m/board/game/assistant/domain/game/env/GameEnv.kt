package org.vl4ds4m.board.game.assistant.domain.game.env

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.vl4ds4m.board.game.assistant.domain.Initializable
import org.vl4ds4m.board.game.assistant.domain.player.Player

interface GameEnv : GameState {
    val players: StateFlow<List<Player>>

    val name: MutableStateFlow<String>

    val timeout: MutableStateFlow<Boolean>

    val secondsToEnd: StateFlow<Int>

    fun setSecondsToEnd(time: Int)

    val completed: StateFlow<Boolean>

    fun addPlayer(playerName: String)

    fun renamePlayer(player: Player, name: String)

    fun changePlayerScore(player: Player, score: Int)

    fun removePlayer(player: Player)

    fun freezePlayer(player: Player)

    fun unfreezePlayer(player: Player)

    val initializables: Array<Initializable>

    fun start()

    fun stop()

    fun complete()
}
