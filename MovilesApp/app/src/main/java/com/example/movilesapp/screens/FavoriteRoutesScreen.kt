package com.example.movilesapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FavoriteRoutesScreen(favoriteRoutes: MutableList<String>, isDarkMode: Boolean) {
    // Lista de rutas disponibles para marcar
    val allRoutes = listOf("29-A", "40-C", "29-B", "30-A", "37-C")

    // Estado para el estado de los favoritos
    val favoriteStates = remember { mutableStateMapOf<String, Boolean>() }

    // Inicializar los favoritos con las rutas que ya están en la lista de favoritas
    allRoutes.forEach { route ->
        if (favoriteStates[route] == null) {
            favoriteStates[route] = favoriteRoutes.contains(route)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDarkMode) Color.Black else Color.White)
            .padding(20.dp)
    ) {
        Text(
            text = "Rutas Favoritas",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDarkMode) Color.White else Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Si no hay rutas favoritas seleccionadas
        if (favoriteRoutes.isEmpty()) {
            Text(
                text = "No hay rutas favoritas seleccionadas.",
                color = if (isDarkMode) Color.Gray else Color.DarkGray
            )
        }

        // Filtrar solo las rutas que están marcadas como favoritas
        val favoriteRoutesFiltered = allRoutes.filter { favoriteStates[it] == true }

        // Mostrar la lista de rutas favoritas con los íconos de corazón
        favoriteRoutesFiltered.forEach { route ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(if (isDarkMode) Color.DarkGray else Color.LightGray, shape = MaterialTheme.shapes.small)
                    .padding(16.dp)
            ) {
                // Usamos el ícono de corazón lleno o vacío dependiendo del estado
                IconButton(
                    onClick = {
                        // Cambiar el estado de favorito
                        val isFavorite = !(favoriteStates[route] ?: false)
                        favoriteStates[route] = isFavorite

                        // Si está marcado, agregar a favoritos, si no, eliminarlo
                        if (isFavorite) {
                            if (!favoriteRoutes.contains(route)) {
                                favoriteRoutes.add(route)
                            }
                        } else {
                            favoriteRoutes.remove(route)
                        }
                    }
                ) {
                    // Mostrar el corazón lleno si es favorito, vacío si no lo es
                    Icon(
                        imageVector = if (favoriteStates[route] == true) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (isDarkMode) Color.Red else Color.Red
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = route,
                    fontSize = 18.sp,
                    color = if (isDarkMode) Color.White else Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FavoriteRoutesScreenPreview() {
    // Inicializando con rutas favoritas
    val favoriteRoutes = remember { mutableStateListOf("29-A", "40-C") }
    FavoriteRoutesScreen(
        favoriteRoutes = favoriteRoutes,
        isDarkMode = false
    )
}

