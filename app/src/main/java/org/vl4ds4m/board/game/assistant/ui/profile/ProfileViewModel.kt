package org.vl4ds4m.board.game.assistant.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.data.repository.UserDataRepository

class ProfileViewModel private constructor(
    private val userDataRepository: UserDataRepository
) : ViewModel() {
    val userName: StateFlow<String?> = userDataRepository.userName
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun saveUserName(name: String) {
        name.takeUnless { it.isBlank() }
            ?.let { userDataRepository.editUserName(it.trim()) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer<ProfileViewModel> {
                val app = BoardGameAssistantApp.from(this)
                ProfileViewModel(app.userDataRepository)
            }
        }
    }
}
