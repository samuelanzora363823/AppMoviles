import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun RegisterScreen(
    isDarkMode: Boolean,
    authViewModel: AuthViewModel,
    onRegisterSuccess: (String, String) -> Unit,
    onLoginClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var registerError by remember { mutableStateOf<String?>(null) }

    // Estado para manejar la animación de carga
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    val backgroundColor = if (isDarkMode) Color.Black else Color.White
    val primaryColor = Color(0xFF3366FF)
    val textColor = if (isDarkMode) Color.White else Color.Black
    val hintColor = if (isDarkMode) Color.LightGray else Color.DarkGray
    val textFieldBg = if (isDarkMode) Color(0xFF1E1E1E) else Color(0xFFF6F8FE)

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
                            val user = auth.currentUser
                            onRegisterSuccess(user?.email ?: "", "")
                        },
                        onError = { error ->
                            registerError = error
                        }
                    )
                }
            } catch (e: Exception) {
                registerError = e.message ?: "Error al obtener token de Google"
            }
        } else {
            registerError = "Registro con Google cancelado"
        }
    }

    val isPasswordValid = password.length >= 8
    val isButtonEnabled = name.trim().isNotBlank() && email.trim().isNotBlank() && isPasswordValid

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            // Indicador de carga (se muestra cuando isLoading es true)
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)), // Fondo translúcido
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = primaryColor
                    )
                }
            }

            // Botón de volver atrás
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver atrás", tint = primaryColor)
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Crear cuenta",
                fontSize = 28.sp,
                color = primaryColor,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Únete a nuestra comunidad y descubre todas las ventajas",
                fontSize = 12.sp,
                color = hintColor,
                modifier = Modifier.padding(horizontal = 8.dp),
                lineHeight = 16.sp
            )

            registerError?.let {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            // Botón de Google
            SocialLoginButton(
                icon = Icons.Outlined.Email,
                text = "Google",
                color = Color(0xFFDB4437),
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    isLoading = true  // Mostrar animación
                    googleSignInClient.signOut().addOnCompleteListener {
                        val signInIntent = googleSignInClient.signInIntent
                        launcher.launch(signInIntent)
                    }
                }
            )

            Spacer(Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider(modifier = Modifier.weight(1f), thickness = 1.dp, color = Color.LightGray)
                Text("  o  ", color = hintColor, modifier = Modifier.padding(horizontal = 4.dp))
                Divider(modifier = Modifier.weight(1f), thickness = 1.dp, color = Color.LightGray)
            }

            Spacer(Modifier.height(16.dp))
        }

        item {
            // Formulario de registro: nombre, correo y contraseña
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("Nombre completo", color = hintColor) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                leadingIcon = {
                    Icon(Icons.Outlined.Person, contentDescription = "Nombre", tint = hintColor)
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

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Correo electrónico", color = hintColor) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = {
                    Icon(Icons.Outlined.Email, contentDescription = "Email", tint = hintColor)
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

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Contraseña (mínimo 8 caracteres)", color = hintColor) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = {
                    Icon(Icons.Outlined.Lock, contentDescription = "Contraseña", tint = hintColor)
                },
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            icon,
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = hintColor
                        )
                    }
                },
                isError = !isPasswordValid && password.isNotEmpty(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = textFieldBg,
                    unfocusedContainerColor = textFieldBg,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor,
                    focusedIndicatorColor = if (!isPasswordValid && password.isNotEmpty()) Color.Red else primaryColor,
                    unfocusedIndicatorColor = if (!isPasswordValid && password.isNotEmpty()) Color.Red else hintColor
                )
            )

            if (!isPasswordValid && password.isNotEmpty()) {
                Text(
                    text = "La contraseña debe tener al menos 8 caracteres",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, top = 4.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    isLoading = true  // Mostrar animación
                    registerError = null
                    authViewModel.register(
                        name = name.trim(),
                        email = email.trim(),
                        password = password,
                        onSuccess = {
                            isLoading = false  // Detener animación
                            onRegisterSuccess(email.trim(), password)
                        },
                        onError = { error ->
                            isLoading = false  // Detener animación
                            registerError = error
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                enabled = isButtonEnabled
            ) {
                Text("Registrarse", color = Color.White, fontSize = 16.sp)
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "¿Ya tienes cuenta?",
                    color = hintColor,
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }
        }
    }
}
