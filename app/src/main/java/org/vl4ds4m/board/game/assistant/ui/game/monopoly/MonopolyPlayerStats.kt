package org.vl4ds4m.board.game.assistant.ui.game.monopoly

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.vl4ds4m.board.game.assistant.game.data.PlayerState
import org.vl4ds4m.board.game.assistant.game.monopoly.monopolyFields
import org.vl4ds4m.board.game.assistant.game.monopoly.position
import java.util.Locale

@Composable
fun RowScope.MonopolyPlayerStats(state: State<PlayerState>) {
    Spacer(Modifier.weight(1f))
    Text(monopolyField(state.value.position ?: 0))
    Text("|")
    val money = state.value.score
    Text(showMoney(money))
}

@Composable
fun monopolyField(position: Int): String =
    monopolyFields[position]?.let { stringResource(it.resId) } ?: "???"

private fun showMoney(money: Int): String {
    val m = money / 1_000_000
    val k = money % 1_000_000 / 1_000
    val r = money - m * 1_000_000 - k * 1_000
    return if (m > 0) {
        String.format(Locale.getDefault(), "%d.%03d M", m, k)
    } else if (k > 0) {
        String.format(Locale.getDefault(), "%d.%03d K", k, r)
    } else {
        "$r"
    }
}
