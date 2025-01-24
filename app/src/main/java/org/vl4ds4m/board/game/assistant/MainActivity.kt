package org.vl4ds4m.board.game.assistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.vl4ds4m.board.game.assistant.ui.MainContent
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BoardGameAssistantTheme {
                MainContent()
            }
        }
    }
}

@Preview
@Composable
private fun MainActivityViewPreview() {
    BoardGameAssistantTheme {
        MainContent()
    }
}
