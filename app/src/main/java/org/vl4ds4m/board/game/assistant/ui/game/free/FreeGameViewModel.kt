package org.vl4ds4m.board.game.assistant.ui.game.free

import org.vl4ds4m.board.game.assistant.domain.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.domain.game.simple.FreeGame
import org.vl4ds4m.board.game.assistant.domain.player.Player
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelFactory

class FreeGameViewModel private constructor(
    private val game: FreeGame = FreeGame(),
    sessionId: Long? = null,
) : GameViewModel(
    game = game,
    sessionId = sessionId
) {
    override val name: String = "${game.name.value} (free)"

    override fun addPoints(points: Int) {
        mCurrentPlayerId.value?.let {
            game.addPoints(it, points)
        }
    }

    fun selectCurrentPlayer(player: Player) {
        mCurrentPlayerId.value = player.id
    }

    companion object : GameViewModelFactory<FreeGameViewModel> {
        override fun createFrom(gameEnv: GameEnv): FreeGameViewModel {
            return FreeGameViewModel(game = gameEnv as FreeGame)
        }

        override fun createFrom(sessionId: Long): FreeGameViewModel {
            return FreeGameViewModel(sessionId = sessionId)
        }
    }
}
