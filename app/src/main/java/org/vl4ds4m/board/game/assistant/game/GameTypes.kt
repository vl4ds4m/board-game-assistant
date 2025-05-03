package org.vl4ds4m.board.game.assistant.game

import androidx.annotation.StringRes
import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.game.carcassonne.CarcassonneGameEnv
import org.vl4ds4m.board.game.assistant.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.game.monopoly.MonopolyGameEnv
import org.vl4ds4m.board.game.assistant.game.simple.FreeGameEnv
import org.vl4ds4m.board.game.assistant.game.simple.SimpleOrderedGameEnv
import org.vl4ds4m.board.game.assistant.ui.game.carcassonne.CarcassonneGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.dice.DiceGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.simple.FreeGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.monopoly.MonopolyGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.simple.SimpleOrderedGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModelProducer
import org.vl4ds4m.board.game.assistant.ui.game.GameActionPresenter
import org.vl4ds4m.board.game.assistant.ui.game.GameUI
import org.vl4ds4m.board.game.assistant.ui.game.carcassonne.CarcassonneGameUI
import org.vl4ds4m.board.game.assistant.ui.game.dice.DiceGameUI
import org.vl4ds4m.board.game.assistant.ui.game.monopoly.MonopolyGameActionPresenter
import org.vl4ds4m.board.game.assistant.ui.game.monopoly.MonopolyGameUI
import org.vl4ds4m.board.game.assistant.ui.game.simple.FreeGameUI
import org.vl4ds4m.board.game.assistant.ui.game.simple.SimpleOrderedGameUI

@Serializable
sealed interface GameType {
    val title: String

    @get:StringRes
    val localizedStringId: Int

    fun createGameEnv(): GameEnv

    val viewModelProducer: GameViewModelProducer<GameViewModel>

    val uiFactory: GameUI.Factory

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

    override val localizedStringId = R.string.game_type_free

    override fun createGameEnv() = FreeGameEnv()

    override val viewModelProducer = FreeGameViewModel

    override val uiFactory = FreeGameUI
}

@Serializable
sealed interface OrderedGameType : GameType

@Serializable
data object SimpleOrdered : OrderedGameType {
    override val title: String = "Ordered"

    override val localizedStringId = R.string.game_type_simple

    override fun createGameEnv() = SimpleOrderedGameEnv()

    override val viewModelProducer = SimpleOrderedGameViewModel

    override val uiFactory = SimpleOrderedGameUI
}

@Serializable
data object Dice : OrderedGameType {
    override val title: String = "Dice"

    override val localizedStringId = R.string.game_type_dice

    override fun createGameEnv() = DiceGameEnv()

    override val viewModelProducer = DiceGameViewModel

    override val uiFactory = DiceGameUI
}

@Serializable
data object Carcassonne : OrderedGameType {
    override val title: String = "Carcassonne"

    override val localizedStringId = R.string.game_type_carcassonne

    override fun createGameEnv() = CarcassonneGameEnv()

    override val viewModelProducer = CarcassonneGameViewModel

    override val uiFactory = CarcassonneGameUI
}

@Serializable
data object Monopoly : OrderedGameType {
    override val title: String = "Monopoly"

    override val localizedStringId = R.string.game_type_monopoly

    override fun createGameEnv() = MonopolyGameEnv()

    override val viewModelProducer = MonopolyGameViewModel

    override val uiFactory = MonopolyGameUI
}

val GameType.gameActionPresenter: GameActionPresenter
    get() = when (this) {
        Free, SimpleOrdered, Dice, Carcassonne -> GameActionPresenter.Default
        Monopoly -> MonopolyGameActionPresenter
    }
