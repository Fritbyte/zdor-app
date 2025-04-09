package com.zdor.application.domain.repository

import com.zdor.application.data.model.output.AuthResponse
import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    suspend fun getToken(): String?
    suspend fun saveToken(token: String)
    suspend fun clearToken()
    suspend fun login(email: String, password: String): AuthResponse
    suspend fun register(email: String, password: String): AuthResponse
    fun hasValidToken(): Flow<Boolean>
    suspend fun refreshTokenExpiry()
} 