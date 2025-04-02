package org.vl4ds4m.board.game.assistant.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.vl4ds4m.board.game.assistant.data.dao.GameSessionDao
import org.vl4ds4m.board.game.assistant.data.entity.GameActionEntity
import org.vl4ds4m.board.game.assistant.data.entity.GameSessionData
import org.vl4ds4m.board.game.assistant.data.entity.GameSessionEntity
import org.vl4ds4m.board.game.assistant.data.entity.PlayerEntity
import org.vl4ds4m.board.game.assistant.game.Carcassonne
import org.vl4ds4m.board.game.assistant.game.Dice
import org.vl4ds4m.board.game.assistant.game.Free
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.Monopoly
import org.vl4ds4m.board.game.assistant.game.OrderedGameType
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.SimpleOrdered
import org.vl4ds4m.board.game.assistant.game.data.CarcassonneGameState
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.game.data.GameSessionInfo
import org.vl4ds4m.board.game.assistant.game.data.GameState
import org.vl4ds4m.board.game.assistant.game.data.MonopolyGameState
import org.vl4ds4m.board.game.assistant.game.data.OrderedGameState
import org.vl4ds4m.board.game.assistant.game.data.Score
import org.vl4ds4m.board.game.assistant.game.data.SimpleOrderedGameState
import org.vl4ds4m.board.game.assistant.game.log.CurrentPlayerChangeAction
import org.vl4ds4m.board.game.assistant.game.log.GameAction
import org.vl4ds4m.board.game.assistant.game.log.PlayerStateChangeAction

class GameSessionRepository(
    private val sessionDao: GameSessionDao,
    private val coroutineScope: CoroutineScope
) {
    fun getAllSessions(): Flow<List<GameSessionInfo>> = sessionDao.getAllSessions()
        .map { sessions ->
            sessions.map {
                GameSessionInfo(
                    id = it.id,
                    completed = it.completed,
                    type = GameType.valueOf(it.type),
                    name = it.name,
                    startTime = it.startTime,
                    stopTime = it.stopTime
                )
            }
        }

    suspend fun loadSession(id: String): GameSession? = sessionDao.findSession(id)?.fromData()

    fun saveSession(session: GameSession, id: String) = coroutineScope.launch {
        GameSessionData(
            entity = session.asEntity(id),
            players = session.getPlayers(id),
            actions = session.actions.mapIndexed { index, action ->
                action.asEntity(id, index)
            }
        ).let {
            sessionDao.insertSession(it)
        }
    }
}

private fun GameSessionData.fromData() = GameSession(
    completed = entity.completed,
    type = GameType.valueOf(entity.type),
    name = entity.name,
    players = players.fromEntities(),
    currentPlayerId = entity.currentPlayerId,
    nextNewPlayerId = entity.nextNewPlayerId,
    startTime = entity.startTime,
    stopTime = entity.stopTime,
    timeout = entity.timeout,
    secondsUntilEnd = entity.secondsUntilEnd,
    actions = actions.fromEntities(),
    currentActionPosition = entity.currentActionPosition,
    additional = gameState
)

private fun List<PlayerEntity>.fromEntities(): Map<Long, Player> = buildMap {
    this@fromEntities.forEach {
        val player = Player(
            name = it.name,
            active = it.active,
            state = Score(it.score)
        )
        put(it.id, player)
    }
}

private fun List<GameActionEntity>.fromEntities(): List<GameAction> = sortedBy { it.position }
    .map {
        when (it.type) {
            GameAction.CHANGE_CURRENT_PLAYER -> CurrentPlayerChangeAction(
                oldPlayerId = it.oldPlayerId,
                newPlayerId = it.newPlayerId
            )
            GameAction.CHANGE_PLAYER_STATE -> PlayerStateChangeAction(
                playerId = it.playerId ?: -1,
                oldState = Score(it.oldScore ?: 0),
                newState = Score(it.newScore ?: 0)
            )
            else -> throw IllegalStateException("Unknown GameAction type: ${it.type}")
        }
    }

private val GameSessionData.gameState: GameState?
    get() {
        val orderedGameType = GameType.valueOf(entity.type)
            .let {
                when (it) {
                    is Free -> return null
                    is OrderedGameType -> it
                }
            }
        val orderedPlayerIds = players.sortedBy { it.order }
            .map { it.id }
        return when (orderedGameType) {
            is SimpleOrdered, Dice -> SimpleOrderedGameState(orderedPlayerIds)
            is Carcassonne -> CarcassonneGameState(orderedPlayerIds, entity.finalStage!!)
            is Monopoly -> MonopolyGameState(orderedPlayerIds)
        }
    }

private fun GameSession.asEntity(id: String) = GameSessionEntity(
    id = id,
    completed = completed,
    type = type.title,
    name = name,
    currentPlayerId = currentPlayerId,
    nextNewPlayerId = nextNewPlayerId,
    startTime = startTime,
    stopTime = stopTime,
    timeout = timeout,
    secondsUntilEnd = secondsUntilEnd,
    currentActionPosition = currentActionPosition,
    finalStage = additional?.takeIf { type is Carcassonne }
        ?.let { it as CarcassonneGameState }
        ?.finalStage
)

private fun GameSession.getPlayers(sessionId: String): List<PlayerEntity> =
    when (type) {
        is Free -> players.toList()
            .sortedBy { (id, _) -> id }
            .mapIndexed { index, (id, player) ->
                PlayerEntity(
                    sessionId = sessionId,
                    id = id,
                    name = player.name,
                    active = player.active,
                    score = player.state.score,
                    order = index
                )
            }
        is OrderedGameType -> additional.let { it as OrderedGameState }
            .orderedPlayerIds.mapIndexed { index, id ->
                val player = players[id]!!
                PlayerEntity(
                    sessionId = sessionId,
                    id = id,
                    name = player.name,
                    active = player.active,
                    score = player.state.score,
                    order = index
                )
            }
    }

private fun GameAction.asEntity(sessionId: String, position: Int): GameActionEntity =
    when (this) {
        is CurrentPlayerChangeAction -> GameActionEntity(
            sessionId = sessionId,
            position = position,
            type = GameAction.CHANGE_CURRENT_PLAYER,
            playerId = null,
            oldScore = null,
            newScore = null,
            oldPlayerId = oldPlayerId,
            newPlayerId = newPlayerId
        )
        is PlayerStateChangeAction -> GameActionEntity(
            sessionId = sessionId,
            position = position,
            type = GameAction.CHANGE_PLAYER_STATE,
            playerId = playerId,
            oldScore = oldState.score,
            newScore = newState.score,
            oldPlayerId = null,
            newPlayerId = null
        )
    }
