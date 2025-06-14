package com.example.movilesapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.movilesapp.models.Ruta

class RutasViewModel : ViewModel() {

    // Lista de rutas favoritas
    var favoriteRoutes = mutableStateListOf<Ruta>()
        private set

    // Estado de la ruta seleccionada (para detalle)
    private val _rutaSeleccionada = MutableStateFlow<Ruta?>(null)
    val rutaSeleccionada: StateFlow<Ruta?> get() = _rutaSeleccionada


    fun toggleFavorito(ruta: Ruta) {
        if (favoriteRoutes.any { it.Id == ruta.Id }) {
            favoriteRoutes.removeAll { it.Id == ruta.Id }
        } else {
            favoriteRoutes.add(ruta.copy(Favorito = true))
        }
    }

    fun isFavorito(ruta: Ruta): Boolean {
        return favoriteRoutes.any { it.Id == ruta.Id }
    }

    private fun isFavoritoPorNombre(nombre: String): Boolean {
        return favoriteRoutes.any { it.Nombre == nombre }
    }
}
