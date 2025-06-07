package com.example.movilesapp.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// --- MODELO DE DATOS ---
data class Ruta(
    val Id: Int,
    val Nombre: String,
    val Ruta: String,
    val CostoPasaje: Double,
    val Favorito: Boolean,
    val Mapa: String // XML como texto
)

// --- INTERFAZ DE LA API ---
interface RutasApi {
    @GET("rutas/1")
    suspend fun obtenerRuta(): Ruta
}

// --- CLIENTE RETROFIT ---
//cambia localhost por tu IP para probar
object RetrofitClient {
    private const val BASE_URL = "http://localhost:3000/"

    val api: RutasApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
            .create(RutasApi::class.java)
    }
}

// --- COMPOSABLE PRINCIPAL ---
@Composable
fun MainScreen(navController: NavController) {
    val rutaXml = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val ruta = RetrofitClient.api.obtenerRuta()
            rutaXml.value = ruta.Mapa
        } catch (e: Exception) {
            Log.e("API", "Error al obtener ruta: ${e.message}", e)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            rutaXml.value?.let {
                KMLMapWithIdaRegresoPolylines(it)
            } ?: Text("Cargando mapa...", modifier = Modifier.padding(16.dp))
        }

        Button(
            onClick = { navController.navigate("userScreen") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Ir a UserScreen")
        }
    }
}

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
                                    if (lat != null && lon != null) {
                                        val latLng = LatLng(lat, lon)
                                        if (isPoint) {
                                            paradas.add(Pair(latLng, currentName ?: "Parada"))
                                        } else {
                                            currentRoute?.add(latLng)
                                        }
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
                            if (it.isNotEmpty()) {
                                when (currentRouteType) {
                                    "ida" -> rutasIda.add(it)
                                    "regreso" -> rutasRegreso.add(it)
                                    else -> rutasIda.add(it)
                                }
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
