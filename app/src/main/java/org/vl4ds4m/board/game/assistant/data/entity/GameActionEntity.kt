package org.vl4ds4m.board.game.assistant.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = GameActionEntity.TABLE_NAME,
    primaryKeys = [GameActionEntity.SESSION_ID, GameActionEntity.POSITION],
    foreignKeys = [
        ForeignKey(
            entity = GameSessionEntity::class,
            parentColumns = [GameSessionEntity.ID],
            childColumns = [GameActionEntity.SESSION_ID],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )
    ]
)
data class GameActionEntity(
    @ColumnInfo(name = SESSION_ID)
    val sessionId: String,

    @ColumnInfo(name = POSITION)
    val position: Int,

    @ColumnInfo(name = ACTION)
    val action: String
) {
    companion object {
        const val TABLE_NAME = "actions"
        const val SESSION_ID = "session_id"
        const val POSITION = "position"
        const val ACTION = "action_content"
    }
}
