package com.it342.miniapp.data.model

data class AuthResponse(
    val token: String?,
    val user: UserDTO?,
    val message: String,
    val success: Boolean
)
