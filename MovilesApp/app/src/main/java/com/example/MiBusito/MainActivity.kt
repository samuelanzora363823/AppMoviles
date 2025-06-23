package com.example.MiBusito

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.MiBusito.ui.components.BottomBar
import com.example.MiBusito.ui.navigation.NavGraph
import com.example.MiBusito.ui.theme.MovilesAppTheme
import com.example.MiBusito.viewmodels.AuthViewModel
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        MobileAds.initialize(this)
        enableEdgeToEdge()

        setContent {
            val authViewModel: AuthViewModel = viewModel()
            val isDarkMode by authViewModel.isDarkMode.collectAsState()

            val navController = rememberNavController()
            val showBottomBar = remember { mutableStateOf(true) }
            val currentRoute by navController.currentBackStackEntryAsState()
            val isAtRoot = currentRoute?.destination?.route in listOf("home", "favorites", "profile")

            BackHandler(enabled = isAtRoot) {
                finish()
            }

            MovilesAppTheme(darkTheme = isDarkMode) {
                LaunchedEffect(navController) {
                    navController.currentBackStackEntryFlow.collect { backStackEntry ->
                        showBottomBar.value = when (backStackEntry.destination.route) {
                            "home", "favorites", "profile", "login", "register" -> true
                            else -> false
                        }
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar.value) {
                            Column {
                                BottomBar(
                                    navController = navController,
                                    authViewModel = authViewModel
                                )
                                AdMobBanner(adUnitId = Constants.BANNER_AD_UNIT_ID)
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavGraph(
                            navController = navController,
                            isDarkMode = isDarkMode,
                            onToggleDarkMode = { authViewModel.toggleDarkMode(it) },
                            authViewModel = authViewModel
                        )
                    }
                }
            }
        }
    }
}
