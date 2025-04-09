package com.zdor.application.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zdor.application.domain.repository.TokenRepository
import com.zdor.application.presentation.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState

    init {
        viewModelScope.launch {
            val token = tokenRepository.getToken()
            _loginState.value = LoginState(token = token)
        }
    }

    suspend fun checkAuth(): Boolean {
        return tokenRepository.hasValidToken().first()
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = _loginState.value.copy(isLoading = true, error = null)
            try {
                val response = tokenRepository.login(username, password)
                _loginState.value = LoginState(
                    token = response.token,
                    message = response.message
                )
            } catch (e: Exception) {
                _loginState.value = LoginState(error = e.message)
            }
        }
    }

    fun register(username: String, password: String, confirmPassword: String) {
        if (password != confirmPassword) {
            _loginState.value = LoginState(error = "Пароли не совпадают")
            return
        }

        viewModelScope.launch {
            _loginState.value = _loginState.value.copy(isLoading = true, error = null)
            try {
                val response = tokenRepository.register(username, password)
                _loginState.value = LoginState(
                    token = response.token,
                    registrationMessage = response.message ?: "Регистрация успешна"
                )
            } catch (e: Exception) {
                _loginState.value = LoginState(error = e.message)
            }
        }
    }

    fun refreshToken() {
        viewModelScope.launch {
            val token = tokenRepository.getToken()
            _loginState.value = _loginState.value.copy(token = token)
        }
    }

    fun logout() {
        viewModelScope.launch {
            tokenRepository.clearToken()
            _loginState.value = LoginState()
        }
    }
}
