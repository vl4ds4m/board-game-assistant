package org.vl4ds4m.board.game.assistant.data.repository

import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.game.data.GameSessionInfo
import org.vl4ds4m.board.game.assistant.data.dao.SessionDao
import org.vl4ds4m.board.game.assistant.data.entity.GameActionEntity
import org.vl4ds4m.board.game.assistant.data.entity.PlayerEntity
import org.vl4ds4m.board.game.assistant.data.entity.GameSessionEntity
import org.vl4ds4m.board.game.assistant.game.Carcassonne
import org.vl4ds4m.board.game.assistant.game.Dice
import org.vl4ds4m.board.game.assistant.game.Free
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.Monopoly
import org.vl4ds4m.board.game.assistant.game.OrderedGameType
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.SimpleOrdered
import org.vl4ds4m.board.game.assistant.game.carcassonne.CarcassonneGameState
import org.vl4ds4m.board.game.assistant.game.log.CurrentPlayerChangeAction
import org.vl4ds4m.board.game.assistant.game.log.GameAction
import org.vl4ds4m.board.game.assistant.game.log.PlayerStateChangeAction
import org.vl4ds4m.board.game.assistant.game.monopoly.MonopolyGameState
import org.vl4ds4m.board.game.assistant.game.data.GameState
import org.vl4ds4m.board.game.assistant.game.data.OrderedGameState
import org.vl4ds4m.board.game.assistant.game.data.Score

class SessionRepository(private val dao: SessionDao) {
    fun getAllSessions(): List<GameSessionInfo> {
        return dao.getAllSessions()
            .map {
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

    fun loadSession(id: Long): GameSession? {
        return dao.findSession(id)?.let {
            GameSession(
                completed = it.completed,
                type = GameType.valueOf(it.type),
                name = it.name,
                players = getPlayers(it.players),
                currentPlayerId = it.currentPlayerId,
                nextNewPlayerId = it.nextNewPlayerId,
                startTime = it.startTime,
                timeout = it.timeout,
                secondsToEnd = it.secondsUntilEnd,
                actions = getActions(it.actions),
                currentActionPosition = it.currentActionPosition,
                state = getGameState(it)
            )
        }
    }

    fun saveSession(id: Long?, session: GameSession) {
        val entity = GameSessionEntity(
            id = id,
            completed = session.completed,
            type = session.type!!.title, // TODO remove nullability
            name = session.name,
            players = getPlayers(session, id),
            currentPlayerId = session.currentPlayerId,
            nextNewPlayerId = session.nextNewPlayerId!!, // TODO remove nullability
            startTime = session.startTime!!, // TODO remove nullability
            stopTime = session.startTime!!, // TODO Add stop time
            timeout = session.timeout,
            secondsUntilEnd = session.secondsToEnd, // TODO rename
            actions = session.actions.mapIndexed { index, action ->
                getAction(action, id, index)
            },
            currentActionPosition = session.currentActionPosition,
            finalStage = session.state
                ?.let { it as? CarcassonneGameState }
                ?.onFinal
        )
        if (id == null) dao.insertSession(entity)
        else dao.updateSession(entity)
    }
}

private fun getPlayers(players: List<PlayerEntity>): Map<Long, Player> {
    return buildMap {
        players.forEach {
            val player = Player(
                name = it.name,
                active = it.active,
                state = Score(it.score)
            )
            put(it.id, player)
        }
    }
}

private fun getPlayers(session: GameSession, sessionId: Long?): List<PlayerEntity> {
    return when (session.type) {
        is Free, null -> session.players
            .toList()
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

        is OrderedGameType -> session.state
            .let { it as OrderedGameState }
            .orderedPlayerIds.mapIndexed { index, id ->
                val player = session.players[id]!!
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
}

private fun getActions(actions: List<GameActionEntity>): List<GameAction> {
    return actions.sortedBy { it.position }
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
}

private fun getAction(
    action: GameAction,
    sessionId: Long?,
    position: Int
): GameActionEntity {
    return when (action) {
        is CurrentPlayerChangeAction -> GameActionEntity(
            sessionId = sessionId,
            position = position,
            type = GameAction.CHANGE_CURRENT_PLAYER,
            playerId = null,
            oldScore = null,
            newScore = null,
            oldPlayerId = action.oldPlayerId,
            newPlayerId = action.newPlayerId
        )

        is PlayerStateChangeAction -> GameActionEntity(
            sessionId = sessionId,
            position = position,
            type = GameAction.CHANGE_PLAYER_STATE,
            playerId = action.playerId,
            oldScore = action.oldState.score,
            newScore = action.newState.score,
            oldPlayerId = null,
            newPlayerId = null
        )
    }
}

private fun getGameState(session: GameSessionEntity): GameState? {
    val orderedGameType = GameType.valueOf(session.type)
        .let {
            when (it) {
                is Free -> return null
                is OrderedGameType -> it
            }
        }
    val orderedPlayerIds = session.players
        .sortedBy { it.order }
        .map { it.id }
    return when (orderedGameType) {
        is SimpleOrdered, Dice -> OrderedGameState(orderedPlayerIds)
        is Carcassonne -> CarcassonneGameState(orderedPlayerIds, session.finalStage!!)
        is Monopoly -> MonopolyGameState(orderedPlayerIds)
    }
}
