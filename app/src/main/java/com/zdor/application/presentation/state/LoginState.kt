package com.zdor.application.presentation.state

data class LoginState(
    val isLoading: Boolean = false,
    val token: String? = null,
    val error: String? = null,
    val registrationMessage: String? = null
)