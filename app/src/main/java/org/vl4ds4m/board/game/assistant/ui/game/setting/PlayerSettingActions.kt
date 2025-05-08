package org.vl4ds4m.board.game.assistant.ui.game.setting

import androidx.compose.runtime.Immutable
import org.vl4ds4m.board.game.assistant.data.User
import org.vl4ds4m.board.game.assistant.game.PID

@Immutable
class PlayerSettingActions(
    val onSelect: (PID) -> Unit,
    val onOrderChange: ((PID, Int) -> Unit)?,
    val onBind: (PID, User) -> Unit,
    val onUnbind: (PID) -> Unit,
    val onRename: (PID, String) -> Unit,
    val onRemove: (PID) -> Unit,
    val onFreeze: (PID) -> Unit,
    val onUnfreeze: (PID) -> Unit
) {
    companion object {
        val Empty = PlayerSettingActions(
            onSelect = {},
            onOrderChange = { _, _ -> },
            onBind = { _, _ -> },
            onUnbind = {},
            onRename = { _, _ -> },
            onRemove = {},
            onFreeze = {},
            onUnfreeze = {}
        )
    }
}
