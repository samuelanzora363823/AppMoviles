package com.example.movilesapp.ui.screens

import LoginScreen
import RegisterScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movilesapp.viewmodels.AuthViewModel

// Estados para controlar la navegación
enum class AuthScreen {
    PROFILE, LOGIN, REGISTER
}

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel = viewModel(),
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit
) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    var currentScreen by remember { mutableStateOf(if (isLoggedIn) AuthScreen.PROFILE else AuthScreen.LOGIN) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    when (currentScreen) {
        AuthScreen.PROFILE -> {
            ProfileAuthenticated(
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode,
                onLogout = {
                    authViewModel.logout()
                    currentScreen = AuthScreen.LOGIN
                }
            )
        }
        AuthScreen.LOGIN -> LoginScreen(
            isDarkMode = isDarkMode,
            onLoginSuccess = { email, password ->
                authViewModel.login(
                    email = email,
                    password = password,
                    onSuccess = {
                        currentScreen = AuthScreen.PROFILE
                        errorMessage = null
                    },
                    onError = { message -> errorMessage = message }
                )
            },
            onRegisterClick = { currentScreen = AuthScreen.REGISTER },
            onBackClick = { /* No hay acción de retroceso porque es la pantalla principal */ }
        )
        AuthScreen.REGISTER -> RegisterScreen(
            isDarkMode = isDarkMode,
            onRegisterSuccess = { name, email, password ->
                authViewModel.login(
                    email = email,
                    password = password,
                    onSuccess = { currentScreen = AuthScreen.PROFILE },
                    onError = {}
                )
            },
            onLoginClick = { currentScreen = AuthScreen.LOGIN },
            onBackClick = { currentScreen = AuthScreen.LOGIN }
        )
    }
}

// Resto del código permanece igual...
// 2. Pantalla de perfil autenticado (TU DISEÑO EXACTO)
@Composable
fun ProfileAuthenticated(
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    onLogout: () -> Unit
) {
    val backgroundColor = if (isDarkMode) Color.Black else Color.White
    val primaryTextColor = if (isDarkMode) Color.White else Color.Black
    val secondaryTextColor = if (isDarkMode) Color.LightGray else Color.DarkGray
    val iconTintColor = primaryTextColor

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Mi perfil",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = primaryTextColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Avatar con ícono de usuario
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(if (isDarkMode) Color(0xFF333333) else Color(0xFFBFDBFE)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Foto de perfil",
                    tint = if (isDarkMode) Color(0xFF93C5FD) else Color(0xFF1E3A8A),
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Isaac Cañas",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = primaryTextColor
            )
            Text(
                text = "test@gmail.com",
                color = secondaryTextColor
            )

            Spacer(modifier = Modifier.height(24.dp))

            ProfileItem(
                icon = Icons.Default.Phone,
                label = "Teléfono: 0000-0000",
                isDarkMode = isDarkMode
            )
            ProfileItem(
                icon = Icons.Default.FavoriteBorder,
                label = "Mis favoritos",
                trailing = Icons.Default.ChevronRight,
                isDarkMode = isDarkMode
            )
            ProfileItem(
                icon = Icons.Default.ChatBubbleOutline,
                label = "Comentarios",
                trailing = Icons.Default.ChevronRight,
                isDarkMode = isDarkMode
            )
            ProfileItem(
                icon = Icons.Default.StarBorder,
                label = "Suscripciones",
                trailing = Icons.Default.ChevronRight,
                isDarkMode = isDarkMode
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Modo oscuro con switch
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.DarkMode,
                    contentDescription = "Modo oscuro",
                    tint = iconTintColor
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Modo oscuro",
                    fontSize = 16.sp,
                    color = primaryTextColor
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = onToggleDarkMode,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        uncheckedThumbColor = Color.Gray,
                        checkedTrackColor = Color.DarkGray,
                        uncheckedTrackColor = Color.LightGray
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botón cerrar sesión
            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDarkMode) Color.DarkGray else Color.Gray
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = "Cerrar sesión", color = Color.White)
            }
        }
    }
}

// 3. Pantalla de perfil no autenticado
@Composable
fun ProfileUnauthenticated(
    onLoginClick: () -> Unit,
    isDarkMode: Boolean
) {
    val backgroundColor = if (isDarkMode) Color.Black else Color.White
    val primaryTextColor = if (isDarkMode) Color.White else Color.Black

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar con ícono de usuario (versión no autenticada)
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(if (isDarkMode) Color(0xFF333333) else Color(0xFFBFDBFE)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Usuario no autenticado",
                    tint = if (isDarkMode) Color(0xFF93C5FD) else Color(0xFF1E3A8A),
                    modifier = Modifier.size(80.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "No has iniciado sesión",
                color = primaryTextColor,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onLoginClick,
                modifier = Modifier.width(200.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDarkMode) Color(0xFFBB86FC) else Color(0xFF6200EE)
                )
            ) {
                Text("Iniciar sesión", color = Color.White)
            }
        }
    }
}

// 4. Componente ProfileItem (igual al tuyo)
@Composable
fun ProfileItem(
    icon: ImageVector,
    label: String,
    trailing: ImageVector? = null,
    isDarkMode: Boolean,
    onClick: () -> Unit = {}
) {
    val textColor = if (isDarkMode) Color.White else Color.Black
    val iconColor = textColor

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = iconColor
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            color = textColor
        )
        Spacer(modifier = Modifier.weight(1f))
        trailing?.let {
            Icon(
                it,
                contentDescription = null,
                tint = iconColor
            )
        }
    }
}




// Previews para pruebas
@Preview(showBackground = true, name = "Perfil Autenticado - Light")
@Composable
fun ProfileAuthenticatedLightPreview() {
    ProfileAuthenticated(
        isDarkMode = false,
        onToggleDarkMode = {},
        onLogout = {}
    )
}

@Preview(showBackground = true, name = "Perfil Autenticado - Dark")
@Composable
fun ProfileAuthenticatedDarkPreview() {
    ProfileAuthenticated(
        isDarkMode = true,
        onToggleDarkMode = {},
        onLogout = {}
    )
}

@Preview(showBackground = true, name = "Perfil No Autenticado - Light")
@Composable
fun ProfileUnauthenticatedLightPreview() {
    ProfileUnauthenticated(
        onLoginClick = {},
        isDarkMode = false
    )
}

@Preview(showBackground = true, name = "Perfil No Autenticado - Dark")
@Composable
fun ProfileUnauthenticatedDarkPreview() {
    ProfileUnauthenticated(
        onLoginClick = {},
        isDarkMode = true
    )
}