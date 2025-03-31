package org.vl4ds4m.board.game.assistant.game

import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.game.carcassonne.CarcassonneGame
import org.vl4ds4m.board.game.assistant.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.game.monopoly.MonopolyGame
import org.vl4ds4m.board.game.assistant.game.simple.FreeGame
import org.vl4ds4m.board.game.assistant.game.simple.SimpleOrderedGame
import org.vl4ds4m.board.game.assistant.ui.game.carcassonne.CarcassonneGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.dice.DiceGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.free.FreeGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.monopoly.MonopolyGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.ordered.SimpleOrderedGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelProducer

sealed interface GameType {
    val title: String

    fun createGame(): GameEnv

    val viewModelProducer: GameViewModelProducer<GameViewModel>

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

@Serializable
data object Free : GameType {
    override val title: String = "Free"

    override fun createGame() = FreeGame()

    override val viewModelProducer = FreeGameViewModel
}

sealed interface OrderedGameType : GameType

@Serializable
data object SimpleOrdered : OrderedGameType {
    override val title: String = "Ordered"

    override fun createGame() = SimpleOrderedGame()

    override val viewModelProducer = SimpleOrderedGameViewModel
}

@Serializable
data object Dice : OrderedGameType {
    override val title: String = "Dice"

    override fun createGame() = DiceGame()

    override val viewModelProducer = DiceGameViewModel
}

@Serializable
data object Carcassonne : OrderedGameType {
    override val title: String = "Carcassonne"

    override fun createGame() = CarcassonneGame()

    override val viewModelProducer = CarcassonneGameViewModel
}

@Serializable
data object Monopoly : OrderedGameType {
    override val title: String = "Monopoly"

    override fun createGame() = MonopolyGame()

    override val viewModelProducer = MonopolyGameViewModel
}
