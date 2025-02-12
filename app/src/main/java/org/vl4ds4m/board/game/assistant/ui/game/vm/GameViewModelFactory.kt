package org.vl4ds4m.board.game.assistant.ui.game.vm

interface GameViewModelFactory<out VM : GameViewModel> {
    fun create(sessionId: Long?): VM
}
