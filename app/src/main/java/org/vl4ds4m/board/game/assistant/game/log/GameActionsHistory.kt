package org.vl4ds4m.board.game.assistant.game.log

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.vl4ds4m.board.game.assistant.game.Actions

class GameActionsHistory {
    private var mActionsContainer = mutableListOf<GameAction>()
    val actionsContainer: Actions
        get() = mActionsContainer

    private var history: MutableListIterator<GameAction> = mActionsContainer.listIterator()

    val nextActionIndex: Int
        get() = history.nextIndex()

    private val mActions = MutableStateFlow<Actions>(listOf())
    val actions: StateFlow<Actions> = mActions.asStateFlow()

    private val mReverted = MutableStateFlow(false)
    val reverted: StateFlow<Boolean> = mReverted.asStateFlow()

    private val mRepeatable = MutableStateFlow(false)
    val repeatable: StateFlow<Boolean> = mRepeatable.asStateFlow()
    
    fun setup(actions: Actions, nextActionIndex: Int) {
        val index = when (nextActionIndex) {
            in 0 .. actions.size -> nextActionIndex
            else -> {
                Log.w(
                    TAG,
                    "Invalid current position of GameActionsHistory" +
                        " during initialization. The position is set on the end"
                )
                actions.size
            }
        }
        mActionsContainer = actions.toMutableList()
        history = mActionsContainer.listIterator(index)
        updateActions()
    }

    private fun updateActions() {
        mActions.value = mActionsContainer.asSequence()
            .take(nextActionIndex)
            .toList()
        mReverted.value = history.hasPrevious()
        mRepeatable.value = history.hasNext()
    }

    operator fun plusAssign(action: GameAction) {
        while (history.hasNext()) {
            history.next()
            history.remove()
        }
        history.add(action)
        updateActions()
    }

    fun revert(): GameAction? {
        if (!history.hasPrevious()) {
            Log.w(
                TAG,
                "Can't revert action. There is no previous one"
            )
            return null
        }
        val action = history.previous()
        updateActions()
        return action
    }

    fun repeat(): GameAction? {
        if (!history.hasNext()) {
            Log.w(
                TAG,
                "Can't repeat action. There is no next one"
            )
            return null
        }
        val action = history.next()
        updateActions()
        return action
    }
}

private const val TAG = "GameActionsHistory"
