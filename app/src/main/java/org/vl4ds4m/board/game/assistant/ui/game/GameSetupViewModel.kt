package org.vl4ds4m.board.game.assistant.ui.game

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.vl4ds4m.board.game.assistant.domain.game.GameType

class GameSetupViewModel : ViewModel() {
    var type = mutableStateOf<GameType?>(null)
    var name = mutableStateOf("")

    val players = mutableStateListOf<String>()
}
