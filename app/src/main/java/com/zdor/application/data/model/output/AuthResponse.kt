package com.zdor.application.data.model.output

data class AuthResponse(
    val token: String,
    val message: String? = null
)