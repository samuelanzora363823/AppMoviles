package com.example.movilesapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movilesapp.api.RetrofitClient
import com.example.movilesapp.models.Ruta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RutasViewModel : ViewModel() {

    // Lista observable de rutas favoritas
    var favoriteRoutes = mutableStateListOf<Ruta>()
        private set

    // Estado de la ruta seleccionada para detalle
    private val _rutaSeleccionada = MutableStateFlow<Ruta?>(null)
    val rutaSeleccionada: StateFlow<Ruta?> = _rutaSeleccionada

    // Carga la ruta por nombre desde API
    fun cargarRutaPorNombre(nombre: String) {
        viewModelScope.launch {
            try {
                val rutas = RetrofitClient.api.obtenerRutas()
                val ruta = rutas.find { it.Nombre == nombre }
                _rutaSeleccionada.value = ruta
            } catch (e: Exception) {
                _rutaSeleccionada.value = null
            }
        }
    }

    // Añade o remueve una ruta de favoritos
    fun toggleFavorito(ruta: Ruta) {
        if (favoriteRoutes.any { it.Id == ruta.Id }) {
            favoriteRoutes.removeAll { it.Id == ruta.Id }
        } else {
            favoriteRoutes.add(ruta.copy(Favorito = true))
        }
    }

    // Revisa si la ruta está en favoritos
    fun isFavorito(ruta: Ruta): Boolean {
        return favoriteRoutes.any { it.Id == ruta.Id }
    }
}
