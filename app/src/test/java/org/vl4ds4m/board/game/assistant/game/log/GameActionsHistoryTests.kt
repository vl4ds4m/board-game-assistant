package org.vl4ds4m.board.game.assistant.game.log

import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class GameActionsHistoryTests {
    private lateinit var history: GameActionsHistory

    @Before
    fun initHistory() {
        history = GameActionsHistory()
    }

    @Test
    fun setup() {
        val actions = List(4) { createGameAction(it) }
        history.setup(actions, 2)
        assertTrue(history.reverted.value)
        assertTrue(history.repeatable.value)
        assertEquals(actions[1], history.revert())
    }

    @Test
    fun moveCurrentAction() {
        val count = 5
        val actions = List(count) { createGameAction(it) }
        history.setup(actions, count)
        assertTrue(!history.repeatable.value)
        repeat(count) {
            val action = history.revert()
            assertEquals(actions[count - 1 - it], action)
        }
        assertTrue(!history.reverted.value)
        repeat(count - 1) {
            val action = history.repeat()
            assertEquals(actions[it], action)
        }
    }

    @Test
    fun addAction() {
        history.setup(List(3) { createGameAction(it) }, 1)
        val newAction = createGameAction(555)
        history += newAction
        assertEquals(2, history.actionsContainer.size)
        assertEquals(2, history.nextActionIndex)
        assertEquals(newAction, history.revert())
    }
}

private fun createGameAction(num: Int) = GameAction(
    type = "game_action_test",
    data = mapOf("name" to "$num")
)
