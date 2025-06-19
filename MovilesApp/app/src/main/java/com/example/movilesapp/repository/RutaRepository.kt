package com.example.movilesapp.repository

import com.example.movilesapp.model.Ruta
import com.example.movilesapp.model.RutaDetalle
import com.example.movilesapp.network.RetrofitClient


class RutaRepository {
    suspend fun fetchRutas(): List<Ruta> {
        return RetrofitClient.apiService.getRutas()
    }

    suspend fun fetchRutaDetalle(id: Int): RutaDetalle {
        return RetrofitClient.apiService.getRutaDetalle(id)
    }

}


