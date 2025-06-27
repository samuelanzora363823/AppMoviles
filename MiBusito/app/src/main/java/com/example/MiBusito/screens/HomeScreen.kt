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
    rutasDetalle: List<RutaDetalle> = emptyList()  // Valor por defecto para evitar errores
) {
    var searchQuery by remember { mutableStateOf("") }

    // Mapa para acceso rápido a RutaDetalle por id
    val rutasDetalleMap = remember(rutasDetalle) {
        rutasDetalle.associateBy { it.id }
    }

    // Filtrado flexible buscando en TODO:
    val filteredRutas = rutas.filter { ruta ->
        val query = searchQuery.trim().lowercase()

        // Busca en nombre y ruta de Ruta
        val matchNombre = ruta.nombre.lowercase().contains(query)
        val matchRuta = ruta.ruta.lowercase().contains(query)

        // Busca en las paradas asociadas a la ruta (si existen)
        val matchParada = rutasDetalleMap[ruta.id]?.paradas?.any { parada ->
            parada.nombre.lowercase().contains(query)
        } ?: false

        // Aquí puedes agregar más campos si quieres (descripción, observaciones, etc.)

        // Retorna true si coincide en cualquiera de los campos
        query.isEmpty() || matchNombre || matchRuta || matchParada
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
            text = "Explora San Salvador \uD83C\uDDF8\uD83C\uDDFB",
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
            androidx.compose.foundation.lazy.LazyColumn(
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
                        Text(
                            text = ruta.nombre,
                            color = if (isDarkMode) Color.White else Color.Black,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
