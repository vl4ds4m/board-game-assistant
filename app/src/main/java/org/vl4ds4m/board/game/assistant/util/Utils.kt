package org.vl4ds4m.board.game.assistant.util

val Enum<*>.title: String get() = this.name
    .replaceFirstChar { it.uppercaseChar() }
