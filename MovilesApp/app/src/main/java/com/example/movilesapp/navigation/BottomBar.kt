// archivo: ui/components/BottomBar.kt
package com.example.movilesapp.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.movilesapp.ui.navigation.BottomNavItem


@Composable
fun BottomBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorites,
        BottomNavItem.Profile

    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.popBackStack(0, inclusive = true) // ✅ limpia TODO el stack
                        navController.navigate(item.route) {
                            launchSingleTop = true
                        }
                    }
                }


            )
        }
    }
}