package org.vl4ds4m.board.game.assistant.ui.game.start

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.vl4ds4m.board.game.assistant.data.Store
import org.vl4ds4m.board.game.assistant.domain.game.GameType

class GameSetupViewModel : ViewModel() {
    val type = mutableStateOf<GameType?>(null)
    val name = mutableStateOf("")

    private val _players = mutableStateListOf<String>()

    val players: List<String> = _players

    fun addPlayer(name: String) {
        _players.add(name)
    }

    fun renamePlayer(index: Int, newName: String) {
        _players[index] = newName
    }

    fun removePlayerAt(index: Int) {
        _players.removeAt(index)
    }

    fun save() {
        Store.addSession(
            name = name.value,
            type = type.value!!,
            players = players
        )
    }
}
