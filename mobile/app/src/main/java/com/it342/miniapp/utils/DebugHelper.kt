package com.it342.miniapp.utils

object DebugHelper {
    
    // Developer test credentials
    const val DEV_USERNAME = "developer"
    const val DEV_EMAIL = "dev@test.com"
    const val DEV_PASSWORD = "password123"
    
    // Alternative test accounts
    const val TEST_USERNAME = "testuser"
    const val TEST_EMAIL = "test@test.com"
    const val TEST_PASSWORD = "test123"
    
    // Debug features enabled
    const val DEBUG_ENABLED = true
    
    // Quick reference for debugging
    fun getDebugInfo(): String {
        return """
            Debug Account 1:
            Email: $DEV_EMAIL
            Password: $DEV_PASSWORD
            
            Debug Account 2:
            Email: $TEST_EMAIL
            Password: $TEST_PASSWORD
        """.trimIndent()
    }
}
