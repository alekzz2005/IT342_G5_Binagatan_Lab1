package com.it342.miniapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.it342.miniapp.data.api.RetrofitClient
import com.it342.miniapp.data.model.UserDTO
import com.it342.miniapp.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    
    private val tokenManager = TokenManager(application)
    private val apiService = RetrofitClient.apiService
    
    private val _user = MutableStateFlow<UserDTO?>(null)
    val user: StateFlow<UserDTO?> = _user
    
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    init {
        // Set the token when ViewModel is created
        val token = tokenManager.getToken()
        if (token != null) {
            RetrofitClient.setAuthToken(token)
        }
    }
    
    fun loadUser() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = ""
            
            try {
                val response = apiService.getCurrentUser()
                
                if (response.isSuccessful) {
                    _user.value = response.body()
                } else {
                    _errorMessage.value = "Session expired. Please log in again."
                    tokenManager.clearToken()
                }
            } catch (e: Exception) {
                _errorMessage.value = "Session expired. Please log in again."
                tokenManager.clearToken()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            try {
                apiService.logout()
            } catch (e: Exception) {
                // Ignore errors on logout
            } finally {
                tokenManager.clearToken()
                RetrofitClient.setAuthToken(null)
            }
        }
    }
}
