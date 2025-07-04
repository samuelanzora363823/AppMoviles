package com.example.MiBusito.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.MiBusito.viewmodels.AuthViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val user by authViewModel.firebaseUser.collectAsState()
    val hasNavigated = remember { mutableStateOf(false) }

    // Estado para controlar si el usuario eliminó anuncios (simulado)
    var hasAdsRemoved by rememberSaveable { mutableStateOf(false) }
    // Estado para mostrar el diálogo de simulación de pago
    var showPayDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn && !hasNavigated.value) {
            hasNavigated.value = true
            if (navController.currentBackStackEntry?.destination?.route == "profile") {
                onNavigateToLogin()
            }
        }
    }

    BackHandler(enabled = true) {
        if (navController.previousBackStackEntry?.destination?.route == "home") {
            navController.popBackStack()
        } else {
            navController.navigate("home") {
                launchSingleTop = true
                popUpTo("profile") { inclusive = true }
            }
        }
    }

    val currentUser = user
    if (isLoggedIn && currentUser != null) {
        val backgroundColor = if (isDarkMode) Color.Black else Color.White
        val textColor = if (isDarkMode) Color.White else Color.Black
        val secondaryTextColor = if (isDarkMode) Color.LightGray else Color.DarkGray

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (navController.previousBackStackEntry?.destination?.route == "home") {
                        navController.popBackStack()
                    } else {
                        navController.navigate("home") {
                            popUpTo("profile") { inclusive = true }
                        }
                    }
                }) {
                    // Puedes poner un ícono aquí si quieres
                }
            }

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(if (isDarkMode) Color.DarkGray else Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (currentUser.photoUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(currentUser.photoUrl),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Foto de perfil",
                        tint = Color.White,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = currentUser.displayName ?: "Nombre no disponible",
                color = textColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = currentUser.email ?: "Correo no disponible",
                color = secondaryTextColor,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isDarkMode) "Modo claro" else "Modo oscuro",
                    color = textColor,
                    fontSize = 16.sp
                )
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { onToggleDarkMode(!isDarkMode) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF3366FF),
                        uncheckedThumbColor = Color.Gray
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ProfileDetailItem(
                icon = Icons.Default.Email,
                label = currentUser.email ?: "Correo no disponible",
                isDarkMode = isDarkMode
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Texto estado de anuncios
            Text(
                text = if (hasAdsRemoved) "✅ ¡Gracias por eliminar los anuncios!" else "🚫 Los anuncios están activados.",
                color = if (hasAdsRemoved) Color(0xFF66BB6A) else Color.Red,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botón para simular pago
            Button(
                onClick = {
                    if (!hasAdsRemoved) {
                        showPayDialog = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !hasAdsRemoved,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDarkMode) Color(0xFF4CAF50) else Color(0xFF388E3C)
                )
            ) {
                Text(
                    text = if (hasAdsRemoved) "Anuncios eliminados" else "Eliminar anuncios (Simulado)",
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón cerrar sesión
            Button(
                onClick = {
                    hasNavigated.value = false
                    authViewModel.logout()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDarkMode) Color.Red.copy(alpha = 0.8f) else Color.Red
                )
            ) {
                Text("Cerrar sesión", color = Color.White)
            }
        }

        // Diálogo de simulación de pago
        if (showPayDialog) {
            AlertDialog(
                onDismissRequest = { showPayDialog = false },
                title = { Text("Simulación de compra") },
                text = { Text("¿Quieres simular el pago para eliminar los anuncios?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            hasAdsRemoved = true
                            showPayDialog = false
                        }
                    ) {
                        Text("Pagar")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showPayDialog = false
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun ProfileDetailItem(
    icon: ImageVector,
    label: String,
    isDarkMode: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isDarkMode) Color.White else Color.Black,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            color = if (isDarkMode) Color.White else Color.Black,
            fontSize = 16.sp
        )
    }
}
