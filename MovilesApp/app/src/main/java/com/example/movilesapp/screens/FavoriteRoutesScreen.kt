package com.example.movilesapp.screens

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
import com.example.movilesapp.viewmodels.AuthViewModel


@Composable
fun FavoriteRoutesScreen(
    authViewModel: AuthViewModel,
    isDarkMode: Boolean
) {
    val favoriteRoutes by remember { derivedStateOf { authViewModel.favoriteRoutes } }

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

        favoriteRoutes.forEach { route ->
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
                        authViewModel.toggleFavorite(route)
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
                    text = route,
                    fontSize = 18.sp,
                    color = if (isDarkMode) Color.White else Color.Black
                )
            }
        }
    }
}
