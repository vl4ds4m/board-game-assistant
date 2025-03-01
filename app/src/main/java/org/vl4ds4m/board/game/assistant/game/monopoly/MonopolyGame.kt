package org.vl4ds4m.board.game.assistant.game.monopoly

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.vl4ds4m.board.game.assistant.game.Monopoly
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.data.GameState
import org.vl4ds4m.board.game.assistant.game.data.OrderedGameState
import org.vl4ds4m.board.game.assistant.game.env.OrderedGameEnv
import org.vl4ds4m.board.game.assistant.game.monopoly.entity.MonopolyEntity
import org.vl4ds4m.board.game.assistant.game.monopoly.entity.Supplier
import org.vl4ds4m.board.game.assistant.util.updateMap

@Suppress("unused")
class MonopolyGame : OrderedGameEnv(Monopoly) {
    private val mEntityOwner: MutableStateFlow<Map<MonopolyEntity, Long>> =
        MutableStateFlow(mapOf())
    private val entityOwner: StateFlow<Map<MonopolyEntity, Long>> =
        mEntityOwner.asStateFlow()

    private var repeatCount: Int = 0

    private val mAfterStepField: MutableStateFlow<MonopolyField?> =
        MutableStateFlow(null)
    private val afterStepField: StateFlow<MonopolyField?> =
        mAfterStepField.asStateFlow()

    override fun addPlayer(name: String) {
        addPlayer(name, MonopolyPlayerState())
    }

    override fun restoreAdditionalState(state: GameState?) {
        super.restoreAdditionalState(state)
        state.let {
            it as? MonopolyGameState
        }?.let {
            this.mEntityOwner.value = it.entityOwner
            this.repeatCount = it.repeatCount
            this.mAfterStepField.value = it.afterStepField
        }
    }

    override val additionalState: OrderedGameState
        get() = MonopolyGameState(
            super.additionalState,
            entityOwner.value,
            repeatCount,
            afterStepField.value
        )

    fun movePlayer(step1: Int, step2: Int) {
        if (afterStepField.value != null) return
        sequenceOf(step1, step2).forEach {
            if (it !in 1..6) return
        }
        val id = currentPlayerId.value ?: return
        val state = players.value[id]?.monopolyState ?: return
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
                state.score += Ahead.MONEY
                it % MonopolyField.COUNT
            } else {
                it
            }
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
        changeCurrentPlayerId()
    }

    override fun changeCurrentPlayerId() {
        repeatCount = 0
        mAfterStepField.value = null
        changeCurrentPlayerId()
    }

    fun buyCurrentEntity() {
        val entity = afterStepField.value as? MonopolyEntity ?: return
        val id = currentPlayerId.value ?: return
        buyEntity(id, entity, entity.cost)
    }

    private fun buyEntity(playerId: Long, entity: MonopolyEntity, cost: Int) {
        if (cost <= 0) return
        if (entity in entityOwner.value) return
        if (playerId !in players.value) return
        val state = players.value[playerId]?.monopolyState ?: return
        if (state.score >= cost) {
            state.score -= cost
            state.entities += entity
            mEntityOwner.updateMap {
                put(entity, playerId)
            }
        }
    }

    fun payRent(factor: Int? = null) {
        val payerId = currentPlayerId.value ?: return
        val entity = afterStepField.value as? MonopolyEntity ?: return
        if (entity is Supplier) {
            factor ?: return
            if (factor !in 2..12) return
        }
        val ownerId = entityOwner.value[entity] ?: return
        if (ownerId == payerId) return
        val payeeState = players.value[ownerId]?.monopolyState ?: return
        val cost = when (entity) {
            is Supplier -> entity.rent * factor!!
            else -> entity.rent
        }
        val payerState = players.value[payerId]?.monopolyState ?: return
        if (payerState.score >= cost) {
            payerState.score -= cost
            payeeState.score += cost
        }
    }
}

private val Player.monopolyState: MonopolyPlayerState?
    get() = state as? MonopolyPlayerState
