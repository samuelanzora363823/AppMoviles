import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.MiBusito.viewmodels.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


@Composable
fun LoginScreen(
    isDarkMode: Boolean,
    authViewModel: AuthViewModel,
    onLoginSuccess: (String, String) -> Unit,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) } // 👈 Estado de carga

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    val backgroundColor = if (isDarkMode) Color.Black else Color.White
    val primaryColor = Color(0xFF3366FF)
    val textColor = if (isDarkMode) Color.White else Color.Black
    val hintColor = if (isDarkMode) Color.LightGray else Color.DarkGray
    val textFieldBg = if (isDarkMode) Color(0xFF1E1E1E) else Color(0xFFF6F8FE)

    // Google SignIn
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("583794477347-vk5rvmf02s51qsdj96lqfsv7n4qk76rc.apps.googleusercontent.com")
        .requestEmail()
        .build()

    val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(Exception::class.java)
                account?.idToken?.let { idToken ->
                    val credential = GoogleAuthProvider.getCredential(idToken, null)
                    authViewModel.loginWithCredential(
                        credential = credential,
                        onSuccess = {
                            isLoading = false
                            val user = auth.currentUser
                            onLoginSuccess(user?.email ?: "", "")
                        },
                        onError = { error ->
                            isLoading = false
                            loginError = error
                        }
                    )
                }
            } catch (e: Exception) {
                isLoading = false
                loginError = e.message ?: "Error al obtener token de Google"
            }
        } else {
            isLoading = false
            loginError = "Inicio de sesión con Google cancelado"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.Start)
            ) {}

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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                SocialLoginButton(
                    icon = Icons.Outlined.Email,
                    text = "Google",
                    color = Color(0xFFDB4437),
                    modifier = Modifier.weight(1f),
                    onClick = {
                        isLoading = true // 👈 Mostrar loader
                        googleSignInClient.signOut().addOnCompleteListener {
                            val signInIntent = googleSignInClient.signInIntent
                            launcher.launch(signInIntent)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider(modifier = Modifier.weight(1f), thickness = 1.dp, color = Color.LightGray)
                Text(text = "  o  ", color = hintColor, modifier = Modifier.padding(horizontal = 4.dp))
                Divider(modifier = Modifier.weight(1f), thickness = 1.dp, color = Color.LightGray)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Correo electrónico", color = hintColor) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = "Email",
                        tint = hintColor
                    )
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

            Spacer(modifier = Modifier.height(12.dp))

            // Contraseña
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

            loginError?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    isLoading = true // 👈 Mostrar loader
                    authViewModel.login(
                        email = email,
                        password = password,
                        onSuccess = {
                            isLoading = false
                            onLoginSuccess(email, password)
                        },
                        onError = { error ->
                            isLoading = false
                            loginError = error
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                enabled = email.isNotBlank() && password.isNotBlank()
            ) {
                Text("Iniciar sesión", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("¿No tienes una cuenta? ", color = hintColor)
                TextButton(onClick = onRegisterClick) {
                    Text("Regístrate", color = primaryColor)
                }
            }
        }

        // 👇 Overlay de carga que bloquea la pantalla
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = primaryColor,
                    strokeWidth = 4.dp
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
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
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
            Text(text = text, color = Color.White, fontSize = 14.sp)
        }
    }
}
