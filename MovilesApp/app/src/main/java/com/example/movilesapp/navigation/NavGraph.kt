package com.example.movilesapp.ui.navigation

import HomeScreen
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movilesapp.ui.screens.FavoriteRoutesScreen
import com.example.movilesapp.ui.screens.MapScreen
import com.example.movilesapp.ui.screens.ProfileScreen
import com.example.movilesapp.ui.screens.RouteDetailScreen
import com.example.movilesapp.ui.screens.SplashScreen
import com.example.movilesapp.ui.theme.MovilesAppTheme
import com.example.movilesapp.viewmodels.AuthViewModel
import com.example.movilesapp.viewmodel.RutasViewModel // Asegúrate de importar esto desde el paquete correcto
import java.net.URLDecoder

@Composable
fun NavGraph(
    navController: NavHostController,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit
) {
    val authViewModel: AuthViewModel = viewModel()
    val rutasViewModel: RutasViewModel = viewModel()

    MovilesAppTheme(darkTheme = isDarkMode) {
        NavHost(
            navController = navController,
            startDestination = "splash"
        ) {
            composable("splash") {
                SplashScreen(navController = navController)
            }

            composable("home") {
                HomeScreen(
                    isDarkMode = isDarkMode,
                    navController = navController
                )
            }

            composable("profile") {
                ProfileScreen(
                    authViewModel = authViewModel,
                    isDarkMode = isDarkMode,
                    onToggleDarkMode = onToggleDarkMode
                )
            }

            composable("favorites") {
                FavoriteRoutesScreen(
                    viewModel = rutasViewModel,
                    isDarkMode = isDarkMode
                )
            }

            composable("routeDetail/{routeName}") { backStackEntry ->
                val routeName = backStackEntry.arguments?.getString("routeName") ?: ""

                LaunchedEffect(routeName) {
                    rutasViewModel.cargarRutaPorNombre(routeName)
                }

                val ruta = rutasViewModel.rutaSeleccionada.collectAsState().value

                if (ruta != null) {
                    RouteDetailScreen(
                        ruta = ruta,
                        navController = navController,
                        onBackClick = { navController.popBackStack() },
                        isDarkMode = isDarkMode,
                        viewModel = rutasViewModel // PASAMOS EL ViewModel AQUÍ TAMBIÉN
                    )
                } else {
                    Text("Cargando ruta...")
                }
            }

            composable("mapScreen/{mapUrl}") { backStackEntry ->
                val urlEncoded = backStackEntry.arguments?.getString("mapUrl") ?: ""
                val urlMapa = URLDecoder.decode(urlEncoded, "UTF-8")

                MapScreen(
                    urlMapa = urlMapa,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
