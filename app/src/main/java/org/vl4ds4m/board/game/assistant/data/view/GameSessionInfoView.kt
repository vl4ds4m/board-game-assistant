package org.vl4ds4m.board.game.assistant.data.view

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import org.vl4ds4m.board.game.assistant.data.entity.GameSessionEntity

@DatabaseView(
    viewName = GameSessionInfoView.VIEW_NAME,
    value = """
        SELECT
            ${GameSessionEntity.ID},
            ${GameSessionEntity.COMPLETED},
            ${GameSessionEntity.TYPE},
            ${GameSessionEntity.NAME},
            ${GameSessionEntity.START_TIME},
            ${GameSessionEntity.STOP_TIME}
        FROM
            ${GameSessionEntity.TABLE_NAME}
    """
)
data class GameSessionInfoView(
    @ColumnInfo(name = GameSessionEntity.ID)
    val id: Long,

    @ColumnInfo(name = GameSessionEntity.COMPLETED)
    val completed: Boolean,

    @ColumnInfo(name = GameSessionEntity.TYPE)
    val type: String,

    @ColumnInfo(name = GameSessionEntity.NAME)
    val name: String,

    @ColumnInfo(name = GameSessionEntity.START_TIME)
    val startTime: Long?,

    @ColumnInfo(name = GameSessionEntity.STOP_TIME)
    val stopTime: Long?
) {
    companion object {
        const val VIEW_NAME = "sessions_info"
    }
}
