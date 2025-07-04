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




// ViewModel para manejar la autenticación y preferencias del usuario
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    // Instancia de FirebaseAuth para manejar autenticación
    private val auth = FirebaseAuth.getInstance()
    // Instancia para manejar las preferencias locales (DataStore)
    private val userPrefs = UserPreferences(application)

    // Estado que indica si el usuario está logueado o no
    private val _isLoggedIn = MutableStateFlow(auth.currentUser != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    // Estado que contiene el usuario actual de Firebase
    private val _firebaseUser = MutableStateFlow(auth.currentUser)
    val firebaseUser: StateFlow<FirebaseUser?> = _firebaseUser

    // Estado para el modo oscuro (dark mode)
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode

    // Lista de rutas favoritas del usuario, inicializada desde DataStore
    private val _favoriteRoutes = MutableStateFlow<List<String>>(emptyList())
    val favoriteRoutes: StateFlow<List<String>> = _favoriteRoutes.asStateFlow()

    init {
        // Observa cambios en el estado de login guardado en DataStore y actualiza el estado
        viewModelScope.launch {
            userPrefs.isLoggedIn.collect { saved ->
                _isLoggedIn.value = saved && auth.currentUser != null
                _firebaseUser.value = auth.currentUser
            }
        }

        // Observa cambios en el modo oscuro guardado en DataStore y actualiza el estado
        viewModelScope.launch {
            userPrefs.isDarkMode.collect { savedDarkMode ->
                _isDarkMode.value = savedDarkMode
            }
        }

        // Observa la lista de rutas favoritas guardadas en DataStore y actualiza el estado
        viewModelScope.launch {
            userPrefs.favoriteRoutes.collect { routesSet ->
                _favoriteRoutes.value = routesSet.toList()
            }
        }
    }

    // Cambia el modo oscuro y guarda el valor en DataStore
    fun toggleDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
        viewModelScope.launch {
            userPrefs.setDarkMode(enabled)
        }
    }

    // Función para iniciar sesión con email y contraseña
    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Actualiza estados si el login fue exitoso
                    _isLoggedIn.value = true
                    _firebaseUser.value = auth.currentUser
                    // Guarda en DataStore que está logueado
                    viewModelScope.launch {
                        userPrefs.setLoggedIn(true)
                    }
                    onSuccess()
                } else {
                    onError(task.exception?.message ?: "Error al iniciar sesión")
                }
            }
    }

    // Función para iniciar sesión con credenciales externas (ejemplo: Google)
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

    // Registro de usuario con email y contraseña
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
                    // Actualiza el nombre del usuario recién creado
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

    // Cierra la sesión del usuario y actualiza estados y DataStore
    fun logout() {
        auth.signOut()
        _isLoggedIn.value = false
        _firebaseUser.value = null
        viewModelScope.launch {
            userPrefs.setLoggedIn(false)
        }
    }

    // Agrega o elimina una ruta de la lista de favoritos y guarda los cambios localmente
    fun toggleFavorite(routeName: String) {
        val current = _favoriteRoutes.value.toMutableSet()
        if (current.contains(routeName)) {
            current.remove(routeName)  // Quitar si ya estaba
        } else {
            current.add(routeName)     // Agregar si no estaba
        }
        _favoriteRoutes.value = current.toList()

        // Guarda la lista actualizada en DataStore
        viewModelScope.launch {
            userPrefs.setFavoriteRoutes(current)
        }
    }

    // Consulta si una ruta está marcada como favorita
    fun isFavorite(routeName: String): Boolean {
        return _favoriteRoutes.value.contains(routeName)
    }
}
