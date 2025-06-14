package com.example.movilesapp.screens


import com.example.movilesapp.models.Ruta
import retrofit2.http.GET

interface RutasApi {
    @GET("rutas")
    suspend fun obtenerRutas(): List<Ruta>
}
