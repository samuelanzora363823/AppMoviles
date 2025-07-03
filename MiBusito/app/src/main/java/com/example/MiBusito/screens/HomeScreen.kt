package com.example.MiBusito.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.MiBusito.model.Ruta
import com.example.MiBusito.model.RutaDetalle

@Composable
fun HomeScreen(
    isDarkMode: Boolean,
    navController: NavHostController,
    rutas: List<Ruta>,
    rutasDetalle: List<RutaDetalle> = emptyList()
) {
    var searchQuery by remember { mutableStateOf("") }

    // Mapa para acceso rápido a RutaDetalle por id
    val rutasDetalleMap = remember(rutasDetalle) {
        rutasDetalle.associateBy { it.id }
    }

    // Búsqueda por nombre, recorrido, paradas y XML del mapa
    val filteredRutas = rutas.filter { ruta ->
        val query = searchQuery.trim().lowercase()

        val detalle = rutasDetalleMap[ruta.id]
        val paradasXml = extraerParadasDesdeMapa(ruta.mapa)

        val fullSearchText = buildString {
            append(ruta.nombre.lowercase()).append(" ")
            append(ruta.ruta.lowercase()).append(" ")
            detalle?.paradas?.forEach { append(it.nombre.lowercase()).append(" ") }
            paradasXml.forEach { append(it.lowercase()).append(" ") }
        }

        query.isEmpty() || fullSearchText.contains(query)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDarkMode) Color.Black else Color.White)
            .padding(20.dp)
    ) {
        Text(
            text = "Bienvenido!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDarkMode) Color.White else Color.Black
        )
        Text(
            text = "Explora San Salvador 🇸🇻",
            color = if (isDarkMode) Color.Gray else Color.DarkGray
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Buscar rutas, recorrido o paradas") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = if (isDarkMode) Color.DarkGray else Color(0xFFF5F5F5),
                focusedContainerColor = if (isDarkMode) Color.DarkGray else Color(0xFFF5F5F5),
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                cursorColor = if (isDarkMode) Color.White else Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (isDarkMode) Color.DarkGray else Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(12.dp)
                )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Rutas populares",
            fontWeight = FontWeight.Bold,
            color = if (isDarkMode) Color.White else Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredRutas.isEmpty()) {
            Text(
                text = "No se encontraron rutas.",
                color = if (isDarkMode) Color.White else Color.Black,
                modifier = Modifier.padding(20.dp)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredRutas) { ruta ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (isDarkMode) Color.DarkGray else Color(0xFFEFEFEF),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(12.dp)
                            .clickable {
                                navController.navigate("routeDetail/${ruta.id}")
                            }
                    ) {
                        Column {
                            Text(
                                text = ruta.nombre,
                                color = if (isDarkMode) Color.White else Color.Black,
                                fontWeight = FontWeight.Medium
                            )

                            // Mostrar texto si coincidió con alguna parada
                            if (
                                searchQuery.isNotEmpty() &&
                                (
                                        rutasDetalleMap[ruta.id]?.paradas?.any {
                                            it.nombre.contains(searchQuery, ignoreCase = true)
                                        } == true ||
                                                extraerParadasDesdeMapa(ruta.mapa).any {
                                                    it.contains(searchQuery, ignoreCase = true)
                                                }
                                        )
                            ) {
                                Text(
                                    text = "Coincide con una parada",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Extrae los nombres de paradas desde un String XML (KML)
 * buscando etiquetas <name>...</name>
 */
fun extraerParadasDesdeMapa(xml: String): List<String> {
    val regex = Regex("<name>(.*?)</name>")
    return regex.findAll(xml).map { it.groupValues[1].trim() }.toList()
}
