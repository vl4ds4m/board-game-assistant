package org.vl4ds4m.board.game.assistant.domain.game

import org.vl4ds4m.board.game.assistant.domain.game.env.BaseGameEnv
import org.vl4ds4m.board.game.assistant.domain.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.domain.player.state.Score
import org.vl4ds4m.board.game.assistant.domain.player.state.resolver.PlayerStateResolver
import org.vl4ds4m.board.game.assistant.domain.player.state.resolver.SimpleScoreResolver

class FreeGame : GameEnv by BaseGameEnv(),
    PlayerStateResolver<Score> by SimpleScoreResolver(TODO("Not yet implemented"))
