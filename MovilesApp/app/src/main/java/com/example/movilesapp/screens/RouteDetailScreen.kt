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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun RouteDetailScreen(
    routeName: String,
    onBackClick: () -> Unit,
    isDarkMode: Boolean // Recibimos el parámetro de isDarkMode
) {
    // Definición de los detalles de la ruta
    val price = "$25 por persona"
    val duration = "2 horas"
    val rating = 4.7f
    val places = listOf(
        "Plaza Mayor",
        "Catedral de la ciudad",
        "Museo de arte moderno",
        "Parque central"
    )

    // Colores para el modo oscuro y claro
    val backgroundColor = if (isDarkMode) Color.Black else Color.White
    val primaryTextColor = if (isDarkMode) Color.White else Color.Black
    val secondaryTextColor = if (isDarkMode) Color.LightGray else Color.DarkGray
    val cardBackgroundColor = if (isDarkMode) Color(0xFF333333) else Color(0xFFF5F5F5)
    val iconTintColor = if (isDarkMode) Color.White else Color.Black

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor) // Establece el color de fondo
            .padding(20.dp)
    ) {
        // Fila con los iconos de retroceso y guardar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver",
                modifier = Modifier.clickable { onBackClick() },
                tint = iconTintColor // Color del icono
            )
            Icon(
                imageVector = Icons.Default.BookmarkBorder,
                contentDescription = "Guardar",
                tint = iconTintColor // Color del icono
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Icono de ubicación
        Icon(
            imageVector = Icons.Filled.Place, // Usamos el ícono de ubicación de Material Icons
            contentDescription = "Ubicación",
            tint = Color(0xFF2196F3), // Color profesional
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Caja con el nombre de la ruta y el precio
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(cardBackgroundColor, RoundedCornerShape(12.dp)) // Color de fondo adecuado
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = routeName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = primaryTextColor // Color del texto
                )
                Text(
                    text = "Precio: $price",
                    color = secondaryTextColor // Color del texto
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Título para la sección de detalles
        Text(
            text = "Detalles",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = primaryTextColor // Color del texto
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Información sobre duración y calificación
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Duración: $duration", color = secondaryTextColor) // Color del texto
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "⭐ $rating", color = Color(0xFFFFC107)) // Color amarillo para la calificación
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de lugares
        places.forEach {
            Text(
                text = "- $it",
                color = secondaryTextColor, // Color del texto
                style = TextStyle(fontSize = 14.sp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Caja para el botón de "Ver rutas"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.Black, RoundedCornerShape(16.dp))
                .clickable { /* Acción al hacer clic */ },
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
}

@Preview(showBackground = true)
@Composable
fun RouteDetailScreenPreviewDarkMode() {
    RouteDetailScreen(
        routeName = "Ruta 29-A",
        onBackClick = {},
        isDarkMode = true // Cambia entre true o false para ver el efecto
    )
}

@Preview(showBackground = true)
@Composable
fun RouteDetailScreenPreviewLightMode() {
    RouteDetailScreen(
        routeName = "Ruta 29-A",
        onBackClick = {},
        isDarkMode = false // Cambia entre true o false para ver el efecto
    )
}


