package com.it342.miniapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.it342.miniapp.data.api.RetrofitClient
import com.it342.miniapp.navigation.AppNavigation
import com.it342.miniapp.navigation.Screen
import com.it342.miniapp.ui.theme.MiniAppTheme
import com.it342.miniapp.utils.TokenManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val tokenManager = TokenManager(this)
        val token = tokenManager.getToken()
        
        // Set the auth token if it exists
        if (token != null) {
            RetrofitClient.setAuthToken(token)
        }
        
        // Determine start destination based on login state
        val startDestination = if (tokenManager.isLoggedIn()) {
            Screen.Dashboard.route
        } else {
            Screen.Login.route
        }
        
        setContent {
            MiniAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavigation(
                        navController = navController,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}
