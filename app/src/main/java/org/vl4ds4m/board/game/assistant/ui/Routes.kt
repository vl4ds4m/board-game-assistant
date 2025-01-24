package org.vl4ds4m.board.game.assistant.ui

import androidx.compose.runtime.saveable.Saver
import kotlinx.serialization.Serializable

sealed interface Route {
    companion object {
        val Saver = Saver<Route, String>(
            save = { it.name },
            restore = { it.route }
        )
    }
}

private val Route.name: String
    get() = when (this) {
        is Home -> "home"
        is Results -> "results"
        is Profile -> "profile"
    }

private val String.route: Route
    get() = when (this) {
        Home.name -> Home
        Results.name -> Results
        Profile.name -> Profile
        else -> throw IllegalArgumentException("There is no Route for name '$this'.")
    }

@Serializable
data object Home : Route

@Serializable
data object Results : Route

@Serializable
data object Profile : Route
