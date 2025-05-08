package org.vl4ds4m.board.game.assistant.ui.game.monopoly

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.vl4ds4m.board.game.assistant.R
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

@Composable
private fun showMoney(money: Int): String {
    val m = money / 1_000
    val k = money % 1_000
    val units = stringResource(R.string.game_monopoly_money_k)
    return if (m > 0) {
        String.format(Locale.getDefault(), "%d %03d $units", m, k)
    } else {
        String.format(Locale.getDefault(), "%d $units", k)
    }
}
