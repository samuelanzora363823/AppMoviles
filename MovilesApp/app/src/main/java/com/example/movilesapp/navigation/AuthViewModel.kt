package com.example.movilesapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.movilesapp.data.UserPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val userPrefs = UserPreferences(application)

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val mockUsers = listOf(
        User(email = "usuario@ejemplo.com", password = "contraseña123"),
        User(email = "test@test.com", password = "test123")
    )

    init {
        viewModelScope.launch {
            userPrefs.isLoggedIn.collect { savedStatus ->
                _isLoggedIn.value = savedStatus
            }
        }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            delay(1000) // Simulación de red

            val user = mockUsers.find { it.email == email && it.password == password }
            if (user != null) {
                userPrefs.setLoggedIn(true)
                _isLoggedIn.value = true
                onSuccess()
            } else {
                onError("Credenciales incorrectas")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPrefs.setLoggedIn(false)
            _isLoggedIn.value = false
        }
    }

    data class User(val email: String, val password: String)
}
