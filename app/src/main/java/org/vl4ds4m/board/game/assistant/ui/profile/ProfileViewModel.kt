package org.vl4ds4m.board.game.assistant.ui.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.data.repository.UserDataRepository

class ProfileViewModel private constructor(
    private val userDataRepository: UserDataRepository
) : ViewModel() {
    val userName: MutableState<String> = mutableStateOf("")

    init {
        userDataRepository.userName.take(1)
            .onEach { userName.value = it }
            .launchIn(viewModelScope)
    }

    override fun onCleared() {
        userDataRepository.editUserName(userName.value)
        super.onCleared()
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
