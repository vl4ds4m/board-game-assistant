package org.vl4ds4m.board.game.assistant.ui.game.observer

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.network.NetworkGameState
import org.vl4ds4m.board.game.assistant.network.RemoteSessionInfo
import org.vl4ds4m.board.game.assistant.ui.component.TopBarUiState

@Serializable
data class GameObserver(
    val sessionId: String,
    val type: String,
    val name: String,
    val address: String,
    val port: Int
) {
    fun toRemoteSessionInfo() = RemoteSessionInfo(
        id = sessionId,
        type = GameType.valueOf(type),
        name = name,
        ip = address,
        port = port
    )

    companion object {
        fun from(info: RemoteSessionInfo) = GameObserver(
            sessionId = info.id,
            type = info.type.title,
            name = info.name,
            address = info.ip,
            port = info.port
        )
    }
}

fun NavGraphBuilder.observerNavigation(
    navController: NavController,
    topBarUiState: TopBarUiState
) {
    composable<GameObserver> { entry ->
        val sessionInfo = entry.toRoute<GameObserver>().toRemoteSessionInfo()
        val viewModel = viewModel<GameObserverViewModel>(
            factory = GameObserverViewModel.createFactory(sessionInfo)
        )
        val observer = viewModel.observerState.collectAsState()
        val startSession = remember {
            createStartSession(sessionInfo.name, sessionInfo.type)
        }
        val session = produceState(startSession) {
            viewModel.sessionState.collect {
                value = it ?: startSession
            }
        }
        val onBackClick: () -> Unit = { navController.navigateUp() }
        val title = remember {
            derivedStateOf { session.value.name }
        }
        topBarUiState.update(
            title = title.value,
            navigateBack = onBackClick
        )
        val players = remember {
            derivedStateOf { session.value.players.toMap() }
        }
        val users = remember {
            derivedStateOf { session.value.users }
        }
        val gameUiFactory = remember {
            derivedStateOf { session.value.type.uiFactory }
        }
        when (observer.value) {
            NetworkGameState.REGISTRATION -> ObserverStartupScreen()
            NetworkGameState.IN_GAME -> {
                val currentPid = remember {
                    derivedStateOf { session.value.currentPid }
                }
                val actions = remember {
                    derivedStateOf { session.value.actions }
                }
                val timer = remember {
                    derivedStateOf {
                        session.value.secondsUntilEnd.takeIf { session.value.timeout }
                    }
                }
                ObserverGameScreen(
                    players = players,
                    users = users,
                    gameUiFactory = gameUiFactory,
                    currentPid = currentPid,
                    actions = actions,
                    timer = timer
                )
            }
            NetworkGameState.END_GAME -> ObserverEndScreen(
                players = players,
                users = users,
                gameUiFactory = gameUiFactory
            )
            NetworkGameState.EXIT -> onBackClick()
        }
    }
}

private fun createStartSession(name: String, type: GameType) = GameSession(
    completed = false,
    type = type,
    name = name,
    players = listOf(),
    users = mapOf(),
    currentPid = null,
    nextNewPid = 1,
    startTime = null,
    stopTime = null,
    duration = null,
    timeout = false,
    secondsUntilEnd = 0,
    actions = listOf(),
    currentActionPosition = 0
)
