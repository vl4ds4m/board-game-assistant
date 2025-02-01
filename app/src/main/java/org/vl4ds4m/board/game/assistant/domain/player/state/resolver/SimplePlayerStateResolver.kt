package org.vl4ds4m.board.game.assistant.domain.player.state.resolver

import org.vl4ds4m.board.game.assistant.domain.player.Player
import org.vl4ds4m.board.game.assistant.domain.player.state.Score

class SimplePlayerStateResolver(
    private val playersState: MutableMap<Player, Score>
) : PlayerStateResolver<Score> {
    override fun resolve(player: Player, newState: Score) {
        playersState[player] = newState
    }
}
