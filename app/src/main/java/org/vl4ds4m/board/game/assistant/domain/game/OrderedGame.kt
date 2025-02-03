package org.vl4ds4m.board.game.assistant.domain.game

import org.vl4ds4m.board.game.assistant.domain.game.env.BaseOrderedGameEnv
import org.vl4ds4m.board.game.assistant.domain.player.state.resolver.PlayerStateResolver
import org.vl4ds4m.board.game.assistant.domain.player.Player
import org.vl4ds4m.board.game.assistant.domain.player.state.resolver.SimplePlayerStateResolver
import org.vl4ds4m.board.game.assistant.domain.player.state.Score

class OrderedGame : BaseOrderedGameEnv(), PlayerStateResolver<Score> {
    private val stateResolver = SimplePlayerStateResolver(playersStates)

    override fun getPlayers(): List<Player> {
        return orderedPlayers
    }

    override fun completeGame() {
        TODO("Not yet implemented")
    }

    override fun resolve(player: Player, newState: Score) {
        stateResolver.resolve(player, newState)
    }
}
