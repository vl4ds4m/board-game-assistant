package org.vl4ds4m.board.game.assistant.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    ProfileScreenContent(
        viewModel.username,
        modifier = modifier
    )
}

@Composable
fun ProfileScreenContent(
    username: MutableState<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 48.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your profile",
            style = MaterialTheme.typography.headlineMedium
        )
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "User image",
            modifier = Modifier.size(56.dp)
        )
        TextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text("Name") },
            singleLine = true
        )
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    BoardGameAssistantTheme {
        ProfileScreenContent(
            username = remember { mutableStateOf("B. Name") },
            modifier = Modifier.fillMaxSize()
        )
    }
}
