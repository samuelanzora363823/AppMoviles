package com.example.MiBusito.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import com.google.android.gms.maps.model.LatLng
import android.location.Location


// --- MAPA CON KML PARSEADO ---
@Composable
fun KMLMapWithIdaRegresoPolylines(kmlText: String) {
    val rutasIda = remember { mutableStateListOf<List<LatLng>>() }
    val rutasRegreso = remember { mutableStateListOf<List<LatLng>>() }
    val paradas = remember { mutableStateListOf<Pair<LatLng, String>>() }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(13.7, -89.25), 11.5f)
    }

    LaunchedEffect(kmlText) {
        try {
            val (ida, regreso, stops) = parseKmlWithStops(kmlText)
            rutasIda.clear(); rutasIda.addAll(ida)
            rutasRegreso.clear(); rutasRegreso.addAll(regreso)
            paradas.clear(); paradas.addAll(stops)
        } catch (e: Exception) {
            Log.e("DEBUG", "Error leyendo KML: ${e.message}", e)
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(zoomControlsEnabled = true)
    ) {
        rutasIda.forEach { ruta ->
            if (ruta.isNotEmpty()) {
                Polyline(points = ruta, color = Color.Blue, width = 12f)
            }
        }
        rutasRegreso.forEach { ruta ->
            if (ruta.isNotEmpty()) {
                Polyline(points = ruta, color = Color.Red, width = 12f)
            }
        }
        paradas.forEach { (position, name) ->
            Marker(state = MarkerState(position), title = name)
        }
    }
}

// --- FUNCION DE PARSEO DE XML ---
fun parseKmlWithStops(kml: String): Triple<List<List<LatLng>>, List<List<LatLng>>, List<Pair<LatLng, String>>> {
    val rutasIda = mutableListOf<MutableList<LatLng>>()
    val rutasRegreso = mutableListOf<MutableList<LatLng>>()
    val paradas = mutableListOf<Pair<LatLng, String>>()

    var currentRoute: MutableList<LatLng>? = null
    var currentRouteType: String? = null
    var currentName: String? = null
    var insidePlacemark = false
    var isPoint = false

    try {
        val factory = org.xmlpull.v1.XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        parser.setInput(kml.reader())

        var eventType = parser.eventType

        while (eventType != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                org.xmlpull.v1.XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "Placemark" -> {
                            insidePlacemark = true
                            currentRoute = mutableListOf()
                            currentRouteType = null
                            currentName = null
                            isPoint = false
                        }
                        "name" -> {
                            if (insidePlacemark) {
                                currentName = parser.nextText()
                                val nameText = currentName?.lowercase().orEmpty()
                                currentRouteType = when {
                                    nameText.contains("ida") -> "ida"
                                    nameText.contains("regreso") -> "regreso"
                                    else -> null
                                }
                            }
                        }
                        "Point" -> isPoint = true
                        "coordinates" -> {
                            val coordText = parser.nextText()
                            coordText.trim().split("\\s+".toRegex()).forEach { coord ->
                                val parts = coord.split(",")
                                if (parts.size >= 2) {
                                    val lon = parts[0].toDoubleOrNull()
                                    val lat = parts[1].toDoubleOrNull()

                                    if (lat != null && lon != null &&
                                        lat in -90.0..90.0 && lon in -180.0..180.0
                                    ) {
                                        val latLng = LatLng(lat, lon)

                                        if (isPoint) {
                                            paradas.add(Pair(latLng, currentName ?: "Parada"))
                                        } else {
                                            // Validar que el salto entre puntos no sea mayor a 3 km
                                            currentRoute?.let {
                                                if (it.isNotEmpty()) {
                                                    val last = it.last()
                                                    val results = FloatArray(1)
                                                    Location("").apply {
                                                        latitude = last.latitude
                                                        longitude = last.longitude
                                                    }.distanceTo(
                                                        Location("").apply {
                                                            latitude = latLng.latitude
                                                            longitude = latLng.longitude
                                                        }
                                                    ).also { dist -> results[0] = dist }

                                                    if (results[0] > 3_000f) {
                                                        Log.w("KML", "Salto >3km ignorado en ruta '${currentName}': $latLng")
                                                        return@forEach
                                                    }
                                                }
                                            }
                                            currentRoute?.add(latLng)
                                        }
                                    } else {
                                        Log.w("KMLParser", "Coordenada inválida ignorada: $coord")
                                    }
                                }
                            }
                        }
                    }
                }

                org.xmlpull.v1.XmlPullParser.END_TAG -> {
                    if (parser.name == "Placemark") {
                        insidePlacemark = false
                        isPoint = false
                        currentRoute?.let {
                            if (it.isNotEmpty() && it.size < 5000) {
                                when (currentRouteType) {
                                    "ida" -> rutasIda.add(it)
                                    "regreso" -> rutasRegreso.add(it)
                                    else -> rutasIda.add(it)
                                }
                            } else {
                                Log.w("KMLParser", "Ruta descartada (${currentName ?: "Sin nombre"}) por exceso de puntos o estar vacía")
                            }
                        }
                        currentRoute = null
                        currentRouteType = null
                        currentName = null
                    }
                }
            }
            eventType = parser.next()
        }
    } catch (e: Exception) {
        Log.e("KMLParser", "Error al parsear KML: ${e.message}", e)
    }

    return Triple(rutasIda, rutasRegreso, paradas)
}
