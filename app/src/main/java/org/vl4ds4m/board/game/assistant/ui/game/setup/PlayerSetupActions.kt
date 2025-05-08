package org.vl4ds4m.board.game.assistant.ui.game.setup

import androidx.compose.runtime.Immutable

@Immutable
class PlayerSetupActions(
    val onRename: (Int, String) -> Unit,
    val onRemove: (Int) -> Unit,
    val onOrderChange: (Int, Int) -> Unit
) {
    companion object {
        val Empty = PlayerSetupActions(
            onRename = { _, _ -> },
            onRemove = {},
            onOrderChange = { _, _ -> }
        )
    }
}
