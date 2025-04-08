package com.zdor.application.presentation.state

data class ConnectivityState(
    val isChecking: Boolean = false,
    val isConnected: Boolean = false,
    val error: String? = null
)
