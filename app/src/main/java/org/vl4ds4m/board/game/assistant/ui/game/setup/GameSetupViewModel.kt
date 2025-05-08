package org.vl4ds4m.board.game.assistant.ui.game.setup

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.data.User
import org.vl4ds4m.board.game.assistant.data.repository.GameEnvRepository
import org.vl4ds4m.board.game.assistant.data.repository.GameSessionRepository
import org.vl4ds4m.board.game.assistant.game.GameType

class GameSetupViewModel private constructor(
    private val gameEnvRepository: GameEnvRepository,
    sessionRepository: GameSessionRepository
) : ViewModel() {
    val type = MutableStateFlow<GameType?>(null)

    val defaultNameNum: Flow<Int> = sessionRepository.getAllSessions()
        .combine(type) { sessions, type ->
            type ?: 0
            val count = sessions.count { it.type == type }
            return@combine count + 1
        }

    fun createGame(type: GameType, name: String) {
        type.createGameEnv()
            .also { it.name.value = name}
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
                GameSetupViewModel(app.gameEnvRepository, app.sessionRepository)
            }
        }
    }
}
