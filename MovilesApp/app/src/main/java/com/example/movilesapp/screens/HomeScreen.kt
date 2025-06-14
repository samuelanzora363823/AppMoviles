import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import com.example.movilesapp.api.RetrofitClient
import com.example.movilesapp.models.Ruta

@Composable
fun HomeScreen(isDarkMode: Boolean, navController: NavHostController) {
    val rutasList = remember { mutableStateListOf<Ruta>() }
    val checkboxStates = remember { mutableStateMapOf<String, Boolean>() }
    var isLoading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        try {
            val rutasFromApi = RetrofitClient.api.obtenerRutas()
            rutasList.clear()
            rutasList.addAll(rutasFromApi)
            rutasFromApi.forEach { ruta ->
                if (checkboxStates[ruta.Nombre] == null) {
                    checkboxStates[ruta.Nombre] = ruta.Favorito
                }
            }
            isLoading = false
        } catch (e: Exception) {
            Log.e("API", "Error cargando rutas: ${e.message}")
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDarkMode) Color.Black else Color.White)
        ) {
            // Header fijo
            Column(modifier = Modifier.padding(20.dp)) {
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

                // Buscador
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (isDarkMode) Color.DarkGray else Color(0xFFF5F5F5),
                            RoundedCornerShape(12.dp)
                        ),
                    placeholder = {
                        Text(text = "Buscar rutas", color = Color.Gray)
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filtro",
                            tint = if (isDarkMode) Color.White else Color.Black,
                            modifier = Modifier.clickable {
                                // lógica para filtro
                            }
                        )
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

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

                Row {
                    FilterChip(text = "Más vistas", isSelected = true, isDarkMode = isDarkMode)
                    Spacer(modifier = Modifier.width(8.dp))
                    FilterChip(text = "Nearby", isDarkMode = isDarkMode)
                    Spacer(modifier = Modifier.width(8.dp))
                    FilterChip(text = "Latest", isDarkMode = isDarkMode)
                }
            }

            // Lista con scroll y filtrado
            val rutasFiltradas = if (searchQuery.isBlank()) {
                rutasList
            } else {
                rutasList.filter {
                    it.Nombre.contains(searchQuery, ignoreCase = true) ||
                            it.Ruta.contains(searchQuery, ignoreCase = true)
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                if (rutasFiltradas.isEmpty()) {
                    Text(
                        text = "No se encontraron rutas",
                        color = if (isDarkMode) Color.White else Color.Black,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    rutasFiltradas.forEach { ruta ->
                        val isChecked = checkboxStates[ruta.Nombre] ?: false
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable {
                                    navController.navigate("routeDetail/${ruta.Nombre}")
                                }
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { checked ->
                                    checkboxStates[ruta.Nombre] = checked
                                    // Actualizar estado de favorito en la API
                                    // RetrofitClient.api.actualizarFavorito(ruta.Id, checked)
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = ruta.Nombre,
                                color = if (isDarkMode) Color.White else Color.Black
                            )
                        }
                    }
                }
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
    val navController = rememberNavController()
    HomeScreen(isDarkMode = false, navController = navController)
}
