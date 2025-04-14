package org.vl4ds4m.board.game.assistant.ui.game.setup

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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

    private val mPlayers = mutableStateListOf<NewPlayer>()

    val players: List<NewPlayer> = mPlayers

    val remotePlayers: List<NewPlayer> = listOf() // TODO Implement

    fun addPlayer(name: String, netDevId: String?) {
        mPlayers.add(
            NewPlayer(name = name, netDevId = netDevId)
        )
    }

    fun renamePlayer(index: Int, newName: String) {
        if (index !in players.indices) return
        mPlayers[index] = mPlayers[index].copy(name = newName)
    }

    fun removePlayerAt(index: Int) {
        if (index !in players.indices) return
        mPlayers.removeAt(index)
    }

    fun movePlayerUp(index: Int) {
        if (index !in players.indices || index == 0) return
        val player = mPlayers.removeAt(index)
        mPlayers.add(index - 1, player)
    }

    fun movePlayerDown(index: Int) {
        if (index !in players.indices || index == players.lastIndex) return
        val player = mPlayers.removeAt(index)
        mPlayers.add(index + 1, player)
    }

    fun startGame(gameViewModel: GameViewModel) {
        players.forEach { gameViewModel.addPlayer(it.netDevId, it.name) }
        gameViewModel.initialize()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer<GameSetupViewModel> {
                val app = BoardGameAssistantApp.from(this)
                GameSetupViewModel(app.gameRepository)
            }
        }
    }
}
