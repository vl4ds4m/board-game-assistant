package org.vl4ds4m.board.game.assistant.data.repository

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.vl4ds4m.board.game.assistant.data.User
import org.vl4ds4m.board.game.assistant.data.dao.GameSessionDao
import org.vl4ds4m.board.game.assistant.data.entity.GameActionEntity
import org.vl4ds4m.board.game.assistant.data.entity.GameSessionData
import org.vl4ds4m.board.game.assistant.data.entity.GameSessionEntity
import org.vl4ds4m.board.game.assistant.data.entity.PlayerData
import org.vl4ds4m.board.game.assistant.data.entity.PlayerEntity
import org.vl4ds4m.board.game.assistant.data.entity.UserEntity
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.OrderedPlayers
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.Users
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.game.data.GameSessionInfo
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
            Log.d(TAG, "Save GameSession[id = $id, " +
                    "type = ${session.type}, name = ${session.name}]")
        }
    }

    fun removeSession(id: String) {
        val pk = GameSessionEntity.PK(id)
        coroutineScope.launch {
            sessionDao.removeSession(pk)
        }
    }
}

private val GameSessionData.gameSession
    get() = GameSession(
        completed = entity.completed,
        type = GameType.valueOf(entity.type),
        name = entity.name,
        players = players.gamePlayers,
        users = players.gameUsers,
        currentPid = entity.currentPid,
        nextNewPid = entity.nextNewPid,
        startTime = entity.startTime,
        stopTime = entity.stopTime,
        duration = entity.duration,
        timeout = entity.timeout,
        secondsUntilEnd = entity.secondsUntilEnd,
        actions = actions.gameActions,
        currentActionPosition = entity.currentActionPosition
    )

private val List<PlayerData>.gamePlayers: OrderedPlayers
    get() = map { data ->
        data.player.let { player ->
            player.id to Player(
                name = player.name,
                presence = player.presence,
                state = PlayerState.fromJson(player.state)
            )
        }
    }

private val List<PlayerData>.gameUsers: Users
    get() = mapNotNull { data ->
        data.user?.let { data.player.id to it }
    }.associate { (id, user) ->
        id to User(
            netDevId = user.id,
            name = user.name,
            self = false
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
    currentPid = currentPid,
    nextNewPid = nextNewPid,
    startTime = startTime,
    stopTime = stopTime,
    duration = duration,
    timeout = timeout,
    secondsUntilEnd = secondsUntilEnd,
    currentActionPosition = currentActionPosition
)

private fun GameSession.getPlayers(sessionId: String): List<PlayerData> =
    players.mapIndexed { index, (id, p) ->
        val user = users[id]?.let {
            UserEntity(
                id = it.netDevId,
                name = it.name,
            )
        }
        val player = PlayerEntity(
            sessionId = sessionId,
            id = id,
            userId = user?.id,
            name = p.name,
            presence = p.presence,
            state = p.state.toJson(),
            order = index
        )
        PlayerData(player = player, user = user)
    }


private fun GameSession.getActions(id: String): List<GameActionEntity> =
    actions.mapIndexed { index, action ->
        GameActionEntity(
            sessionId = id,
            position = index,
            action = action.toJson()
        )
    }

private const val TAG = "GameSessionRepository"
