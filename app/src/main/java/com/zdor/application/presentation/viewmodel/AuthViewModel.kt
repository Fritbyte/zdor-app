package com.zdor.application.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zdor.application.data.local.TokenManager
import com.zdor.application.data.model.input.LoginRequest
import com.zdor.application.data.model.input.RegisterRequest
import com.zdor.application.data.model.output.AuthResponse
import com.zdor.application.data.repository.AuthRepositoryImpl
import com.zdor.application.domain.usecase.LoginUseCase
import com.zdor.application.domain.usecase.RegisterUseCase
import com.zdor.application.presentation.state.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(
    private val context: Context,
    private val loginUseCase: LoginUseCase = LoginUseCase(AuthRepositoryImpl()),
    private val registerUseCase: RegisterUseCase = RegisterUseCase(AuthRepositoryImpl())
) : ViewModel() {

    private val tokenManager: TokenManager = TokenManager(context)
    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState

    fun login(username: String, password: String) {
        _loginState.value = LoginState(isLoading = true)
        val request = LoginRequest(username, password)
        loginUseCase(request).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.token ?: ""
                    viewModelScope.launch {
                        tokenManager.saveToken(token)
                        _loginState.value = LoginState(token = token)
                    }
                } else {
                    _loginState.value = LoginState(error = "Ошибка: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                _loginState.value = LoginState(error = t.message ?: "Неизвестная ошибка")
            }
        })
    }

    fun register(username: String, password: String, confirmPassword: String) {
        val request = RegisterRequest(username, password, confirmPassword)
        registerUseCase(request).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.token ?: ""
                    viewModelScope.launch {
                        tokenManager.saveToken(token)
                        _loginState.value = LoginState(token = token)
                    }
                } else {
                    _loginState.value = LoginState(error = "Ошибка: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                _loginState.value = LoginState(error = t.message ?: "Неизвестная ошибка")
            }
        })
    }

    fun logout() {
        viewModelScope.launch {
            tokenManager.clearToken()
            _loginState.value = LoginState()
        }
    }

    suspend fun checkAuth(): Boolean {
        return tokenManager.getToken() != null
    }
}
