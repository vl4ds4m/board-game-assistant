package org.vl4ds4m.board.game.assistant.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class PlayerData(
    @Embedded
    val player: PlayerEntity,

    @Relation(
        parentColumn = PlayerEntity.USER_ID,
        entityColumn = UserEntity.ID
    )
    val user: UserEntity?
)
