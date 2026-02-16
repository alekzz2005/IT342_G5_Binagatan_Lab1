package com.it342.miniapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.it342.miniapp.data.api.RetrofitClient
import com.it342.miniapp.data.model.RegisterRequest
import com.it342.miniapp.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    
    private val tokenManager = TokenManager(application)
    private val apiService = RetrofitClient.apiService
    
    private val _registerState = MutableStateFlow(false)
    val registerState: StateFlow<Boolean> = _registerState
    
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    fun register(username: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = ""
            
            if (password != confirmPassword) {
                _errorMessage.value = "Passwords do not match."
                _isLoading.value = false
                return@launch
            }
            
            try {
                val request = RegisterRequest(username, email, password, confirmPassword)
                val response = apiService.register(request)
                
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    if (authResponse?.success == true) {
                        if (authResponse.token != null) {
                            tokenManager.saveToken(authResponse.token)
                            RetrofitClient.setAuthToken(authResponse.token)
                        }
                        _registerState.value = true
                    } else {
                        _errorMessage.value = authResponse?.message ?: "Registration failed."
                    }
                } else {
                    _errorMessage.value = "Registration failed. Please try again."
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Registration failed. Please check your connection."
            } finally {
                _isLoading.value = false
            }
        }
    }
}
