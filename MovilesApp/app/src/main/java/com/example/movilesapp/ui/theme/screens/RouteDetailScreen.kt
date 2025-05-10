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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun RouteDetailScreen(
    routeName: String,
    onBackClick: () -> Unit
) {
    // Definición de los detalles de la ruta (puedes reemplazar estos valores con datos dinámicos)
    val price = "$25 por persona"
    val duration = "2 horas"
    val rating = 4.7f
    val places = listOf(
        "Plaza Mayor",
        "Catedral de la ciudad",
        "Museo de arte moderno",
        "Parque central"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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
                tint = Color.Black
            )
            Icon(
                imageVector = Icons.Default.BookmarkBorder,
                contentDescription = "Guardar",
                tint = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Icono de ubicación
        Icon(
            imageVector = Icons.Filled.Place, // Usamos el ícono de ubicación de Material Icons
            contentDescription = "Ubicación",
            tint = Color(0xFF2196F3), // Color profesional, se puede ajustar según la paleta de colores
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Caja con el nombre de la ruta y el precio
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = routeName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Text(
                    text = "Precio: $price",
                    color = Color(0xFF757575)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Título para la sección de detalles
        Text(
            text = "Detalles",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Información sobre duración y calificación
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Duración: $duration", color = Color(0xFF757575))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "⭐ $rating", color = Color(0xFFFFC107))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de lugares
        places.forEach {
            Text(
                text = "- $it",
                color = Color.DarkGray,
                style = androidx.compose.ui.text.TextStyle(fontSize = 14.sp)
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
fun RouteDetailScreenPreview() {
    RouteDetailScreen(routeName = "Ruta 29-A", onBackClick = {})
}
