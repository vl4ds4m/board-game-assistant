package org.vl4ds4m.board.game.assistant.ui.game.start

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.vl4ds4m.board.game.assistant.data.Store
import org.vl4ds4m.board.game.assistant.domain.game.FreeGame
import org.vl4ds4m.board.game.assistant.domain.game.GameType
import org.vl4ds4m.board.game.assistant.domain.game.OrderedGame

class GameSetupViewModel : ViewModel() {
    val type = mutableStateOf<GameType?>(null)
    val name = mutableStateOf("")

    private val mPlayers = mutableStateListOf<String>()

    val players: List<String> = mPlayers

    fun addPlayer(name: String) {
        mPlayers.add(name)
    }

    fun renamePlayer(index: Int, newName: String) {
        mPlayers[index] = newName
    }

    fun removePlayerAt(index: Int) {
        mPlayers.removeAt(index)
    }

    fun createGame() {
        val game = when (type.value!!) {
            GameType.FREE -> {
                FreeGame().also {
                    it.name.value = "${name.value} (free)"
                }
            }
            GameType.ORDERED -> {
                OrderedGame().also {
                    it.name.value = "${name.value} (ordered)"
                }
            }
            else -> TODO()
        }
        players.forEach { game.addPlayer(it) }
        Store.currentGame = game
    }
}
