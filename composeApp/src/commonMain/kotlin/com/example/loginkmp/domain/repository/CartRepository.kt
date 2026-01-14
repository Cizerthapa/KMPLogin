package com.example.loginkmp.domain.repository

import com.example.loginkmp.domain.model.CartResponse

interface CartRepository {
    suspend fun getCartsByUser(userId: Int): Result<CartResponse>
}
