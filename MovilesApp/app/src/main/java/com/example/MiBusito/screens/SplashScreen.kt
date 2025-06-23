package com.example.MiBusito.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.MiBusito.ui.theme.MovilesAppTheme
import kotlinx.coroutines.delay

// Versión principal con navegación
@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate("home") {
            popUpTo("splash") { inclusive = true }
        }
    }

    SplashScreenContent()
}

// Versión reutilizable del contenido visual sin lógica de navegación
@Composable
fun SplashScreenContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0073C5), // azul claro
                        Color(0xFF001B45)  // azul oscuro
                    )
                )
                // Eliminamos el RoundedCornerShape para evitar recortes
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Rutas503",
                fontSize = 38.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Find Your Dream",
                fontSize = 16.sp,
                color = Color.White
            )
            Text(
                text = "Destination With Us",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

// Vista previa que utiliza solo la UI sin lógica de navegación
@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    MovilesAppTheme(darkTheme = false) {
        SplashScreenContent()
    }
}
