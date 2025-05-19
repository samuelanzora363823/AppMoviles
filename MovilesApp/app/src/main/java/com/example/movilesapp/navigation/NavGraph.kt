package com.example.movilesapp.ui.navigation

import HomeScreen
import RouteDetailScreen
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movilesapp.ui.screens.FavoriteRoutesScreen
import com.example.movilesapp.ui.screens.ProfileScreen
import com.example.movilesapp.ui.screens.SplashScreen
import com.example.movilesapp.ui.theme.MovilesAppTheme
import com.example.movilesapp.viewmodels.AuthViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    isDarkMode: Boolean, // Recibimos el parámetro isDarkMode
    onToggleDarkMode: (Boolean) -> Unit // Recibimos el manejador para actualizar el valor de isDarkMode
) {
    val authViewModel: AuthViewModel = viewModel()

    // Hacer mutable la lista de rutas favoritas
    val favoriteRoutes = mutableListOf("29-A", "40-C") // Hacemos mutable la lista

    MovilesAppTheme(darkTheme = isDarkMode) {
        NavHost(
            navController = navController,
            startDestination = "splash"
        ) {
            composable("splash") {
                SplashScreen(navController = navController)
            }
            composable("home") {
                HomeScreen(isDarkMode = isDarkMode, navController = navController) // Pasa navController
            }
            composable("profile") {
                ProfileScreen(
                    authViewModel = authViewModel,
                    isDarkMode = isDarkMode,
                    onToggleDarkMode = onToggleDarkMode // Pasamos la función para actualizar el valor de isDarkMode
                )
            }
            composable("favorites") {
                FavoriteRoutesScreen(
                    favoriteRoutes = favoriteRoutes,  // Ahora es mutable
                    isDarkMode = isDarkMode
                )
            }
            composable("routeDetail/{routeName}") { backStackEntry ->
                val routeName = backStackEntry.arguments?.getString("routeName") ?: ""
                RouteDetailScreen(
                    routeName = routeName,
                    onBackClick = { navController.popBackStack() },
                    isDarkMode = isDarkMode // ✅ ahora sí se pasa el parámetro
                )
            }

            }
        }
    }


