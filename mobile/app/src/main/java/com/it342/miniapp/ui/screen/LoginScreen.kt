package com.it342.miniapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.it342.miniapp.ui.viewmodel.LoginViewModel
import com.it342.miniapp.utils.DebugHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    // Debug: Auto-fill developer credentials
    val fillDevCredentials = {
        email = DebugHelper.DEV_EMAIL
        password = DebugHelper.DEV_PASSWORD
    }
    
    val loginState by viewModel.loginState.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    LaunchedEffect(loginState) {
        if (loginState) {
            onLoginSuccess()
        }
    }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = 28.sp
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Welcome back. Please sign in.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    if (errorMessage.isNotEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Text(
                                text = errorMessage,
                                modifier = Modifier.padding(12.dp),
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        placeholder = { Text("you@example.com") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        enabled = !isLoading
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        placeholder = { Text("Enter your password") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        enabled = !isLoading
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(
                        onClick = {
                            viewModel.login(email, password)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading && email.isNotEmpty() && password.isNotEmpty()
                    ) {
                        Text(
                            text = if (isLoading) "Signing in..." else "Login",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Debug button to auto-fill developer credentials
                    OutlinedButton(
                        onClick = fillDevCredentials,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    ) {
                        Text("🔧 Fill Dev Credentials")
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row {
                        Text(
                            text = "Don't have an account? ",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        TextButton(
                            onClick = onNavigateToRegister,
                            enabled = !isLoading
                        ) {
                            Text("Register")
                        }
                    }
                }
            }
        }
    }
}
