package org.vl4ds4m.board.game.assistant.ui.game.setting

import androidx.compose.runtime.Immutable
import org.vl4ds4m.board.game.assistant.data.User

@Immutable
class PlayerSettingActions(
    val onSelect: (Long) -> Unit,
    val onOrderChange: ((Long, Int) -> Unit)?,
    val onBind: (Long, User) -> Unit,
    val onUnbind: (Long) -> Unit,
    val onRename: (Long, String) -> Unit,
    val onRemove: (Long) -> Unit,
    val onFreeze: (Long) -> Unit,
    val onUnfreeze: (Long) -> Unit
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
