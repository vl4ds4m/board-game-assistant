package org.vl4ds4m.board.game.assistant.domain.player.state.resolver

import org.vl4ds4m.board.game.assistant.domain.player.Player
import org.vl4ds4m.board.game.assistant.domain.player.state.PlayerState

interface PlayerStateResolver<S : PlayerState> {
    fun resolve(player: Player, newState: S)
}
