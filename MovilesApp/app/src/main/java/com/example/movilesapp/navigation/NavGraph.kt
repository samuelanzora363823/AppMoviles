package com.example.movilesapp.ui.navigation

import LoginScreen
import RegisterScreen
import RouteDetailScreen
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movilesapp.screens.FavoriteRoutesScreen
import com.example.movilesapp.screens.HomeScreen
import com.example.movilesapp.ui.screens.ProfileScreen
import com.example.movilesapp.ui.theme.MovilesAppTheme
import com.example.movilesapp.viewmodels.AuthViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    authViewModel: AuthViewModel
) {
    MovilesAppTheme(darkTheme = isDarkMode) {
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable("splash") {
                val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
                val alreadyRedirected = remember { mutableStateOf(false) }

                LaunchedEffect(isLoggedIn) {
                    if (!alreadyRedirected.value) {
                        alreadyRedirected.value = true
                        if (isLoggedIn) {
                            navController.navigate("profile") {
                                popUpTo("splash") { inclusive = true }
                            }
                        } else {
                            navController.navigate("login") {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                    }
                }

                // Interfaz simple de carga usando Material (NO material3)
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
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
                    },
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

            composable("favorites") {
                FavoriteRoutesScreen(
                    authViewModel = authViewModel,
                    isDarkMode = isDarkMode
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
