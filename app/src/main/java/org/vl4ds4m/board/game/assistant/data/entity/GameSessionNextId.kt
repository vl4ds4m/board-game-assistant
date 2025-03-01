package org.vl4ds4m.board.game.assistant.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = GameSessionNextId.TABLE_NAME)
data class GameSessionNextId(
    @PrimaryKey
    @ColumnInfo(name = KEY)
    val key: Int,

    @ColumnInfo(name = VALUE)
    val nextValue: Long
) {
    constructor(nextValue: Long) : this(SINGLE_KEY, nextValue)

    companion object {
        const val TABLE_NAME = "game_session_next_id"
        const val KEY = "primary_key"
        const val VALUE = "next_id_value"

        const val SINGLE_KEY = 1
        const val FIRST_VALUE = 1L
    }
}
