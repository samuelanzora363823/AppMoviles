package com.example.movilesapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movilesapp.api.RetrofitClient
import com.example.movilesapp.models.Ruta
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RutasViewModel : ViewModel() {

    private val _rutaSeleccionada = MutableStateFlow<Ruta?>(null)
    val rutaSeleccionada: StateFlow<Ruta?> = _rutaSeleccionada

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
}
