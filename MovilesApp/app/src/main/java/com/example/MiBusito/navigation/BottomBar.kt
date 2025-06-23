// archivo: ui/components/BottomBar.kt
package com.example.MiBusito.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.MiBusito.ui.navigation.BottomNavItem
import com.example.MiBusito.viewmodels.AuthViewModel


@Composable
fun BottomBar(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorites,
        BottomNavItem.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    NavigationBar {
        items.forEach { item ->
            val selected = when (item.route) {
                "profile" -> currentRoute == "profile"
                else -> currentRoute == item.route
            }

            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = selected,
                onClick = {
                    val destination = when (item.route) {
                        "profile" -> if (isLoggedIn) "profile" else "login"
                        else -> item.route
                    }

                    if (currentRoute != destination) {
                        navController.popBackStack(0, inclusive = true)
                        navController.navigate(destination) {
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}
