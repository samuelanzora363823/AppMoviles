package com.example.movilesapp.ui.navigation

import LoginScreen
import RegisterScreen
import RouteDetailScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movilesapp.screens.FavoriteRoutesScreen
import com.example.movilesapp.screens.HomeScreen
import com.example.movilesapp.ui.screens.ProfileScreen
import com.example.movilesapp.ui.screens.SplashScreen
import com.example.movilesapp.ui.theme.MovilesAppTheme
import com.example.movilesapp.viewmodels.AuthViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    authViewModel: AuthViewModel
) {

    val favoriteRoutes = mutableListOf("29-A", "40-C")

    MovilesAppTheme(darkTheme = isDarkMode) {
        NavHost(
            navController = navController,
            startDestination = "splash"
        ) {
            composable("splash") {
                SplashScreen(navController = navController)
            }

            composable("home") {
                HomeScreen(isDarkMode = isDarkMode, navController = navController)
            }

            composable("login") {
                LoginScreen(
                    isDarkMode = isDarkMode,
                    authViewModel = authViewModel,
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
                    authViewModel = authViewModel,   // Pasamos authViewModel aquí
                    onRegisterSuccess = { email, password ->
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
                )
            }

            composable("favorites") {
                FavoriteRoutesScreen(
                    favoriteRoutes = favoriteRoutes,
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

            composable("profile") {
                ProfileScreen(
                    authViewModel = authViewModel,
                    isDarkMode = isDarkMode,
                    onToggleDarkMode = onToggleDarkMode,
                    onNavigateToLogin = { navController.navigate("login") }
                )
            }
        }
    }
}
