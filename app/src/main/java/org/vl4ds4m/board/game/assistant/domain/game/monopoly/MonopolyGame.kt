package org.vl4ds4m.board.game.assistant.domain.game.monopoly

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.vl4ds4m.board.game.assistant.data.GameSession
import org.vl4ds4m.board.game.assistant.domain.Player
import org.vl4ds4m.board.game.assistant.domain.game.Monopoly
import org.vl4ds4m.board.game.assistant.domain.game.env.BaseOrderedGameEnv
import org.vl4ds4m.board.game.assistant.domain.game.env.OrderedGameEnv
import org.vl4ds4m.board.game.assistant.domain.game.monopoly.entity.MonopolyEntity
import org.vl4ds4m.board.game.assistant.domain.game.monopoly.entity.Supplier
import org.vl4ds4m.board.game.assistant.util.updateMap

class MonopolyGame(
    private val gameEnv: OrderedGameEnv = BaseOrderedGameEnv(Monopoly)
) : OrderedGameEnv by gameEnv
{
    private val mEntityOwner: MutableStateFlow<Map<MonopolyEntity, Long>> =
        MutableStateFlow(mapOf())
    private val entityOwner: StateFlow<Map<MonopolyEntity, Long>> =
        mEntityOwner.asStateFlow()

    private val mPlayerState: MutableStateFlow<Map<Long, MonopolyPlayerState>> =
        MutableStateFlow(mapOf())
    private val playerState: StateFlow<Map<Long, MonopolyPlayerState>> =
        mPlayerState.asStateFlow()

    private var repeatCount: Int = 0

    private val mAfterStepField: MutableStateFlow<MonopolyField?> =
        MutableStateFlow(null)
    private val afterStepField: StateFlow<MonopolyField?> =
        mAfterStepField.asStateFlow()

    override fun saveIn(session: GameSession) {
        session.state = session.state.let {
            it as? MonopolyGameState ?: MonopolyGameState()
        }.also {
            it.entityOwner = this.entityOwner.value
            it.playerState = this.playerState.value
            it.repeatCount = this.repeatCount
            it.afterStepField = this.afterStepField.value
        }
        gameEnv.saveIn(session)
    }

    override fun loadFrom(session: GameSession) {
        gameEnv.loadFrom(session)
        session.state.let {
            it as? MonopolyGameState
        }?.let {
            this.mEntityOwner.value = it.entityOwner
            this.mPlayerState.value = it.playerState
            this.repeatCount = it.repeatCount
            this.mAfterStepField.value = it.afterStepField
        }
    }

    fun movePlayer(step1: Int, step2: Int) {
        if (afterStepField.value != null) return
        sequenceOf(step1, step2).forEach {
            if (it !in 1..6) return
        }
        val player = currentPlayer ?: return
        val id = player.id
        val state = playerState.value[id] ?: return
        val equalSteps = step1 == step2
        if (state.inPrison) {
            if (equalSteps) state.inPrison = false
            else return
        } else if (equalSteps) {
            repeatCount++
            if (repeatCount == 3) {
                moveToPrison(state)
                return
            }
        }
        val step = step1 + step2
        val pos = (state.position + step).let {
            if (it > MonopolyField.COUNT) {
                val score = player.score + Ahead.MONEY
                changePlayerScore(player, score)
                it % MonopolyField.COUNT
            } else it
        }
        state.position = pos
        val field = MonopolyFields[pos]!!
        mAfterStepField.value = field
        if (field is GoToPrison) {
            moveToPrison(state)
        }
    }

    private fun moveToPrison(state: MonopolyPlayerState) {
        state.position = GoToPrison.POSITION
        state.inPrison = true
        nextOrder()
    }

    override fun nextOrder() {
        repeatCount = 0
        mAfterStepField.value = null
        gameEnv.nextOrder()
    }

    fun buyCurrentEntity() {
        val entity = afterStepField.value as? MonopolyEntity ?: return
        val player = currentPlayer ?: return
        buyEntity(player, entity, entity.cost)
    }

    fun buyEntity(player: Player, entity: MonopolyEntity, cost: Int) {
        if (cost <= 0) return
        if (entity in entityOwner.value) return
        if (player !in players.value) return
        val state = playerState.value[player.id] ?: return
        if (player.score >= cost) {
            changePlayerScore(player, player.score - cost)
            state.entities += entity
            mEntityOwner.updateMap {
                put(entity, player.id)
            }
        }
    }

    fun payRent(factor: Int? = null) {
        val payer = currentPlayer ?: return
        val entity = afterStepField.value as? MonopolyEntity ?: return
        if (entity is Supplier) {
            factor ?: return
            if (factor !in 2..12) return
        }
        val ownerId = entityOwner.value[entity] ?: return
        if (ownerId == payer.id) return
        val payee = players.value.find { it.id == ownerId } ?: return
        val cost = when (entity) {
            is Supplier -> entity.rent * factor!!
            else -> entity.rent
        }
        if (payer.score >= cost) {
            changePlayerScore(payer, payer.score - cost)
            changePlayerScore(payee, payee.score + cost)
        }
    }


}


