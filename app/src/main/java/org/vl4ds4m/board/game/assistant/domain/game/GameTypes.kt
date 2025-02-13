package org.vl4ds4m.board.game.assistant.domain.game

import org.vl4ds4m.board.game.assistant.domain.game.carcassonne.CarcassonneGame
import org.vl4ds4m.board.game.assistant.domain.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.domain.game.simple.FreeGame
import org.vl4ds4m.board.game.assistant.domain.game.simple.SimpleOrderedGame

sealed interface GameType {
    val title: String

    fun createGame(): GameEnv

    companion object {
        val entries: List<GameType> = listOf(
            Free,
            SimpleOrdered,
            Dice,
            Carcassonne,
            Monopoly
        )

        fun valueOf(title: String): GameType {
            return when (title) {
                Free.title -> Free
                SimpleOrdered.title -> SimpleOrdered
                Dice.title -> Dice
                Carcassonne.title -> Carcassonne
                Monopoly.title -> Monopoly
                else -> throw IllegalArgumentException("No GameType[title = $title]")
            }
        }
    }
}

data object Free : GameType {
    override val title: String = "Free"

    override fun createGame() = FreeGame()
}

sealed interface OrderedGameType : GameType

data object SimpleOrdered : OrderedGameType {
    override val title: String = "Ordered"

    override fun createGame() = SimpleOrderedGame()
}

data object Dice : OrderedGameType {
    override val title: String = "Dice"

    override fun createGame() = DiceGame()
}

data object Carcassonne : OrderedGameType {
    override val title: String = "Carcassonne"

    override fun createGame() = CarcassonneGame()
}

data object Monopoly : OrderedGameType {
    override val title: String = "Monopoly"

    override fun createGame() = MonopolyGame()
}
