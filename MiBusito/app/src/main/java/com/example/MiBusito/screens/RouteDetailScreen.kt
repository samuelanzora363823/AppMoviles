import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.MiBusito.screens.KMLMapWithIdaRegresoPolylines
import com.example.MiBusito.viewModel.RouteDetailScreenVM
import com.example.MiBusito.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteDetailScreen(
    id: Int,
    onBackClick: () -> Unit,
    isDarkMode: Boolean,
    viewModel: RouteDetailScreenVM = viewModel(),
    authViewModel: AuthViewModel
) {
    val rutaDetalle by viewModel.rutaDetalle
    val sheetState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(id) {
        viewModel.cargarRutaDetalle(id)
    }

    if (rutaDetalle == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val ruta = rutaDetalle!!
    val isFavorite = remember { mutableStateOf(authViewModel.isFavorite(ruta.nombre)) }

    val backgroundColor = if (isDarkMode) Color.Black else Color.White
    val primaryTextColor = if (isDarkMode) Color.White else Color.Black
    val secondaryTextColor = if (isDarkMode) Color.LightGray else Color.DarkGray
    val iconTintColor = if (isDarkMode) Color.White else Color.Black

    val scrollState = rememberScrollState()

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetPeekHeight = 120.dp,
        sheetContainerColor = backgroundColor,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                Spacer(Modifier.height(16.dp))

                // Header actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        modifier = Modifier.clickable { onBackClick() },
                        tint = iconTintColor
                    )
                    Icon(
                        imageVector = if (isFavorite.value) Icons.Filled.Favorite else Icons.Default.BookmarkBorder,
                        contentDescription = "Guardar como favorita",
                        modifier = Modifier.clickable {
                            authViewModel.toggleFavorite(ruta.nombre)
                            isFavorite.value = authViewModel.isFavorite(ruta.nombre)
                        },
                        tint = Color.Red
                    )
                }

                Spacer(Modifier.height(20.dp))

                // Información principal de la ruta
                Text(
                    text = ruta.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = primaryTextColor
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Costo del pasaje: $${ruta.costoPasaje}",
                    fontSize = 16.sp,
                    color = secondaryTextColor
                )

                Spacer(Modifier.height(12.dp))

                // Detalle del recorrido
                Text(
                    text = "Recorrido",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = primaryTextColor
                )
                Text(
                    text = ruta.ruta,
                    fontSize = 14.sp,
                    color = secondaryTextColor
                )

                Spacer(Modifier.height(16.dp))

                // Paradas
                Text(
                    text = "Paradas",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = primaryTextColor
                )

                Spacer(Modifier.height(4.dp))

                ruta.paradas.forEach { parada ->
                    Text(
                        text = "• ${parada.nombre}",
                        fontSize = 14.sp,
                        color = secondaryTextColor,
                        modifier = Modifier.padding(start = 8.dp, top = 2.dp, bottom = 2.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    ) {
        // Mapa de fondo
        Box(
            Modifier
                .fillMaxSize()
                .background(if (isDarkMode) Color.DarkGray else Color.LightGray)
        ) {
            KMLMapWithIdaRegresoPolylines(kmlText = ruta.mapa)
        }
    }
}
