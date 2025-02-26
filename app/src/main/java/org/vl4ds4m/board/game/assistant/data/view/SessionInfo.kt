package org.vl4ds4m.board.game.assistant.data.view

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import org.vl4ds4m.board.game.assistant.data.entity.SessionEntity

@DatabaseView(
    viewName = SessionInfo.VIEW_NAME,
    value = """
        SELECT
            ${SessionEntity.ID},
            ${SessionEntity.COMPLETED},
            ${SessionEntity.TYPE},
            ${SessionEntity.NAME},
            ${SessionEntity.START_TIME},
            ${SessionEntity.STOP_TIME}
        FROM
            ${SessionEntity.TABLE_NAME}
    """
)
data class SessionInfo(
    @ColumnInfo(name = SessionEntity.ID)
    val id: Long,

    @ColumnInfo(name = SessionEntity.COMPLETED)
    val completed: Boolean,

    @ColumnInfo(name = SessionEntity.TYPE)
    val type: String,

    @ColumnInfo(name = SessionEntity.NAME)
    val name: String,

    @ColumnInfo(name = SessionEntity.START_TIME)
    val startTime: Long,

    @ColumnInfo(name = SessionEntity.STOP_TIME)
    val stopTime: Long
) {
    companion object {
        const val VIEW_NAME = "sessions_info"
    }
}
