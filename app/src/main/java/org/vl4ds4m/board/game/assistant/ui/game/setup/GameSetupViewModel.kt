package org.vl4ds4m.board.game.assistant.ui.game.setup

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.vl4ds4m.board.game.assistant.data.Store
import org.vl4ds4m.board.game.assistant.game.GameType

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
        val game = type.value?.createGame() ?: return
        game.name.value = name.value
        players.forEach { game.addPlayer(it) }
        Store.currentGameEnv = game
    }
}
