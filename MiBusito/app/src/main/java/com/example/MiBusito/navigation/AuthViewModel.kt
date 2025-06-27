package com.example.MiBusito.viewmodels
import kotlinx.coroutines.flow.asStateFlow

import android.app.Application
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

    // Flow con la lista de rutas favoritas, inicializada con DataStore
    private val _favoriteRoutes = MutableStateFlow<List<String>>(emptyList())
    val favoriteRoutes: StateFlow<List<String>> = _favoriteRoutes.asStateFlow()

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

        // Observar las rutas favoritas guardadas en DataStore
        viewModelScope.launch {
            userPrefs.favoriteRoutes.collect { routesSet ->
                _favoriteRoutes.value = routesSet.toList()
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

    // Función para agregar o quitar ruta de favoritos y guardar localmente
    fun toggleFavorite(routeName: String) {
        val current = _favoriteRoutes.value.toMutableSet()
        if (current.contains(routeName)) {
            current.remove(routeName)
        } else {
            current.add(routeName)
        }
        _favoriteRoutes.value = current.toList()

        // Guardar en DataStore
        viewModelScope.launch {
            userPrefs.setFavoriteRoutes(current)
        }
    }

    fun isFavorite(routeName: String): Boolean {
        return _favoriteRoutes.value.contains(routeName)
    }
}
