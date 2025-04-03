package org.vl4ds4m.board.game.assistant.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = PlayerEntity.TABLE_NAME,
    primaryKeys = [PlayerEntity.SESSION_ID, PlayerEntity.ID],
    foreignKeys = [
        ForeignKey(
            entity = GameSessionEntity::class,
            parentColumns = [GameSessionEntity.ID],
            childColumns = [PlayerEntity.SESSION_ID],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )
    ]
)
data class PlayerEntity(
    @ColumnInfo(name = SESSION_ID)
    val sessionId: String,

    @ColumnInfo(name = ID)
    val id: Long,

    @ColumnInfo(name = NET_DEV_ID)
    val netDevId: String?,

    @ColumnInfo(name = NAME)
    val name: String,

    @ColumnInfo(name = ACTIVE)
    val active: Boolean,

    @ColumnInfo(name = SCORE)
    val score: Int,

    @ColumnInfo(name = ORDER)
    val order: Int // Ordered game property
) {
    companion object {
        const val TABLE_NAME = "players"
        const val SESSION_ID = "session_id"
        const val ID = "player_id"
        const val NET_DEV_ID = "net_dev_id"
        const val NAME = "nickname"
        const val ACTIVE = "active"
        const val SCORE = "score"
        const val ORDER = "move_order"
    }
}
