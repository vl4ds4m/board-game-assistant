package org.vl4ds4m.board.game.assistant.ui.profile

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp

class ProfileViewModel private constructor(val username: MutableState<String>) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer<ProfileViewModel> {
                get(APPLICATION_KEY)
                    .let { it as BoardGameAssistantApp }
                    .let { ProfileViewModel(it.username) }
            }
        }
    }
}
