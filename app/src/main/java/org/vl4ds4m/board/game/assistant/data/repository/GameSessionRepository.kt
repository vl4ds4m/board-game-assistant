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
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.game.data.GameSessionInfo
import org.vl4ds4m.board.game.assistant.game.data.OrderedPlayers
import org.vl4ds4m.board.game.assistant.game.data.PlayerState
import org.vl4ds4m.board.game.assistant.game.log.GameAction

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
        sessionDao.findSession(id)?.gameSession

    fun saveSession(session: GameSession, id: String) {
        coroutineScope.launch {
            GameSessionData(
                entity = session.asEntity(id),
                players = session.getPlayers(id),
                actions = session.getActions(id)
            ).let {
                sessionDao.insertSession(it)
            }
        }
    }
}

private val GameSessionData.gameSession
    get() = GameSession(
        completed = entity.completed,
        type = GameType.valueOf(entity.type),
        name = entity.name,
        players = players.gamePlayers,
        currentPlayerId = entity.currentPlayerId,
        nextNewPlayerId = entity.nextNewPlayerId,
        startTime = entity.startTime,
        stopTime = entity.stopTime,
        duration = entity.duration,
        timeout = entity.timeout,
        secondsUntilEnd = entity.secondsUntilEnd,
        actions = actions.gameActions,
        currentActionPosition = entity.currentActionPosition
    )

private val List<PlayerEntity>.gamePlayers: OrderedPlayers
    get() = map {
        it.id to Player(
            netDevId = it.netDevId,
            name = it.name,
            active = it.active,
            state = PlayerState.fromJson(it.state)
        )
    }

private val List<GameActionEntity>.gameActions: List<GameAction>
    get() = sortedBy { it.position }
        .map { GameAction.fromJson(it.action) }

private fun GameSession.asEntity(id: String) = GameSessionEntity(
    id = id,
    completed = completed,
    type = type.title,
    name = name,
    currentPlayerId = currentPlayerId,
    nextNewPlayerId = nextNewPlayerId,
    startTime = startTime,
    stopTime = stopTime,
    duration = duration,
    timeout = timeout,
    secondsUntilEnd = secondsUntilEnd,
    currentActionPosition = currentActionPosition
)

private fun GameSession.getPlayers(sessionId: String): List<PlayerEntity> =
    players.mapIndexed { index, (id, player) ->
        PlayerEntity(
            sessionId = sessionId,
            id = id,
            netDevId = player.netDevId,
            name = player.name,
            active = player.active,
            state = player.state.toJson(),
            order = index
        )
    }


private fun GameSession.getActions(id: String): List<GameActionEntity> =
    actions.mapIndexed { index, action ->
        GameActionEntity(
            sessionId = id,
            position = index,
            action = action.toJson()
        )
    }
