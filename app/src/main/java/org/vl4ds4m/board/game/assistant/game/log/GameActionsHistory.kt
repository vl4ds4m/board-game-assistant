package org.vl4ds4m.board.game.assistant.game.log

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.vl4ds4m.board.game.assistant.game.Actions
import org.vl4ds4m.board.game.assistant.game.env.GameEnv

class GameActionsHistory {
    private var mContainer: MutableList<GameAction> = mutableListOf()
    val container: Actions get() = mContainer

    private var history: MutableListIterator<GameAction> = mContainer.listIterator()
    val currentPosition: Int get() = history.nextIndex()

    private val mActions: MutableStateFlow<Actions> = MutableStateFlow(listOf())
    val actions: StateFlow<Actions> = mActions.asStateFlow()

    private val mReverted: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val reverted: StateFlow<Boolean> = mReverted.asStateFlow()

    private val mRepeatable: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val repeatable: StateFlow<Boolean> = mRepeatable.asStateFlow()
    
    fun setup(container: Actions, currentPosition: Int? = null) {
        val index = when (currentPosition) {
            null -> container.size
            !in 0..container.size -> {
                Log.w(
                    GameEnv.TAG,
                    "Invalid current position of GameActionsHistory" +
                            " during initialization. The position is set on the end"
                )
                container.size
            }
            else -> currentPosition
        }
        mContainer = container.toMutableList()
        history = mContainer.listIterator(index)
        updateActions()
        mReverted.value = history.hasPrevious()
        mRepeatable.value = history.hasNext()
    }

    private fun updateActions() {
        mActions.value = mContainer.asSequence()
            .take(currentPosition)
            .toList()
    }

    operator fun plusAssign(action: GameAction) {
        while (history.hasNext()) {
            history.next()
            history.remove()
        }
        history.add(action)
        updateActions()
        mReverted.value = true
        mRepeatable.value = false
    }

    fun revert(): GameAction? {
        if (!history.hasPrevious()) {
            Log.w(
                GameEnv.TAG,
                "Can't revert action. There is no previous one"
            )
            return null
        }
        val action = history.previous()
        updateActions()
        mReverted.value = history.hasPrevious()
        mRepeatable.value = true
        return action
    }

    fun repeat(): GameAction? {
        if (!history.hasNext()) {
            Log.w(
                GameEnv.TAG,
                "Can't repeat action. There is no next one"
            )
            return null
        }
        val action = history.next()
        updateActions()
        mReverted.value = true
        mRepeatable.value = history.hasNext()
        return action
    }
}
