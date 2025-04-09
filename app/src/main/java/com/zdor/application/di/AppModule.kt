package com.zdor.application.di

import android.content.Context
import com.zdor.application.data.api.AuthApi
import com.zdor.application.data.api.ProfileApi
import com.zdor.application.data.local.TokenManager
import com.zdor.application.data.repository.AuthRepositoryImpl
import com.zdor.application.data.repository.ProfileRepositoryImpl
import com.zdor.application.data.repository.TokenRepositoryImpl
import com.zdor.application.domain.repository.AuthRepository
import com.zdor.application.domain.repository.ProfileRepository
import com.zdor.application.domain.repository.TokenRepository
import com.zdor.application.domain.usecase.LoginUseCase
import com.zdor.application.domain.usecase.RegisterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManager(context)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(authApi: AuthApi): AuthRepository {
        return AuthRepositoryImpl(authApi)
    }

    @Provides
    fun provideLoginUseCase(repository: AuthRepository): LoginUseCase {
        return LoginUseCase(repository)
    }

    @Provides
    fun provideRegisterUseCase(repository: AuthRepository): RegisterUseCase {
        return RegisterUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideTokenRepository(
        @ApplicationContext context: Context,
        tokenManager: TokenManager,
        authApi: AuthApi
    ): TokenRepository {
        return TokenRepositoryImpl(context, tokenManager, authApi)
    }
    
    @Provides
    @Singleton
    fun provideProfileRepository(profileApi: ProfileApi): ProfileRepository {
        return ProfileRepositoryImpl(profileApi)
    }
}