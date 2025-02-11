package org.vl4ds4m.board.game.assistant.domain.game.env

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.vl4ds4m.board.game.assistant.domain.player.Player

interface GameEnv : GameState {
    val players: StateFlow<List<Player>>

    val name: MutableStateFlow<String?>

    fun addPlayer(playerName: String)

    fun changePlayerName(player: Player, name: String)

    fun removePlayer(player: Player)

    fun freezePlayer(player: Player)

    fun unfreezePlayer(player: Player)
}
