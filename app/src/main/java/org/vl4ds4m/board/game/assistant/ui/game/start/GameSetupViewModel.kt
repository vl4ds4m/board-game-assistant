package org.vl4ds4m.board.game.assistant.ui.game.start

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.vl4ds4m.board.game.assistant.data.Store
import org.vl4ds4m.board.game.assistant.domain.game.CarcassonneGame
import org.vl4ds4m.board.game.assistant.domain.game.DiceGame
import org.vl4ds4m.board.game.assistant.domain.game.GameType
import org.vl4ds4m.board.game.assistant.domain.game.MonopolyGame
import org.vl4ds4m.board.game.assistant.domain.game.simple.FreeGame
import org.vl4ds4m.board.game.assistant.domain.game.simple.SimpleOrderedGame

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
                SimpleOrderedGame().also {
                    it.name.value = "${name.value} (ordered)"
                }
            }
            GameType.DICE -> {
                DiceGame().also {
                    it.name.value = "Dice '${name.value}'"
                }
            }
            GameType.CARCASSONNE -> {
                CarcassonneGame().also {
                    it.name.value = "Carcassonne '${name.value}'"
                }
            }
            GameType.MONOPOLY -> {
                MonopolyGame().also {
                    it.name.value = "Monopoly '${name.value}'"
                }
            }
        }
        players.forEach { game.addPlayer(it) }
        Store.currentGame = game
    }
}
