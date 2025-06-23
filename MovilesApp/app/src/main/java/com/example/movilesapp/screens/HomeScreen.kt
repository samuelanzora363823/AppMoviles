package com.example.movilesapp.screens

import androidx.compose.ui.tooling.preview.Preview
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.movilesapp.viewModel.HomeScreenVM

@Composable
fun HomeScreen(
    isDarkMode: Boolean,
    navController: NavHostController,
    viewModel: HomeScreenVM = viewModel()
) {
    val rutas by viewModel.rutas.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filteredRutas = rutas.filter {
        it.nombre.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDarkMode) Color.Black else Color.White)
            .padding(20.dp)
    ) {
        Text(
            text = "Hi👋",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDarkMode) Color.White else Color.Black
        )
        Text(
            text = "Explora El Salvador",
            color = if (isDarkMode) Color.Gray else Color.DarkGray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Barra de búsqueda funcional
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Buscar rutas") },
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

        if (rutas.isEmpty()) {
            Text(
                text = "Cargando rutas...",
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
