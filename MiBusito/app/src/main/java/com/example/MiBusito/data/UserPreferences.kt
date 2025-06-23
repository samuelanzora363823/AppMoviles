// UserPreferences.kt
package com.example.MiBusito.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.stringSetPreferencesKey


// Extensión para obtener el DataStore
private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        private val FAVORITE_ROUTES = stringSetPreferencesKey("favorite_routes")
    }

    // Flow para saber si está logueado
    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[IS_LOGGED_IN] ?: false }

    // Flow para modo oscuro
    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[IS_DARK_MODE] ?: false }

    // Flow con rutas favoritas (conjunto de strings)
    val favoriteRoutes: Flow<Set<String>> = context.dataStore.data
        .map { preferences -> preferences[FAVORITE_ROUTES] ?: emptySet() }

    // Guardar estado de login
    suspend fun setLoggedIn(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = value
        }
    }

    // Guardar modo oscuro
    suspend fun setDarkMode(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = value
        }
    }

    // Guardar conjunto completo de rutas favoritas
    suspend fun setFavoriteRoutes(routes: Set<String>) {
        context.dataStore.edit { preferences ->
            preferences[FAVORITE_ROUTES] = routes
        }
    }
}
