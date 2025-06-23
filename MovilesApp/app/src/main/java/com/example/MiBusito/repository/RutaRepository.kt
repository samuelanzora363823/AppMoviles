package com.example.MiBusito.repository

import com.example.MiBusito.model.Ruta
import com.example.MiBusito.model.RutaDetalle
import com.example.MiBusito.network.RetrofitClient


class RutaRepository {
    suspend fun fetchRutas(): List<Ruta> {
        return RetrofitClient.apiService.getRutas()
    }

    suspend fun fetchRutaDetalle(id: Int): RutaDetalle {
        return RetrofitClient.apiService.getRutaDetalle(id)
    }

}


