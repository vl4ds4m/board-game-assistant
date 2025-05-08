package org.vl4ds4m.board.game.assistant.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = UserEntity.TABLE_NAME)
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = ID)
    val id: String,

    @ColumnInfo(name = NAME)
    val name: String,

    // val image
) {
    companion object {
        const val TABLE_NAME = "app_users"
        const val ID = "net_dev_id"
        const val NAME = "user_name"
    }
}
