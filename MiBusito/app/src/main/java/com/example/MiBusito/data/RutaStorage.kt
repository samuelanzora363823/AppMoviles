// RutaStorage.kt
package com.example.MiBusito.data

import android.content.Context
import com.example.MiBusito.model.Ruta
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

object RutaStorage {
    private const val FILE_NAME = "rutas_guardadas.json"

    fun saveRutas(context: Context, rutas: List<Ruta>) {
        val json = Json.encodeToString(rutas)
        File(context.filesDir, FILE_NAME).writeText(json)
    }

    fun loadRutas(context: Context): List<Ruta> {
        val file = File(context.filesDir, FILE_NAME)
        return if (file.exists()) {
            val json = file.readText()
            Json.decodeFromString(json)
        } else {
            emptyList()
        }
    }
}
