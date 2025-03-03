package org.vl4ds4m.board.game.assistant.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.userDataStore by preferencesDataStore("user_data")
