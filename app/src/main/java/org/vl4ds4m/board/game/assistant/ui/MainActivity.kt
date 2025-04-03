package org.vl4ds4m.board.game.assistant.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application.let { it as BoardGameAssistantApp }
            .initDependencies()
        enableEdgeToEdge()
        setContent {
            BoardGameAssistantTheme {
                MainScreen()
            }
        }
    }
}
