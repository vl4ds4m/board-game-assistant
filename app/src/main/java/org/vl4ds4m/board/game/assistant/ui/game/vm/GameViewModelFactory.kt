package org.vl4ds4m.board.game.assistant.ui.game.vm

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.InitializerViewModelFactoryBuilder
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.vl4ds4m.board.game.assistant.data.Store
import org.vl4ds4m.board.game.assistant.game.Carcassonne
import org.vl4ds4m.board.game.assistant.game.Dice
import org.vl4ds4m.board.game.assistant.game.Free
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.Monopoly
import org.vl4ds4m.board.game.assistant.game.SimpleOrdered
import org.vl4ds4m.board.game.assistant.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.ui.game.carcassonne.CarcassonneGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.dice.DiceGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.free.FreeGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.monopoly.MonopolyGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.ordered.SimpleOrderedGameViewModel

interface GameViewModelFactory {
    fun createFrom(gameEnv: GameEnv): GameViewModel

    fun createFrom(sessionId: Long): GameViewModel

    companion object {
        fun create(type: GameType, sessionId: Long?): ViewModelProvider.Factory {
            return viewModelFactory {
                when (type) {
                    is Free -> {
                        initializer(FreeGameViewModel, sessionId)
                    }
                    is SimpleOrdered -> {
                        initializer(SimpleOrderedGameViewModel, sessionId)
                    }
                    is Dice -> {
                        initializer(DiceGameViewModel, sessionId)
                    }
                    is Carcassonne -> {
                        initializer(CarcassonneGameViewModel, sessionId)
                    }
                    is Monopoly -> {
                        initializer(MonopolyGameViewModel, sessionId)
                    }
                }
            }
        }
    }
}

private fun InitializerViewModelFactoryBuilder.initializer(
    factory: GameViewModelFactory,
    sessionId: Long?
) {
    initializer<GameViewModel> {
        if (sessionId == null) {
            factory.createFrom(Store.currentGame as GameEnv)
        } else {
            factory.createFrom(sessionId)
        }
    }
}
