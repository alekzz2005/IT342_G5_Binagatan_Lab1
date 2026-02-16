package com.it342.miniapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.it342.miniapp.ui.viewmodel.DashboardViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel(),
    onLogout: () -> Unit
) {
    val user by viewModel.user.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }
    
    LaunchedEffect(errorMessage) {
        if (errorMessage.contains("Session expired") || errorMessage.contains("Unauthorized")) {
            onLogout()
        }
    }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Loading...")
                }
            }
        } else {
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
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Dashboard",
                                style = MaterialTheme.typography.headlineMedium,
                                fontSize = 28.sp
                            )
                            
                            OutlinedButton(
                                onClick = {
                                    viewModel.logout()
                                    onLogout()
                                }
                            ) {
                                Text("Logout")
                            }
                        }
                        
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
                        
                        user?.let { userData ->
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                ProfileItem(
                                    label = "Username",
                                    value = userData.username
                                )
                                
                                ProfileItem(
                                    label = "Email",
                                    value = userData.email
                                )
                                
                                ProfileItem(
                                    label = "Member Since",
                                    value = userData.createdAt?.let { formatDate(it) } ?: "-"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(dateString)
        val outputFormat = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())
        date?.let { outputFormat.format(it) } ?: dateString
    } catch (e: Exception) {
        dateString
    }
}
