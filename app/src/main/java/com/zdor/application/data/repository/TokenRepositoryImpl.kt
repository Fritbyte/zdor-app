@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.zdor.application.data.repository

import android.content.Context
import com.zdor.application.data.api.AuthApi
import com.zdor.application.data.local.TokenManager
import com.zdor.application.data.model.input.LoginRequest
import com.zdor.application.data.model.input.RegisterRequest
import com.zdor.application.data.model.output.AuthResponse
import com.zdor.application.domain.repository.TokenRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resumeWithException

@Singleton
class TokenRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val tokenManager: TokenManager,
    private val authApi: AuthApi
) : TokenRepository {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override suspend fun getToken(): String? {
        return tokenManager.getToken()
    }

    override suspend fun saveToken(token: String) {
        tokenManager.saveToken(token)
    }

    override suspend fun clearToken() {
        tokenManager.clearToken()
    }

    override fun hasValidToken(): Flow<Boolean> = flow {
        emit(tokenManager.getToken() != null)
    }

    override suspend fun refreshTokenExpiry() {
    }

    override suspend fun login(email: String, password: String): AuthResponse =
        suspendCancellableCoroutine { continuation ->
            val request = LoginRequest(email, password)
            authApi.login(request).enqueue(object : Callback<AuthResponse> {
                override fun onResponse(
                    call: Call<AuthResponse>,
                    response: Response<AuthResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val authResponse = response.body()!!
                        coroutineScope.launch {
                            try {
                                tokenManager.saveToken(authResponse.token)
                                continuation.resume(authResponse) { }
                            } catch (e: Exception) {
                                continuation.resumeWithException(e)
                            }
                        }
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Неизвестная ошибка"
                        continuation.resumeWithException(Exception(errorMessage))
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }

    override suspend fun register(email: String, password: String): AuthResponse =
        suspendCancellableCoroutine { continuation ->
            val request = RegisterRequest(email, password, password)
            authApi.register(request).enqueue(object : Callback<AuthResponse> {
                override fun onResponse(
                    call: Call<AuthResponse>,
                    response: Response<AuthResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val authResponse = response.body()!!
                        coroutineScope.launch {
                            try {
                                tokenManager.saveToken(authResponse.token)
                                continuation.resume(authResponse) { }
                            } catch (e: Exception) {
                                continuation.resumeWithException(e)
                            }
                        }
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Неизвестная ошибка"
                        continuation.resumeWithException(Exception(errorMessage))
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
} 