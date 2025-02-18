package org.vl4ds4m.board.game.assistant.ui.game

import androidx.compose.runtime.MutableState

class GameModifier(
    val topBarTitle: MutableState<String>,
    val onGameComplete: () -> Unit
)
