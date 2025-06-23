package com.example.MiBusito.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.MiBusito.data.UserPreferences
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val auth = FirebaseAuth.getInstance()
    private val userPrefs = UserPreferences(application)

    private val _isLoggedIn = MutableStateFlow(auth.currentUser != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _firebaseUser = MutableStateFlow(auth.currentUser)
    val firebaseUser: StateFlow<FirebaseUser?> = _firebaseUser

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode

    init {
        viewModelScope.launch {
            userPrefs.isLoggedIn.collect { saved ->
                _isLoggedIn.value = saved && auth.currentUser != null
                _firebaseUser.value = auth.currentUser
            }
        }

        viewModelScope.launch {
            userPrefs.isDarkMode.collect { savedDarkMode ->
                _isDarkMode.value = savedDarkMode
            }
        }
    }

    fun toggleDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
        viewModelScope.launch {
            userPrefs.setDarkMode(enabled)
        }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isLoggedIn.value = true
                    _firebaseUser.value = auth.currentUser
                    viewModelScope.launch {
                        userPrefs.setLoggedIn(true)
                    }
                    onSuccess()
                } else {
                    onError(task.exception?.message ?: "Error al iniciar sesión")
                }
            }
    }

    fun loginWithCredential(
        credential: AuthCredential,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isLoggedIn.value = true
                    _firebaseUser.value = auth.currentUser
                    viewModelScope.launch {
                        userPrefs.setLoggedIn(true)
                    }
                    onSuccess()
                } else {
                    onError(task.exception?.message ?: "Error al iniciar sesión con Google")
                }
            }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    user?.updateProfile(profileUpdates)?.addOnCompleteListener {
                        _isLoggedIn.value = true
                        _firebaseUser.value = auth.currentUser
                        viewModelScope.launch {
                            userPrefs.setLoggedIn(true)
                        }
                        onSuccess()
                    }
                } else {
                    onError(task.exception?.message ?: "Error al registrar usuario")
                }
            }
    }

    fun logout() {
        auth.signOut()
        _isLoggedIn.value = false
        _firebaseUser.value = null
        viewModelScope.launch {
            userPrefs.setLoggedIn(false)
        }
    }

    private val _favoriteRoutes = mutableStateListOf<String>()
    val favoriteRoutes: List<String> get() = _favoriteRoutes

    fun toggleFavorite(routeName: String) {
        if (_favoriteRoutes.contains(routeName)) {
            _favoriteRoutes.remove(routeName)
        } else {
            _favoriteRoutes.add(routeName)
        }
    }

    fun isFavorite(routeName: String): Boolean {
        return _favoriteRoutes.contains(routeName)
    }

}
