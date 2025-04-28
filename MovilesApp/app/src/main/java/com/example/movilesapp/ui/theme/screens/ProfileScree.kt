package com.example.movilesapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.movilesapp.ui.navigation.NavGraph
import com.example.movilesapp.ui.theme.MovilesAppTheme

@Composable
fun Profile() {
    var isDarkMode by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDarkMode) Color.White else Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize() // <- usar todo el alto y ancho de la pantalla
                .padding(horizontal = 16.dp, vertical = 24.dp), // padding opcional si quieres algo de margen
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = if (isDarkMode) Color.Black else Color.Black,
                modifier = Modifier
                    .align(Alignment.Start)
                    .size(28.dp)
            )

            Text(
                text = "Mi perfil",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkMode) Color(0xFF60A5FA) else Color(0xFF1E40AF)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Avatar con ícono de usuario
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFBFDBFE)),
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
                color = if (isDarkMode) Color.Black else Color.White
            )
            Text(
                text = "test@gmail.com",
                color = if (isDarkMode) Color.Black else Color.White
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
                    tint = if (isDarkMode) Color.Black else Color.White
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Modo oscuro",
                    fontSize = 16.sp,
                    color = if (isDarkMode) Color.Black else Color.White
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { isDarkMode = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = if (isDarkMode) Color.White else Color.Black,
                        uncheckedThumbColor = if (isDarkMode) Color.Black else Color.White,
                        checkedTrackColor = Color.Black, // Color de la pista activada
                        uncheckedTrackColor = Color.Black // Color de la pista desactivada
                    )
                )

            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botón cerrar sesión
            Button(
                onClick = { /* Acción cerrar sesión */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDarkMode) Color.Black else Color.Gray
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = "Cerrar sesión", color = Color.White)
            }
        }
    }
}

@Composable
fun ProfileItem(
    icon: ImageVector,
    label: String,
    trailing: ImageVector? = null,
    isDarkMode: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = if (isDarkMode) Color.Black else Color.White
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            color = if (isDarkMode) Color.Black else Color.White
        )
        Spacer(modifier = Modifier.weight(1f))
        trailing?.let {
            Icon(
                it,
                contentDescription = null,
                tint = if (isDarkMode) Color.Black else Color.White
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProfilePreviewLight() {
    MovilesAppTheme(darkTheme = true) {
        Profile()
    }
}

