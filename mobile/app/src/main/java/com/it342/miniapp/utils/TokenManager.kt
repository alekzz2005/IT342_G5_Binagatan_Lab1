package com.it342.miniapp.utils

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {
    
    private val prefs: SharedPreferences = 
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    companion object {
        private const val PREFS_NAME = "mini_app_prefs"
        private const val KEY_AUTH_TOKEN = "auth_token"
    }
    
    fun saveToken(token: String) {
        prefs.edit().putString(KEY_AUTH_TOKEN, token).apply()
    }
    
    fun getToken(): String? {
        return prefs.getString(KEY_AUTH_TOKEN, null)
    }
    
    fun clearToken() {
        prefs.edit().remove(KEY_AUTH_TOKEN).apply()
    }
    
    fun isLoggedIn(): Boolean {
        return getToken() != null
    }
}
