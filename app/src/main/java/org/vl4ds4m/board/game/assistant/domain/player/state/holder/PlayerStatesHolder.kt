package org.vl4ds4m.board.game.assistant.domain.player.state.holder

import kotlinx.coroutines.flow.StateFlow
import org.vl4ds4m.board.game.assistant.domain.game.env.GameState
import org.vl4ds4m.board.game.assistant.domain.player.state.PlayerState

interface PlayerStatesHolder<out T : PlayerState<*>> : GameState {
    val playerStates: StateFlow<Map<Long, T>>
}
