package com.example.MiBusito.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.MiBusito.model.Ruta
import com.example.MiBusito.viewModel.HomeScreenVM
import com.example.MiBusito.viewmodels.AuthViewModel




@Composable
fun FavoriteRoutesScreen(
    authViewModel: AuthViewModel,
    isDarkMode: Boolean,
    navController: NavHostController,
    allRutas: List<Ruta>
) {
    val favoriteRoutes by authViewModel.favoriteRoutes.collectAsState()

    // Filtrar rutas completas según los nombres guardados como favoritos
    val favoriteRutaObjects = remember(favoriteRoutes, allRutas) {
        allRutas.filter { ruta -> favoriteRoutes.contains(ruta.nombre) }
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

        if (favoriteRutaObjects.isEmpty()) {
            Text(
                text = "No hay rutas favoritas seleccionadas.",
                color = if (isDarkMode) Color.Gray else Color.DarkGray
            )
        } else {
            favoriteRutaObjects.forEach { ruta ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(
                            if (isDarkMode) Color.DarkGray else Color.LightGray,
                            shape = MaterialTheme.shapes.small
                        )
                        .clickable {
                            navController.navigate("routeDetail/${ruta.id}")
                        }
                        .padding(16.dp)
                ) {
                    IconButton(onClick = {
                        authViewModel.toggleFavorite(ruta.nombre)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Quitar de favoritos",
                            tint = Color.Red
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = ruta.nombre,
                        fontSize = 18.sp,
                        color = if (isDarkMode) Color.White else Color.Black
                    )
                }
            }
        }
    }
}
