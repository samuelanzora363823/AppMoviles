import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movilesapp.viewModel.RouteDetailScreenVM


@Composable
fun RouteDetailScreen(
    id: Int,
    onBackClick: () -> Unit,
    isDarkMode: Boolean,
    viewModel: RouteDetailScreenVM = viewModel()
) {
    val rutaDetalle by viewModel.rutaDetalle

    LaunchedEffect(id) {
        viewModel.cargarRutaDetalle(id)
    }

    if (rutaDetalle == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val ruta = rutaDetalle!!

    val backgroundColor = if (isDarkMode) Color.Black else Color.White
    val primaryTextColor = if (isDarkMode) Color.White else Color.Black
    val secondaryTextColor = if (isDarkMode) Color.LightGray else Color.DarkGray
    val cardBackgroundColor = if (isDarkMode) Color(0xFF333333) else Color(0xFFF5F5F5)
    val iconTintColor = if (isDarkMode) Color.White else Color.Black

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header: íconos
        item {
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
                    tint = iconTintColor
                )
            }
        }

        // Icono ubicación
        item {
            Icon(
                imageVector = Icons.Filled.Place,
                contentDescription = "Ubicación",
                tint = Color(0xFF2196F3),
                modifier = Modifier
                    .size(56.dp)
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }

        // Caja con nombre y precio
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(cardBackgroundColor, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = ruta.nombre,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = primaryTextColor
                    )
                    Text(
                        text = "Precio: $${ruta.costoPasaje}",
                        color = secondaryTextColor
                    )
                }
            }
        }

        // Detalles título
        item {
            Text(
                text = "Detalles de la ruta:",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = primaryTextColor
            )
        }

        // Detalles info
        item {
            Text(
                text = "Ruta: ${ruta.ruta}",
                color = secondaryTextColor
            )
        }

        // Paradas título
        item {
            Text(
                text = "Paradas:",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = primaryTextColor
            )
        }

        // Lista de paradas
        items(ruta.paradas) { parada ->
            Text(
                text = "- ${parada.nombre}",
                color = secondaryTextColor,
                fontSize = 14.sp
            )
        }

        // Botón ver rutas
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(Color.Black, RoundedCornerShape(12.dp))
                    .clickable {
                        // Acción para ver el mapa
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Ver rutas",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}


