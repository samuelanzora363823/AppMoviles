package com.example.movilesapp.ui.screens

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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

@Composable
fun HomeScreen(isDarkMode: Boolean) {
    // Lista de rutas
    val rutas = listOf("29-A", "29-C", "29-C2", "37-B", "40-A", "40-C")

    // Mapa con estado para cada checkbox
    val checkboxStates = remember { mutableStateMapOf<String, Boolean>() }
    rutas.forEach { ruta ->
        if (checkboxStates[ruta] == null) {
            checkboxStates[ruta] = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDarkMode) Color.Black else Color.White)
            .padding(20.dp)
    ) {
        // Saludo
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

        // Título sección
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Rutas populares",
                fontWeight = FontWeight.Bold,
                color = if (isDarkMode) Color.White else Color.Black
            )
            Text(
                text = "View all",
                color = if (isDarkMode) Color.Gray else Color.DarkGray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Filtros
        Row {
            FilterChip(text = "Más vistas", isSelected = true, isDarkMode = isDarkMode)
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(text = "Nearby", isDarkMode = isDarkMode)
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(text = "Latest", isDarkMode = isDarkMode)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de rutas con checkboxes seleccionables
        rutas.forEach { ruta ->
            val isChecked = checkboxStates[ruta] ?: false
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { checked ->
                        checkboxStates[ruta] = checked
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = ruta,
                    color = if (isDarkMode) Color.White else Color.Black
                )
            }
        }
    }
}

@Composable
fun FilterChip(text: String, isSelected: Boolean = false, isDarkMode: Boolean = false) {
    Box(
        modifier = Modifier
            .background(
                if (isSelected) Color.White else if (isDarkMode) Color.Gray else Color(0xFFF5F5F5),
                RoundedCornerShape(16.dp)
            )
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.Black else if (isDarkMode) Color.White else Color.Black,
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(isDarkMode = false)
}
