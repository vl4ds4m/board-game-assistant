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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    uiState: TopBarUiState,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = uiState.title.value
            )
        },
        navigationIcon = {
            IconButton(
                onClick = uiState.navigateBack.value
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = uiState.actions.value,
        modifier = modifier
    )
}

typealias BarActions = @Composable (RowScope.() -> Unit)

@Stable
class TopBarUiState private constructor(
    title: String,
    navigateBack: () -> Unit,
    actions: BarActions
) {
    private val mTitle = mutableStateOf(title)
    private val mNavigateBack = mutableStateOf(navigateBack)
    private val mActions = mutableStateOf(actions)

    val title: State<String> = mTitle
    val navigateBack: State<() -> Unit> = mNavigateBack
    val actions: State<BarActions> = mActions

    fun update(
        title: String? = null,
        navigateBack: (() -> Unit)? = null,
        actions: BarActions? = null
    ) {
        mTitle.value = title ?: ""
        mNavigateBack.value = navigateBack ?: {}
        mActions.value = actions ?: {}
    }

    companion object {
        fun createEmpty() = TopBarUiState(
            title = "",
            navigateBack = {},
            actions = {}
        )

        fun createExample() = TopBarUiState(
            title = "Screen title",
            navigateBack = {},
            actions = {
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

@Preview
@Composable
private fun MainTopBarPreview() {
    BoardGameAssistantTheme {
        MainTopBar(TopBarUiState.createExample())
    }
}
