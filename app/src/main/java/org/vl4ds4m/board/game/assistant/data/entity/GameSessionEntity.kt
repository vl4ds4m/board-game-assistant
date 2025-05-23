package org.vl4ds4m.board.game.assistant.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.vl4ds4m.board.game.assistant.game.PID

@Entity(tableName = GameSessionEntity.TABLE_NAME)
data class GameSessionEntity(
    @PrimaryKey
    @ColumnInfo(name = ID)
    val id: String,

    @ColumnInfo(name = COMPLETED)
    val completed: Boolean,

    @ColumnInfo(name = TYPE)
    val type: String,

    @ColumnInfo(name = NAME)
    val name: String,

    @ColumnInfo(name = CURRENT_PLAYER_ID)
    val currentPid: PID?,

    @ColumnInfo(name = NEXT_NEW_PLAYER_ID)
    val nextNewPid: PID,

    @ColumnInfo(name = START_TIME)
    val startTime: Long?,

    @ColumnInfo(name = STOP_TIME)
    val stopTime: Long?,

    @ColumnInfo(name = DURATION)
    val duration: Long?,

    @ColumnInfo(name = TIMEOUT)
    val timeout: Boolean,

    @ColumnInfo(name = SECONDS_UNTIL_END)
    val secondsUntilEnd: Int,

    @ColumnInfo(name = CURRENT_ACTION_POSITION)
    val currentActionPosition: Int,
) {
    data class PK(
        @ColumnInfo(name = ID)
        val id: String
    )

    companion object {
        const val TABLE_NAME = "sessions"
        const val ID = "id"
        const val COMPLETED = "completed"
        const val TYPE = "game_type"
        const val NAME = "name"
        const val CURRENT_PLAYER_ID = "current_player_id"
        const val NEXT_NEW_PLAYER_ID = "next_new_player_id"
        const val START_TIME = "start_time"
        const val STOP_TIME = "stop_time"
        const val DURATION = "game_duration"
        const val TIMEOUT = "timeout"
        const val SECONDS_UNTIL_END = "seconds_until_end"
        const val CURRENT_ACTION_POSITION = "current_action_position"
    }
}
