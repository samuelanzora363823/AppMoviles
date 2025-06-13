package com.example.movilesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.android.gms.ads.MobileAds // ✅ Import necesario
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.movilesapp.ui.components.BottomBar
import com.example.movilesapp.ui.navigation.NavGraph
import com.example.movilesapp.ui.theme.MovilesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Inicializa AdMob
        MobileAds.initialize(this)

        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            val showBottomBar = remember { mutableStateOf(true) }

            val currentRoute by navController.currentBackStackEntryAsState()
            val isAtRoot = currentRoute?.destination?.route in listOf("home", "favorites", "profile")

            BackHandler(enabled = isAtRoot) {
                finish()
            }

            MovilesAppTheme {
                val systemDarkMode = isSystemInDarkTheme()
                val isDarkMode = remember { mutableStateOf(systemDarkMode) }

                LaunchedEffect(navController) {
                    navController.currentBackStackEntryFlow.collect { backStackEntry ->
                        showBottomBar.value = when (backStackEntry.destination.route) {
                            "home", "favorites", "profile" -> true
                            else -> false
                        }
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar.value) {
                            Column {
                                BottomBar(navController = navController)
                                AdMobBanner(adUnitId = "ca-app-pub-3940256099942544/6300978111") // ✅ Usa ID de prueba en desarrollo
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavGraph(
                            navController = navController,
                            isDarkMode = isDarkMode.value,
                            onToggleDarkMode = { newValue -> isDarkMode.value = newValue }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    MovilesAppTheme {
        val navController = rememberNavController()
        NavGraph(navController = navController, isDarkMode = false, onToggleDarkMode = {})
    }
}
