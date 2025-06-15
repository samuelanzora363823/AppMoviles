package com.example.movilesapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movilesapp.viewmodel.RutasViewModel


@Composable
fun FavoriteRoutesScreen(viewModel: RutasViewModel, isDarkMode: Boolean) {
    // Usamos snapshotFlow para observar cambios reactivos en la lista
    val favoriteRoutes = viewModel.favoriteRoutes

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

        if (favoriteRoutes.isEmpty()) {
            Text(
                text = "No hay rutas favoritas seleccionadas.",
                color = if (isDarkMode) Color.Gray else Color.DarkGray
            )
        }

        favoriteRoutes.forEach { ruta ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(
                        if (isDarkMode) Color.DarkGray else Color.LightGray,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(16.dp)
            ) {
                IconButton(
                    onClick = {
                        viewModel.toggleFavorito(ruta)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Favorito",
                        tint = Color.Red
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = ruta.Nombre,
                    fontSize = 18.sp,
                    color = if (isDarkMode) Color.White else Color.Black
                )
            }
        }
    }
}
