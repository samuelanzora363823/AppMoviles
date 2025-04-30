package com.example.movilesapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movilesapp.ui.screens.HomeScreen
import com.example.movilesapp.ui.screens.SplashScreen
import com.example.movilesapp.ui.screens.Profile
import com.example.movilesapp.ui.theme.MovilesAppTheme

@Composable
fun NavGraph(navController: NavHostController) {
    // Gestionamos el estado del modo oscuro
    var isDarkMode = remember { mutableStateOf(false) } // Usamos mutableStateOf

    // Aplicamos el tema globalmente según el estado de isDarkMode
    MovilesAppTheme(darkTheme = isDarkMode.value) {
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable("home") {
                // Pasamos el estado de isDarkMode a la pantalla de Home
                HomeScreen(isDarkMode = isDarkMode.value)
            }
            composable("splash") {
                SplashScreen(navController)
            }
            composable("profile") {
                // En el perfil, permitimos cambiar el estado de isDarkMode
                Profile(isDarkMode = isDarkMode.value, onToggleDarkMode = { isDarkMode.value = it })
            }
        }
    }
}
