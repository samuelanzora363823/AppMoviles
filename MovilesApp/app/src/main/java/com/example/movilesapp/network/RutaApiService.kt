package com.example.movilesapp.network

import com.example.movilesapp.model.Ruta
import com.example.movilesapp.model.RutaDetalle
import retrofit2.http.GET
import retrofit2.http.Path

interface RutaApiService {
    @GET("rutas")
    suspend fun getRutas(): List<Ruta>

    @GET("rutas/{id}/paradas")
    suspend fun getRutaDetalle(@Path("id") id: Int): RutaDetalle
}
