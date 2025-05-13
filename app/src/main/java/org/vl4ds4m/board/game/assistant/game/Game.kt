package org.vl4ds4m.board.game.assistant.game

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.vl4ds4m.board.game.assistant.data.User
import org.vl4ds4m.board.game.assistant.game.log.GameAction
import org.vl4ds4m.board.game.assistant.game.data.PlayerState

typealias PID = Int // Player identifier
typealias Players = Map<PID, Player>
typealias OrderedPlayers = List<Pair<PID, Player>>
typealias Users = Map<PID, User>
typealias Actions = List<GameAction>

/**
 * Base interface for the game abstraction.
 */
interface Game {
    /**
     * Type of the game.
     */
    val type: GameType

    /**
     * Mutable state flow of the game name.
     */
    val name: MutableStateFlow<String>

    /**
     * State flow of the game players.
     */
    val players: StateFlow<Players>

    /**
     * State flow of the players in some order.
     */
    val orderedPlayers: StateFlow<OrderedPlayers>

    /**
     * State flow of users, joined to the game.
     */
    val users: StateFlow<Users>

    /**
     * State flow of the current player id.
     */
    val currentPid: StateFlow<PID?>

    /**
     * Change current player with id.
     * @param id Player id of the next current player.
     */
    fun changeCurrentPid(id: PID)

    /**
     * Current player id and [Player]
     */
    val currentPlayer: Pair<PID, Player>?
        get() = currentPid.value?.let { id ->
            players.value[id]?.let { player -> id to player }
        }

    /**
     * Add a new player to the game.
     * @param user Bound user to new player or `null`.
     * @param name Player name
     */
    fun addPlayer(user: User?, name: String): PID

    /**
     * Remove player from the game.
     * @param id Player id to remove
     */
    fun removePlayer(id: PID)

    /**
     * Bind a player to a user.
     * @param id Player id for binding
     * @param user User for binding
     */
    fun bindPlayer(id: PID, user: User)

    /**
     * Unbind player if it's bound.
     * @param id Player id
     */
    fun unbindPlayer(id: PID)

    /**
     * Rename player.
     * @param id Player id
     * @param name New player name
     */
    fun renamePlayer(id: PID, name: String)

    /**
     * Freeze player in the game if it's active.
     * @param id Player id
     */
    fun freezePlayer(id: PID)

    /**
     * Unfreeze player in the game if it's frozen.
     * @param id Player id
     */
    fun unfreezePlayer(id: PID)

    /**
     * Change player game state.
     * @param id Player id
     * @param state New player game state
     */
    fun changePlayerState(id: PID, state: PlayerState)

    /**
     * Mutable state flow of whether the game will be stopped by timeout.
     */
    val timeout: MutableStateFlow<Boolean>

    /**
     * State flow of time (in seconds) when the game will be stopped if timeout is enabled.
     * @see timeout
     */
    val secondsToEnd: StateFlow<Int>

    /**
     * Change time (in seconds) when the game will be stopped by timeout.
     * @see timeout
     */
    fun changeSecondsToEnd(seconds: Int)

    /**
     * State flow of whether the game is initialized.
     */
    val initialized: StateFlow<Boolean>

    /**
     * State flow of whether the game is completed.
     */
    val completed: StateFlow<Boolean>

    /**
     * Initialize the game.
     * @see initialized
     */
    fun initialize()

    /**
     * Start the game.
     */
    fun start()

    /**
     * Stop the game.
     */
    fun stop()

    /**
     * Complete the game.
     * @see completed
     */
    fun complete()

    /**
     * Return the game if it was completed.
     * @see complete
     */
    fun returnGame()

    /**
     * State flow of whether current game action can be reverted.
     */
    val reverted: StateFlow<Boolean>

    /**
     * State flow of whether current game action can be repeated.
     */
    val repeatable: StateFlow<Boolean>

    /**
     * State flow of game actions.
     */
    val actions: StateFlow<Actions>

    /**
     * Revert the current game action.
     * @see reverted
     */
    fun revert()

    /**
     * Repeat the current game action.
     * @see repeatable
     */
    fun repeat()

    companion object {
        /**
         * Get a list of player IDs with players, ordered by list of IDs.
         * @param ids List of IDs, used to get sorted list of IDs and players
         * @param players Players for the result list
         * @return List of IDs and players
         */
        fun getOrderedPlayers(ids: List<PID>, players: Players): OrderedPlayers =
            if (ids.isEmpty()) players.toList()
            else ids.mapNotNull { id ->
                players[id]?.let { p -> id to p }
            }
    }
}
