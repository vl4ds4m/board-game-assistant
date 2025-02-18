package org.vl4ds4m.board.game.assistant.ui.game.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTopBar(
    title: State<String>,
    onArrowBackClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(title.value) },
        navigationIcon = {
            IconButton(
                onClick = onArrowBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(
                onClick = onMenuClick
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu"
                )
            }
        }
    )
}
