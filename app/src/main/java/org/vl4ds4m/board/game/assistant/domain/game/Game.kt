package org.vl4ds4m.board.game.assistant.domain.game

import org.vl4ds4m.board.game.assistant.domain.Initializable
import org.vl4ds4m.board.game.assistant.domain.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.domain.player.state.PlayerState
import org.vl4ds4m.board.game.assistant.domain.player.state.holder.PlayerStatesHolder
import org.vl4ds4m.board.game.assistant.domain.player.state.resolver.PlayerStateResolver

interface Game<T : PlayerState<*>> : GameEnv,
    PlayerStatesHolder<T>,
    PlayerStateResolver<T>
{
    val initializables: Array<Initializable>
}
