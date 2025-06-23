package com.example.MiBusito.model

import com.google.gson.annotations.SerializedName

data class Ruta(
    @SerializedName("Id") val id: Int,
    @SerializedName("Nombre") val nombre: String,
    @SerializedName("Ruta") val ruta: String,
    @SerializedName("CostoPasaje") val costoPasaje: Double,
    @SerializedName("Favorito") val favorito: Boolean,
    @SerializedName("Mapa") val mapa: String
)

data class Parada(
    @SerializedName("ParadaId") val id: Int,
    @SerializedName("NombreParada") val nombre: String
)

data class RutaDetalle(
    @SerializedName("AutobusId") val id: Int,
    @SerializedName("Nombre") val nombre: String,
    @SerializedName("Ruta") val ruta: String,
    @SerializedName("CostoPasaje") val costoPasaje: Double,
    @SerializedName("Favorito") val favorito: Boolean,
    @SerializedName("Mapa") val mapa: String,
    @SerializedName("paradas") val paradas: List<Parada>
)

