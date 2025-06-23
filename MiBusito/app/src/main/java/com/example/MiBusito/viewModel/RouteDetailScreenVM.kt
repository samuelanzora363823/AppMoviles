package com.example.MiBusito.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.MiBusito.model.RutaDetalle
import com.example.MiBusito.repository.RutaRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.State


class RouteDetailScreenVM : ViewModel() {
    private val repository = RutaRepository()

    private val _rutaDetalle = mutableStateOf<RutaDetalle?>(null)
    val rutaDetalle: State<RutaDetalle?> = _rutaDetalle

    fun cargarRutaDetalle(id: Int) {
        viewModelScope.launch {
            try {
                _rutaDetalle.value = repository.fetchRutaDetalle(id)
            } catch (e: Exception) {
                Log.e("RouteDetailScreenVM", "Error: ${e.message}")
            }
        }
    }
}
