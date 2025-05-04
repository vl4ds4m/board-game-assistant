package org.vl4ds4m.board.game.assistant.ui.game.setup

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.data.User
import org.vl4ds4m.board.game.assistant.data.repository.GameEnvRepository
import org.vl4ds4m.board.game.assistant.game.GameType

class GameSetupViewModel private constructor(
    private val gameEnvRepository: GameEnvRepository
) : ViewModel() {
    val type = mutableStateOf<GameType?>(null)
    val name = mutableStateOf("")

    fun createGame(type: GameType) {
        type.createGameEnv()
            .also { it.name.value = name.value }
            .let { gameEnvRepository.put(it) }
    }

    private val mPlayers = mutableStateListOf<NewPlayer>()

    val players: List<NewPlayer> = mPlayers

    fun addPlayer(name: String, user: User?) {
        mPlayers.add(NewPlayer(name, user))
    }

    fun renamePlayer(index: Int, newName: String) {
        if (index !in players.indices) return
        mPlayers[index] = mPlayers[index].copy(name = newName)
    }

    fun removePlayerAt(index: Int) {
        if (index !in players.indices) return
        mPlayers.removeAt(index)
    }

    fun changePlayerOrder(index: Int, newPosition: Int) {
        if (index !in players.indices) return
        if (newPosition !in players.indices) return
        if (index == newPosition) return
        val player = mPlayers.removeAt(index)
        mPlayers.add(newPosition, player)
    }

    fun startGame() {
        val game = gameEnvRepository.extract()
        players.forEach {
            game.addPlayer(it.user, it.name)
        }
        game.initialize()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer<GameSetupViewModel> {
                val app = BoardGameAssistantApp.from(this)
                GameSetupViewModel(app.gameEnvRepository)
            }
        }
    }
}
