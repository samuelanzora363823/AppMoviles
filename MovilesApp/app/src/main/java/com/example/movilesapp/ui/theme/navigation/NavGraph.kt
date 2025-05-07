package com.example.movilesapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movilesapp.ui.screens.HomeScreen
import com.example.movilesapp.ui.screens.ProfileScreen
import com.example.movilesapp.ui.screens.SplashScreen
import com.example.movilesapp.ui.theme.MovilesAppTheme
import com.example.movilesapp.viewmodels.AuthViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    val isDarkMode = remember { mutableStateOf(false) }
    val authViewModel: AuthViewModel = viewModel()

    MovilesAppTheme(darkTheme = isDarkMode.value) {
        NavHost(
            navController = navController,
            startDestination = "splash"
        ) {
            composable("splash") {
                SplashScreen(navController = navController)
            }
            composable("home") {
                HomeScreen(isDarkMode = isDarkMode.value)
            }
            composable("profile") {
                ProfileScreen(
                    authViewModel = authViewModel,
                    isDarkMode = isDarkMode.value,
                    onToggleDarkMode = { newValue -> isDarkMode.value = newValue }
                )
            }
        }
    }
}