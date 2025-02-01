package org.vl4ds4m.board.game.assistant.domain.game

enum class GameType {
    FREE,
    ORDERED,
    DICE,
    CARCASSONNE,
    MONOPOLY;

    val title: String = name.lowercase()
        .replaceFirstChar { it.uppercaseChar() }
}
