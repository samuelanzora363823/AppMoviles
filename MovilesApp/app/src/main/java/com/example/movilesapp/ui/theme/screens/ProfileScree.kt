package com.example.movilesapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movilesapp.ui.theme.MovilesAppTheme

@Composable
fun Profile(
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit
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
                    onCheckedChange = { onToggleDarkMode(it) },
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
                onClick = { /* Acción cerrar sesión */ },
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

@Composable
fun ProfileItem(
    icon: ImageVector,
    label: String,
    trailing: ImageVector? = null,
    isDarkMode: Boolean
) {
    val textColor = if (isDarkMode) Color.White else Color.Black
    val iconColor = textColor

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

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun ProfilePreviewLight() {
    MovilesAppTheme(darkTheme = false) {
        Profile(
            isDarkMode = false,
            onToggleDarkMode = {}
        )
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun ProfilePreviewDark() {
    MovilesAppTheme(darkTheme = true) {
        Profile(
            isDarkMode = true,
            onToggleDarkMode = {}
        )
    }
}
