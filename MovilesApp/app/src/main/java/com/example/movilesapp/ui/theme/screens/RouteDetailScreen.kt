import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Place // Icono de ubicación
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RouteDetailScreen(
    routeName: String,
    price: String,
    duration: String,
    rating: Float,
    places: List<String>,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {
        // Fila para los iconos de retroceso y guardar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Volver",
                modifier = Modifier.clickable { onBackClick() }
            )
            Icon(
                Icons.Default.BookmarkBorder,
                contentDescription = "Guardar"
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Icono de ubicación (reemplazando el icono personalizado)
        Icon(
            imageVector = Icons.Filled.Place, // Usamos el ícono de ubicación de Material Icons
            contentDescription = "Ubicación",
            tint = Color.Unspecified,
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
                Text(text = routeName, fontWeight = FontWeight.Bold)
                Text(text = "Precio: $price")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Título para la sección de detalles
        Text("Detalles", fontWeight = FontWeight.Bold)

        // Información sobre duración y calificación
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = duration)
            Spacer(modifier = Modifier.width(8.dp))
            Text("⭐ $rating")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Lista de lugares
        places.forEach {
            Text(text = "- $it", color = Color.DarkGray)
        }

        Spacer(modifier = Modifier.weight(1f))

        // Caja para el botón de "Ver rutas"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.Black, RoundedCornerShape(16.dp))
                .clickable { /* Acción */ },
            contentAlignment = Alignment.Center
        ) {
            Text("Ver rutas", color = Color.White)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun RouteDetailScreenPreview() {
    RouteDetailScreen(
        routeName = "Tour por el centro histórico",
        price = "$25 por persona",
        duration = "2 horas",
        rating = 4.7f,
        places = listOf(
            "Plaza Mayor",
            "Catedral de la ciudad",
            "Museo de arte moderno",
            "Parque central"
        ),
        onBackClick = {}
    )
}