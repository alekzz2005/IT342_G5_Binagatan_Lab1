package com.it342.miniapp.data.api

import com.it342.miniapp.data.model.AuthResponse
import com.it342.miniapp.data.model.LoginRequest
import com.it342.miniapp.data.model.RegisterRequest
import com.it342.miniapp.data.model.UserDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    
    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
    
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
    
    @GET("/api/user/me")
    suspend fun getCurrentUser(): Response<UserDTO>
    
    @POST("/api/auth/logout")
    suspend fun logout(): Response<AuthResponse>
}
