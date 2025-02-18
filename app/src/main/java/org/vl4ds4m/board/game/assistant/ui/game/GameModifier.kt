package org.vl4ds4m.board.game.assistant.ui.game

import androidx.compose.runtime.MutableState

class GameModifier(
    var topAppBarText: MutableState<String>? = null,
    var onGameComplete: (() -> Unit)? = null
)
