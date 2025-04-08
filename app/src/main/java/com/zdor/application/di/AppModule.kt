package com.zdor.application.di

import android.content.Context
import com.zdor.application.data.local.TokenManager
import com.zdor.application.data.repository.AuthRepositoryImpl
import com.zdor.application.domain.repository.AuthRepository
import com.zdor.application.domain.usecase.LoginUseCase
import com.zdor.application.domain.usecase.RegisterUseCase

object AppModule {
    fun provideTokenManager(context: Context): TokenManager {
        return TokenManager(context)
    }

    fun provideAuthRepository(): AuthRepository {
        return AuthRepositoryImpl()
    }

    fun provideLoginUseCase(repository: AuthRepository = provideAuthRepository()): LoginUseCase {
        return LoginUseCase(repository)
    }

    fun provideRegisterUseCase(repository: AuthRepository = provideAuthRepository()): RegisterUseCase {
        return RegisterUseCase(repository)
    }
}