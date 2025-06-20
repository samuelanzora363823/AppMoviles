package com.example.movilesapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Home : BottomNavItem("home", Icons.Filled.Home, "Inicio")
    object Favorites : BottomNavItem("favorites", Icons.Filled.Favorite, "Favoritas")
    object Profile : BottomNavItem("login", Icons.Filled.Person, "Login")
}
