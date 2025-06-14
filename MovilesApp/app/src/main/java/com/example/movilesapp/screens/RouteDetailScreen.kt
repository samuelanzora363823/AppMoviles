package com.example.movilesapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.movilesapp.models.Ruta
import com.example.movilesapp.utils.extraerUrlMapa
import com.example.movilesapp.viewmodel.RutasViewModel
import java.net.URLEncoder



@Composable
fun RouteDetailScreen(
    ruta: Ruta,
    navController: NavHostController,
    onBackClick: () -> Unit,
    isDarkMode: Boolean,
    viewModel: RutasViewModel
) {
    val backgroundColor = if (isDarkMode) Color.Black else Color.White
    val primaryTextColor = if (isDarkMode) Color.White else Color.Black
    val secondaryTextColor = if (isDarkMode) Color.LightGray else Color.DarkGray
    val cardBackgroundColor = if (isDarkMode) Color(0xFF333333) else Color(0xFFF5F5F5)
    val iconTintColor = if (isDarkMode) Color.White else Color.Black

    val urlMapa = extraerUrlMapa(ruta.Mapa) ?: ""
    val isFavorito = viewModel.isFavorito(ruta)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver",
                modifier = Modifier.clickable { onBackClick() },
                tint = iconTintColor
            )
            Icon(
                imageVector = Icons.Default.BookmarkBorder,
                contentDescription = "Guardar",
                tint = if (isFavorito) Color(0xFFFFC107) else iconTintColor,
                modifier = Modifier.clickable {
                    viewModel.toggleFavorito(ruta)
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Icon(
            imageVector = Icons.Filled.Place,
            contentDescription = "Ubicación",
            tint = Color(0xFF2196F3),
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(cardBackgroundColor, RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "Ruta: ${ruta.Nombre}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = primaryTextColor
                )
                Text(
                    text = "Precio: $${ruta.CostoPasaje}",
                    color = secondaryTextColor
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Recorrido",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = primaryTextColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = ruta.Ruta,
            color = secondaryTextColor
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.Black, RoundedCornerShape(16.dp))
                .clickable {
                    if (urlMapa.isNotEmpty()) {
                        val urlEncoded = URLEncoder.encode(urlMapa, "UTF-8")
                        navController.navigate("mapScreen/$urlEncoded")
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Ver rutas",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }

    println("URL extraída: $urlMapa")
}





