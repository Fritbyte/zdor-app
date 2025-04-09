package com.zdor.application.data.repository

import com.zdor.application.data.api.AuthApi
import com.zdor.application.data.model.input.LoginRequest
import com.zdor.application.data.model.input.RegisterRequest
import com.zdor.application.data.model.output.AuthResponse
import com.zdor.application.domain.repository.AuthRepository
import retrofit2.Call
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi
) : AuthRepository {

    override fun login(request: LoginRequest): Call<AuthResponse> {
        return authApi.login(request)
    }

    override fun register(request: RegisterRequest): Call<AuthResponse> {
        return authApi.register(request)
    }
}