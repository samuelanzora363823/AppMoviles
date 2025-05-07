import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Facebook
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    isDarkMode: Boolean,
    onLoginSuccess: (String, String) -> Unit,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val backgroundColor = if (isDarkMode) Color.Black else Color.White
    val primaryColor = Color(0xFF3366FF)
    val textColor = if (isDarkMode) Color.White else Color.Black
    val hintColor = if (isDarkMode) Color.LightGray else Color.DarkGray
    val textFieldBg = if (isDarkMode) Color(0xFF1E1E1E) else Color(0xFFF6F8FE)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Volver atrás",
                tint = primaryColor
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Iniciar sesión",
            fontSize = 28.sp,
            color = primaryColor,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Bienvenido de nuevo, por favor ingresa tus credenciales",
            fontSize = 12.sp,
            color = hintColor,
            modifier = Modifier.padding(horizontal = 8.dp),
            lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botones sociales
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SocialLoginButton(
                icon = Icons.Outlined.Facebook,
                text = "Facebook",
                color = Color(0xFF4267B2),
                modifier = Modifier.weight(1f)
            )

            SocialLoginButton(
                icon = Icons.Outlined.Email, // Usamos el icono de email para Google
                text = "Google",
                color = Color(0xFFDB4437),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Divisor
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Divider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Color.LightGray
            )
            Text(
                text = "  o  ",
                color = hintColor,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Divider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Color.LightGray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Correo electrónico", color = hintColor) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = textFieldBg,
                unfocusedContainerColor = textFieldBg,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedIndicatorColor = primaryColor,
                unfocusedIndicatorColor = hintColor
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = "Email",
                    tint = hintColor
                )
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Campo de contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Contraseña", color = hintColor) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = hintColor
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = textFieldBg,
                unfocusedContainerColor = textFieldBg,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedIndicatorColor = primaryColor,
                unfocusedIndicatorColor = hintColor
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        TextButton(
            onClick = { /* Acción para olvidó contraseña */ },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = primaryColor,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de inicio de sesión
        Button(
            onClick = { onLoginSuccess(email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            enabled = email.isNotBlank() && password.isNotBlank()
        ) {
            Text(
                text = "Iniciar sesión",
                color = Color.White,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Enlace a registro
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¿No tienes una cuenta? ",
                color = hintColor
            )
            TextButton(onClick = onRegisterClick) {
                Text(
                    text = "Regístrate",
                    color = primaryColor
                )
            }
        }
    }
}

@Composable
fun SocialLoginButton(
    icon: ImageVector,
    text: String,
    color: Color,
    iconTint: Color = Color.White,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { /* Acción del botón social */ },
        modifier = modifier.height(44.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Logo $text",
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}