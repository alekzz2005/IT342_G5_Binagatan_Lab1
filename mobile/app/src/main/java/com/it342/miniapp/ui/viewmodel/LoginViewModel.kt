package com.it342.miniapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.it342.miniapp.data.api.RetrofitClient
import com.it342.miniapp.data.model.LoginRequest
import com.it342.miniapp.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    
    private val tokenManager = TokenManager(application)
    private val apiService = RetrofitClient.apiService
    
    private val _loginState = MutableStateFlow(false)
    val loginState: StateFlow<Boolean> = _loginState
    
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = ""
            
            try {
                val request = LoginRequest(email, password)
                val response = apiService.login(request)
                
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    if (authResponse?.success == true && authResponse.token != null) {
                        tokenManager.saveToken(authResponse.token)
                        RetrofitClient.setAuthToken(authResponse.token)
                        _loginState.value = true
                    } else {
                        _errorMessage.value = authResponse?.message ?: "Login failed."
                    }
                } else {
                    // Try to get error message from response body
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.value = if (errorBody != null && errorBody.contains("message")) {
                        try {
                            // Try to parse the error message from JSON
                            val messageStart = errorBody.indexOf("\"message\":\"") + 11
                            val messageEnd = errorBody.indexOf("\"", messageStart)
                            errorBody.substring(messageStart, messageEnd)
                        } catch (e: Exception) {
                            "Login failed: ${response.code()} - ${response.message()}"
                        }
                    } else {
                        "Login failed: ${response.code()} - ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Login failed. Please check your connection."
            } finally {
                _isLoading.value = false
            }
        }
    }
}
