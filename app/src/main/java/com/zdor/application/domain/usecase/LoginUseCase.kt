package com.zdor.application.domain.usecase

import com.zdor.application.data.model.input.LoginRequest
import com.zdor.application.data.model.output.AuthResponse
import com.zdor.application.domain.repository.AuthRepository
import retrofit2.Call

class LoginUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(request: LoginRequest): Call<AuthResponse> = authRepository.login(request)
}