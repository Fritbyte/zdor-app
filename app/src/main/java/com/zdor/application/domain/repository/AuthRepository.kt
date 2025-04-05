package com.zdor.application.domain.repository

import com.zdor.application.data.model.input.LoginRequest
import com.zdor.application.data.model.input.RegisterRequest
import com.zdor.application.data.model.output.AuthResponse
import retrofit2.Call

interface AuthRepository {
    fun login(request: LoginRequest): Call<AuthResponse>
    fun register(request: RegisterRequest): Call<AuthResponse>
}