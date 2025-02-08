package org.vl4ds4m.board.game.assistant.ui.game.free

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.StateFlow
import org.vl4ds4m.board.game.assistant.domain.game.FreeGame
import org.vl4ds4m.board.game.assistant.domain.player.Player
import org.vl4ds4m.board.game.assistant.domain.player.state.Score
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModel

class FreeGameViewModel private constructor(
    name: String,
    playerNames: List<String>,
    private val game: FreeGame
) : GameViewModel(
    name = name,
    playerNames = playerNames,
    game = game
) {
    constructor(name: String, playerNames: List<String>) : this(
        name = name,
        playerNames = playerNames,
        game = FreeGame()
    )

    private val mCurrentPlayerId: MutableState<Long?> = mutableStateOf(null)
    val currentPlayerId: State<Long?> = mCurrentPlayerId

    fun selectCurrentPlayer(player: Player) {
        mCurrentPlayerId.value = player.id
    }

    val playerScores: StateFlow<Map<Long, Score>> = game.playerStates

    fun addScore(points: Int) {
        game.addPoints(currentPlayerId.value ?: -1, points)
    }

    companion object {
        fun getFactory(gameName: String, players: List<String>): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    FreeGameViewModel(gameName, players)
                }
            }
    }
}
