package org.vl4ds4m.board.game.assistant.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import org.vl4ds4m.board.game.assistant.game.PID
import org.vl4ds4m.board.game.assistant.game.Player

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
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = [UserEntity.ID],
            childColumns = [PlayerEntity.USER_ID],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class PlayerEntity(
    @ColumnInfo(name = SESSION_ID)
    val sessionId: String,

    @ColumnInfo(name = ID)
    val id: PID,

    @ColumnInfo(name = USER_ID)
    val userId: String?,

    @ColumnInfo(name = NAME)
    val name: String,

    @ColumnInfo(name = PRESENCE)
    val presence: Player.Presence,

    @ColumnInfo(name = STATE)
    val state: String,

    @ColumnInfo(name = ORDER)
    val order: Int
) {
    companion object {
        const val TABLE_NAME = "players"
        const val SESSION_ID = "session_id"
        const val ID = "player_id"
        const val USER_ID = "user_net_dev_id"
        const val NAME = "nickname"
        const val PRESENCE = "player_presence"
        const val STATE = "player_state"
        const val ORDER = "move_order"
    }
}
