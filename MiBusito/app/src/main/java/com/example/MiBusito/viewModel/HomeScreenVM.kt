package com.example.MiBusito.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.MiBusito.model.Ruta
import com.example.MiBusito.repository.RutaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeScreenVM : ViewModel() {

    private val repository = RutaRepository()

    private val _rutas = MutableStateFlow<List<Ruta>>(emptyList())
    val rutas: StateFlow<List<Ruta>> = _rutas

    init {
        cargarRutas()
    }

    private fun cargarRutas() {
        viewModelScope.launch {
            try {
                val lista = repository.fetchRutas()
                _rutas.value = lista
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

