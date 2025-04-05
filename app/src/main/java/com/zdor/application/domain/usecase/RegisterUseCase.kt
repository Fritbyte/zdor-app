package com.zdor.application.domain.usecase

import com.zdor.application.data.model.input.RegisterRequest
import com.zdor.application.data.model.output.AuthResponse
import com.zdor.application.domain.repository.AuthRepository
import retrofit2.Call

class RegisterUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(request: RegisterRequest): Call<AuthResponse> = authRepository.register(request)
}