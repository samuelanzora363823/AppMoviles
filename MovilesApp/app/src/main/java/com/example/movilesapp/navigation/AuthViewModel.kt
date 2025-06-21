package com.example.movilesapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.movilesapp.data.UserPreferences
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val userPrefs = UserPreferences(application)
    private val auth = FirebaseAuth.getInstance()

    private val _isLoggedIn = MutableStateFlow(auth.currentUser != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    init {
        // Sincroniza con preferencias
        viewModelScope.launch {
            userPrefs.isLoggedIn.collect { savedStatus ->
                _isLoggedIn.value = auth.currentUser != null && savedStatus
            }
        }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModelScope.launch {
                        userPrefs.setLoggedIn(true)
                        _isLoggedIn.value = true
                        onSuccess()
                    }
                } else {
                    onError(task.exception?.message ?: "Error al iniciar sesión")
                }
            }
    }

    fun loginWithCredential(credential: AuthCredential, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModelScope.launch {
                        userPrefs.setLoggedIn(true)
                        _isLoggedIn.value = true
                        onSuccess()
                    }
                } else {
                    onError(task.exception?.message ?: "Error al iniciar sesión con credencial")
                }
            }
    }

    fun logout() {
        auth.signOut()
        viewModelScope.launch {
            userPrefs.setLoggedIn(false)
            _isLoggedIn.value = false
        }
    }
}
