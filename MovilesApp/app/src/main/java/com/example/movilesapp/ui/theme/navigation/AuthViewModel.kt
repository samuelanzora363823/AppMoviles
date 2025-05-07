// AuthViewModel.kt
package com.example.movilesapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val mockUsers = listOf(
        User(email = "usuario@ejemplo.com", password = "contraseña123"),
        User(email = "test@test.com", password = "test123")
    )

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            kotlinx.coroutines.delay(1000) // Simulación de red

            val user = mockUsers.find { it.email == email && it.password == password }
            if (user != null) {
                _isLoggedIn.value = true
                onSuccess()
            } else {
                onError("Credenciales incorrectas")
            }
        }
    }

    fun logout() {
        _isLoggedIn.value = false
    }

    data class User(val email: String, val password: String)
}