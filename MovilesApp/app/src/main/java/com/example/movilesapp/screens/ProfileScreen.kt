package com.example.movilesapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.movilesapp.viewmodels.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel = viewModel(),
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {
            onNavigateToLogin()
        }
    }

    if (isLoggedIn) {
        val user = FirebaseAuth.getInstance().currentUser

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
            // Avatar
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(if (isDarkMode) Color.DarkGray else Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (user?.photoUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(user.photoUrl),
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

            // Nombre y correo
            Text(
                text = user?.displayName ?: "Nombre no disponible",
                color = textColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = user?.email ?: "Correo no disponible",
                color = secondaryTextColor,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Switch de modo claro/oscuro
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

            // Teléfono y correo como detalle
            ProfileDetailItem(
                icon = Icons.Default.Phone,
                label = user?.phoneNumber ?: "Teléfono no disponible",
                isDarkMode = isDarkMode
            )

            ProfileDetailItem(
                icon = Icons.Default.Email,
                label = user?.email ?: "Correo no disponible",
                isDarkMode = isDarkMode
            )

            Spacer(modifier = Modifier.weight(1f))

            // Botón logout
            Button(
                onClick = { authViewModel.logout() },
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
