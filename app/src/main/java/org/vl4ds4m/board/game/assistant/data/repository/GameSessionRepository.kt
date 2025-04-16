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
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.Monopoly
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.game.data.GameSessionInfo
import org.vl4ds4m.board.game.assistant.game.data.MonopolyPlayerState
import org.vl4ds4m.board.game.assistant.game.data.Score
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

    suspend fun loadSession(id: String): GameSession? =
        sessionDao.findSession(id)?.gameSession()

    fun saveSession(session: GameSession, id: String) {
        coroutineScope.launch {
            GameSessionData(
                entity = session.asEntity(id),
                players = session.getPlayers(id),
                actions = session.actions.mapIndexed { index, action ->
                    action.asEntity(session.type, id, index)
                }
            ).let {
                sessionDao.insertSession(it)
            }
        }
    }
}

private fun GameSessionData.gameSession(): GameSession {
    val gameType = GameType.valueOf(entity.type)
    return GameSession(
        completed = entity.completed,
        type = gameType,
        name = entity.name,
        players = players.fromEntities(gameType),
        orderedPlayerIds = orderedPlayerIds,
        currentPlayerId = entity.currentPlayerId,
        nextNewPlayerId = entity.nextNewPlayerId,
        startTime = entity.startTime,
        stopTime = entity.stopTime,
        timeout = entity.timeout,
        secondsUntilEnd = entity.secondsUntilEnd,
        actions = actions.fromEntities(gameType),
        currentActionPosition = entity.currentActionPosition
    )
}

private fun List<PlayerEntity>.fromEntities(type: GameType): Map<Long, Player> = buildMap {
    this@fromEntities.forEach {
        val player = Player(
            netDevId = it.netDevId,
            name = it.name,
            active = it.active,
            state = when (type) {
                is Monopoly -> MonopolyPlayerState(
                    score = it.score,
                    position = it.position!!,
                    inPrison = it.inPrison!!
                )
                else -> Score(it.score)
            }
        )
        put(it.id, player)
    }
}

private val GameSessionData.orderedPlayerIds
    get() = players.sortedBy { it.order }.map { it.id }

private fun List<GameActionEntity>.fromEntities(gameType: GameType): List<GameAction> = sortedBy { it.position }
    .map {
        when (it.type) {
            GameAction.CHANGE_CURRENT_PLAYER -> CurrentPlayerChangeAction(
                oldPlayerId = it.oldPlayerId,
                newPlayerId = it.newPlayerId
            )
            GameAction.CHANGE_PLAYER_STATE -> {
                val (oldState, newState) = when (gameType) {
                    is Monopoly -> {
                        when {
                            it.oldPos != null -> MonopolyPlayerState(position = it.oldPos) to
                                MonopolyPlayerState(position = it.newPos!!)
                            it.inPrison != null -> MonopolyPlayerState(inPrison = !it.inPrison) to
                                MonopolyPlayerState(inPrison = it.inPrison)
                            else -> MonopolyPlayerState(score = it.oldScore ?: 0) to
                                MonopolyPlayerState(score = it.newScore ?: 0)
                        }
                    }
                    else -> Score(it.oldScore ?: 0) to Score(it.newScore ?: 0)
                }
                PlayerStateChangeAction(
                    playerId = it.playerId ?: -1,
                    oldState = oldState,
                    newState = newState
                )
            }
            else -> throw IllegalStateException("Unknown GameAction type: ${it.type}")
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
    currentActionPosition = currentActionPosition
)

private fun GameSession.getPlayers(sessionId: String): List<PlayerEntity> =
    orderedPlayerIds.mapIndexed { index, id ->
        val player = players[id]!!
        when (type) {
            is Monopoly -> {
                val state = player.state as MonopolyPlayerState
                createPlayerEntity(
                    sessionId, id, player, index,
                    state.position, state.inPrison
                )
            }
            else -> createPlayerEntity(sessionId, id, player, index)
        }
    }

private fun createPlayerEntity(
    sessionId: String, id: Long,
    player: Player, order: Int,
    position: Int? = null, inPrison: Boolean? = null
) = PlayerEntity(
    sessionId = sessionId,
    id = id,
    netDevId = player.netDevId,
    name = player.name,
    active = player.active,
    score = player.state.score,
    order = order,
    position = position,
    inPrison = inPrison
)

private fun GameAction.asEntity(gameType: GameType, sessionId: String, position: Int): GameActionEntity =
    when (this) {
        is CurrentPlayerChangeAction -> GameActionEntity(
            sessionId = sessionId,
            position = position,
            type = GameAction.CHANGE_CURRENT_PLAYER,
            playerId = null,
            oldScore = null,
            newScore = null,
            oldPlayerId = oldPlayerId,
            newPlayerId = newPlayerId,
            oldPos = null,
            newPos = null,
            inPrison = null
        )
        is PlayerStateChangeAction -> when {
            gameType is Monopoly && oldState.score == newState.score -> {
                val (oldState, newState) = oldState as MonopolyPlayerState to
                    newState as MonopolyPlayerState
                when {
                    oldState.position != newState.position -> GameActionEntity(
                        sessionId = sessionId,
                        position = position,
                        type = GameAction.CHANGE_PLAYER_STATE,
                        playerId = playerId,
                        oldScore = null,
                        newScore = null,
                        oldPlayerId = null,
                        newPlayerId = null,
                        oldPos = oldState.position,
                        newPos = newState.position,
                        inPrison = null
                    )
                    else -> GameActionEntity(
                        sessionId = sessionId,
                        position = position,
                        type = GameAction.CHANGE_PLAYER_STATE,
                        playerId = playerId,
                        oldScore = null,
                        newScore = null,
                        oldPlayerId = null,
                        newPlayerId = null,
                        oldPos = null,
                        newPos = null,
                        inPrison = newState.inPrison
                    )
                }
            }
            else -> GameActionEntity(
                sessionId = sessionId,
                position = position,
                type = GameAction.CHANGE_PLAYER_STATE,
                playerId = playerId,
                oldScore = oldState.score,
                newScore = newState.score,
                oldPlayerId = null,
                newPlayerId = null,
                oldPos = null,
                newPos = null,
                inPrison = null
            )
        }
    }
