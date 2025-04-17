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

    @ColumnInfo(name = CONTENT)
    val content: String

    /*@ColumnInfo(name = TYPE) TODO Resolve
    val type: String,

    @ColumnInfo(name = PLAYER_ID)
    val playerId: Long?,

    @ColumnInfo(name = OLD_SCORE)
    val oldScore: Int?,

    @ColumnInfo(name = NEW_SCORE)
    val newScore: Int?,

    @ColumnInfo(name = OLD_PLAYER_ID)
    val oldPlayerId: Long?,

    @ColumnInfo(name = NEW_PLAYER_ID)
    val newPlayerId: Long?,

    @ColumnInfo(name = OLD_POS)
    val oldPos: Int?,

    @ColumnInfo(name = NEW_POS)
    val newPos: Int?,

    @ColumnInfo(name = IN_PRISON)
    val inPrison: Boolean?*/
) {
    companion object {
        const val TABLE_NAME = "actions"
        const val SESSION_ID = "session_id"
        const val POSITION = "position"
        const val CONTENT = "action_content"
        /*const val TYPE = "action_type" TODO Resolve
        const val PLAYER_ID = "player_id"
        const val OLD_SCORE = "old_score"
        const val NEW_SCORE = "new_score"
        const val OLD_PLAYER_ID = "old_player_id"
        const val NEW_PLAYER_ID = "new_player_id"
        const val OLD_POS = "old_position"
        const val NEW_POS = "new_position"
        const val IN_PRISON = "in_prison"*/
    }
}
