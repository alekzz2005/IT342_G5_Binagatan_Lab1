package com.it342.miniapp.data.model

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)
