package org.vl4ds4m.board.game.assistant.game

import kotlinx.coroutines.flow.StateFlow

/**
 * Abstraction for the game, where players have an order how they should make actions.
 */
interface OrderedGame : Game {
    /**
     * State flow of the id of the next player.
     */
    val nextPid: StateFlow<PID?>

    /**
     * Select the next player.
     * @see nextPid
     */
    fun changeCurrentPid()

    /**
     * Change the order of the player.
     * @param id Player id
     * @param order New position of the player in the list of order.
     */
    fun changePlayerOrder(id: PID, order: Int)
}
