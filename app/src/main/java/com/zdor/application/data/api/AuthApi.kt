package com.zdor.application.data.api

import com.zdor.application.data.model.input.LoginRequest
import com.zdor.application.data.model.input.RegisterRequest
import com.zdor.application.data.model.output.AuthResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/auth/login")
    fun login(@Body request: LoginRequest) : Call<AuthResponse>

    @POST("/api/auth/register")
    fun register(@Body request: RegisterRequest) : Call<AuthResponse>
}