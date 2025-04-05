package com.zdor.application.data.model.input

data class RegisterRequest(
    val username: String,
    val password: String,
    val confirmPassword: String
)