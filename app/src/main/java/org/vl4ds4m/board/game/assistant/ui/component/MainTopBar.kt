package org.vl4ds4m.board.game.assistant.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    uiState: State<TopBarParams>,
    modifier: Modifier = Modifier
) {
    val params = uiState.value
    TopAppBar(
        title = {
            Text(
                text = params.title
            )
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = params.navigateBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            params.actions.forEach { action ->
                action()
            }
        }
    )
}

data class TopBarParams(
    val title: String,
    val navigateBack: () -> Unit,
    val actions: List<BarAction>
) {
    companion object {
        val Empty = TopBarParams(
            title = "",
            navigateBack = {},
            actions = listOf()
        )

        val Example get() = TopBarParams(
            title = "Screen title",
            navigateBack = {},
            actions = listOf {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = null
                    )
                }
            }
        )
    }
}

typealias BarAction = @Composable (RowScope.() -> Unit)

@Preview
@Composable
private fun MainTopBarPreview() {
    BoardGameAssistantTheme {
        MainTopBar(
            uiState = remember {
                mutableStateOf(TopBarParams.Example)
            }
        )
    }
}
