package org.vl4ds4m.board.game.assistant.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.ui.component.isValidPlayerName
import org.vl4ds4m.board.game.assistant.ui.component.playerName
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

/**
 * Displays a profile of the application user with the user name text field.
 */
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    ProfileScreenContent(
        viewModel.userName.collectAsState(),
        viewModel::saveUserName,
        modifier = modifier
    )
}

@Composable
fun ProfileScreenContent(
    userNameState: State<String?>,
    saveUserName: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val (userName, editUserName) = rememberSaveable(userNameState.value) {
        mutableStateOf(userNameState.value ?: "")
    }
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 48.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.profile_title),
            style = MaterialTheme.typography.headlineMedium
        )
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "User image",
            modifier = Modifier.size(56.dp)
        )
        TextField(
            value = userName,
            onValueChange = editUserName,
            label = { Text(stringResource(R.string.profile_name_label)) },
            singleLine = true
        )
        Button(
            onClick = { saveUserName(userName.playerName) },
            enabled = userName.isValidPlayerName &&
                userName.playerName != userNameState.value
        ) {
            Text(stringResource(R.string.profile_save_changes))
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    BoardGameAssistantTheme {
        ProfileScreenContent(
            userNameState = rememberUpdatedState("B. Name"),
            saveUserName = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
