package org.vl4ds4m.board.game.assistant.ui.game.setup

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.data.repository.GameRepository
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModel

class GameSetupViewModel private constructor(
    private val gameRepository: GameRepository
) : ViewModel() {
    val type = mutableStateOf<GameType?>(null)
    val name = mutableStateOf("")

    fun createGame() {
        type.value?.createGame()
            ?.also { it.name.value = name.value }
            ?.let { gameRepository.put(it) }
    }

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

    fun startGame(gameViewModel: GameViewModel) {
        players.forEach { gameViewModel.addPlayer(it) }
        gameViewModel.initialize()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer<GameSetupViewModel> {
                get(APPLICATION_KEY)
                    .let { it as BoardGameAssistantApp }
                    .let { GameSetupViewModel(it.gameRepository) }
            }
        }
    }
}
