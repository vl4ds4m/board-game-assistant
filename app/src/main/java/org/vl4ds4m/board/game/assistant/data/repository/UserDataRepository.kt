package org.vl4ds4m.board.game.assistant.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class UserDataRepository(
    private val userDataStore: DataStore<Preferences>,
    private val coroutineScope: CoroutineScope
) {
    val userName: Flow<String> = userDataStore.data.map { preferences ->
        preferences[USER_NAME] ?: ""
    }

    fun editUserName(name: String) = coroutineScope.launch {
        userDataStore.edit { preferences ->
            preferences[USER_NAME] = name
        }
    }
}

private val USER_NAME = stringPreferencesKey("user_name")
