package com.it342.miniapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.it342.miniapp.ui.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    val registerState by viewModel.registerState.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    LaunchedEffect(registerState) {
        if (registerState) {
            onRegisterSuccess()
        }
    }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
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
                        text = "Register",
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = 28.sp
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Create your account in seconds.",
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
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        placeholder = { Text("Your username") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !isLoading
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
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
                        placeholder = { Text("Create a password") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        enabled = !isLoading
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm Password") },
                        placeholder = { Text("Repeat your password") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        enabled = !isLoading
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(
                        onClick = {
                            viewModel.register(username, email, password, confirmPassword)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading && 
                                username.isNotEmpty() && 
                                email.isNotEmpty() && 
                                password.isNotEmpty() && 
                                confirmPassword.isNotEmpty()
                    ) {
                        Text(
                            text = if (isLoading) "Creating..." else "Register",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row {
                        Text(
                            text = "Already have an account? ",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        TextButton(
                            onClick = onNavigateToLogin,
                            enabled = !isLoading
                        ) {
                            Text("Login")
                        }
                    }
                }
            }
        }
    }
}
