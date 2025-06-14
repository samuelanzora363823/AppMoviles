





package com.example.movilesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.android.gms.ads.MobileAds // Anuncion
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

        //Inicializa AdMob
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

                Column(modifier = Modifier.fillMaxSize()) {
                    Scaffold(
                        modifier = Modifier.weight(1f),
                        bottomBar = {
                            if (showBottomBar.value) {
                                BottomBar(navController = navController)
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

                    // AdMobBanner
                    if (showBottomBar.value) {
                        AdMobBanner(modifier = Modifier.fillMaxWidth())
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









