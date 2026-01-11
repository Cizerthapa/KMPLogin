package com.example.loginkmp

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String
)
