package org.vl4ds4m.board.game.assistant.domain.player.state.resolver

import org.vl4ds4m.board.game.assistant.domain.player.state.PlayerState

interface PlayerStateResolver<in S : PlayerState<*>> {
    fun resolve(playerId: Long, newState: S)
}
