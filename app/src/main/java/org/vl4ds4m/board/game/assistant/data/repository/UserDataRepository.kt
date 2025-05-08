package org.vl4ds4m.board.game.assistant.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.vl4ds4m.board.game.assistant.data.User
import java.util.UUID

class UserDataRepository(
    private val userDataStore: DataStore<Preferences>,
    private val coroutineScope: CoroutineScope
) {
    val userName: Flow<String> = userDataStore.data.map { preferences ->
        preferences[USER_NAME] ?: "Player"
    }

    fun editUserName(name: String) {
        coroutineScope.launch {
            userDataStore.edit { preferences ->
                preferences[USER_NAME] = name
            }
        }
    }

    val netDevId: Flow<String> = userDataStore.data.map { preferences ->
        preferences[NET_DEV_ID] ?: throw IllegalStateException("${NET_DEV_ID.title} must be set")
    }

    fun setNetDevId() {
        coroutineScope.launch {
            userDataStore.edit { preferences ->
                val value = preferences[NET_DEV_ID] ?: run {
                    Log.i(TAG, "${NET_DEV_ID.title} isn't set. Generate it")
                    UUID.randomUUID().toString()
                        .also { preferences[NET_DEV_ID] = it }
                }
                Log.i(TAG, "${NET_DEV_ID.title}: $value")
            }
        }
    }

    val user: Flow<User> = netDevId.combine(userName) { id, name ->
        User(netDevId = id, self = true, name = name)
    }
}

private const val TAG = "UserDataRepository"

private val USER_NAME = stringPreferencesKey("user_name")
private val NET_DEV_ID = stringPreferencesKey("net_dev_id")

private val Preferences.Key<String>.title: String
    get() = name.uppercase()
