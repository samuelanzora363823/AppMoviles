package com.example.MiBusito.network

import com.example.MiBusito.model.Ruta
import com.example.MiBusito.model.RutaDetalle
import retrofit2.http.GET
import retrofit2.http.Path

interface RutaApiService {
    @GET("rutas")
    suspend fun getRutas(): List<Ruta>

    @GET("rutas/{id}/paradas")
    suspend fun getRutaDetalle(@Path("id") id: Int): RutaDetalle
}
