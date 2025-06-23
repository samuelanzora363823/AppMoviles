// UserPreferences.kt
package com.example.movilesapp.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {
    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[IS_LOGGED_IN] ?: false }

    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[IS_DARK_MODE] ?: false }

    suspend fun setLoggedIn(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = value
        }
    }

    suspend fun setDarkMode(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = value
        }
    }
}
