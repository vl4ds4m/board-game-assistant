package org.vl4ds4m.board.game.assistant.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class GameSessionData(
    @Embedded
    val entity: GameSessionEntity,

    @Relation(
        entity = PlayerEntity::class,
        parentColumn = GameSessionEntity.ID,
        entityColumn = PlayerEntity.SESSION_ID
    )
    val players: List<PlayerData>,

    @Relation(
        parentColumn = GameSessionEntity.ID,
        entityColumn = GameActionEntity.SESSION_ID
    )
    val actions: List<GameActionEntity>
)
