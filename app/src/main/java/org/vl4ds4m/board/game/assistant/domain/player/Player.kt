package org.vl4ds4m.board.game.assistant.domain.player

data class Player(var id: Long) {
    var name: String = id.toString()
    var active: Boolean = true
}
