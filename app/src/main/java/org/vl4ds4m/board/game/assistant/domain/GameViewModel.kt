package org.vl4ds4m.board.game.assistant.domain

import androidx.lifecycle.ViewModel
import org.vl4ds4m.board.game.assistant.data.Game

class GameViewModel : ViewModel() {
    val game: Game = Game()
}