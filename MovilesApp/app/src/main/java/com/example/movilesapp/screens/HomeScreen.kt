package com.example.movilesapp.screens

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.items
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.movilesapp.viewModel.HomeScreenVM

@Composable
fun HomeScreen(
    isDarkMode: Boolean,
    navController: NavHostController,
    viewModel: HomeScreenVM = viewModel()
) {
    val rutas by viewModel.rutas.collectAsState()
    val checkboxStates = remember { mutableStateMapOf<String, Boolean>() }

    rutas.forEach { ruta ->
        if (checkboxStates[ruta.nombre] == null) {
            checkboxStates[ruta.nombre] = false
        }
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

        // Barra de búsqueda
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (isDarkMode) Color.DarkGray else Color(0xFFF5F5F5),
                    RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Buscar rutas",
                color = Color.Gray,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filtro",
                tint = if (isDarkMode) Color.White else Color.Black
            )
        }

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
                items(rutas) { ruta ->
                    val isChecked = checkboxStates[ruta.nombre] ?: false

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
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { checked ->
                                checkboxStates[ruta.nombre] = checked
                            }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
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


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(isDarkMode = false, navController = navController)
}
