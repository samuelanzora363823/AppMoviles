package com.example.MiBusito.ui.navigation

import LoginScreen
import RegisterScreen
import RouteDetailScreen
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.MiBusito.screens.FavoriteRoutesScreen
import com.example.MiBusito.screens.HomeScreen
import com.example.MiBusito.ui.screens.ProfileScreen
import com.example.MiBusito.ui.theme.MovilesAppTheme
import com.example.MiBusito.viewmodels.AuthViewModel
import kotlinx.coroutines.delay
import com.example.MiBusito.viewModel.HomeScreenVM

@Composable
fun NavGraph(
    navController: NavHostController,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    authViewModel: AuthViewModel
) {
    // Obtén el ViewModel de la pantalla principal
    val homeViewModel: HomeScreenVM = viewModel()

    MovilesAppTheme(darkTheme = isDarkMode) {
        NavHost(
            navController = navController,
            startDestination = "splash"
        ) {
            composable("splash") {
                val alreadyRedirected = remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    if (!alreadyRedirected.value) {
                        alreadyRedirected.value = true
                        delay(2000)
                        navController.navigate("home") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            composable("home") {
                HomeScreen(
                    isDarkMode = isDarkMode,
                    navController = navController,
                    rutas = homeViewModel.rutas.collectAsState().value,
                    rutasDetalle = emptyList()
                )
            }


            composable("favorites") {
                val rutas by homeViewModel.rutas.collectAsState()
                FavoriteRoutesScreen(
                    authViewModel = authViewModel,
                    isDarkMode = isDarkMode,
                    navController = navController,
                    allRutas = rutas
                )
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
                    authViewModel = authViewModel,
                    onRegisterSuccess = { _, _ ->
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
                    }
                )
            }

            composable("routeDetail/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: return@composable
                RouteDetailScreen(
                    id = id,
                    onBackClick = { navController.popBackStack() },
                    isDarkMode = isDarkMode,
                    authViewModel = authViewModel
                )
            }

            composable("profile") {
                ProfileScreen(
                    navController = navController,
                    authViewModel = authViewModel,
                    isDarkMode = isDarkMode,
                    onToggleDarkMode = onToggleDarkMode,
                    onNavigateToLogin = {
                        navController.navigate("login") {
                            popUpTo("profile") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
