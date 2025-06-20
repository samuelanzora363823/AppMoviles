package com.example.movilesapp.ui.navigation

import LoginScreen
import RegisterScreen
import RouteDetailScreen
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movilesapp.screens.FavoriteRoutesScreen
import com.example.movilesapp.screens.HomeScreen
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

            composable("login") {
                LoginScreen(
                    isDarkMode = isDarkMode,
                    onLoginSuccess = { _, _ ->
                        navController.navigate("profile") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onRegisterClick = {
                        navController.navigate("register")
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable("register") {
                RegisterScreen(
                    isDarkMode = isDarkMode,
                    onRegisterSuccess = { name, email, password ->
                        // Aquí puedes manejar el registro (Firebase, por ejemplo)
                        // Y navegar a profile o a home
                        navController.navigate("profile") {
                            popUpTo("register") { inclusive = true }
                        }
                    },
                    onLoginClick = {
                        navController.navigate("login") {
                            popUpTo("register") { inclusive = true }
                        }
                    },
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onGoogleSignInClick = {
                        // Acción para Google sign-in
                    },
                    onFacebookSignInClick = {
                        // Acción para Facebook sign-in
                    },
                    errorMessage = null
                )
            }

            composable("favorites") {
                FavoriteRoutesScreen(
                    favoriteRoutes = favoriteRoutes,  // Ahora es mutable
                    isDarkMode = isDarkMode
                )
            }

            composable("routeDetail/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: return@composable
                RouteDetailScreen(
                    id = id,
                    onBackClick = { navController.popBackStack() },
                    isDarkMode = isDarkMode
                )
            }

            // Puedes agregar aquí la pantalla "profile" u otras según tu app
        }
    }
}
